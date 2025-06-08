<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Confirm Contact Admin</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <%--<script type="text/javascript" src="<c:url value="/js/jsr303js-codebase.js"/>"></script>--%>
    <%--<link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">--%>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <b>Thanks for your message!</b>

          <br><br>

          <b>IMPORTANT: <a href="<c:url value="/contact_confirm"/>?confirm=true">YOU MUST CLICK HERE TO SEND YOUR MESSAGE!!</a></b>

          <br><br>

          This step is required to confirm that your message was entered by a person and not a spam program.

        </div></div>

      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <%--<script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>--%>
  <%--<script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>--%>
<%--
  <script type="text/javascript" src="<c:url value="/js/self-labeled-forms.js"/>"></script>
  <script type="text/javascript"><!--
    new Axent.SelfLabeledInput();
  //--></script>
--%>
  </body>
</html>