package org.example.bank_rest.service.user;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserPatchDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.mapper.UserMapper;
import org.example.bank_rest.persistence.model.filter.UserFilter;
import org.example.bank_rest.persistence.repository.UserRepository;
import org.example.bank_rest.persistence.specificationBuilder.UserSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserSpecificationBuilder userSpecificationBuilder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    public UserViewDto getUser(UUID uuid) {
        var user = userValidator.getUser(uuid);
        return userMapper.toDto(user);
    }


    @Transactional(readOnly = true)
    public List<UserViewDto> getUsers(UserFilter filter, Pageable pageable) {

        var spec = userSpecificationBuilder.build(filter);

        var page = userRepository.findAll(spec, pageable);

        return page.stream().map(userMapper::toDto).toList();
    }

    @Transactional
    public UserViewDto saveUser(UserCreateDto userCreateDto) {
        var user = userMapper.toEntity(userCreateDto);

        var savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public UserViewDto patchUser(UUID uuid, UserPatchDto userPatchDto) {
        var user = userValidator.getUser(uuid);

        userMapper.patchEntity(userPatchDto, user);

        return userMapper.toDto(user);
    }

    public void deleteUserById(UUID uuid) {
        userRepository.deleteByUuid(uuid);
    }

}
