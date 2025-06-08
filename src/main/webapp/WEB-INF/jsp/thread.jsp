<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
  <head>
    <%@ include file="/WEB-INF/jsp/include/head.jsp" %>
    <title><spring:message code="site.name"/>: Thread '<c:out value="${thread.subject}"/>'</title>
    <c:if test="${loggedIn}">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/form.css"/>">
    <script type="text/javascript" src="<c:url value="/js/textarea_limiter.js"/>"></script>
    </c:if>
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

          <jsp:include page="/WEB-INF/jsp/include/thread_paging.jsp"/>

          <br> <br>

          <c:set var="post" value="${thread}"/>

          <%@ include file="/WEB-INF/jsp/include/post.jsp" %>
            <%--<jsp:param name="post" value="${thread}"/>--%>
          <%--</jsp:include>--%>

          <c:if test="${search.totalResults > 0}">
            <c:forEach var="reply" items="${searchResults}" varStatus="loopStatus">
              <c:set var="post" value="${reply}"/>
              <c:set var="oddPost" value="${loopStatus.index mod 2 eq 0}"/>
              <%@ include file="/WEB-INF/jsp/include/post.jsp" %>
<%--
              <jsp:include page="include/post.jsp">
                <jsp:param name="post" value="${reply}"/>
                <jsp:param name="oddPost" value="${loopStatus.index mod 2 eq 0}"/>
              </jsp:include>
--%>
            </c:forEach>
          </c:if>

          <div id="replyDiv" style="display:none;margin-bottom:16px"></div>

          <jsp:include page="/WEB-INF/jsp/include/thread_paging.jsp"/>

        </div></div>

      </div>

      <div id="rightNavBox" class="yui-b">
        <jsp:include page="/WEB-INF/jsp/include/message_board_rightnav.jsp"/>
      </div>

    </div>

    <jsp:include page="/WEB-INF/jsp/include/footer.jsp"/>
  </div>
  <script type="text/javascript" src="<c:url value="/js/prototype-1.6.0.2.js"/>"></script>
  <script type="text/javascript" language="JavaScript"><!--
  var multiquoteURL = '<c:url value="/multiquote_post"/>';
  var addTitle = 'Add post to multiquote';
  var removeTitle = 'Remove post from multiquote';
  function toggleMultiquote(postId) {
    new Ajax.Request(multiquoteURL, {
      asynchronous:false,
      parameters: {
        id : postId
      },
      method:'post',
      onSuccess:function(transport) {
        var responseJSON = transport.responseJSON;
        if (responseJSON && responseJSON.success) {
          var mqlink = $('multiquote'+postId);
          if (mqlink.hasClassName('quoted')) {
            mqlink.removeClassName('quoted');
            mqlink.title = addTitle;
          } else {
            mqlink.addClassName('quoted');
            mqlink.title = removeTitle;
          }
        } else {
          alert(responseJSON.error);
        }
      },
      onFailure:function() {
        alert("Could not multquote post, please try again");
      }
    })
  }
  //--></script>
  <c:if test="${loggedIn}">
  <script type="text/javascript" language="JavaScript"><!--
    var threadId = <c:out value="${thread.id}"/>;
    var deleteThreadURL = '<c:url value="/user/delete_thread"/>';
    function deleteThread() {
      if (confirm('Are you sure you want to delete this thread?\nAll replies will be deleted as well.\nThis cannot be undone.')) {
        new Ajax.Request(deleteThreadURL, {
          asynchronous:false,
          parameters: {
            id : threadId
          },
          method:'post',
          onSuccess:function(transport) {
            var responseJSON = transport.responseJSON;
            if (responseJSON && responseJSON.success) {
              location.href = '<c:url value="/message_board"><c:param name="msg" value="Thread deleted."/></c:url>';
            } else {
              alert(responseJSON.error);
            }
          },
          onFailure : function() {
            alert('Could not delete thread, please try again');
          }
        })
      }
    }
    var deletePostURL = '<c:url value="/user/delete_post"/>';
    function deletePost(postId) {
      if (confirm('Are you sure you want to delete this post?\nThis cannot be undone.')) {
        new Ajax.Request(deletePostURL, {
          asynchronous:false,
          parameters: {
            id : postId
          },
          method:'post',
          onSuccess:function(transport) {
            var responseJSON = transport.responseJSON;
            if (responseJSON && responseJSON.success) {
              location.href = '<c:url value="/thread"><c:param name="id" value="${thread.id}"/><c:param name="msg" value="Post deleted."/></c:url>';
              // skip page reload and just remove div?
//              $('postContent'+postId).remove();
            } else {
              alert(responseJSON.error);
            }
          },
          onFailure : function() {
            alert('Could not delete post, please try again');
          }
        })
      }
    }
    var replyFormURL = '<c:url value="/user/reply"/>';
    function postToThread(quoteId) {
      if (!$('replyDiv').innerHTML) {
        new Ajax.Request(replyFormURL, {
          parameters : {
            threadId : threadId,
            quoteId : quoteId
          },
          method : 'get',
          onSuccess : function(transport) {
            $('replyDiv').innerHTML = transport.responseText;
            TextareaLimiter.addLimiter('postBody', 4000);
            $('replyDiv').show();
            $('replyDiv').scrollTo();
            document.forms.replyForm.content.focus();
          },
          onFailure : function() {
            alert('Could not load reply form, please try again');
          }
        })
      } else {
        $('replyDiv').scrollTo();
        document.forms.replyForm.content.focus();
      }
    }
    function cancelPost(postId) {
      if (postId) {
        $('postContent'+postId).innerHTML = originalPostContent[postId];
      } else {
        $('replyDiv').hide();
        $('replyDiv').innerHTML = '';
      }
    }
    var originalPostContent = [];
    function editPost(postId) {
      var postDiv = $('post'+postId);
      if (postDiv.firstDescendant() == null || postDiv.firstDescendant().tagName != 'form') {
        new Ajax.Request(replyFormURL, {
          parameters : {
            id : postId
          },
          method : 'get',
          onSuccess : function(transport) {
            originalPostContent[postId] = postDiv.innerHTML;
            postDiv.innerHTML = transport.responseText;
            TextareaLimiter.addLimiter('postBody'+postId, 4000);
            postDiv.show();
            postDiv.scrollTo();
            document.forms['replyForm'+postId].content.focus();
          },
          onFailure : function() {
            alert('Could not load reply form, please try again');
          }
        })
      } else {
        postDiv.scrollTo();
        document.forms['replyForm'+postId].content.focus();
      }
    }
  <c:if test="${isAdmin}">
    var lockURL = '<c:url value="/admin/lock_thread"/>';
    function toggleLocked() {
      new Ajax.Request(lockURL, {
        parameters : {
          threadId : threadId
        },
        method : 'post',
        onSuccess : function(transport) {
          var responseJSON = transport.responseJSON;
          if (responseJSON && responseJSON.success) {
            $$('.addLink').each(function(s) { s.toggle() });
            $$('.removeLink').each(function(s) { s.toggle() })
          } else {
            alert(responseJSON.error);
          }
        },
        onFailure : function() {
          alert('Could not lock/unlock thread, please try again');
        }
      });
    }
  </c:if>
  //--></script>
  </c:if>
  </body>
</html>
