yieldUnescaped '<!DOCTYPE html>'
html {
    head {
        title('Title')
    }
    body {
        p('This is events page.')
        table {
            tbody {
                tr {
                    td('장소 ID')
                    td(event.placeId)
                }
                tr {
                    td('이벤트명')
                    td(event.eventName)
                }
                tr {
                    td('이벤트 상태')
                    td(event.eventStatus)
                }
                tr {
                    td('시작 일시')
                    td(event.eventStartDatetime)
                }
                tr {
                    td('종료 일시')
                    td(event.eventEndDatetime)
                }
                tr {
                    td('현재 인원')
                    td(event.currentNumberOfPeople)
                }
                tr {
                    td('최대 수용 인원')
                    td(event.capacity)
                }
                tr {
                    td { label(for: 'memo') { yield '메모' } }
                    td { textarea(id: 'memo', readonly: true) { yield event.memo } }
                }
            }
        }
        a(href: '/events', '상세')
    }
}
