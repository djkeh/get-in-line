package com.uno.getinline.dto;

import com.uno.getinline.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("데이터 - API 기본 응답")
class APIDataResponseTest {

    @DisplayName("문자열 데이터가 주어지면, 표준 성공 응답을 생성한다.")
    @Test
    void givenStringData_whenCreatingResponse_thenReturnsSuccessfulResponse() {
        // Given
        String data = "test data";

        // When
        APIDataResponse<String> response = APIDataResponse.of(data);

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", data);
    }

    @DisplayName("데이터가 없을 때, 비어있는 표준 성공 응답을 생성한다.")
    @Test
    void givenNothing_whenCreatingResponse_thenReturnsEmptySuccessfulResponse() {
        // Given

        // When
        APIDataResponse<String> response = APIDataResponse.empty();

        // Then
        assertThat(response)
                .hasFieldOrPropertyWithValue("success", true)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.OK.getCode())
                .hasFieldOrPropertyWithValue("message", ErrorCode.OK.getMessage())
                .hasFieldOrPropertyWithValue("data", null);
    }

}