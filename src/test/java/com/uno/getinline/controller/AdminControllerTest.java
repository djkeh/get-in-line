package com.uno.getinline.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uno.getinline.config.SecurityConfig;
import com.uno.getinline.constant.AdminOperationStatus;
import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.constant.PlaceType;
import com.uno.getinline.dto.*;
import com.uno.getinline.service.EventService;
import com.uno.getinline.service.PlaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 어드민")
@WebMvcTest(
        controllers = AdminController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)
)
class AdminControllerTest {

    private final MockMvc mvc;

    @MockBean private EventService eventService;
    @MockBean private PlaceService placeService;

    private final ObjectMapper mapper;


    public AdminControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper mapper
            ) {
        this.mvc = mvc;
        this.mapper = mapper;
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
                .andExpect(model().attribute("placeTypeOption", PlaceType.values()));

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
        given(eventService.getEvent(eq(placeId), any(PageRequest.class))).willReturn(Page.empty());

        // When & Then
        mvc.perform(get("/admin/places/" + placeId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/place-detail"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("adminOperationStatus", AdminOperationStatus.MODIFY))
                .andExpect(model().attribute("placeTypeOption", PlaceType.values()))
                .andExpect(model().attributeExists("place"))
                .andExpect(model().attributeExists("events"));
        then(placeService).should().getPlace(placeId);
        then(eventService).should().getEvent(eq(placeId), any(PageRequest.class));
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
        then(eventService).shouldHaveNoInteractions();
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 새로 만들기 뷰")
    @Test
    void givenNothing_whenRequestingNewPlacePage_thenReturnsNewPlacePage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/admin/places/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/place-detail"))
                .andExpect(model().attribute("adminOperationStatus", AdminOperationStatus.CREATE))
                .andExpect(model().attribute("placeTypeOption", PlaceType.values()));
    }

    @DisplayName("[view][POST] 어드민 페이지 - 장소 세부 정보 뷰, 장소 저장")
    @Test
    void givenNewPlace_whenSavingPlace_thenSavesPlaceAndReturnsToListPage() throws Exception {
        // Given
        PlaceRequest placeRequest = PlaceRequest.of(null, PlaceType.SPORTS, "강남 배드민턴장", "서울시 강남구 강남동", "010-1231-2312", 10, null);
        given(placeService.upsertPlace(placeRequest.toDto())).willReturn(true);

        // When & Then
        mvc.perform(
                post("/admin/places")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectToFormData(placeRequest))
        )
                .andExpect(status().isSeeOther())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(flash().attribute("adminOperationStatus", AdminOperationStatus.CREATE))
                .andExpect(flash().attribute("redirectUrl", "/admin/places"));
        then(placeService).should().upsertPlace(placeRequest.toDto());
    }

    @DisplayName("[view][GET] 어드민 페이지 - 장소 세부 정보 뷰, 장소 삭제")
    @Test
    void givenPlaceId_whenDeletingPlace_thenDeletesPlaceAndReturnsToListPage() throws Exception {
        // Given
        long placeId = 1L;
        given(placeService.removePlace(placeId)).willReturn(true);

        // When & Then
        mvc.perform(
                get("/admin/places/" + placeId + "/delete")
                        .contentType(MediaType.TEXT_HTML)
        )
                .andExpect(status().isSeeOther())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(flash().attribute("adminOperationStatus", AdminOperationStatus.DELETE))
                .andExpect(flash().attribute("redirectUrl", "/admin/places"));
        then(placeService).should().removePlace(placeId);
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
                .andExpect(model().attribute("eventStatusOption", EventStatus.values()));
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
                .andExpect(model().attribute("adminOperationStatus", AdminOperationStatus.MODIFY))
                .andExpect(model().attribute("eventStatusOption", EventStatus.values()))
                .andExpect(model().attributeExists("event"));
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

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 새로 만들기 뷰")
    @Test
    void givenNothing_whenRequestingNewEventPage_thenReturnsNewEventPage() throws Exception {
        // Given
        long placeId = 1L;
        PlaceDto placeDto = PlaceDto.of(null, null, "test name", null, null, null, null, null, null);
        EventResponse expectedEventResponse = EventResponse.empty(placeDto);
        given(placeService.getPlace(placeId)).willReturn(Optional.of(placeDto));

        // When & Then
        mvc.perform(get("/admin/places/" + placeId + "/newEvent"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/event-detail"))
                .andExpect(model().attribute("adminOperationStatus", AdminOperationStatus.CREATE))
                .andExpect(model().attribute("eventStatusOption", EventStatus.values()))
                .andExpect(model().attribute("event", expectedEventResponse));
        then(placeService).should().getPlace(placeId);
    }

    @DisplayName("[view][POST] 어드민 페이지 - 이벤트 세부 정보 뷰, 이벤트 저장")
    @Test
    void givenNewEvent_whenSavingEvent_thenSavesEventAndReturnsToListPage() throws Exception {
        // Given
        long placeId = 1L;
        EventRequest eventRequest = EventRequest.of(null, "test event", EventStatus.OPENED, LocalDateTime.now(), LocalDateTime.now(), 10, 10, null);
        given(eventService.upsertEvent(eventRequest.toDto(PlaceDto.idOnly(placeId)))).willReturn(true);

        // When & Then
        mvc.perform(
                post("/admin/places/" + placeId + "/events")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(objectToFormData(eventRequest))
        )
                .andExpect(status().isSeeOther())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(flash().attribute("adminOperationStatus", AdminOperationStatus.CREATE))
                .andExpect(flash().attribute("redirectUrl", "/admin/places/" + placeId));
        then(eventService).should().upsertEvent(eventRequest.toDto(PlaceDto.idOnly(placeId)));
    }

    @DisplayName("[view][GET] 어드민 페이지 - 이벤트 세부 정보 뷰, 이벤트 삭제")
    @Test
    void givenEventId_whenDeletingEvent_thenDeletesEventAndReturnsToListPage() throws Exception {
        // Given
        long eventId = 1L;
        given(eventService.removeEvent(eventId)).willReturn(true);

        // When & Then
        mvc.perform(
                get("/admin/events/" + eventId + "/delete")
                        .contentType(MediaType.TEXT_HTML)
        )
                .andExpect(status().isSeeOther())
                .andExpect(view().name("redirect:/admin/confirm"))
                .andExpect(redirectedUrl("/admin/confirm"))
                .andExpect(flash().attribute("adminOperationStatus", AdminOperationStatus.DELETE))
                .andExpect(flash().attribute("redirectUrl", "/admin/events"));
        then(eventService).should().removeEvent(eventId);
    }

    @DisplayName("[view][GET] 어드민 페이지 - 기능 확인 페이지")
    @Test
    void given_whenAfterOperation_thenRedirectsToPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(
                get("/admin/confirm")
                        .flashAttr("adminOperationStatus", AdminOperationStatus.CREATE)
                        .flashAttr("redirectUrl", "/admin/places")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("admin/confirm"))
                .andExpect(model().attribute("adminOperationStatus", AdminOperationStatus.CREATE))
                .andExpect(model().attribute("redirectUrl", "/admin/places"));
    }

    @DisplayName("장소 객체를 form data 로 변환")
    @Test
    void givenPlaceObject_whenConverting_thenReturnsFormData() {
        // Given
        PlaceRequest placeRequest = PlaceRequest.of(null, PlaceType.SPORTS, "강남 배드민턴장", "서울시 강남구 강남동", "010-1231-2312", 10, null);

        // When
        String result = objectToFormData(placeRequest);

        // Then
        assertThat(result).isEqualTo("placeType=SPORTS&placeName=%EA%B0%95%EB%82%A8+%EB%B0%B0%EB%93%9C%EB%AF%BC%ED%84%B4%EC%9E%A5&address=%EC%84%9C%EC%9A%B8%EC%8B%9C+%EA%B0%95%EB%82%A8%EA%B5%AC+%EA%B0%95%EB%82%A8%EB%8F%99&phoneNumber=010-1231-2312&capacity=10");
    }


    private String objectToFormData(Object obj) {
        Map<String, String> map = mapper.convertValue(obj, new TypeReference<>() {});

        return map.entrySet().stream()
                .map(entry -> entry.getValue() == null ? "" : entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .filter(str -> !str.isBlank())
                .collect(Collectors.joining("&"));
    }

}
