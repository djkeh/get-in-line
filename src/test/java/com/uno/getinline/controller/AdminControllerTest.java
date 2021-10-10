package com.uno.getinline.controller;

import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.constant.PlaceType;
import com.uno.getinline.domain.Place;
import com.uno.getinline.dto.EventDto;
import com.uno.getinline.dto.PlaceDto;
import com.uno.getinline.service.EventService;
import com.uno.getinline.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 어드민")
@WebMvcTest(AdminController.class)
class AdminControllerTest {

    private final MockMvc mvc;

    @MockBean private EventService eventService;
    @MockBean private PlaceService placeService;

    public AdminControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 리스트 뷰")
    @Test
    void givenQueryParams_whenRequestingAdminPlacesPage_thenReturnsAdminPlacesPage() throws Exception {
        // Given
        given(placeService.getPlaces(any())).willReturn(List.of());

        // When & Then
        mvc.perform(
                get("/admin/places")
                        .queryParam("placeType", PlaceType.SPORTS.name())
                        .queryParam("placeName", "랄라배드민턴장")
                        .queryParam("address", "서울시 강남구 강남대로 1234")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/places"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("places"))
                .andExpect(model().attributeExists("placeType"));

        then(placeService).should().getPlaces(any());
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 세부 정보 뷰")
    @Test
    void givenPlaceId_whenRequestingAdminPlaceDetailPage_thenReturnsAdminPlaceDetailPage() throws Exception {
        // Given
        long placeId = 1L;
        given(placeService.getPlace(placeId)).willReturn(Optional.of(
                PlaceDto.of(placeId, null, null, null, null, null, null, null, null)
        ));

        // When & Then
        mvc.perform(get("/admin/places/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/place-detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("placeType"));
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 세부 정보 뷰, 데이터 없음")
    @Test
    void givenNonexistentPlaceId_whenRequestingAdminPlaceDetailPage_thenReturnsErrorPage() throws Exception {
        // Given
        long placeId = 1L;
        given(placeService.getPlace(placeId)).willReturn(Optional.empty());

        // When & Then
        mvc.perform(get("/admin/places/" + placeId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));
        then(placeService).should().getPlace(placeId);
    }

    @DisplayName("[view][POST] 어드민 페이지 - 장소 세부 정보 뷰, 장소 저장")
    @Test
    void givenNewPlace_whenSavingAdminPlaceDetail_thenSavesPlaceAndReturnsToListPage() throws Exception {
        // Given
        Place
        given(placeService.createPlace(placeId)).willReturn(Optional.empty());

        // When & Then
        mvc.perform(
                post("/admin/places/" + placeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content()
        )
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));
        then(placeService).should().getPlace(placeId);
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 리스트 뷰")
    @Test
    void givenQueryParams_whenRequestingAdminEventsPage_thenReturnsAdminEventsPage() throws Exception {
        // Given
        given(eventService.getEvents(any())).willReturn(List.of());

        // When & Then
        mvc.perform(
                get("/admin/events")
                        .contentType(MediaType.TEXT_HTML)
                        .queryParam("placeId", "1")
                        .queryParam("placeName", "랄라배드민턴장")
                        .queryParam("eventName", "오후 운동")
                        .queryParam("eventStatus", EventStatus.OPENED.name())
                        .queryParam("eventStartDatetime", LocalDateTime.now().minusDays(1).toString())
                        .queryParam("eventEndDatetime", LocalDateTime.now().toString())
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/events"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("events"))
                .andExpect(model().attributeExists("eventStatus"));
        then(eventService).should().getEvents(any());
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 세부 정보 뷰")
    @Test
    void givenEventId_whenRequestingAdminEventDetailPage_thenReturnsAdminEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.of(
                EventDto.of(eventId, null, null, null, null, null, null, null, null, null, null)
        ));

        // When & Then
        mvc.perform(get("/admin/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/event-detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("eventStatus"));
        then(eventService).should().getEvent(eventId);
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 세부 정보 뷰, 데이터 없음")
    @Test
    void givenNonexistentEventId_whenRequestingAdminEventDetailPage_thenReturnsErrorPage() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.getEvent(eventId)).willReturn(Optional.empty());

        // When & Then
        mvc.perform(get("/admin/events/" + eventId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("error"));
        then(eventService).should().getEvent(eventId);
    }

}
