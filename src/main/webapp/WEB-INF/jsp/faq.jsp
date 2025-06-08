<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: FAQ</title>
    <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
  </head>
  <body>
  <div id="doc3" class="yui-t5">
    <jsp:include page="/WEB-INF/jsp/include/header.jsp"/>
    <div id="bd">

      <div id="yui-main">

        <div class="yui-b"><div class="yui-g">

          <c:if test="${param['msg'] ne null}">
            <div class="successMessage">
              <c:out value="${param['msg']}"/>
            </div>
            <br>
          </c:if>

          <div class="songComments">
            <div class="songCommentsHeader">
              <span style="font-weight:bold">Info about <spring:message code="site.name"/>:</span>
            </div>
            <div class="songCommentsHeader">
              <c:choose>
                <c:when test="${faqContent ne null and fn:length(faqContent) > 0}">
                  <c:forEach var="faq" items="${faqContent}" varStatus="loopStatus">
                    <c:set var="rowStyle" value="oddRow"/>
                    <c:if test="${loopStatus.index % 2 != 0}">
                      <c:set var="rowStyle" value="evenRow"/>
                    </c:if>
              <div class="<c:out value="${rowStyle}"/>">
                  <ssj:escapeMessageBoardPost content="${faq.content}"/>
              </div>
                  </c:forEach>
                </c:when>
                <c:otherwise>
              <div>
                No FAQs available.
              </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>

          <br> <br>
          
          <div id="comments" class="songComments">
            <div class="songCommentsHeader">
              <span style="font-weight:bold">User comments/questions/answers:</span>
            </div>
            <div id="noComments"<c:if test="${faqPosts ne null and fn:length(faqPosts) gt 0}"> style="display:none"</c:if>>None.</div>
            <div class="songCommentsHeader" style="border-top:0<c:if test="${faqPosts eq null or fn:length(faqPosts) eq 0}">;display:none</c:if>">
              <c:forEach var="comment" items="${faqPosts}" varStatus="loopStatus">
              <c:set var="commentStyle" value="evenRow"/>
              <c:if test="${loopStatus.index % 2 == 0}">
                <c:set var="commentStyle" value="oddRow"/>
              </c:if>
              <div id="faq<c:out value="${comment.id}"/>" class="<c:out value="${commentStyle}"/> songComment">
                <div id="faqContent<c:out value="${comment.id}"/>"><ssj:escapeSongComment content="${comment.content}"/></div>
                <span>---
                  <c:url var="userLink" value="/profile">
                    <c:param name="id" value="${comment.user.id}"/>
                  </c:url>
                  <a href="<c:out value="${userLink}"/>"><c:out value="${comment.user.displayName}"/></a>
                  <span class="medium"><fmt:formatDate value="${comment.createDate}" type="both" pattern="yyyy-MM-dd HH:mm"/></span>
                </span>
                <c:if test="${comment.user.username == authName}">
                  <div id="faqButtons<c:out value="${comment.id}"/>" style="width:400px"><a class="editLink" href="javascript:editFaq(<c:out value="${comment.id}"/>)">Edit</a></div>
                </c:if>
              </div>
              </c:forEach>
            </div>
          </div>


          <c:if test="${loggedIn}">
          <br> <br> 

            <form id="addFaqForm" action="<c:url value="/user/edit_faq"/>" method="post" onsubmit="addFaq(); return false">

              <div class="songComments">
                <div class="songCommentsHeader">
                  <b>Add Comment/Question/Answer:</b>
                </div>
                <div class="songListHeader">
                  <textarea name="content" id="content" style="width:90%;height:100px;"></textarea>
                  <script>TextareaLimiter.addLimiter('content', 4000)</script>
                  <div id="allowedHTML" class="allowedTags">Allowed HTML: &lt;i&gt; &lt;font&gt; &lt;b&gt; &lt;u&gt; &lt;a&gt; &lt;br&gt; &lt;blockquote&gt; &lt;ul&gt; &lt;ol&gt; &lt;li&gt; &lt;em&gt; &lt;strong&gt; &lt;sup&gt; &lt;sub&gt; &lt;credit&gt;</div>
                  <input type="submit" name="submitBtn" value="Post Comment">
                </div>
              </div>
                
            </form>

          </c:if>

          <br> <br> <br>

          <%--<jsp:include page="include/thread_paging.jsp"/>--%>

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
  <c:if test="${loggedIn}">
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" language="JavaScript"><!--
    var originalContentMap = {};
    function editFaq(id) {
      var faqContent = $('faqContent'+id).innerHTML;
      originalContentMap[id] = faqContent;
      $('faqContent'+id).innerHTML = '<textarea style="width:400px;height:100px;" id="faqTextarea'+id+'">'+faqContent.replace(/<br>/g, "\n")+'</textarea>';
      var allowedHTML = $('allowedHTML').innerHTML;
      var faqAllowedHTMLContent = '<div id="faqAllowedHTML'+id+'" class="allowedTags">'+allowedHTML+'</div>';
      $('faqContent'+id).insert({ after : faqAllowedHTMLContent });
      var faqButtonsHTML = '<span style="float:right">';
      faqButtonsHTML += '<input type="button" name="cancelBtn" value="Cancel" onclick="cancelEdit('+id+')"> ';
      faqButtonsHTML += '<input type="button" name="submitBtn" value="Save" onclick="saveEdit('+id+')">';
      faqButtonsHTML += '</span>';
      faqButtonsHTML += '<input type="button" name="deleteBtn" value="Delete" onclick="deleteFaq('+id+')"';
//      faqButtonsHTML += '</form>';
      $('faqButtons'+id).innerHTML = faqButtonsHTML;
      TextareaLimiter.addLimiter('faqTextarea'+id, 4000);
    }
    function cancelEdit(id) {
      $('faqContent'+id).innerHTML = originalContentMap[id];
      $('faqAllowedHTML'+id).remove();
      $('faqButtons'+id).innerHTML = '<a class="editLink" href="javascript:editFaq('+id+')">Edit</a>';
    }
    var editFaqURL = '<c:url value="/user/edit_faq"/>';
    function addFaq() {
      $('addFaqForm').request({
        asynchronous:false,
        onSuccess:function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            location.href = '<c:url value="/faq"/>';
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure:function() {
          alert("Unable to save changes, please try again");
        }
      })
    }
    function saveEdit(id) {
      new Ajax.Request(editFaqURL, {
        asynchronous:false,
        parameters:{
          id : id,
          content : $F('faqTextarea'+id)
        },
        method:'post',
        onSuccess:function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            $('faqContent'+id).innerHTML = $F('faqTextarea'+id).replace(/\r?\n/g, "<br>");
            $('faqAllowedHTML'+id).remove();
            $('faqButtons'+id).innerHTML = '<a class="editLink" href="javascript:editFaq('+id+')">Edit</a>';
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure:function() {
          alert("Unable to save changes, please try again");
        }
      })
    }
    var deleteFaqURL = '<c:url value="/user/delete_faq"/>';
    function deleteFaq(id) {
      new Ajax.Request(deleteFaqURL, {
        asynchronous:false,
        parameters:{
          id : id
        },
        method:'post',
        onSuccess:function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            $('faq'+id).remove();
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure:function() {
          alert("Unable to delete faq, please try again");
        }
      })
    }
  //--></script>
  </c:if>
  </body>
</html>