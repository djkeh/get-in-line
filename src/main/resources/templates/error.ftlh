<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<p>
  This is <span th:text="${statusCode} ?: _">???</span> error page.
</p>
<ul>
  <li>errorCode: <span th:text="${errorCode} ?: _">?</span></li>
  <li>message: <span th:text="${message} ?: _">?</span></li>
</ul>
</body>
</html>
