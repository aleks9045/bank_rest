package org.example.bank_rest.mapper;


import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserPatchDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.persistence.model.entity.User;
import org.mapstruct.*;


@Mapper(uses = {UserRoleMapper.class})
public interface UserMapper {


    User toEntity(UserCreateDto dto);

    UserViewDto toDto(User dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchEntity(UserPatchDto dto, @MappingTarget User entity);
}
