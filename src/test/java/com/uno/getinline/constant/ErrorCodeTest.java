package com.uno.getinline.constant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("에러 처리 - 에러 코드")
class ErrorCodeTest {

    @DisplayName("예외를 받으면, 예외 메시지가 포함된 메시지 출력")
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0} ===> {1}")
    void givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage(ErrorCode sut, String expected) {
        // Given
        Exception e = new Exception("This is test message.");

        // When
        String actual = sut.getMessage(e);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenExceptionWithMessage_whenGettingMessage_thenReturnsMessage() {
        return Stream.of(
                arguments(ErrorCode.OK, "Ok - This is test message."),
                arguments(ErrorCode.BAD_REQUEST, "Bad request - This is test message."),
                arguments(ErrorCode.SPRING_BAD_REQUEST, "Spring-detected bad request - This is test message."),
                arguments(ErrorCode.VALIDATION_ERROR, "Validation error - This is test message."),
                arguments(ErrorCode.NOT_FOUND, "Requested resource is not found - This is test message."),
                arguments(ErrorCode.INTERNAL_ERROR, "Internal error - This is test message."),
                arguments(ErrorCode.SPRING_INTERNAL_ERROR, "Spring-detected internal error - This is test message."),
                arguments(ErrorCode.DATA_ACCESS_ERROR, "Data access error - This is test message.")
        );
    }

    @DisplayName("에러 메시지를 받으면, 해당 에러 메시지로 출력")
    @MethodSource
    @ParameterizedTest(name = "[{index}] \"{0}\" ===> \"{1}\"")
    void givenMessage_whenGettingMessage_thenReturnsMessage(String input, String expected) {
        // Given

        // When
        String actual = ErrorCode.INTERNAL_ERROR.getMessage(input);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenMessage_whenGettingMessage_thenReturnsMessage() {
        return Stream.of(
                arguments(null, ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("   ", ErrorCode.INTERNAL_ERROR.getMessage()),
                arguments("This is test message.", "This is test message.")
        );
    }

    @DisplayName("toString() 호출 포맷")
    @Test
    void givenErrorCode_whenToString_thenReturnsSimplifiedToString() {
        // Given

        // When
        String result = ErrorCode.INTERNAL_ERROR.toString();

        // Then
        assertThat(result).isEqualTo("INTERNAL_ERROR (20000)");
    }

}
