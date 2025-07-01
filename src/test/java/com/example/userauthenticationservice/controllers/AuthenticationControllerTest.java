package com.example.userauthenticationservice.controllers;

import com.example.userauthenticationservice.dtos.LoginRequestDto;
import com.example.userauthenticationservice.dtos.SignupRequestDto;
import com.example.userauthenticationservice.dtos.ValidateTokenDto;
import com.example.userauthenticationservice.exceptions.PasswordDoesNotMatchException;
import com.example.userauthenticationservice.exceptions.SessionNotFoundException;
import com.example.userauthenticationservice.exceptions.UserAlreadyExistsException;
import com.example.userauthenticationservice.exceptions.UserDoesNotExistException;
import com.example.userauthenticationservice.models.User;
import com.example.userauthenticationservice.services.IAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.misc.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @MockBean
    private IAuthenticationService authenticationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testSignUp_SignUpsSuccessfully() throws Exception {
        User user = new User();
        user.setEmail("abcd");
        user.setPassword("abcd");
        user.setRoles(new ArrayList<>());

        when(authenticationService.signUp(anyString(), anyString(), anyList())).thenReturn(user);

        SignupRequestDto signupRequestDto = new SignupRequestDto();
        signupRequestDto.setEmail("abcd");
        signupRequestDto.setPassword("abcd");
        signupRequestDto.setRoles(new ArrayList<>());

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("abcd")); // ✅ assert response content
    }

    @Test
    public void testSignUp_ThrowsUserAlreadyExistsException() throws Exception {
        SignupRequestDto requestDto = new SignupRequestDto();
        requestDto.setEmail("email");
        requestDto.setPassword("password");
        requestDto.setRoles(new ArrayList<>());

        when(authenticationService.signUp(anyString(), anyString(), anyList()))
                .thenThrow(new UserAlreadyExistsException("User already exists"));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));
    }

    @Test
    public void testLogin_LogsInSuccessfully() throws Exception {
        User user = new User();
        user.setEmail("abcd");
        user.setPassword("abcd");
        user.setRoles(new ArrayList<>());

        Pair<User, String> pair = new Pair<>(user, "mock-token");

        when(authenticationService.login(anyString(), anyString())).thenReturn(pair);

        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("abcd");
        loginRequestDto.setPassword("abcd");

        mockMvc.perform(post("/auth/login").content(objectMapper.writeValueAsString(loginRequestDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("abcd"));
    }


    @Test
    public void Test_Login_ThrowsUserDoesNotExistException() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("abcd");
        loginRequestDto.setPassword("abcd");

        when(authenticationService.login(anyString(), anyString())).thenThrow(new UserDoesNotExistException("User does not exist"));

        mockMvc.perform(post("/auth/login").content(objectMapper.writeValueAsString(loginRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(content().string("User does not exist"));}

    @Test
    public void Test_Login_ThrowsPasswordDoesNotMatchException() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setEmail("abcd");
        loginRequestDto.setPassword("abcd");

        when(authenticationService.login(anyString(), anyString())).thenThrow(new PasswordDoesNotMatchException("Wrong password"));

        mockMvc.perform(post("/auth/login").content(objectMapper.writeValueAsString(loginRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string("Wrong password"));
    }

    @Test
    public void Test_ValidateToken_ValidatesTokenSuccessfully() throws Exception {
        boolean response = true;

        when(authenticationService.validateToken(anyString(), eq(1L))).thenReturn(response);

        ValidateTokenDto tokenDto = new ValidateTokenDto();
        tokenDto.setToken("abcd");
        tokenDto.setUserId(1L);

        mockMvc.perform(post("/auth/validate").content(objectMapper.writeValueAsString(tokenDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.response").value(true));


    }


    @Test
    public void Test_ValidateToken_ThrowsSessionNotFoundException() throws Exception {
        ValidateTokenDto tokenDto = new ValidateTokenDto();
        tokenDto.setToken("abcd");
        tokenDto.setUserId(1L);

        when(authenticationService.validateToken(anyString(), eq(1L))).thenThrow(new SessionNotFoundException("Session does not exist"));

        mockMvc.perform(post("/auth/validate").content(objectMapper.writeValueAsString(tokenDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Session does not exist"));
    }
}
