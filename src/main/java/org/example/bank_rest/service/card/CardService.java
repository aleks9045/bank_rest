package org.example.bank_rest.service.card;

import lombok.RequiredArgsConstructor;
import org.example.bank_rest.dto.CardAdminViewDto;
import org.example.bank_rest.dto.CardCreateDto;
import org.example.bank_rest.dto.CardPatchDto;
import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.mapper.CardMapper;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.persistence.repository.CardRepository;
import org.example.bank_rest.persistence.specificationBuilder.CardSpecificationBuilder;
import org.example.bank_rest.service.user.UserValidator;
import org.example.bank_rest.util.CardPanGenerator;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CardService {

    private final CardSpecificationBuilder cardSpecificationBuilder;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final CardValidator cardValidator;
    private final UserValidator userValidator;
    private final CardEncryptor cardEncryptor;

    @Transactional(readOnly = true)
    public List<CardAdminViewDto> getCards(CardFilter filter, Pageable pageable) {

        var spec = cardSpecificationBuilder.build(filter);

        var page = cardRepository.findAll(spec, pageable);

        return page.stream().map(cardMapper::toAdminDto).toList();
    }

    @Transactional(readOnly = true)
    public String getDecryptedPan(Long id) {
        var card = cardValidator.getCard(id);
        return cardEncryptor.decrypt(card.getEncryptedPan());
    }

    @Transactional(readOnly = true)
    public List<CardUserViewDto> getUserCards(CardFilter filter, Pageable pageable) {
        var user = userValidator.getUserFromSecurityContext();
        filter.setUserUuid(user.getUuid());
        var spec = cardSpecificationBuilder.build(filter);

        var page = cardRepository.findAll(spec, pageable);

        return page.stream().map(cardMapper::toUserDto).toList();
    }

    @Transactional(readOnly = true)
    public CardAdminViewDto getCard(Long id) {
        var card = cardValidator.getCardWithOwner(id);
        return cardMapper.toAdminDto(card);
    }

    @Transactional
    public CardAdminViewDto saveCard(UUID uuid, CardCreateDto cardCreateDto) {
        var pan = CardPanGenerator.generatePan();
        var last4 = pan.substring(pan.length() - 4);
        var encryptedPan = cardEncryptor.encrypt(pan);

        var card = cardMapper.toEntity(cardCreateDto);
        card.setEncryptedPan(encryptedPan);
        card.setLast4(last4);

        var owner = userValidator.getUser(uuid);
        card.setOwner(owner);

        var savedCard = cardRepository.save(card);
        return cardMapper.toAdminDto(savedCard);
    }

    @Transactional
    public CardAdminViewDto patchCard(Long id, CardPatchDto cardPatchDto) {
        var card = cardValidator.getCard(id);
        cardMapper.patchEntity(cardPatchDto, card);
        return cardMapper.toAdminDto(card);
    }

    public void deleteCardById(Long id) {
        cardRepository.deleteById(id);
    }

}
