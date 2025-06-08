package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ssj.model.song.Song;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.user.User;
import com.ssj.service.song.SongException;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongCommentService;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SongCommentsIncludeController {

  private String viewName = "include/comment_list";

  private SongService songService;
  private SongCommentService songCommentService;

  @RequestMapping("/include/song_comments")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int songId,
                                               @RequestParam(value = "refresh", required = false) Boolean refresh,
                                               @RequestParam(value = "start", required = false) Integer start) {

    Song song = songService.getSong(songId);
    if (song == null) {
      throw new SongException("Unable to find song with id " + songId);
    }

    // showing a page of comments
    SongCommentSearch commentSearch = new SongCommentSearch();
    commentSearch.setDescending(false);
    commentSearch.setSong(song);

    User user = SecurityUtil.getUser();
    if (user != null) {
      commentSearch.setNotByIgnoredUsers(user.getId());
    }

    ModelAndView modelAndView = new ModelAndView(getViewName(), "commentSearch", commentSearch);

    if (refresh != null && refresh) {
      // make sure to show the user their comment, since they just edited it (the "start" param used later won't be set in this case)
      SongComment comment = songCommentService.getComment(user, song);

      if (comment != null) {
        modelAndView.addObject("myComment", comment);

        int commentStartNum = songCommentService.getCommentPosition(comment);
        int pageStartNum = (commentStartNum / commentSearch.getResultsPerPage()) * commentSearch.getResultsPerPage();
        commentSearch.setStartResultNum(pageStartNum);
      } // else comment was just deleted
    } else {
      // default to Integer.MAX_VALUE to select last page of comments if no start param is present
      if (start == null) {
        // default to Integer.MAX_VALUE to select last page of comments if no start param is present
        start = Integer.MAX_VALUE;
      }
      // turn 1-based start into 0-based row num
      start--;
      commentSearch.setStartResultNum(start);
    }

    List<SongComment> comments = songCommentService.findComments(commentSearch);

    modelAndView.addObject("commentSearchResults", comments);
    modelAndView.addObject("song", song);
    return modelAndView;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  public String getViewName() {
    return viewName;
  }
}

