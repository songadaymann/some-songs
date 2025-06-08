package com.ssj.service.song;

import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.SongCommentReply;
import com.ssj.model.user.User;

import java.util.List;
import java.util.Set;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongCommentService {

  public List<SongComment> findComments(SongCommentSearch search);

  public void setSongComment(User user, Song song, String comment);

  public SongComment getSongComment(int songCommentId);

  public List<SongCommentReply> findReplies(SongCommentReplySearch search);

  public SongCommentReply getSongCommentReply(int replyId);

  public void saveReply(SongCommentReply reply);

  public void deleteReply(SongCommentReply reply);

  public SongComment getComment(User user, Song song);

  public SongCommentReplySearch getSongCommentReplySearch(int searchId);

  public SongCommentSearch getSongCommentSearch(int searchId);

  public SongCommentReplySearch getDefaultCommentReplySearch();

  public SongCommentSearch getDefaultCommentSearch();

  public int countComments(User user);

  public int countReplies(User user);

  public SongComment getNewerComment(int commentId);

  public SongComment getOlderComment(int commentId);

  public SongComment getNewerCommentForSong(int comentId, int songId);

  public SongComment getOlderCommentForSong(int comentId, int songId);

  public int getCommentPosition(SongComment comment);

  public int getReplyPosition(SongCommentReply reply);

  public void deleteComment(SongComment commentId);

  public SongComment getComment(int commentId);

  public List getSongCommentReplies(Set multiquoteReplyIds);

  public void saveComment(SongComment comment);
}
