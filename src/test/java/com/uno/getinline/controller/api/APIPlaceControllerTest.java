package com.uno.getinline.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uno.getinline.constant.ErrorCode;
import com.uno.getinline.constant.PlaceType;
import com.uno.getinline.dto.PlaceRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("API 컨트롤러가 필요없는 상황이어서 비활성화")
@DisplayName("API 컨트롤러 - 장소")
@WebMvcTest(APIPlaceController.class)
class APIPlaceControllerTest {

    private final MockMvc mvc;
    private final ObjectMapper mapper;

    public APIPlaceControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
            ) {
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("[API][GET] 장소 리스트 조회 - 장소 리스트 데이터를 담은 표준 API 출력")
    @Test
    void givenNothing_whenRequestingPlaces_thenReturnsPlacesInStandardResponse() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/api/places"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].placeType").value(PlaceType.COMMON.name()))
                .andExpect(jsonPath("$.data[0].placeName").value("랄라배드민턴장"))
                .andExpect(jsonPath("$.data[0].address").value("서울시 강남구 강남대로 1234"))
                .andExpect(jsonPath("$.data[0].phoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.data[0].capacity").value(30))
                .andExpect(jsonPath("$.data[0].memo").value("신장개업"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][POST] 장소 생성")
    @Test
    void givenPlace_whenCreatingAPlace_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        PlaceRequest placeRequest = PlaceRequest.of(
                PlaceType.COMMON,
                "랄라배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                30,
                "신장개업"
        );

        // When & Then
        mvc.perform(
                post("/api/places")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(placeRequest))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][GET] 단일 장소 조회 - 장소 있는 경우, 장소 데이터를 담은 표준 API 출력")
    @Test
    void givenPlaceId_whenRequestingExistentPlace_thenReturnsPlaceInStandardResponse() throws Exception {
        // Given
        long placeId = 1L;

        // When & Then
        mvc.perform(get("/api/places/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.placeType").value(PlaceType.COMMON.name()))
                .andExpect(jsonPath("$.data.placeName").value("랄라배드민턴장"))
                .andExpect(jsonPath("$.data.address").value("서울시 강남구 강남대로 1234"))
                .andExpect(jsonPath("$.data.phoneNumber").value("010-1234-5678"))
                .andExpect(jsonPath("$.data.capacity").value(30))
                .andExpect(jsonPath("$.data.memo").value("신장개업"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][GET] 단일 장소 조회 - 장소 없는 경우, 빈 표준 API 출력")
    @Test
    void givenPlaceId_whenRequestingNonexistentPlace_thenReturnsEmptyStandardResponse() throws Exception {
        // Given
        long placeId = 2L;

        // When & Then
        mvc.perform(get("/api/places/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][PUT] 장소 변경")
    @Test
    void givenPlace_whenModifyingAPlace_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        long placeId = 1L;
        PlaceRequest placeRequest = PlaceRequest.of(
                PlaceType.COMMON,
                "랄라배드민턴장",
                "서울시 강남구 강남대로 1234",
                "010-1234-5678",
                30,
                "신장개업"
        );

        // When & Then
        mvc.perform(
                put("/api/places/" + placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(placeRequest))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

    @DisplayName("[API][DELETE] 장소 삭제")
    @Test
    void givenPlace_whenDeletingAPlace_thenReturnsSuccessfulStandardResponse() throws Exception {
        // Given
        long placeId = 1L;

        // When & Then
        mvc.perform(delete("/api/places/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.OK.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorCode.OK.getMessage()));
    }

}
