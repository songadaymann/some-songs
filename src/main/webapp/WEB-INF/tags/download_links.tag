<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="song" type="com.ssj.model.song.Song" required="true" %>
<%@ attribute name="loopStatusIndex" type="java.lang.Integer" required="true" %>
<c:if test="${song.selfHosted}"><a id="song<c:out value="${loopStatusIndex}"/>" class="songLink inline-exclude exclude" href="<c:out value="${song.url}"/>"
title="Download '<c:out value="${song.title}"/>'" target="_blank">MP3</a></c:if>
<c:if test="${song.bandcampUrl ne null and song.bandcampUrl ne ''}"><a href="${song.bandcampUrl}" target="_blank"
    title="Buy/Download on BandCamp"><img style="vertical-align:middle"
    src="http://s0.bcbits.com/img/buttons/bandcamp_22x22_white.png" width="22" height="22"></a></c:if>
<c:if test="${song.soundCloudUrl ne null and song.soundCloudUrl ne ''}"><a href="${song.soundCloudUrl}" target="_blank"
    title="Buy/Download on SoundCloud"><img style="vertical-align:middle"
    src="<c:url value="/img/soundcloud-24x24.png"/>" width="24" height="24"></a></c:if>