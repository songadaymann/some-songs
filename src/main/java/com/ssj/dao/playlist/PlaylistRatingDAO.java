package com.ssj.dao.playlist;

import com.ssj.model.playlist.PlaylistRating;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.user.User;
import com.ssj.dao.DAO;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistRatingDAO extends DAO<PlaylistRating> {

  public List<PlaylistRating> getRatings(User user, List<Playlist> playlists);

  public PlaylistRating getRating(User user, Playlist playlist);
}
