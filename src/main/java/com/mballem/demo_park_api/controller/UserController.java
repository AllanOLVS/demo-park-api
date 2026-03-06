package com.mballem.demo_park_api.controller;

import com.mballem.demo_park_api.dto.ErrorMessage;
import com.mballem.demo_park_api.dto.UserCreateDTO;
import com.mballem.demo_park_api.dto.UserPasswordDTO;
import com.mballem.demo_park_api.dto.UserResponseDTO;
import com.mballem.demo_park_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Referente ao Swagger
@Tag(name = "Usuarios", description = "Contem todas as operações referentes a usuarios")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Buscando usuario por ID", description = "Buscando usuario por ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        UserResponseDTO newDto = userService.findById(id);
        return ResponseEntity.ok(newDto);
    }

    @Operation(summary = "Listagem de usuarios", description = "Listar todos os usuarios cadastrados",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listagem de usuarios cadastrados",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))),
                    @ApiResponse(responseCode = "404", description = "Nenhum usuario cadastrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<UserResponseDTO> allUsers = userService.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @Operation(summary = "Criando um novo usuario", description = "Recurso pra criar novo usuario",
            responses = {
                @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
                @ApiResponse(responseCode = "409", description = "E-mail ja cadastrado no sistema",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "422", description = "Dados de entrada invalidos, não correspondem",
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody UserCreateDTO createDTO) {
        UserResponseDTO newResponseDto = userService.insert(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newResponseDto);
    }

    // Futuramente criar um DTO que carregue so a senha do usuario, ja que desta
    // forma nao da pra usar o @Valid
    // pelo Postman ele valida tod o body, mas como o user ta vazio ja que so quero
    // mudar a senha, ele diz que o user é num e quebra
    // Então tem a necessidade de um DTO que carregue so a senha
    @Operation(summary = "Atualizar senha", description = "Atualizar senha",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
                    @ApiResponse(responseCode = "404", description = "Recurso não encontrado",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Senha não confere",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Campos invalidos ou mal formatados",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            }
    )
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,
            @Valid @RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(id, userPasswordDTO);
        return ResponseEntity.noContent().build();
    }

}
