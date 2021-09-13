yieldUnescaped '<!DOCTYPE html>'
html {
  head {
    title('Title')
  }
  body {
    p('This is events page.')
    table {
      thead {
        tr {
          th('장소 ID')
          th('이벤트명')
          th('이벤트 상태')
          th('시작 일시')
          th('종료 일시')
          th('현재 인원')
          th('최대 수용 인원')
          th('상세')
        }
      }
      tbody {
        events.each { event ->
          tr {
            td(event.placeId)
            td(event.eventName)
            td(event.eventStatus)
            td(event.eventStartDatetime)
            td(event.eventEndDatetime)
            td(event.currentNumberOfPeople)
            td(event.capacity)
            td { a(href: "/events/${event.id}", '상세') }
          }
        }
      }
    }
  }
}
