package com.ssj.service.playlist;

import com.ssj.model.base.CommentEvent;
import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.model.playlist.search.PlaylistCommentReplySearchFactory;
import com.ssj.model.playlist.search.PlaylistCommentSearchFactory;
import com.ssj.model.base.Comment;
import com.ssj.dao.playlist.PlaylistCommentDAO;
import com.ssj.dao.playlist.PlaylistCommentReplyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class PlaylistCommentServiceImpl
implements   PlaylistCommentService, ApplicationEventPublisherAware {
  private PlaylistCommentDAO playlistCommentDAO;
  private PlaylistCommentReplyDAO playlistCommentReplyDAO;
  private ApplicationEventPublisher publisher;

  @Transactional(readOnly = false)
  public void setPlaylistComment(User user, Playlist playlist, String comment) {
    PlaylistComment userComment = playlistCommentDAO.getComment(user, playlist);
    String playlistAction = null;
    if (comment == null) {
      if (userComment != null) {
        playlist.getComments().remove(userComment);
        playlistAction = CommentEvent.DELETE_ACTION_VALUE;
      }
    } else {
      if (comment.length() > PlaylistComment.COMMENT_TOTAL_MAX_LENGTH) {
        throw new PlaylistException("Comment can not be longer than " + Comment.COMMENT_TOTAL_MAX_LENGTH + " characters");
      }
      if (userComment == null) {
        userComment = new PlaylistComment();

        userComment.setUser(user);
        userComment.setPlaylist(playlist);

        playlist.getComments().add(userComment);
        playlistAction = CommentEvent.ADD_ACTION_VALUE;
      } else {
        playlistAction = CommentEvent.EDIT_ACTION_VALUE;
      }

      userComment.setChangeDate(new Date());
      userComment.setCommentText(comment);
      userComment.splitContent();
    }
    if (playlistAction != null) {
      PlaylistCommentEvent event = new PlaylistCommentEvent(this, userComment);
      event.setActionValue(playlistAction);
      publisher.publishEvent(event);
    }
  }

  public PlaylistComment getComment(User user, Playlist playlist) {
    return playlistCommentDAO.getComment(user, playlist);
  }

  public PlaylistComment getPlaylistComment(int playlistCommentId) {
    return playlistCommentDAO.get(playlistCommentId);
  }

  public List<PlaylistCommentReply> findReplies(PlaylistCommentReplySearch replySearch) {
    List<PlaylistCommentReply> replies = null;
    replySearch.setTotalResults(playlistCommentReplyDAO.doCount(replySearch));
    if (replySearch.getTotalResults() > 0) {
      replies = playlistCommentReplyDAO.doSearch(replySearch);
    }
    return replies;
  }

  public int getCommentPosition(PlaylistComment comment) {
    return playlistCommentDAO.getCommentPosition(comment);
  }

  public List<PlaylistComment> findComments(PlaylistCommentSearch commentSearch) {
    List<PlaylistComment> comments = null;
    commentSearch.setTotalResults(playlistCommentDAO.doCount(commentSearch));
    if (commentSearch.getTotalResults() > 0) {
      comments = playlistCommentDAO.doSearch(commentSearch);
    }
    return comments;

  }

  public PlaylistCommentReply getPlaylistCommentReply(int replyId) {
    return playlistCommentReplyDAO.get(replyId);
  }

  @Transactional(readOnly = false)
  public void saveReply(PlaylistCommentReply reply) {
    reply.splitContent();
    playlistCommentReplyDAO.save(reply);
    PlaylistComment originalComment = reply.getOriginalComment();
    int numReplies = playlistCommentReplyDAO.countReplies(originalComment);
    originalComment.setNumReplies(numReplies);
    playlistCommentDAO.save(originalComment);
  }

  public int getReplyPosition(PlaylistCommentReply reply) {
    return playlistCommentReplyDAO.getReplyPosition(reply);
  }

  @Transactional(readOnly = false)
  public void deleteReply(PlaylistCommentReply reply) {
    PlaylistComment originalComment = reply.getOriginalComment();
    playlistCommentReplyDAO.delete(reply);
    int numReplies = playlistCommentReplyDAO.countReplies(originalComment);
    originalComment.setNumReplies(numReplies);
    playlistCommentDAO.save(originalComment);
  }

  public List getSongCommentReplies(Set multiquoteReplyIds) {
    return playlistCommentReplyDAO.findRepliesByIds(multiquoteReplyIds);
  }

  public PlaylistCommentReplySearch getPlaylistCommentReplySearch(int searchId) {
    return PlaylistCommentReplySearchFactory.getSearch(searchId);
  }

  public PlaylistCommentSearch getPlaylistCommentSearch(int searchId) {
    return PlaylistCommentSearchFactory.getSearch(searchId);
  }

  public PlaylistCommentReplySearch getDefaultCommentReplySearch() {
    return PlaylistCommentReplySearchFactory.getSearch(PlaylistCommentReplySearchFactory.SEARCH_ID_RECENT);
  }

  public PlaylistCommentSearch getDefaultCommentSearch() {
    return PlaylistCommentSearchFactory.getSearch(PlaylistCommentSearchFactory.SEARCH_ID_RECENT);
  }

  @Transactional(readOnly = false)
  public void saveComment(PlaylistComment comment) {
    playlistCommentDAO.save(comment);
  }

  @Autowired
  public void setPlaylistCommentDAO(PlaylistCommentDAO playlistCommentDAO) {
    this.playlistCommentDAO = playlistCommentDAO;
  }

  @Autowired
  public void setPlaylistCommentReplyDAO(PlaylistCommentReplyDAO playlistCommentReplyDAO) {
    this.playlistCommentReplyDAO = playlistCommentReplyDAO;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    publisher = applicationEventPublisher;
  }
}
