package com.trello.trello.mapper;

import com.trello.trello.dto.users.UserResponseDto;
import com.trello.trello.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.trello.trello.dto.users.UserRequestDto;
@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    // De UserRequestDto a User
    User toEntity(UserRequestDto userRequestDto);

    // De User a UserResponseDto
    UserResponseDto toResponseDto(User user);
}
