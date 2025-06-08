package com.ssj.web.spring.controllers;

import com.ssj.web.spring.exception.NotFoundException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.lang.math.NumberUtils;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongException;
import com.ssj.service.song.SongCommentService;
import com.ssj.service.song.SongRatingService;
import com.ssj.service.user.UserService;
import com.ssj.service.artist.ArtistService;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongRating;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Controller
public class SongController {

  private String viewName = "song";

  private UserService userService;
  private SongService songService;
  private SongCommentService songCommentService;
  private SongRatingService songRatingService;
  private ArtistService artistService;
  @Value("${soundcloud.somesongs.app.clientId:}")
  private String soundCloudClientId;

  @RequestMapping(value = "/songs/{title:[a-zA-Z0-9\\-]+}-{id:[0-9]+}", produces = MediaType.TEXT_HTML_VALUE)
  public ModelAndView newSongHandler(WebRequest request,
                                     @PathVariable("title") String title,
                                     @PathVariable("id") int songId,
                                     @RequestParam(value = "start", required = false) String startString,
                                     @RequestParam(value = "comment", required = false) Integer songCommentId) {

    Song song = songService.getSong(songId);
    if (song == null) {
      throw new SongException("Unable to find song with id " + songId);
    }
    if (!song.getTitleForUrl().equalsIgnoreCase(title)) {
      // return 404
      throw new NotFoundException("Could not find song with title " + title);
    }
    ModelAndView modelAndView = new ModelAndView(getViewName(), "song", song);

    int start = NumberUtils.toInt(startString);
    if (startString != null) {
      modelAndView.addObject("start", start);
    }
    
    User user = SecurityUtil.getUser();

    try {
/*
      SearchBase search = null;
      String searchObjectName = null;
*/

      if (songCommentId != null) {
        // showing a comment and some page of its replies
        SongCommentReplySearch replySearch = new SongCommentReplySearch();
        // show oldest reply first
        replySearch.setDescending(false);
        replySearch.setAlwaysShowFullPage(false);
        if (user != null) {
          replySearch.setNotByIgnoredUsers(user.getId());
        }

        SongComment originalComment = songCommentService.getSongComment(songCommentId);

        Integer multiquoteSongCommentId = (Integer) request.getAttribute("mqSongCommentId", WebRequest.SCOPE_SESSION);
        if (multiquoteSongCommentId != null && !multiquoteSongCommentId.equals(originalComment.getId())) {
          request.removeAttribute("mqSongCommentId", WebRequest.SCOPE_SESSION);
          request.removeAttribute("mqSongCommentReplyIds", WebRequest.SCOPE_SESSION);
          request.removeAttribute("mqSongQuoteOriginal", WebRequest.SCOPE_SESSION);
        }

        modelAndView.addObject("originalComment", originalComment);

        replySearch.setOriginalComment(originalComment);

        // turn 1-based start into 0-based row num?
        start--;
        replySearch.setStartResultNum(start);

/*
        search = replySearch;
        searchObjectName = "replies";
*/
        modelAndView.addObject("replySearch", replySearch);
        List<SongCommentReply> replies = songCommentService.findReplies(replySearch);
        modelAndView.addObject("replies", replies);
/*
      } else {
        // showing a page of comments
        SongCommentSearch commentSearch = new SongCommentSearch();
        commentSearch.setSong(song);

        int start = NumberUtils.toInt(request.getParameter("start"), 1);
        // turn 1-based start into 0-based row num
        start--;
        commentSearch.setStartResultNum(start);

        songCommentService.findComments(commentSearch);

        search = commentSearch;
        searchObjectName = "comments";
*/
      }


//      modelAndView.addObject(searchObjectName, search);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (user != null) {
      if (!song.isShowSong() && (song.getArtist().getUser().getId() != user.getId() && !user.isAdmin())) {
        throw new SongException("This song is currently unavailable. It may have been hidden by its owner or hidden because its link is broken.");
      }

      // used in comments, etc.
      modelAndView.addObject("user", user);

      boolean canEdit = artistService.canEditArtist(song.getArtist(), user);
      modelAndView.addObject("canEdit", canEdit);

      SongRating rating = songRatingService.getRating(user, song);

      if (rating != null) {
        modelAndView.addObject("rating", rating);
      }

      SongComment comment = songCommentService.getComment(user, song);

      if (comment != null) {
        modelAndView.addObject("myComment", comment);
      }

      boolean isFavorite = userService.isFavoriteSong(user, song);
      modelAndView.addObject("isFavorite", isFavorite);
    } else if (!song.isShowSong()) {
      throw new SongException("This song is currently unavailable. It may have been hidden by its owner or hidden because its link is broken.");
    }

    return modelAndView;
  }

  @RequestMapping(value = "/songs/stream/{title:[a-zA-Z0-9\\-]+}-{id:[0-9]+}.mp3", method = RequestMethod.GET)
  public @ResponseBody void stream(@PathVariable int id, HttpServletResponse response) throws IOException {
    // todo secure this somehow
    Song song = songService.getSong(id);
    if (song == null) {
      throw new NotFoundException("Could not find song with id " + id);
    }

    if (song.isImportedFromSoundCloud()) {
      URL urlOne = new URL(song.getUrl() + "?client_id=" + soundCloudClientId);
      HttpURLConnection httpURLConnection = (HttpURLConnection) urlOne.openConnection();
      httpURLConnection.setInstanceFollowRedirects(false);
      httpURLConnection.connect();
      String location = httpURLConnection.getHeaderField("Location");
      httpURLConnection.disconnect();
      URL mp3Url = new URL(location);
      httpURLConnection = (HttpURLConnection) mp3Url.openConnection();
      httpURLConnection.connect();
      response.setContentType(httpURLConnection.getContentType());
      response.setContentLength(httpURLConnection.getContentLength());
      IOUtils.copy(httpURLConnection.getInputStream(), response.getOutputStream());
      return;
    }
    URL url = new URL(song.getUrl());
    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
    httpURLConnection.connect();
    response.setContentType(httpURLConnection.getContentType());
    response.setContentLength(httpURLConnection.getContentLength());
    IOUtils.copy(httpURLConnection.getInputStream(), response.getOutputStream());
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  public String getViewName() {
    return viewName;
  }
}
