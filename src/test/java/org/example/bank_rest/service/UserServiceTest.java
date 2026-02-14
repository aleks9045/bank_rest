package org.example.bank_rest.service;

import org.example.bank_rest.dto.UserCreateDto;
import org.example.bank_rest.dto.UserPatchDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.mapper.UserMapper;
import org.example.bank_rest.persistence.model.entity.User;
import org.example.bank_rest.persistence.model.filter.UserFilter;
import org.example.bank_rest.persistence.repository.UserRepository;
import org.example.bank_rest.persistence.specificationBuilder.UserSpecificationBuilder;
import org.example.bank_rest.service.user.UserService;
import org.example.bank_rest.service.user.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserSpecificationBuilder userSpecificationBuilder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserService userService;

    @Test
    void getUser_shouldReturnMappedDto() {

        var uuid = UUID.randomUUID();
        var user = new User();
        var dto = new UserViewDto();

        when(userValidator.getUser(uuid)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        var result = userService.getUser(uuid);

        assertEquals(dto, result);

        verify(userValidator).getUser(uuid);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUsers_shouldReturnMappedDtos() {

        var filter = new UserFilter();
        var pageable = PageRequest.of(0, 10);

        var spec = mock(Specification.class);
        var user = new User();
        var dto = new UserViewDto();

        when(userSpecificationBuilder.build(filter)).thenReturn(spec);
        when(userRepository.findAll(spec, pageable))
            .thenReturn(new PageImpl<>(List.of(user)));
        when(userMapper.toDto(user)).thenReturn(dto);

        var result = userService.getUsers(filter, pageable);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(userSpecificationBuilder).build(filter);
        verify(userRepository).findAll(spec, pageable);
    }

    @Test
    void saveUser_shouldSaveAndReturnDto() {

        var createDto = new UserCreateDto();
        var user = new User();
        var savedUser = new User();
        var viewDto = new UserViewDto();

        when(userMapper.toEntity(createDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(viewDto);

        var result = userService.saveUser(createDto);

        assertEquals(viewDto, result);

        verify(userMapper).toEntity(createDto);
        verify(userRepository).save(user);
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void patchUser_shouldPatchAndReturnDto() {

        var uuid = UUID.randomUUID();
        var patchDto = new UserPatchDto();
        var user = new User();
        var viewDto = new UserViewDto();

        when(userValidator.getUser(uuid)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(viewDto);

        var result = userService.patchUser(uuid, patchDto);

        verify(userValidator).getUser(uuid);
        verify(userMapper).patchEntity(patchDto, user);
        verify(userMapper).toDto(user);

        assertEquals(viewDto, result);
    }

    @Test
    void deleteUserById_shouldCallRepository() {

        var uuid = UUID.randomUUID();

        userService.deleteUserById(uuid);

        verify(userRepository).deleteByUuid(uuid);
    }

}

