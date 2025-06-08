<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Site Link</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <%--<script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>--%>
    <%--<script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>--%>
    <script type="text/javascript" src="<c:url value="/js/jsr303js-codebase.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Site Link:</div>

          <form:form action="site_link" name="contentForm" cssClass="cmxform" commandName="siteLink">

            <form:hidden path="id"/>

            <div id="errorsDiv">
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>

            <jsr303js:validate commandName="siteLink"/>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="url">URL:</label><br>
                  <form:input path="url" id="url"/>
                </li>
                <li>
                  <label for="label">Label:</label><br>
                  <form:input path="label" id="label"/>
                </li>
                <li>
                  <label for="hover">Hover:</label><br>
                  <form:input path="hover" id="hover"/>
                </li>
                <li>
                  <label for="position">Position:</label><br>
                  <form:select path="position" id="position">
                    <form:option value="1">Header Left</form:option>
                    <form:option value="2">Header Right</form:option>
                    <form:option value="3">Footer Left</form:option>
                    <form:option value="4">Footer Right</form:option>
                  </form:select>
                </li>
                <li>
                  <label for="submitBtn" style="font-size:80%;">&nbsp;</label>
                  <input type="submit" id="submitBtn" name="submitBtn" class="submitBtn" value="Save Content">
                </li>
              </ol>
            </fieldset>

          </form:form>

          <br> <br> <br>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/right_nav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <%--<script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>--%>
  <%--<script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>--%>
  </body>
</html>