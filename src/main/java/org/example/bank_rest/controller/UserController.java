package org.example.bank_rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.controller.openapi.UserApi;
import org.example.bank_rest.dto.UserPatchDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.persistence.model.filter.UserFilter;
import org.example.bank_rest.service.user.UserService;
import org.example.bank_rest.util.PageableFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController("api/v1/")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(UUID uuid) {

        userService.deleteUserById(uuid);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<UserViewDto> getUser(UUID uuid) {

        var userViewDto = userService.getUser(uuid);

        return ResponseEntity.ok(userViewDto);
    }

    @Override
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<List<UserViewDto>> getUsers(Integer page, Integer size, String sort, String email, String firstName, String lastName) {

        var pageable = PageableFactory.getPageable(page, size, sort);
        var userFilter = new UserFilter(email, firstName, lastName);

        var users = userService.getUsers(userFilter, pageable);
        return ResponseEntity.ok(users);
    }

    @Override
    @PreAuthorize("hasRole('DEVELOPER')")
    public ResponseEntity<UserViewDto> patchUser(UUID uuid, UserPatchDto userPatchDto) {
        var userViewDto = userService.patchUser(uuid, userPatchDto);
        return ResponseEntity.ok(userViewDto);
    }
}
