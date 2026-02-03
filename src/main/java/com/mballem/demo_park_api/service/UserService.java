package com.mballem.demo_park_api.service;

import com.mballem.demo_park_api.dto.UserCreateDTO;
import com.mballem.demo_park_api.dto.UserPasswordDTO;
import com.mballem.demo_park_api.dto.UserResponseDTO;
import com.mballem.demo_park_api.entity.User;
import com.mballem.demo_park_api.entity.enums.Role;
import com.mballem.demo_park_api.repository.UserRepository;
import com.mballem.demo_park_api.service.Exception.ResorceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id){
        User user = userRepository.findById(id).orElseThrow(() -> new ResorceNotFoundException("User not found"));
        return new UserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> findAll(Pageable pageable){
        Page<User> allUser = userRepository.findAll(pageable);
        return allUser.map(x -> new UserResponseDTO(x));
    }

    @Transactional
    public UserResponseDTO insert(UserCreateDTO createDTO){
        User user = new User();
        copyDtoToEntity(user, createDTO);
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updatePassword(Long id, UserPasswordDTO userPasswordDTO){
        User user = userRepository.findById(id).orElseThrow(() -> new ResorceNotFoundException("User not found"));
        if (!user.getPassword().equals(userPasswordDTO.getCurrentPassword())){
            throw new RuntimeException("Senha atual está incorreta");
        }
        if (!userPasswordDTO.getNewPassword().equals(userPasswordDTO.getConfirmPassword())){
            throw new RuntimeException("Confirmação de senha não está correta");
        }
        user.setPassword(userPasswordDTO.getNewPassword());
        user = userRepository.save(user);
        return new UserResponseDTO(user);
    }

    public static void copyDtoToEntity(User user, UserCreateDTO dto){
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(Role.ROLE_CLIENT);
    }

}
