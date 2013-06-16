<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:remove var="userBean"/>
<c:if test="${empty param.userId || empty param.password}">
  <c:set var="errorMsg" value="Please provide userId and password!" scope="request"/>
  <c:redirect url="/login.jsp"/>
</c:if>

<jsp:useBean id="userBean" scope="session" class="de.m3y3r.WebFileManager.beans.UserBean">
  <c:set target="${userBean}" property="userId" value="${param.userId}"/>
  <c:set target="${userBean}" property="password" value="${param.password}"/>
</jsp:useBean>

<c:if test="${!userBean.authenticated}">
  <c:remove var="userBean"/>
  <c:set var="errorMsg" value="Invalid userId or password" scope="request"/>
  <c:redirect url="/login.jsp"/>
</c:if>

<c:choose>
  <c:when test="${!empty param.origURL}">
    <c:redirect url="${param.origURL}"/>
  </c:when>
  <c:otherwise>
    <c:redirect url="/main.jsp"/>
  </c:otherwise>
</c:choose>