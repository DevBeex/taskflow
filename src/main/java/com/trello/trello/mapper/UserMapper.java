package com.trello.trello.mapper;

import com.trello.trello.dto.users.UserResponseDto;
import com.trello.trello.dto.users.UserRequestDto;
import com.trello.trello.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Ignora el campo "boards" ya que no está presente en el DTO
    @Mapping(target = "boards", ignore = true)
    User toEntity(UserRequestDto userRequestDto);

    UserResponseDto toResponseDto(User user);
}