<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title>Soundcloud Account Connection Status</title>
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
            <h3 style="color:red">You have already connected a different Soundcloud account.</h3>
            <br><br>
          </c:if>

          <h3>Your Soundcloud account is connected to your account!</h3>

          <br><br>

          If you would like to disconnect your Soundcloud account click this button:<br><br>
          <form name="soundcloud" action="<c:url value="/connect/soundcloud"/>" method="post">
            <input type="hidden" name="_method" value="delete">
            <input type="submit" name="submitBtn" value="Disconnect From Soundcloud">
          </form>

        </div></div>

      </div>
    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>


