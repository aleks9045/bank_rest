package org.example.bank_rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.controller.openapi.CardApi;
import org.example.bank_rest.dto.*;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.service.card.CardService;
import org.example.bank_rest.util.PageableFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping(value = "api/v1/")
@RequiredArgsConstructor
public class CardController implements CardApi {

    private final CardService cardService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCard(Long id) {

        cardService.deleteCardById(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardAdminViewDto> getCard(Long id) {

        var cardViewDto = cardService.getCard(id);

        return ResponseEntity.ok(cardViewDto);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardAdminViewDto> createCard(UUID uuid, CardCreateDto cardCreateDto) {

        var cardViewDto = cardService.saveCard(uuid, cardCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(cardViewDto);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Object> getCardPan(Long id) {
        var pan = cardService.getDecryptedPan(id);
        return ResponseEntity.ok(pan);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CardAdminViewDto>> getCards(Integer page,
                                                           Integer size,
                                                           String sort,
                                                           UUID userUuid,
                                                           CardStatusDto status) {

        var pageable = PageableFactory.getPageable(page, size, sort);
        var cardFilter = new CardFilter(userUuid, status);

        var cards = cardService.getCards(cardFilter, pageable);
        return ResponseEntity.ok(cards);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardAdminViewDto> patchCard(Long id, CardPatchDto cardPatchDto) {
        var cardViewDto = cardService.patchCard(id, cardPatchDto);
        return ResponseEntity.ok(cardViewDto);
    }
}
