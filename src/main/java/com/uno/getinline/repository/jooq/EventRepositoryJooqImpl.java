package com.uno.getinline.repository.jooq;

import com.uno.getinline.constant.EventStatus;
import com.uno.getinline.dto.EventViewResponse;
import com.uno.getinline.tables.Event;
import com.uno.getinline.tables.Place;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SelectField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static org.jooq.impl.DSL.trueCondition;

@RequiredArgsConstructor
@Repository
public class EventRepositoryJooqImpl implements EventRepositoryJooq {

    private final DSLContext dslContext;

    @Override
    public Page<EventViewResponse> findEventViewPageBySearchParams(
            String placeName,
            String eventName,
            EventStatus eventStatus,
            LocalDateTime eventStartDatetime,
            LocalDateTime eventEndDatetime,
            Pageable pageable
    ) {
        final Event EVENT = Event.EVENT;
        final Place PLACE = Place.PLACE;

        Condition condition = trueCondition();

        SelectField<?>[] select = {
                EVENT.ID,
                PLACE.PLACE_NAME,
                EVENT.EVENT_NAME,
                EVENT.EVENT_STATUS,
                EVENT.EVENT_START_DATETIME,
                EVENT.EVENT_END_DATETIME,
                EVENT.CURRENT_NUMBER_OF_PEOPLE,
                EVENT.CAPACITY,
                EVENT.MEMO
        };

        if (placeName != null && !placeName.isBlank()) {
            condition = condition.and(PLACE.PLACE_NAME.containsIgnoreCase(placeName));
        }
        if (eventName != null && !eventName.isBlank()) {
            condition = condition.and(EVENT.EVENT_NAME.contains(eventName));
        }
        if (eventStatus != null) {
            condition = condition.and(EVENT.EVENT_STATUS.eq(eventStatus));
        }
        if (eventStartDatetime != null) {
            condition = condition.and(EVENT.EVENT_START_DATETIME.ge(eventStartDatetime));
        }
        if (eventEndDatetime != null) {
            condition = condition.and(EVENT.EVENT_END_DATETIME.le(eventEndDatetime));
        }

        int count = dslContext
                .selectCount()
                .from(EVENT)
                .innerJoin(PLACE)
                .onKey()
                .where(condition)
                .fetchSingleInto(int.class);

        List<EventViewResponse> pagedList = dslContext
                .select(select)
                .from(EVENT)
                .innerJoin(PLACE)
                .onKey()
                .where(condition)
//                .orderBy() // TODO: pageable 로부터 컬럼명을 얻어내서 알맞은 jooq 테이블의 OrderField 로 변환해야 한다. 까다로운 부분.
                .limit(pageable.getOffset(), pageable.getPageSize())
                .fetchInto(EventViewResponse.class);

        return new PageImpl<>(pagedList, pageable, count);
    }

}
