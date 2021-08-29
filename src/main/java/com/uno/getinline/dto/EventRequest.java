package com.uno.getinline.dto;

import com.uno.getinline.constant.EventStatus;

import java.time.LocalDateTime;

public record EventRequest(
        Long placeId,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo
) {

    public static EventRequest of(
            Long placeId,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity,
            String memo
    ) {
        return new EventRequest(
                placeId,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity,
                memo
        );
    }

    public EventDTO toDTO() {
        return EventDTO.of(
                this.placeId(),
                this.eventName(),
                this.eventStatus(),
                this.eventStartDatetime(),
                this.eventEndDatetime(),
                this.currentNumberOfPeople(),
                this.capacity(),
                this.memo(),
                null,
                null
        );
    }

}
