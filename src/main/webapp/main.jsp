<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${userBean == null}">
  <c:set value="${pageContext.request.requestURL}" var="origURL" scope="request"/>
  <c:set value="Not logged in" var="errorMsg" scope="request"/>
  <jsp:forward page="/login.jsp"/>
</c:if>

<html>
<title>File List</title>
<body>
<h1>File List</h1>
<h2>Private</h2>
<table>
  <tr>
    <td>name</td>
    <td>size</td>
  </tr>
  <c:forEach items="${userBean.privateFiles}" var="file">
  <tr>
    <td>
      <c:url value="/download/${userBean.userId}/${file.value.name}" var="fileUrl">
      </c:url>
      <a href="${fileUrl}"><c:out value="${file.value.name}"/></a>
    </td>
    <td>
    <c:out value="${file.value.length()}"/>
    </td>
  </tr>
  </c:forEach>
</table>
<h2>Public</h2>
<table>
  <tr>
    <td>name</td>
    <td>size</td>
  </tr>
  <c:forEach items="${userBean.publicFiles}" var="file">
  <tr>
    <td>
      <c:url value="/download/${userBean.publicDirectoryName}/${file.value.name}" var="fileUrl">
      </c:url>
      <a href="${fileUrl}"><c:out value="${file.value.name}"/></a>
    </td>
    <td>
    <c:out value="${file.value.length()}"/>
    </td>
  </tr>
  </c:forEach>
</table>
<hr/>
<h1>Upload (a) file(s)</h1>
<form action="<c:url value="/upload.jsp"/>" method="post" enctype="multipart/form-data">
  Public<input type="checkbox" name="publicUpload">
  <p/><input type="file" size="100" name="upload" multiple>
  <p/><input type="submit" value="Upload"/>
</form>
</body>
</html>