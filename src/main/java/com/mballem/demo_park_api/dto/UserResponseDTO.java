package com.mballem.demo_park_api.dto;

import com.mballem.demo_park_api.entity.User;
import com.mballem.demo_park_api.entity.enums.Role;

public class UserResponseDTO {

    private Long id;
    private String username;
    private Role role;

    public UserResponseDTO(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

    public UserResponseDTO(Long id, String username, Role role){
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
