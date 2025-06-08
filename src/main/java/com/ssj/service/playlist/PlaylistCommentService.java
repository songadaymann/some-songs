package com.ssj.service.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import com.ssj.model.playlist.search.PlaylistCommentSearch;

import java.util.List;
import java.util.Set;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistCommentService {
  public void setPlaylistComment(User user, Playlist playlist, String comment);

  public PlaylistComment getComment(User user, Playlist playlist);

  public PlaylistComment getPlaylistComment(int playlistCommentId);

  public List<PlaylistCommentReply> findReplies(PlaylistCommentReplySearch replySearch);

  public int getCommentPosition(PlaylistComment comment);

  public List<PlaylistComment> findComments(PlaylistCommentSearch commentSearch);

  public PlaylistCommentReply getPlaylistCommentReply(int replyId);

  public void saveReply(PlaylistCommentReply reply);

  public int getReplyPosition(PlaylistCommentReply reply);

  public void deleteReply(PlaylistCommentReply reply);

  public List getSongCommentReplies(Set multiquoteReplyIds);

  public PlaylistCommentReplySearch getPlaylistCommentReplySearch(int searchId);

  public PlaylistCommentSearch getPlaylistCommentSearch(int searchId);

  public PlaylistCommentReplySearch getDefaultCommentReplySearch();

  public PlaylistCommentSearch getDefaultCommentSearch();

  public void saveComment(PlaylistComment comment);
}
