package org.example.bank_rest.mapper.helpers;

import org.example.bank_rest.dto.UserRoleDto;
import org.example.bank_rest.persistence.model.entity.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper {
    public UserRole toEntity(UserRoleDto dto){
        return UserRole.valueOf(dto.toString());
    }

    public UserRoleDto toDto(UserRole entity){
        return UserRoleDto.valueOf(entity.toString());
    }
}
