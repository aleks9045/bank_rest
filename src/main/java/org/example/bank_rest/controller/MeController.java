package org.example.bank_rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.controller.openapi.MeApi;
import org.example.bank_rest.dto.CardStatusDto;
import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.persistence.model.entity.Transaction;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.persistence.model.filter.TransactionFilter;
import org.example.bank_rest.service.me.MeService;
import org.example.bank_rest.util.PageableFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/")
@RequiredArgsConstructor
public class MeController implements MeApi {

    private final MeService meService;

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserViewDto> getMe() {

        var userViewDto = meService.getMe();

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

        var cards = meService.getMyCards(cardFilter, pageable);
        return ResponseEntity.ok(cards);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TransactionViewDto>> getMyTransactions(Integer page,
                                                                      Integer size,
                                                                      String sort,
                                                                      Long senderCardId,
                                                                      Long receiverCardId) {

        var pageable = PageableFactory.getPageable(page, size, sort);
        var transactionFilter = TransactionFilter.builder()
            .fromCardId(senderCardId)
            .toCardId(receiverCardId)
            .build();

        var transactions = meService.getMyTransactions(transactionFilter, pageable);
        return ResponseEntity.ok(transactions);
    }
}
