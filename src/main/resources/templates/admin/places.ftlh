<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
<title>Title</title>
</head>
<body>
<p>
  This is admin places page.
</p>
<ul>
  <li>placeType: <span th:text="${placeType} ?: _">?</span></li>
  <li>placeName: <span th:text="${placeName} ?: _">?</span></li>
  <li>address: <span th:text="${address} ?: _">?</span></li>
</ul>
</body>
</html>
