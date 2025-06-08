<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="jsr303js" uri="http://kenai.com/projects/jsr303js/" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp"%>
    <title><spring:message code="site.name"/>: Site Content</title>
  <%-- style code taken from http://alistapart.com/articles/prettyaccessibleforms //--%>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/valang_codebase.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <div id="pageHeaderDiv" class="pageHeader">Site Content:</div>

          <form:form action="content" name="contentForm" cssClass="cmxform" commandName="content">

            <form:hidden path="id"/>

            <form:hidden path="type"/>

            <div id="errorsDiv">
              <form:errors path="*" cssClass="errors" element="div"/>
            </div>

            <jsr303js:validate commandName="content"/>

            <fieldset>
              <legend>Required Information</legend>
              <ol>
                <li>
                  <label for="contentTextarea">Content:</label><br>
                  <form:textarea path="content" id="contentTextarea" cssStyle="width:100%;height:250px;"/>
                  <script type="text/javascript">TextareaLimiter.addLimiter('contentTextarea', 8000)</script>
                  <div class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
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
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/scriptaculous-1.8.1.js"/>"></script>
  <script type="text/javascript"><!--
    document.forms.contentForm.content.focus();
  //--></script>
  </body>
</html>