package com.trello.trello.service;

import com.trello.trello.constants.EntityConstant;
import com.trello.trello.constants.ErrorMessages;
import com.trello.trello.dto.users.UserRequestDto;
import com.trello.trello.exception.ResourceNotFoundException;
import com.trello.trello.model.Role;
import com.trello.trello.model.Status;
import com.trello.trello.model.User;
import com.trello.trello.repository.RoleRepository;
import com.trello.trello.repository.StatusRepository;
import com.trello.trello.repository.UserRepository;
import com.trello.trello.util.PasswordUtil;
import org.springframework.stereotype.Service;
import com.trello.trello.dto.users.UserResponseDto;
import com.trello.trello.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, StatusRepository statusRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
    }


    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        return userMapper.toResponseDto(user);
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param userRequestDto Datos de la solicitud para crear el usuario.
     * @return DTO de respuesta del usuario creado.
     * @throws IllegalArgumentException si el nombre de usuario o correo electrónico ya existen.
     */
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        // Verificar si el nombre de usuario ya existe
        if (userRepository.findByUsername(userRequestDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }

        // Verificar si el correo electrónico ya existe
        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new IllegalArgumentException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }

        // Mapear DTO a entidad
        User userEntity = userMapper.toEntity(userRequestDto);

        // Encriptar la contraseña
        userEntity.setPassword(PasswordUtil.hashPassword(userRequestDto.getPassword()));

        // Asignar rol predeterminado (ROLE_USER)
        Role userRole = roleRepository.findByName(EntityConstant.RoleName.USER.toString())
                .orElseThrow(() -> new IllegalArgumentException("Rol predeterminado no encontrado: " + EntityConstant.RoleName.USER.toString()));
        userEntity.setRole(userRole);

        // Asignar estado activo (ACTIVE)
        Status activeStatus = statusRepository.findByName(EntityConstant.StatusName.ACTIVE.toString())
                .orElseThrow(() -> new IllegalArgumentException("Estado predeterminado no encontrado: " + EntityConstant.StatusName.ACTIVE.toString()));
        userEntity.setStatus(activeStatus);

        // Guardar el usuario en la base de datos
        User savedUser = userRepository.save(userEntity);

        // Mapear entidad a DTO de respuesta
        return userMapper.toResponseDto(savedUser);
    }
}
