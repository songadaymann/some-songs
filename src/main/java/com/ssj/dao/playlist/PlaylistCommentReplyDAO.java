package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistCommentReply;
import com.ssj.model.playlist.PlaylistComment;
import com.ssj.model.playlist.search.PlaylistCommentReplySearch;
import com.ssj.dao.SearchDAO;

import java.util.Set;
import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistCommentReplyDAO extends SearchDAO<PlaylistCommentReply, PlaylistCommentReplySearch> {
  public int getReplyPosition(PlaylistCommentReply reply);

  public List findRepliesByIds(Set multiquoteReplyIds);

  public int countReplies(PlaylistComment comment);

}
