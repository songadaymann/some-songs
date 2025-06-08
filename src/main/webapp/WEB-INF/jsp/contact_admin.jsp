<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: Contact Admin</title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${param['success'] eq 'true'}">
            <div class="successMessage">
              Message sent.
            </div>
            <br> <br>
          </c:if>
          <c:if test="${param['error'] ne null}">
            <div class="errors">
              <c:out value="${param['error']}"/>
            </div>
            <br> <br>
          </c:if>


          <form id="contactForm" action="<c:url value="/contact_admin"/>" method="post">

            <div class="songComments">
              <div class="songCommentsHeader">
                <b>Send a message to the administrator(s):</b>
              </div>
              <div class="songListHeader">
                <textarea name="content" id="content" style="width:90%;height:100px;"></textarea><br>
                <script type="text/javascript">TextareaLimiter.addLimiter('content', 4000)</script>
                <div id="allowedHTML" class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>

                <br>

                <div>
                <c:choose>
                  <c:when test="${loggedIn}">
                    <input type="radio" name="anonymous" value="false" checked="true"> Include username in message.<br>
                    <input type="radio" name="anonymous" value="true"> Send message anonymously.<br>
                  </c:when>
                  <c:otherwise>
                    You are not logged in, so this message will be sent anonymously.
                  </c:otherwise>
                </c:choose>
                </div>

                <br>

                <div>
                  (If this message is anonymous and you expect a response, please include your email address in the message.)
                </div>

                <br>

                <input type="submit" name="submitBtn" value="Send">
              </div>
            </div>

          </form>

          <br> <br> <br>

          <div class="songComments">
            <div class="songCommentsHeader">
              <b>Administrator(s) Email:</b>
            </div>
            <div class="songListHeader">
            You may also contact the administrator directly via email:<br>
            ${fn:replace(adminEmailAddress, '@', '<br>at<br>')}
            </div>
          </div>

        </div></div>

      </div>

<%--
      <div id="rightNavBox" class="yui-b">
        <jsp:include page="include/message_board_rightnav.jsp"/>
      </div>
--%>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  </body>
</html>