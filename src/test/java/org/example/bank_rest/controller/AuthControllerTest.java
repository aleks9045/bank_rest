package org.example.bank_rest.controller;


import org.example.bank_rest.dto.*;
import org.example.bank_rest.security.filter.CustomAuthFilter;
import org.example.bank_rest.service.auth.AuthService;
import org.example.bank_rest.util.JwtCookieManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtCookieManager authCookieFabric;
    @MockitoBean
    private CustomAuthFilter authFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/api/v1/auth/";

    @Test
    void login_shouldReturn200_andSetCookies() throws Exception {

        var loginDto = new UserLoginDto();
        loginDto.setEmail("test@mail.com");
        loginDto.setPassword("password");

        var tokens = new JwtTokensDto("access-token", "refresh-token");

        var accessCookie = ResponseCookie.from("access", "access-token").build();
        var refreshCookie = ResponseCookie.from("refresh", "refresh-token").build();

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        when(authService.getTokens(any())).thenReturn(tokens);
        when(authCookieFabric.createAccessCookie(any())).thenReturn(accessCookie);
        when(authCookieFabric.createRefreshCookie(any())).thenReturn(refreshCookie);
        when(authCookieFabric.getCookiesInHeaders(any())).thenReturn(headers);

        mockMvc.perform(post(BASE_URL + "login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.SET_COOKIE));

        verify(authService).getTokens(any());
    }

    @Test
    void register_shouldReturn201_andUser() throws Exception {

        var createDto = new UserCreateDto();
        createDto.setEmail("new@mail.com");
        createDto.setPassword("Asdf1234!");
        createDto.setFirstName("name1");
        createDto.setLastName("name2");

        var userView = new UserViewDto();
        userView.setEmail("new@mail.com");
        userView.setFirstName("name1");
        userView.setLastName("name2");
        userView.setRole(UserRoleDto.ROLE_USER);

        when(authService.saveUser(any())).thenReturn(userView);

        mockMvc.perform(post(BASE_URL + "register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
                .accept(MediaType.APPLICATION_JSON))

            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value("new@mail.com"))
            .andExpect(jsonPath("$.first_name").value("name1"))
            .andExpect(jsonPath("$.last_name").value("name2"))
            .andExpect(jsonPath("$.password").doesNotExist());

        verify(authService).saveUser(any());
    }

    @Test
    void logout_shouldReturn200_andDeleteCookies() throws Exception {

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "access=; Max-Age=0");

        when(authCookieFabric.deleteCookiesInHeaders()).thenReturn(headers);

        mockMvc.perform(post(BASE_URL + "logout"))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.SET_COOKIE));

        verify(authCookieFabric).deleteCookiesInHeaders();
    }
}
