package com.uno.getinline.controller.error;

import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.dto.ApiErrorResponse;
import com.uno.getinline.exception.GeneralException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.handler.DispatcherServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("핸들러 - API 에러 처리")
class ApiExceptionHandlerTest {

    private ApiExceptionHandler sut;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        sut = new ApiExceptionHandler();
        webRequest = new DispatcherServletWebRequest(new MockHttpServletRequest());
    }

    @DisplayName("검증 오류 - 응답 데이터 정의")
    @Test
    void givenValidationException_whenHandlingApiException_thenReturnsResponseEntity() {
        // Given
        ConstraintViolationException e = new ConstraintViolationException(Set.of());

        // When
        ResponseEntity<Object> response = sut.validation(e, webRequest);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, ErrorCode.VALIDATION_ERROR, e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.BAD_REQUEST);
    }

    @DisplayName("프로젝트 일반 오류 - 응답 데이터 정의")
    @Test
    void givenGeneralException_whenHandlingApiException_thenReturnsResponseEntity() {
        // Given
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        GeneralException e = new GeneralException(errorCode);

        // When
        ResponseEntity<Object> response = sut.general(e, webRequest);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, errorCode, e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DisplayName("스프링이 던진 오류 - 응답 데이터 정의")
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0} ===> {1}")
    void givenSpringException_whenHandlingApiException_thenReturnsResponseEntity(Exception e, HttpStatus httpStatus) {
        // Given
        HttpHeaders headers = HttpHeaders.EMPTY;
        ErrorCode errorCode = ErrorCode.valueOf(httpStatus);

        // When
        ResponseEntity<Object> response = sut.handleExceptionInternal(e, null, headers, httpStatus, webRequest);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, errorCode, e))
                .hasFieldOrPropertyWithValue("headers", headers)
                .hasFieldOrPropertyWithValue("statusCode", httpStatus)
                .extracting(ResponseEntity::getBody)
                .hasFieldOrPropertyWithValue("message", errorCode.getMessage() + " - " + e.getMessage());
    }

    static Stream<Arguments> givenSpringException_whenHandlingApiException_thenReturnsResponseEntity() {
        String msg = "test message";

        return Stream.of(
                arguments(new HttpRequestMethodNotSupportedException(HttpMethod.POST.name(), msg), HttpStatus.METHOD_NOT_ALLOWED),
                arguments(new HttpMediaTypeNotSupportedException(msg), HttpStatus.UNSUPPORTED_MEDIA_TYPE),
                arguments(new HttpMediaTypeNotAcceptableException(msg), HttpStatus.NOT_ACCEPTABLE),
                arguments(new ServletRequestBindingException(msg), HttpStatus.BAD_REQUEST),
                arguments(new HttpMessageNotWritableException(msg), HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @DisplayName("기타(전체) 오류 - 응답 데이터 정의")
    @Test
    void givenOtherException_whenHandlingApiException_thenReturnsResponseEntity() {
        // Given
        Exception e = new Exception();

        // When
        ResponseEntity<Object> response = sut.exception(e, webRequest);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("body", ApiErrorResponse.of(false, ErrorCode.INTERNAL_ERROR, e))
                .hasFieldOrPropertyWithValue("headers", HttpHeaders.EMPTY)
                .hasFieldOrPropertyWithValue("statusCode", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
