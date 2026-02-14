package org.example.bank_rest.service.transaction;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.dto.TransactionCreateDto;
import org.example.bank_rest.mapper.TransactionMapper;
import org.example.bank_rest.persistence.model.filter.TransactionFilter;
import org.example.bank_rest.persistence.repository.TransactionRepository;
import org.example.bank_rest.persistence.specificationBuilder.TransactionSpecificationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionSpecificationBuilder transactionSpecificationBuilder;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionValidator transactionValidator;

    @Transactional(readOnly = true)
    public List<TransactionViewDto> getTransactions(TransactionFilter filter, Pageable pageable) {

        var spec = transactionSpecificationBuilder.build(filter);

        var page = transactionRepository.findAll(spec, pageable);

        return page.stream().map(transactionMapper::toDto).toList();
    }


    @Transactional(readOnly = true)
    public TransactionViewDto getTransaction(Long id) {
        var transaction = transactionValidator.getTransaction(id);
        return transactionMapper.toDto(transaction);
    }

    @Transactional
    public TransactionViewDto saveTransaction(TransactionCreateDto dto) {

        transactionValidator.makeMeansTransfer(dto);

        var transaction = transactionMapper.toEntity(dto);
        var saved = transactionRepository.save(transaction);

        return transactionMapper.toDto(saved);
    }
}
