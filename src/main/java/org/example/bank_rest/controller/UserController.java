package org.example.bank_rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.controller.openapi.UserApi;
import org.example.bank_rest.dto.*;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.persistence.model.filter.UserFilter;
import org.example.bank_rest.service.card.CardService;
import org.example.bank_rest.service.user.UserService;
import org.example.bank_rest.service.user.UserValidator;
import org.example.bank_rest.util.PageableFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "api/v1")
@RequiredArgsConstructor
public class UserController implements UserApi {

    private final UserService userService;
    private final CardService cardService;

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteUser(UUID uuid) {

        userService.deleteUserById(uuid);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserViewDto> getUser(UUID uuid) {

        var userViewDto = userService.getUser(uuid);

        return ResponseEntity.ok(userViewDto);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserViewDto> getMe() {

        var userViewDto = userService.getMe();

        return ResponseEntity.ok(userViewDto);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CardUserViewDto>> getMyCards(Integer page,
                                                            Integer size,
                                                            String sort,
                                                            CardStatusDto status) {

        var pageable = PageableFactory.getPageable(page, size, sort);
        var cardFilter = CardFilter.builder()
            .status(status)
            .build();

        var cards = cardService.getUserCards(cardFilter, pageable);
        return ResponseEntity.ok(cards);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserViewDto>> getUsers(Integer page, Integer size, String sort, String email, String firstName, String lastName) {

        var pageable = PageableFactory.getPageable(page, size, sort);
        var userFilter = new UserFilter(email, firstName, lastName);

        var users = userService.getUsers(userFilter, pageable);
        return ResponseEntity.ok(users);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserViewDto> patchUser(UUID uuid, UserPatchDto userPatchDto) {
        var userViewDto = userService.patchUser(uuid, userPatchDto);
        return ResponseEntity.ok(userViewDto);
    }
}
