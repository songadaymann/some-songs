package com.ssj.service.playlist;

import com.ssj.model.user.User;
import com.ssj.model.playlist.PlaylistRating;
import com.ssj.model.playlist.Playlist;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface PlaylistRatingService {

  public List<PlaylistRating> getRatings(User user, List<Playlist> playlist);

  public void ratePlaylist(User user, Playlist playlist, String ratingString);

  public PlaylistRating getRating(User user, Playlist playlist);
}
