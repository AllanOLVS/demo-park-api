package com.mballem.demo_park_api;

import com.mballem.demo_park_api.dto.ErrorMessage;
import com.mballem.demo_park_api.dto.UserCreateDTO;
import com.mballem.demo_park_api.dto.UserPasswordDTO;
import com.mballem.demo_park_api.dto.UserResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static com.mballem.demo_park_api.entity.enums.Role.ROLE_ADMIN;
import static com.mballem.demo_park_api.entity.enums.Role.ROLE_CLIENT;

// Teste de Ponto a Ponto / que é um tipo de teste de integração
// Testando toda a parte referente ao usuario (da requisição ate a resposta)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// scripts = passar o caminho do arquivo sql
// executionPhase = diz quando deve ser executado
// Before = Pra que antes de executar os usuarios sejam inseridos
@Sql(scripts = "/sql/users/users-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/users-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserIT {

        @Autowired
        WebTestClient webTestClient;

        @Test
        public void createUser_WithValidUsernameAndPassword_ReturnCreatedUserWith201Stats() {
                UserResponseDTO userResponseDTO = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("tody@gmail.com", "12345678"))
                                .exchange()
                                .expectStatus().isCreated()
                                .expectBody(UserResponseDTO.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(userResponseDTO).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(userResponseDTO.getId()).isNotNull();
                // Compara o valor que foi retornado, com o valor entre as aspas
                org.assertj.core.api.Assertions.assertThat(userResponseDTO.getUsername()).isEqualTo("tody@gmail.com");
                org.assertj.core.api.Assertions.assertThat(userResponseDTO.getRole()).isEqualTo(ROLE_CLIENT);
        }

        @Test
        public void createUser_WithInvalidUsername_ReturnErrorMessageWith422Stats() {
                ErrorMessage ErrorMessage = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("", "12345678"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o status retornado é igual a 422, que é o esperado
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);

                // Errando diferente, desta vez colocando um nome no lugar do username
                ErrorMessage = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("Allan", "12345678"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o status retornado é igual a 422, que é o esperado
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);

                // Fazendo o mesmo sem o .com no email
                ErrorMessage = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("Allan@gmail", "12345678"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o status retornado é igual a 422, que é o esperado
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);
        }

        @Test
        public void createUser_WithInvalidPassword_ReturnErrorMessageWith422Stats() {
                ErrorMessage ErrorMessage = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("allan@gmail.com", ""))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o status retornado é igual a 422, que é o esperado
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);

                // Errando diferente, desta vez colocando um nome no lugar do username
                ErrorMessage = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("allan@gmail.com", "12345"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o status retornado é igual a 422, que é o esperado
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);
        }

        @Test
        public void createUser_WithUsernameRepeated_ReturnErrorMessageWith409Stats() {
                ErrorMessage ErrorMessage = webTestClient
                                .post()
                                .uri("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserCreateDTO("Allaney@gmail.com", "12345678"))
                                .exchange()
                                .expectStatus().isEqualTo(409)
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o status retornado é igual a 422, que é o esperado
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(409);
        }

        @Test
        public void findUser_WithValidId_ReturnUserWith200Stats() {
                UserResponseDTO userResponseDTO = webTestClient
                                .get()
                                .uri("/api/users/20")
                                .exchange()
                                .expectStatus().isOk()
                                .expectBody(UserResponseDTO.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(userResponseDTO).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(userResponseDTO.getId()).isEqualTo(20);
                // Compara o valor que foi retornado, com o valor entre as aspas
                org.assertj.core.api.Assertions.assertThat(userResponseDTO.getUsername())
                                .isEqualTo("Allaney@gmail.com");
                org.assertj.core.api.Assertions.assertThat(userResponseDTO.getRole()).isEqualTo(ROLE_ADMIN);
        }

        @Test
        public void findUser_WithInvalidId_ReturnErrorMessageWith404Stats() {
                ErrorMessage ErrorMessage = webTestClient
                                .get()
                                .uri("/api/users/0")
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(404);
        }

        @Test
        public void updatePassword_WithValidPassword_Return204Stats() {
                webTestClient
                                .patch()
                                .uri("/api/users/20")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserPasswordDTO("12345678910", "12345678", "12345678"))
                                .exchange()
                                .expectStatus().isNoContent();
        }

        @Test
        public void updatePassword_WithInvalidId_ReturnErrorMessageWith404Stats() {
                ErrorMessage ErrorMessage = webTestClient
                                .patch()
                                .uri("/api/users/0")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserPasswordDTO("12345678910", "12345678", "12345678"))
                                .exchange()
                                .expectStatus().isNotFound()
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(404);
        }

        @Test
        public void updatePassword_WithInvalidInformations_ReturnErrorMessageWith422Stats() {
                // Campos de senha vazios
                ErrorMessage ErrorMessage = webTestClient
                                .patch()
                                .uri("/api/users/20")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserPasswordDTO("", "", ""))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);

                // Senhas com menos de 6 digitos
                ErrorMessage = webTestClient
                                .patch()
                                .uri("/api/users/20")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserPasswordDTO("12345", "12345", "12345"))
                                .exchange()
                                .expectStatus().isEqualTo(422)
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(422);
        }

        @Test
        public void updatePassword_WithInvalidPasswords_ReturnErrorMessageWith400Stats() {
                // Senhas não conferem
                // Senha atual valida, mas nova senha e confirmação de senha diferentes
                ErrorMessage ErrorMessage = webTestClient
                                .patch()
                                .uri("/api/users/20")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserPasswordDTO("12345678910", "1234567", "12345678"))
                                .exchange()
                                .expectStatus().isEqualTo(400)
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(400);

                // Senha atual invalida, não confere com a do banco
                ErrorMessage = webTestClient
                                .patch()
                                .uri("/api/users/20")
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new UserPasswordDTO("0000000", "12345678", "12345678"))
                                .exchange()
                                .expectStatus().isEqualTo(400)
                                .expectBody(ErrorMessage.class)
                                // Retorna um objeto do tipo UserRespondeDTO
                                .returnResult().getResponseBody();

                // Testando o objeto de resposta
                // Testando se o retorno não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage).isNotNull();
                // Testando se o ID não é nulo
                org.assertj.core.api.Assertions.assertThat(ErrorMessage.getStatus()).isEqualTo(400);
        }

        @Test
        public void findAllUsers_WithValidData_ReturnUserListWith200Stats() {
        List<UserResponseDTO> userResponseDTOlist = webTestClient
                                .get()
                                .uri("/api/users")
                                .exchange()
                                .expectStatus().isOk()
                .expectBodyList(UserResponseDTO.class)
                // Retorna um objeto do tipo UserRespondeDTO
                .returnResult().getResponseBody();

        // Testando o objeto de resposta
        // Testando se o retorno não é nulo
        org.assertj.core.api.Assertions.assertThat(userResponseDTOlist).isNotNull();
        // Testando tamanho da lista
        org.assertj.core.api.Assertions.assertThat(userResponseDTOlist.size()).isEqualTo(3);
        }

}
