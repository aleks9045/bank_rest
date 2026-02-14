package org.example.bank_rest.controller;

import org.example.bank_rest.dto.CardAdminViewDto;
import org.example.bank_rest.dto.CardCreateDto;
import org.example.bank_rest.dto.CardPatchDto;
import org.example.bank_rest.dto.CardStatusDto;
import org.example.bank_rest.security.filter.CustomAuthFilter;
import org.example.bank_rest.service.card.CardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.UUID;

@WebMvcTest(CardController.class)
@AutoConfigureMockMvc(addFilters = false)
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CardService cardService;
    @MockitoBean
    private CustomAuthFilter authFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/card";

    @Test
    void getCard_shouldReturnCard() throws Exception {

        var id = 1L;

        var dto = new CardAdminViewDto();
        dto.setId(id);
        dto.setBalance(BigDecimal.valueOf(1500.75));
        dto.setLast4(1234);
        dto.setStatus(CardStatusDto.ACTIVE);

        when(cardService.getCard(id)).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.balance").value(1500.75))
            .andExpect(jsonPath("$.last4").value(1234))
            .andExpect(jsonPath("$.status").value("ACTIVE"));

        verify(cardService).getCard(id);
    }

    @Test
    void deleteCard_shouldReturn204() throws Exception {

        var id = 10L;

        doNothing().when(cardService).deleteCardById(id);

        mockMvc.perform(delete(BASE_URL + "/{id}", id))
            .andExpect(status().isNoContent());

        verify(cardService).deleteCardById(id);
    }

    @Test
    void createCard_shouldReturn201() throws Exception {

        var uuid = UUID.randomUUID();

        var createDto = new CardCreateDto();
        createDto.setExpiryMonth(12);
        createDto.setExpiryYear(2028);

        var responseDto = new CardAdminViewDto();
        responseDto.setId(5L);
        responseDto.setLast4(1234);

        when(cardService.saveCard(eq(uuid), any(CardCreateDto.class)))
            .thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/user/{uuid}/card", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(5L));

        verify(cardService).saveCard(eq(uuid), any(CardCreateDto.class));
    }

    @Test
    void getCardPan_shouldReturnPan() throws Exception {

        var id = 1L;
        var pan = "1234567812341234";

        when(cardService.getDecryptedPan(id)).thenReturn(pan);

        mockMvc.perform(get(BASE_URL + "/{id}/pan", id))
            .andExpect(status().isOk())
            .andExpect(content().string(pan));

        verify(cardService).getDecryptedPan(id);
    }

    @Test
    void patchCard_shouldReturnUpdatedCard() throws Exception {

        var id = 1L;

        var patchDto = new CardPatchDto();
        patchDto.setNeedToBlock(false);
        patchDto.setStatus(CardStatusDto.BLOCKED);

        var responseDto = new CardAdminViewDto();
        responseDto.setId(id);
        responseDto.setNeedToBlock(false);
        responseDto.setStatus(CardStatusDto.BLOCKED);

        when(cardService.patchCard(eq(id), any(CardPatchDto.class)))
            .thenReturn(responseDto);

        mockMvc.perform(patch(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.need_to_block").value(false))
            .andExpect(jsonPath("$.status").value(CardStatusDto.BLOCKED.toString()));

        verify(cardService).patchCard(eq(id), any(CardPatchDto.class));
    }

}

