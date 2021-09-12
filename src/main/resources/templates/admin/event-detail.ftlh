<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
<title>Title</title>
</head>
<body>
<p>
  This is admin event detail page.
</p>
<p>
  <span th:text="${event} ?: _">?</span>
</p>
</body>
</html>
