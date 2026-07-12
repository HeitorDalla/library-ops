package com.heitor.app.mapper;

import com.heitor.app.dto.input.UserPatchDTO;
import com.heitor.app.dto.input.UserUpsertDTO;
import com.heitor.app.dto.output.UserResponseDTO;
import com.heitor.app.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public List<UserResponseDTO> toDtoList(List<User> entities) {
        if (entities.isEmpty()) {
            return List.of();
        }

        return entities.stream()
                .map(this::toDto)
                .toList();
    }

    // Converte Entity para o que será exposto na API
    public UserResponseDTO toDto(User entity) {
        if (entity == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .number(entity.getNumber())
                .email(entity.getEmail())
                .registrationDate(entity.getRegistrationDate())
                .userStatus(entity.getUserStatus())
                .recordStatus(entity.getRecordStatus())
                .build();
    }

    // Converte o DTO de entrada para uma nova Entity (POST ou PUT)
    public User toEntity(UserUpsertDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .name(dto.getName())
                .number(dto.getNumber())
                .email(dto.getEmail())
                .build();
    }

    // Atualiza apenas os campos NÃO-NULL do DTO na Entity existente (EVITAR SOBRESCREVER CAMPOS COM 'NULL')
    public void patchEntity(UserPatchDTO dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getNumber() != null) {
            entity.setNumber(dto.getNumber());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
    }

    public void updateEntity(UserUpsertDTO dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setName(dto.getName());
        entity.setNumber(dto.getNumber());
        entity.setEmail(dto.getEmail());
    }
}
