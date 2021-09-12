<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<p>
  <span id="eventTitle">This is events page.</span>
</p>
<table id="eventTable">
  <tbody>
    <tr>
      <td>장소 ID</td>
      <td>${event.placeId()!"?"}</td>
    </tr>
    <tr>
      <td>이벤트명</td>
      <td>${event.eventName()!"?"}</td>
    </tr>
    <tr>
      <td>이벤트 상태</td>
      <td>${event.eventStatus()!"?"}</td>
    </tr>
    <tr>
      <td>시작 일시</td>
      <td>${event.eventStartDatetime()!"?"}</td>
    </tr>
    <tr>
      <td>종료 일시</td>
      <td>${event.eventEndDatetime()!"?"}</td>
    </tr>
    <tr>
      <td>현재 인원</td>
      <td>${event.currentNumberOfPeople()!"?"}</td>
    </tr>
    <tr>
      <td>최대 수용 인원</td>
      <td>${event.capacity()!"?"}</td>
    </tr>
    <tr>
      <td><label for="memo">메모</label></td>
      <td><textarea id="memo" readonly>${event.memo()!"-"}</textarea></td>
    </tr>
  </tbody>
</table>
<a href="/events" type="button">취소</a>
</body>
</html>