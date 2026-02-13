package org.example.bank_rest.controller;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.controller.openapi.TransactionApi;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.dto.TransactionCreateDto;
import org.example.bank_rest.persistence.model.filter.TransactionFilter;
import org.example.bank_rest.service.transaction.TransactionService;
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
public class TransactionController implements TransactionApi {

    private final TransactionService transactionService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionViewDto> getTransaction(Long id) {

        var transactionViewDto = transactionService.getTransaction(id);

        return ResponseEntity.ok(transactionViewDto);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TransactionViewDto> createTransaction(TransactionCreateDto transactionCreateDto) {

        var transactionViewDto = transactionService.saveTransaction(transactionCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionViewDto);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionViewDto>> getTransactions(Integer page,
                                                                    Integer size,
                                                                    String sort,
                                                                    Long senderCardId,
                                                                    Long receiverCardId,
                                                                    UUID senderUuid,
                                                                    UUID receiverUuid) {
        var pageable = PageableFactory.getPageable(page, size, sort);
        var transactionFilter = new TransactionFilter(
            senderCardId,
            receiverCardId,
            senderUuid,
            receiverUuid
        );

        var transactions = transactionService.getTransactions(transactionFilter, pageable);
        return ResponseEntity.ok(transactions);
    }
}
