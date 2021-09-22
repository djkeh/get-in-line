package com.uno.getinline.controller.error;

import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.exception.GeneralException;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("핸들러 - 기본 에러 처리")
class BaseExceptionHandlerTest {

    private BaseExceptionHandler sut;

    @BeforeEach
    void setUp() {
        sut = new BaseExceptionHandler();

    }

    @DisplayName("프로젝트 일반 오류 - 응답 정의")
    @Test
    void givenGeneralException_whenHandlingException_thenReturnsModelAndView() {
        // Given
        ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
        GeneralException e = new GeneralException(errorCode);

        // When
        ModelAndView result = sut.general(e);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("viewName", "error")
                .extracting(ModelAndView::getModel, as(InstanceOfAssertFactories.MAP))
                .containsEntry("statusCode", errorCode.getHttpStatus().value())
                .containsEntry("errorCode", errorCode)
                .containsEntry("message", errorCode.getMessage());
    }

    @DisplayName("기타(전체) 오류 - 응답 정의")
    @Test
    void givenOtherException_whenHandlingException_thenReturnsModelAndView() {
        // Given
        Exception e = new Exception("This is error message.");

        // When
        ModelAndView result = sut.exception(e);

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("viewName", "error")
                .extracting(ModelAndView::getModel, as(InstanceOfAssertFactories.MAP))
                .containsEntry("statusCode", ErrorCode.INTERNAL_ERROR.getHttpStatus().value())
                .containsEntry("errorCode", ErrorCode.INTERNAL_ERROR)
                .containsEntry("message", ErrorCode.INTERNAL_ERROR.getMessage(e));
    }

}
