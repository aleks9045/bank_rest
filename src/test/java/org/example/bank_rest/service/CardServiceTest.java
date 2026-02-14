package org.example.bank_rest.service;

import org.example.bank_rest.dto.CardAdminViewDto;
import org.example.bank_rest.dto.CardBlockDto;
import org.example.bank_rest.dto.CardCreateDto;
import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.mapper.CardMapper;
import org.example.bank_rest.persistence.model.entity.Card;
import org.example.bank_rest.persistence.model.entity.User;
import org.example.bank_rest.persistence.model.filter.CardFilter;
import org.example.bank_rest.persistence.repository.CardRepository;
import org.example.bank_rest.persistence.specificationBuilder.CardSpecificationBuilder;
import org.example.bank_rest.service.card.CardEncryptor;
import org.example.bank_rest.service.card.CardService;
import org.example.bank_rest.service.card.CardValidator;
import org.example.bank_rest.service.user.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardSpecificationBuilder cardSpecificationBuilder;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardMapper cardMapper;
    @Mock
    private CardValidator cardValidator;
    @Mock
    private UserValidator userValidator;
    @Mock
    private CardEncryptor cardEncryptor;

    @InjectMocks
    private CardService cardService;


    @Test
    void getCards_shouldReturnMappedDtos() {

        var filter = new CardFilter();
        var pageable = PageRequest.of(0, 10);

        var spec = mock(Specification.class);
        var card = new Card();
        var dto = new CardAdminViewDto();

        when(cardSpecificationBuilder.build(filter)).thenReturn(spec);
        when(cardRepository.findAll(spec, pageable))
            .thenReturn(new PageImpl<>(List.of(card)));
        when(cardMapper.toAdminDto(card)).thenReturn(dto);

        var result = cardService.getCards(filter, pageable);

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));

        verify(cardSpecificationBuilder).build(filter);
        verify(cardRepository).findAll(spec, pageable);
        verify(cardMapper).toAdminDto(card);
    }
    @Test
    void getDecryptedPan_shouldReturnDecryptedValue() {

        var card = new Card();
        card.setEncryptedPan("encrypted");

        when(cardValidator.getCard(1L)).thenReturn(card);
        when(cardEncryptor.decrypt("encrypted")).thenReturn("decrypted");

        var result = cardService.getDecryptedPan(1L);

        assertEquals("decrypted", result);

        verify(cardValidator).getCard(1L);
        verify(cardEncryptor).decrypt("encrypted");
    }
    @Test
    void getUserCards_shouldSetUserUuidAndReturnDtos() {

        var filter = new CardFilter();
        var pageable = PageRequest.of(0, 10);

        var user = new User();
        user.setUuid(UUID.randomUUID());

        var spec = mock(Specification.class);
        var card = new Card();
        var dto = new CardUserViewDto();

        when(userValidator.getUserFromSecurityContext()).thenReturn(user);
        when(cardSpecificationBuilder.build(filter)).thenReturn(spec);
        when(cardRepository.findAll(spec, pageable))
            .thenReturn(new PageImpl<>(List.of(card)));
        when(cardMapper.toUserDto(card)).thenReturn(dto);

        var result = cardService.getUserCards(filter, pageable);

        assertEquals(user.getUuid(), filter.getUserUuid());
        assertEquals(1, result.size());
    }
    @Test
    void saveCard_shouldSaveAndReturnDto() {

        var uuid = UUID.randomUUID();
        var dto = new CardCreateDto();
        var card = new Card();
        var saved = new Card();
        var adminDto = new CardAdminViewDto();
        var user = new User();

        when(cardMapper.toEntity(dto)).thenReturn(card);
        when(cardEncryptor.encrypt(any())).thenReturn("encrypted");
        when(userValidator.getUser(uuid)).thenReturn(user);
        when(cardRepository.save(card)).thenReturn(saved);
        when(cardMapper.toAdminDto(saved)).thenReturn(adminDto);

        var result = cardService.saveCard(uuid, dto);

        assertEquals(adminDto, result);

        verify(cardRepository).save(card);
        verify(cardEncryptor).encrypt(any());
    }
    @Test
    void needToBlock_shouldUpdateFlag() {

        var id = 1L;
        var dto = new CardBlockDto();
        dto.setNeedToBlock(true);

        var uuid = UUID.randomUUID();

        var user = new User();
        user.setUuid(uuid);

        var owner = new User();
        owner.setUuid(uuid);

        var card = new Card();
        card.setOwner(owner);

        var responseDto = new CardUserViewDto();

        when(userValidator.getUserFromSecurityContext()).thenReturn(user);
        when(cardValidator.getCardWithOwner(id)).thenReturn(card);
        when(cardMapper.toUserDto(card)).thenReturn(responseDto);

        var result = cardService.needToBlock(id, dto);

        assertTrue(card.getNeedToBlock());
        assertEquals(responseDto, result);
    }
    @Test
    void needToBlock_shouldThrowAccessDenied() {

        var id = 1L;
        var dto = new CardBlockDto();
        dto.setNeedToBlock(true);

        var user = new User();
        user.setUuid(UUID.randomUUID());

        var owner = new User();
        owner.setUuid(UUID.randomUUID());

        var card = new Card();
        card.setOwner(owner);

        when(userValidator.getUserFromSecurityContext()).thenReturn(user);
        when(cardValidator.getCardWithOwner(id)).thenReturn(card);

        assertThrows(AccessDeniedException.class,
            () -> cardService.needToBlock(id, dto));
    }
    @Test
    void deleteCardById_shouldCallRepository() {

        cardService.deleteCardById(1L);

        verify(cardRepository).deleteById(1L);
    }

}

