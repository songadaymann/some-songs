package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.service.messageboard.MessageBoardException;
import com.ssj.service.song.SongException;
import com.ssj.service.song.SongCommentService;
import com.ssj.service.playlist.PlaylistException;
import com.ssj.service.playlist.PlaylistCommentService;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.PlaylistCommentReply;

/**
 * Class javadoc comment here...
 *
 * TODO simplify this a lot by using a common interface across SongComment, SongCommentReply, PlaylistComment, PlaylistCommentReply, and MessageBoardPost.
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class WholePostController {

  private String viewName = "whole_post";

  private MessageBoardService messageBoardService;
  private SongCommentService songCommentService;
  private PlaylistCommentService playlistCommentService;

  @RequestMapping("/whole_post")
  protected ModelAndView get(@RequestParam("type") String type,
                             @RequestHeader("Referer") String referer,
                             @RequestParam(value = "id") int id) {

    ModelAndView modelAndView = new ModelAndView(getViewName());

    if (referer != null) {
      modelAndView.addObject("back", referer);
    }

    if ("post".equals(type)) {

      MessageBoardPost post = messageBoardService.getPost(id);

      if (post == null) {
        throw new MessageBoardException("Unable to find post with id " + id);
      }

      MessageBoardPost thread = post.getOriginalPost() == null ? post : post.getOriginalPost();

      modelAndView.addObject("subject", thread.getSubject());
      modelAndView.addObject("type", "thread");
      modelAndView.addObject("postUser", post.getUser());
      modelAndView.addObject("content", post.getContent());
      modelAndView.addObject("moreContent", post.getMoreContent());
      modelAndView.addObject("createDate", post.getCreateDate());
      modelAndView.addObject("changeDate", post.getChangeDate());

      // get current topic here to keep this logic out of the JSP
      MessageBoardTopic currentTopic = thread.getTopic();
      modelAndView.addObject("currentTopic", currentTopic);
    } else if ("songComment".equals(type)) {

      SongComment songComment = songCommentService.getComment(id);

      if (songComment == null) {
        throw new SongException("Unable to find song comment with id " + id);
      }

      modelAndView.addObject("subject", songComment.getSong().getTitle());
      modelAndView.addObject("type", "song");
      modelAndView.addObject("postUser", songComment.getUser());
      modelAndView.addObject("content", songComment.getCommentText());
      modelAndView.addObject("moreContent", songComment.getMoreCommentText());
      modelAndView.addObject("createDate", songComment.getCreateDate());
      modelAndView.addObject("changeDate", songComment.getChangeDate());
    } else if ("songCommentReply".equals(type)) {

      SongCommentReply songCommentReply = songCommentService.getSongCommentReply(id);

      if (songCommentReply == null) {
        throw new SongException("Unable to find song comment reply with id " + id);
      }

      modelAndView.addObject("subject", songCommentReply.getOriginalComment().getSong().getTitle());
      modelAndView.addObject("type", "song");
      modelAndView.addObject("postUser", songCommentReply.getUser());
      modelAndView.addObject("content", songCommentReply.getCommentText());
      modelAndView.addObject("moreContent", songCommentReply.getMoreCommentText());
      modelAndView.addObject("createDate", songCommentReply.getCreateDate());
      modelAndView.addObject("changeDate", songCommentReply.getChangeDate());
    } else if ("playlistComment".equals(type)) {

      PlaylistComment playlistComment = playlistCommentService.getPlaylistComment(id);

      if (playlistComment == null) {
        throw new SongException("Unable to find playlist comment with id " + id);
      }

      modelAndView.addObject("subject", playlistComment.getPlaylist().getTitle());
      modelAndView.addObject("type", "playlist");
      modelAndView.addObject("postUser", playlistComment.getUser());
      modelAndView.addObject("content", playlistComment.getCommentText());
      modelAndView.addObject("moreContent", playlistComment.getMoreCommentText());
      modelAndView.addObject("createDate", playlistComment.getCreateDate());
      modelAndView.addObject("changeDate", playlistComment.getChangeDate());
    } else if ("playlistCommentReply".equals(type)) {

      PlaylistCommentReply playlistCommentReply = playlistCommentService.getPlaylistCommentReply(id);

      if (playlistCommentReply == null) {
        throw new PlaylistException("Unable to find playlist comment reply with id " + id);
      }

      modelAndView.addObject("subject", playlistCommentReply.getOriginalComment().getPlaylist().getTitle());
      modelAndView.addObject("type", "playlist");
      modelAndView.addObject("postUser", playlistCommentReply.getUser());
      modelAndView.addObject("content", playlistCommentReply.getCommentText());
      modelAndView.addObject("moreContent", playlistCommentReply.getMoreCommentText());
      modelAndView.addObject("createDate", playlistCommentReply.getCreateDate());
      modelAndView.addObject("changeDate", playlistCommentReply.getChangeDate());
    } else {
      throw new RuntimeException("Invalid post type");
    }

    return modelAndView;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  @Autowired
  public void setPlaylistCommentService(PlaylistCommentService playlistCommentService) {
    this.playlistCommentService = playlistCommentService;
  }

  public String getViewName() {
    return viewName;
  }
}