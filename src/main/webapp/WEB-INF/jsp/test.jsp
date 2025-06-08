<%--
<%@ page import="com.ssj.model.user.User"%>
<%@ page import="com.ssj.model.song.Song"%>
<%@ page import="com.ssj.model.artist.Artist"%>
<%@ page import="org.apache.commons.lang.math.NumberUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="com.ssj.service.user.UserService" %>
<%@ page import="com.ssj.service.artist.ArtistService" %>
<%@ page import="com.ssj.service.song.SongService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  boolean saveUser = "saveUser".equals(request.getParameter("formAction"));
  boolean saveArtist = "saveArtist".equals(request.getParameter("formAction"));
  boolean saveSong = "saveSong".equals(request.getParameter("formAction"));

  WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
  ArtistService artistService = (ArtistService) appContext.getBean("artistService");
  SongService songService = (SongService) appContext.getBean("songService");
  UserService userService = (UserService) appContext.getBean("userService");

  String result = "";
  if (saveUser) {
    User user = new User();
    user.setUsername(request.getParameter("username"));
    user.setPassword(request.getParameter("password"));
    user.setDisplayName(request.getParameter("displayName"));
    user.setAdmin(true);
    user.setEmail(request.getParameter("email"));
    user.setLocation(request.getParameter("location"));

    try {
      userService.save(user);
      result = "User saved.";
    } catch (Exception e) {
      e.printStackTrace();
      // oh well, too bad
      result = "Unable to save user, please try again.";
    }
  } else if (saveArtist) {
    Artist artist = new Artist();
    artist.setName(request.getParameter("name"));
    artist.setInfo(request.getParameter("info"));
    User user = new User();
    user.setId(Integer.valueOf(request.getParameter("userId")));
    artist.setUser(user);

    try {
      artistService.save(artist);
      result = "Artist saved.";
    } catch (Exception e) {
      e.printStackTrace();
      result = "Unable to save artist, please try again.";
    }
  } else if (saveSong) {
    Song song = new Song();
    song.setTitle(request.getParameter("title"));
    song.setUrl(request.getParameter("url"));
    song.setInfo(request.getParameter("info"));
    song.setMoreInfo(request.getParameter("moreInfo"));

    int artistId = NumberUtils.toInt(request.getParameter("artistId"), -1);
    if (artistId > -1) {
      try {
        song.setArtist(artistService.getArtist(artistId));
      } catch (Exception e) {
        e.printStackTrace();
        result = "Unable to load artist with id " + artistId + ". ";
      }
    }

    try {
      songService.save(song);
      result += "Song saved.";
    } catch (Exception e) {
      e.printStackTrace();
      result += "Unable to save song, please try again.";
    }
  }
%>
<html>
  <head><title>Create a User</title></head>
  <body>

  <h3><%= result %></h3>

  <form action="test.jsp" method="get">
    <input type="hidden" name="formAction" value="saveUser">

    <b>Username:</b> <input type="text" name="username" maxlength="64" size="24"><br>
    <b>Password:</b> <input type="password" name="password" maxlength="32" size="12"><br>
    <b>Display Name:</b> <input type="text" name="displayName"><br>
    <b>E-mail:</b> <input type="text" name="email" maxlength="128" size="32"><br>
    <b>City:</b> <input type="text" name="location" maxlength="64" size="24"><br>

    <input type="submit" name="submitBtn" value="Create User">
  </form>

  <form action="test.jsp" method="get">
    <input type="hidden" name="formAction" value="saveArtist">

    <b>Name:</b> <input type="text" name="name" maxlength="64" size="24"><br>
    <b>Info:</b> <input type="text" name="info" maxlength="128" size="32"><br>
    <b>User ID:</b> <input type="text" name="userId" maxlength="2" size="2"><br>

    <input type="submit" name="submitBtn" value="Create Artist">
  </form>

  <form action="test.jsp" method="get">
    <input type="hidden" name="formAction" value="saveSong">

    <b>Artist ID:</b> <input type="text" name="artistId" maxlength="3" size="4"><br>
    <b>Title:</b> <input type="text" name="title" maxlength="64" size="24"><br>
    <b>URL:</b> <input type="text" name="url" maxlength="256" size="32"><br>
    <b>Info:</b> <input type="text" name="info" maxlength="128" size="32"><br>
    <b>More Info:</b> <input type="text" name="moreInfo" maxlength="64" size="24"><br>

    <input type="submit" name="submitBtn" value="Create Song">
  </form>

  </body>
</html>--%>
