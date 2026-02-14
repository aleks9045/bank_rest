package org.example.bank_rest.controller;

import org.example.bank_rest.dto.CardStatusDto;
import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.dto.TransactionViewDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.security.filter.CustomAuthFilter;
import org.example.bank_rest.service.me.MeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@WebMvcTest(MeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeService meService;
    @MockitoBean
    private CustomAuthFilter authFilter;

    private static final String BASE_URL = "/api/v1/me";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMe_shouldReturnUser() throws Exception {

        var dto = new UserViewDto();
        dto.setUuid(UUID.randomUUID());
        dto.setEmail("user@test.com");

        when(meService.getMe()).thenReturn(dto);

        mockMvc.perform(get(BASE_URL))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("user@test.com"));

        verify(meService).getMe();
    }

    @Test
    void getMyCards_shouldReturnCards() throws Exception {

        var card = new CardUserViewDto();
        card.setId(1L);
        card.setLast4(1234);
        card.setStatus(CardStatusDto.ACTIVE);

        when(meService.getMyCards(any(), any()))
            .thenReturn(List.of(card));

        mockMvc.perform(get(BASE_URL + "/cards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].last4").value(1234))
            .andExpect(jsonPath("$[0].status").value("ACTIVE"));

        verify(meService).getMyCards(any(), any());
    }

    @Test
    void getMyTransactions_shouldReturnTransactions() throws Exception {

        var tx = new TransactionViewDto();
        tx.setId(10L);
        tx.setAmount(BigDecimal.valueOf(250.50));
        tx.setFromCardId(1L);
        tx.setToCardId(2L);

        when(meService.getMyTransactions(any(), any()))
            .thenReturn(List.of(tx));

        mockMvc.perform(get(BASE_URL + "/transactions")
                .param("page", "0")
                .param("size", "20")
                .param("senderCardId", "1")
                .param("receiverCardId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(10L))
            .andExpect(jsonPath("$[0].amount").value(250.50))
            .andExpect(jsonPath("$[0].from_card_id").value(1L))
            .andExpect(jsonPath("$[0].to_card_id").value(2L))
        ;

        verify(meService).getMyTransactions(any(), any());
    }

}