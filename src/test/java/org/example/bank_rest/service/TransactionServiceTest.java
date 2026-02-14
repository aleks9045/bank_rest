package org.example.bank_rest.service;

import org.example.bank_rest.dto.TransactionCreateDto;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.mapper.TransactionMapper;
import org.example.bank_rest.persistence.model.entity.Transaction;
import org.example.bank_rest.persistence.model.filter.TransactionFilter;
import org.example.bank_rest.persistence.repository.TransactionRepository;
import org.example.bank_rest.persistence.specificationBuilder.TransactionSpecificationBuilder;
import org.example.bank_rest.service.transaction.TransactionService;
import org.example.bank_rest.service.transaction.TransactionValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionSpecificationBuilder transactionSpecificationBuilder;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransactionValidator transactionValidator;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getTransactions_shouldReturnMappedDtos() {

        var filter = new TransactionFilter();
        var pageable = PageRequest.of(0, 10);

        var spec = mock(Specification.class);
        var transaction = new Transaction();
        var dto = new TransactionViewDto();

        when(transactionSpecificationBuilder.build(filter)).thenReturn(spec);
        when(transactionRepository.findAll(spec, pageable))
            .thenReturn(new PageImpl<>(List.of(transaction)));
        when(transactionMapper.toDto(transaction)).thenReturn(dto);

        var result = transactionService.getTransactions(filter, pageable);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(transactionSpecificationBuilder).build(filter);
        verify(transactionRepository).findAll(spec, pageable);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    void getTransaction_shouldReturnMappedDto() {

        var id = 1L;
        var transaction = new Transaction();
        var dto = new TransactionViewDto();

        when(transactionValidator.getTransaction(id)).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(dto);

        var result = transactionService.getTransaction(id);

        assertEquals(dto, result);

        verify(transactionValidator).getTransaction(id);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    void saveTransaction_shouldValidateSaveAndReturnDto() {

        var createDto = new TransactionCreateDto();
        var entity = new Transaction();
        var saved = new Transaction();
        var viewDto = new TransactionViewDto();

        when(transactionMapper.toEntity(createDto)).thenReturn(entity);
        when(transactionRepository.save(entity)).thenReturn(saved);
        when(transactionMapper.toDto(saved)).thenReturn(viewDto);

        var result = transactionService.saveTransaction(createDto);

        assertEquals(viewDto, result);

        verify(transactionValidator).makeMeansTransfer(createDto);
        verify(transactionMapper).toEntity(createDto);
        verify(transactionRepository).save(entity);
        verify(transactionMapper).toDto(saved);
    }

    @Test
    void saveTransaction_shouldThrowException_whenValidationFails() {

        var createDto = new TransactionCreateDto();

        doThrow(new RuntimeException("Validation failed"))
            .when(transactionValidator)
            .makeMeansTransfer(createDto);

        assertThrows(RuntimeException.class,
            () -> transactionService.saveTransaction(createDto));

        verify(transactionRepository, never()).save(any());
    }

}

