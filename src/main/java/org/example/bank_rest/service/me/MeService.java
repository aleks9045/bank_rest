package org.example.bank_rest.service.me;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.mapper.CardMapper;
import org.example.bank_rest.mapper.UserMapper;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.persistence.model.filter.TransactionFilter;
import org.example.bank_rest.persistence.repository.CardRepository;
import org.example.bank_rest.persistence.repository.TransactionRepository;
import org.example.bank_rest.persistence.specificationBuilder.CardSpecificationBuilder;
import org.example.bank_rest.persistence.specificationBuilder.TransactionSpecificationBuilder;
import org.example.bank_rest.service.card.CardService;
import org.example.bank_rest.service.transaction.TransactionService;
import org.example.bank_rest.service.user.UserValidator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MeService {

    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final CardService cardService;
    private final TransactionService transactionService;


    public UserViewDto getMe() {
        var user = userValidator.getUserFromSecurityContext();
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public List<CardUserViewDto> getMyCards(CardFilter filter, Pageable pageable) {
        var user = userValidator.getUserFromSecurityContext();
        filter.setUserUuid(user.getUuid());
        return cardService.getUserCards(filter, pageable);
    }


    @Transactional(readOnly = true)
    public List<TransactionViewDto> getMyTransactions(TransactionFilter filter, Pageable pageable) {
        var user = userValidator.getUserFromSecurityContext();
        filter.setReceiverUuid(user.getUuid());
        filter.setSenderUuid(user.getUuid());
        return transactionService.getTransactions(filter, pageable);
    }


}
