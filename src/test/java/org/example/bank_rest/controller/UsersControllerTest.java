package org.example.bank_rest.controller;

import org.example.bank_rest.dto.CardUserViewDto;
import org.example.bank_rest.dto.UserPatchDto;
import org.example.bank_rest.dto.UserRoleDto;
import org.example.bank_rest.dto.UserViewDto;
import org.example.bank_rest.security.filter.CustomAuthFilter;
import org.example.bank_rest.service.card.CardService;
import org.example.bank_rest.service.me.MeService;
import org.example.bank_rest.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeService meService;
    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private CustomAuthFilter customAuthFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/user/";

    private static final UUID UUID_VALUE = UUID.randomUUID();


    @Test
    void deleteUser_shouldReturn204() throws Exception {

        doNothing().when(userService).deleteUserById(any());

        mockMvc.perform(delete(BASE_URL + UUID_VALUE))
            .andExpect(status().isNoContent());

        verify(userService).deleteUserById(UUID_VALUE);
    }

    @Test
    void getUser_shouldReturn200_andUser() throws Exception {

        var userView = new UserViewDto();
        userView.setEmail("new@mail.com");
        userView.setFirstName("name1");
        userView.setRole(UserRoleDto.ROLE_USER);

        when(userService.getUser(any())).thenReturn(userView);

        mockMvc.perform(get(BASE_URL + UUID_VALUE)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("new@mail.com"))
            .andExpect(jsonPath("$.first_name").value("name1"))
            .andExpect(jsonPath("$.role").value(UserRoleDto.ROLE_USER.toString()))
            .andExpect(jsonPath("$.password").doesNotExist());

        verify(userService).getUser(UUID_VALUE);
    }

    @Test
    void getMe_shouldReturn200_andUser() throws Exception {

        var userView = new UserViewDto();
        userView.setEmail("me@mail.com");
        userView.setFirstName("me");

        when(meService.getMe()).thenReturn(userView);

        mockMvc.perform(get(BASE_URL + "me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("me@mail.com"));

        verify(meService).getMe();
    }


    @Test
    void getMyCards_shouldReturn200_andCards() throws Exception {

        var card = new CardUserViewDto();
        card.setBalance(BigDecimal.TEN);
        card.setId(1L);

        var cards = List.of(card);

        when(cardService.getUserCards(any(), any())).thenReturn(cards);

        mockMvc.perform(get(BASE_URL + "cards")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].balance").value(BigDecimal.TEN.toString()))
            .andExpect(jsonPath("$[0].id").value("1"));

        verify(cardService).getUserCards(any(), any());
    }


    @Test
    void getUsers_shouldReturn200_andUsers() throws Exception {

        var user = new UserViewDto();
        user.setEmail("admin@mail.com");

        when(userService.getUsers(any(), any()))
            .thenReturn(List.of(user));

        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("admin@mail.com"));

        verify(userService).getUsers(any(), any());
    }

    @Test
    void patchUser_shouldReturn200_andUpdatedUser() throws Exception {

        var patchDto = new UserPatchDto();
        patchDto.setFirstName("updated");

        var responseDto = new UserViewDto();
        responseDto.setFirstName("updated");

        when(userService.patchUser(any(), any()))
            .thenReturn(responseDto);

        mockMvc.perform(patch(BASE_URL + UUID_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.first_name").value("updated"));

        verify(userService).patchUser(eq(UUID_VALUE), any());
    }
}
