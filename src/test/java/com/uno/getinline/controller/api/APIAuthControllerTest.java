package com.uno.getinline.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.dto.AdminRequest;
import com.uno.getinline.dto.LoginRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("API 컨트롤러가 필요없는 상황이어서 비활성화")
@DisplayName("API 컨트롤러 - 인증")
@WebMvcTest(APIAuthController.class)
class APIAuthControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    public APIAuthControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
    ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[API][POST] 관리자 가입 - 정상 입력하면 회원 정보를 추가하고 안내 메시지 리턴")
    @Test
    void givenAdminDetails_whenSigningUp_thenCreatesAdminAndReturns() throws Exception {
        // Given
        AdminRequest adminRequest = AdminRequest.of(
                "test@test.com",
                "testNick",
                "passwd",
                "010-1234-5678",
                "test memo"
        );

        // When & Then
        mvc.perform(
                post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(adminRequest))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][POST] 로그인 - 존재하는 유저 정보로 인증 요청하면 인증 통과")
    @Test
    void givenUsernameAndPassword_whenLoggingIn_thenCreatesAdminAndReturns() throws Exception {
        // Given
        LoginRequest loginRequest = LoginRequest.of(
                "test@test.com",
                "passwd"
        );

        // When & Then
        mvc.perform(
                post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

}
