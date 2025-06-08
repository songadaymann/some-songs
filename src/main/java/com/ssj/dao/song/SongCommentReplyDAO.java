package com.ssj.dao.song;

import com.ssj.model.song.SongCommentReply;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.search.SongCommentReplySearch;
import com.ssj.model.user.User;
import com.ssj.dao.SearchDAO;

import java.util.Set;
import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface SongCommentReplyDAO extends SearchDAO<SongCommentReply, SongCommentReplySearch> {
  public int countReplies(User user);

  public int getReplyPosition(SongCommentReply reply);

  public List findRepliesByIds(Set multiquoteReplyIds);

  public int countReplies(SongComment comment);

}
