package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.search.PlaylistCommentSearch;
import com.ssj.model.user.User;
import com.ssj.dao.SearchDAO;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistCommentDAO extends SearchDAO<PlaylistComment, PlaylistCommentSearch> {
  public PlaylistComment getComment(User user, Playlist playlist);

  public int getCommentPosition(PlaylistComment comment);
}
