package com.uno.getinline.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    private final MockMvc mvc;

    public EventControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view][GET] 이벤트 리스트 페이지")
    @Test
    void givenNothing_whenRequestingEventsPage_thenReturnsEventsPage() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/index"));
    }

    @DisplayName("[view][GET] 이벤트 세부 정보 페이지")
    @Test
    void givenNothing_whenRequestingEventDetailPage_thenReturnsEventDetailPage() throws Exception {
        // Given
        long eventId = 1L;

        // When & Then
        mvc.perform(get("/events/" + eventId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("event/detail"));
    }

}
