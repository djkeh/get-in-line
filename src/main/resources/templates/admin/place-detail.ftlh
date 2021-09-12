<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
<title>Title</title>
</head>
<body>
<p>
  This is admin place detail page.
</p>
<p>
  <span th:text="${place} ?: _">?</span>
</p>
</body>
</html>
