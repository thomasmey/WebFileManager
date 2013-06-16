<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${userBean == null}">
  <c:set value="${pageContext.request.requestURL}" var="origURL" scope="request"/>
  <c:set value="Not logged in" var="errorMsg" scope="request"/>
  <jsp:forward page="/login.jsp"/>
</c:if>
<jsp:useBean id="fileUpload" class="de.m3y3r.WebFileManager.beans.FileUploadBean">
  <c:set target="${fileUpload}" property="pageContext" value="${pageContext}"/>
</jsp:useBean>

<c:redirect url="/main.jsp"/>