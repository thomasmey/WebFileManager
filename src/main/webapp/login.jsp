<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${userBean != null}">
  <jsp:forward page="/main.jsp"/>
</c:if>

<html>
<title>WebFileManager</title>
<body>
<h1>Welcome to the WebFileManager</h1>
<font color="red">${fn:escapeXml(requestScope.errorMsg)}</font>
<form action="<c:url value="/authenticate.jsp"/>" method="post">
  <input type="hidden" name="origURL" value="${fn:escapeXml(requestScope.origURL)}"/>
  Please log in:
  <p/>UserId: <input name="userId"/>
  <p/>Password: <input name="password" type="password"/>
  <p/><input type="submit" value="Login"/>
</form>
</body>
</html>