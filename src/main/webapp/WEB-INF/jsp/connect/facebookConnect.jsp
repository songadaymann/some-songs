<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title>Facebook Account Connection Status</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms --%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${requestScope['social.addConnection.duplicate'] ne null}">
            <h3 style="color:red">The Facebook account you tried to use is already connected to another account.</h3>
            <br><br>
          </c:if>

          <h3>You do not have a Facebook account connected to your account.</h3>

          <br><br>

          <form name="twitter" action="<c:url value="/connect/facebook"/>" method="post">
            <input type="submit" name="submitBtn" value="Connect With Facebook!">
          </form>

        </div></div>

      </div>
    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>


