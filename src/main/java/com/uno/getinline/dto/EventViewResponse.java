package com.uno.getinline.dto;

import com.uno.getinline.constant.EventStatus;

import java.time.LocalDateTime;

public record EventViewResponse(
        Long id,
        String placeName,
        String eventName,
        EventStatus eventStatus,
        LocalDateTime eventStartDatetime,
        LocalDateTime eventEndDatetime,
        Integer currentNumberOfPeople,
        Integer capacity,
        String memo
) {
    public EventViewResponse(Long id, String placeName, String eventName, EventStatus eventStatus, LocalDateTime eventStartDatetime, LocalDateTime eventEndDatetime, Integer currentNumberOfPeople, Integer capacity, String memo) {
        this.id = id;
        this.placeName = placeName;
        this.eventName = eventName;
        this.eventStatus = eventStatus;
        this.eventStartDatetime = eventStartDatetime;
        this.eventEndDatetime = eventEndDatetime;
        this.currentNumberOfPeople = currentNumberOfPeople;
        this.capacity = capacity;
        this.memo = memo;
    }

    public static EventViewResponse of(
            Long id,
            String placeName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Integer currentNumberOfPeople,
            Integer capacity,
            String memo
    ) {
        return new EventViewResponse(
                id,
                placeName,
                eventName,
                eventStatus,
                eventStartDatetime,
                eventEndDatetime,
                currentNumberOfPeople,
                capacity,
                memo
        );
    }

    public static EventViewResponse from(EventDto eventDto) {
        if (eventDto == null) { return null; }
        return EventViewResponse.of(
                eventDto.id(),
                eventDto.placeDto().placeName(),
                eventDto.eventName(),
                eventDto.eventStatus(),
                eventDto.eventStartDatetime(),
                eventDto.eventEndDatetime(),
                eventDto.currentNumberOfPeople(),
                eventDto.capacity(),
                eventDto.memo()
        );
    }

}
