package com.uno.getinline.repository.jooq;

import com.uno.getinline.config.JooqConfig;
import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.dto.EventViewResponse;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DB - 이벤트 (Jooq)")
@Import({EventRepositoryJooqImpl.class, JooqConfig.class})
@EnableConfigurationProperties(JooqConfig.CustomJooqProperties.class)
@JooqTest
class EventRepositoryJooqImplTest {

    private final DSLContext dslContext; // 원래 @JooqTest 의 관심사
    private final EventRepositoryJooq sut;

    public EventRepositoryJooqImplTest(
            @Autowired DSLContext dslContext,
            @Autowired EventRepositoryJooqImpl eventRepositoryJooq
    ) {
        this.dslContext = dslContext;
        this.sut = eventRepositoryJooq;
    }

    @DisplayName("이벤트 뷰 데이터를 검색 파라미터와 함께 조회하면, 조건에 맞는 데이터를 페이징 처리하여 리턴한다.")
    @Test
    void givenSearchParams_whenFindingEventViewPage_thenReturnsEventViewResponsePage() {
        // Given

        // When
        Page<EventViewResponse> eventPage = sut.findEventViewPageBySearchParams(
                "배드민턴",
                "운동1",
                EventStatus.OPENED,
                LocalDateTime.of(2021, 1, 1, 0, 0, 0),
                LocalDateTime.of(2021, 1, 2, 0, 0, 0),
                PageRequest.of(0, 5)
        );

        // Then
        assertThat(eventPage.getTotalPages()).isEqualTo(1);
        assertThat(eventPage.getNumberOfElements()).isEqualTo(1);
        assertThat(eventPage.getTotalElements()).isEqualTo(1);
        assertThat(eventPage.getContent().get(0))
                .hasFieldOrPropertyWithValue("placeName", "서울 배드민턴장")
                .hasFieldOrPropertyWithValue("eventName", "운동1")
                .hasFieldOrPropertyWithValue("eventStatus", EventStatus.OPENED)
                .hasFieldOrPropertyWithValue("eventStartDatetime", LocalDateTime.of(2021, 1, 1, 9, 0, 0))
                .hasFieldOrPropertyWithValue("eventEndDatetime", LocalDateTime.of(2021, 1, 1, 12, 0, 0));
    }

    @DisplayName("이벤트 뷰 데이터 검색어에 따른 조회 결과가 없으면, 빈 데이터를 페이징 정보와 함께 리턴한다.")
    @Test
    void givenSearchParams_whenFindingNonexistentEventViewPage_thenReturnsEmptyEventViewResponsePage() {
        // Given

        // When
        Page<EventViewResponse> eventPage = sut.findEventViewPageBySearchParams(
                "없은 장소",
                "없는 이벤트",
                null,
                LocalDateTime.of(1000, 1, 1, 1, 1, 1),
                LocalDateTime.of(1000, 1, 1, 1, 1, 0),
                PageRequest.of(0, 5)
        );

        // Then
        assertThat(eventPage.getTotalPages()).isEqualTo(0);
        assertThat(eventPage.getNumberOfElements()).isEqualTo(0);
        assertThat(eventPage.getTotalElements()).isEqualTo(0);
    }

    @DisplayName("이벤트 뷰 데이터를 검색 파라미터 없이 페이징 값만 주고 조회하면, 전체 데이터를 페이징 처리하여 리턴한다.")
    @Test
    void givenPagingInfoOnly_whenFindingEventViewPage_thenReturnsEventViewResponsePage() {
        // Given

        // When
        Page<EventViewResponse> eventPage = sut.findEventViewPageBySearchParams(
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 5, Sort.by(Sort.Order.asc("placeName"), Sort.Order.desc("eventName"), Sort.Order.desc("eventType")))
        );

        // Then
        assertThat(eventPage.getTotalPages()).isEqualTo(6);
        assertThat(eventPage.getNumberOfElements()).isEqualTo(5);
        assertThat(eventPage.getTotalElements()).isEqualTo(26); // data.sql 테스트 데이터를 참고
    }

}
