package com.trello.trello.service;

import com.trello.trello.constants.ErrorMessages;
import com.trello.trello.dto.users.UserRequestDto;
import com.trello.trello.exception.ResourceNotFoundException;
import com.trello.trello.model.User;
import com.trello.trello.repository.UserRepository;
import com.trello.trello.util.PasswordUtil;
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
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        return userMapper.toResponseDto(user);
    }

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
        User userEntity = userMapper.toEntity(userRequestDto);
        userEntity.setPassword(PasswordUtil.hashPassword(userRequestDto.getPassword()));
        User savedUser = userRepository.save(userEntity);
        return userMapper.toResponseDto(savedUser);
    }
}
