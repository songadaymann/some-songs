package com.ssj.service.song;

import com.ssj.dao.song.SongCommentDAO;
import com.ssj.dao.song.SongCommentReplyDAO;
import com.ssj.model.base.CommentEvent;
import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.song.search.SongCommentReplySearchFactory;
import com.ssj.model.song.search.SongCommentSearchFactory;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class SongCommentServiceImpl
implements   SongCommentService, ApplicationEventPublisherAware {

  private SongCommentDAO songCommentDAO;
  private SongCommentReplyDAO songCommentReplyDAO;
  private ApplicationEventPublisher publisher;

  public List<SongComment> findComments(SongCommentSearch search) {
    List<SongComment> comments = null;
    search.setTotalResults(songCommentDAO.doCount(search));
    if (search.getTotalResults() > 0) {
      comments = songCommentDAO.doSearch(search);
    }
    return comments;
  }

  @Transactional(readOnly = false)
  public void setSongComment(User user, Song song, String comment) {
    SongComment userComment = songCommentDAO.getComment(user, song);
    String commentAction = null;
    if (comment == null) {
      if (userComment != null) {
        song.getComments().remove(userComment);
        commentAction = CommentEvent.DELETE_ACTION_VALUE;
      }
    } else {
      if (comment.length() > SongComment.COMMENT_TOTAL_MAX_LENGTH) {
        throw new SongException("Comment can not be longer than " + SongComment.COMMENT_TOTAL_MAX_LENGTH + " characters");
      }
      if (userComment == null) {
        userComment = new SongComment();

        userComment.setUser(user);
        userComment.setSong(song);

        song.getComments().add(userComment);
        commentAction = CommentEvent.ADD_ACTION_VALUE;
      } else {
        commentAction = CommentEvent.EDIT_ACTION_VALUE;
      }

      userComment.setChangeDate(new Date());
      userComment.setCommentText(comment);
      userComment.splitContent();
    }
    if (commentAction != null) {
      SongCommentEvent event = new SongCommentEvent(this, userComment);
      event.setActionValue(commentAction);
      publisher.publishEvent(event);
    }
  }

  public SongComment getSongComment(int songCommentId) {
    return songCommentDAO.get(songCommentId);
  }

  public List<SongCommentReply> findReplies(SongCommentReplySearch search) {
    List<SongCommentReply> replies = null;
    search.setTotalResults(songCommentReplyDAO.doCount(search));
    if (search.getTotalResults() > 0) {
      replies = songCommentReplyDAO.doSearch(search);
    }
    return replies;
  }

  public SongCommentReply getSongCommentReply(int replyId) {
    return songCommentReplyDAO.get(replyId);
  }

  @Transactional(readOnly = false)
  public void saveReply(SongCommentReply reply) {
    reply.splitContent();
    songCommentReplyDAO.save(reply);
    SongComment originalComment = reply.getOriginalComment();
    int numReplies = songCommentReplyDAO.countReplies(originalComment);
    originalComment.setNumReplies(numReplies);
    songCommentDAO.save(originalComment);
  }

  @Transactional(readOnly = false)
  public void deleteReply(SongCommentReply reply) {
    SongComment originalComment = reply.getOriginalComment();
    songCommentReplyDAO.delete(reply);
    int numReplies = songCommentReplyDAO.countReplies(originalComment);
    originalComment.setNumReplies(numReplies);
    songCommentDAO.save(originalComment);
  }

  public SongComment getComment(User user, Song song) {
    return songCommentDAO.getComment(user, song);
  }

  public SongCommentReplySearch getSongCommentReplySearch(int searchId) {
    return SongCommentReplySearchFactory.getSearch(searchId);
  }

  public SongCommentSearch getSongCommentSearch(int searchId) {
    return SongCommentSearchFactory.getSearch(searchId);
  }

  public SongCommentReplySearch getDefaultCommentReplySearch() {
    return SongCommentReplySearchFactory.getSearch(SongCommentReplySearchFactory.SEARCH_ID_RECENT);
  }

  public SongCommentSearch getDefaultCommentSearch() {
    return SongCommentSearchFactory.getSearch(SongCommentSearchFactory.SEARCH_ID_RECENT);
  }

  public int countComments(User user) {
    return songCommentDAO.countComments(user);
  }

  public int countReplies(User user) {
    return songCommentReplyDAO.countReplies(user);
  }

  public SongComment getNewerComment(int commentId) {
    return songCommentDAO.getNewerComment(commentId);
  }

  public SongComment getOlderComment(int commentId) {
    return songCommentDAO.getOlderComment(commentId);
  }

  public SongComment getNewerCommentForSong(int comentId, int songId) {
    return songCommentDAO.getNewerCommentForSong(comentId, songId);
  }

  public SongComment getOlderCommentForSong(int comentId, int songId) {
    return songCommentDAO.getOlderCommentForSong(comentId, songId);
  }

  public int getCommentPosition(SongComment comment) {
    return songCommentDAO.getCommentPosition(comment);
  }

  public int getReplyPosition(SongCommentReply reply) {
    return songCommentReplyDAO.getReplyPosition(reply);
  }

  @Transactional(readOnly = false)
  public void deleteComment(SongComment commentId) {
    songCommentDAO.delete(commentId);
  }

  public SongComment getComment(int commentId) {
    return songCommentDAO.get(commentId);
  }

  public List getSongCommentReplies(Set multiquoteReplyIds) {
    return songCommentReplyDAO.findRepliesByIds(multiquoteReplyIds);
  }

  @Transactional(readOnly = false)
  public void saveComment(SongComment comment) {
    songCommentDAO.save(comment);
  }

  @Autowired
  public void setSongCommentDAO(SongCommentDAO songCommentDAO) {
    this.songCommentDAO = songCommentDAO;
  }

  @Autowired
  public void setSongCommentReplyDAO(SongCommentReplyDAO songCommentReplyDAO) {
    this.songCommentReplyDAO = songCommentReplyDAO;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }
}
