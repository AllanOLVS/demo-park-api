package com.mballem.demo_park_api.controller;

import com.mballem.demo_park_api.dto.UserCreateDTO;
import com.mballem.demo_park_api.dto.UserPasswordDTO;
import com.mballem.demo_park_api.dto.UserResponseDTO;
import com.mballem.demo_park_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id){
        UserResponseDTO newDto = userService.findById(id);
        return ResponseEntity.ok(newDto);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> findAll(Pageable pageable){
        Page<UserResponseDTO> allUsers = userService.findAll(pageable);
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> insert(@Valid @RequestBody UserCreateDTO createDTO){
        UserResponseDTO newResponseDto = userService.insert(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newResponseDto);
    }

    // Futuramente criar um DTO que carregue so a senha do usuario, ja que desta forma nao da pra usar o @Valid
    //pelo Postman ele valida tod o body, mas como o user ta vazio ja que so quero mudar a senha, ele diz que o user é num e quebra
    // Então tem a necessidade de um DTO que carregue so a senha
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UserPasswordDTO userPasswordDTO){
        userService.updatePassword(id, userPasswordDTO);
        return ResponseEntity.noContent().build();
    }


}
