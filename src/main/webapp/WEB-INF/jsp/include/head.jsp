<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<link id="base-link" href="<c:url value="/"/>" />
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.2.0/build/reset-fonts-grids/reset-fonts-grids.css">
<link rel="stylesheet" type="text/css" href="<c:url value="/css/default.css"/>">
<%-- TODO move head.jsp include to bottom of <head></head> on all pages --%>
<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', '<spring:message code="google.analytics.id"/>']);
  _gaq.push(['_trackPageview']);
  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
  var baseUrl = document.getElementById('base-link').href;
</script>