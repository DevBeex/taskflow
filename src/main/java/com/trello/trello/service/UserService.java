package com.trello.trello.service;

import com.trello.trello.dto.users.UserRequestDto;
import com.trello.trello.model.User;
import com.trello.trello.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.trello.trello.dto.users.UserResponseDto;
import com.trello.trello.mapper.UserMapper;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Convertimos el UserRequestDto a una entidad User
        User userEntity = userMapper.toEntity(userRequestDto);
        User savedUser = userRepository.save(userEntity);
        return userMapper.toResponseDto(savedUser);
    }
}
