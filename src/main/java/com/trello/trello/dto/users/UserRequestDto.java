package com.trello.trello.dto.users;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDto {
    private String username;
    private String email;
    private String password;

    // Constructor
    public UserRequestDto() {}

    public UserRequestDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}