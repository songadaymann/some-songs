package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistItem;
import com.ssj.dao.DAO;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistItemDAO extends DAO<PlaylistItem> {
  public List getByPlaylistId(int id);
}
