package com.ssj.service.playlist;

import com.ssj.model.playlist.PlaylistRating;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.user.User;
import com.ssj.model.base.Rating;
import com.ssj.dao.playlist.PlaylistRatingDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Service
@Transactional(readOnly = true)
public class PlaylistRatingServiceImpl
implements   PlaylistRatingService, ApplicationEventPublisherAware {

  private PlaylistRatingDAO playlistRatingDAO;
  private ApplicationEventPublisher eventPublisher;

  public List<PlaylistRating> getRatings(User user, List<Playlist> playlists) {
    return playlistRatingDAO.getRatings(user, playlists);
  }

  @Transactional(readOnly = false)
  public void ratePlaylist(User user, Playlist playlist, String rating) {
    PlaylistRating userRating = playlistRatingDAO.getRating(user, playlist);

    if (userRating == null) {
      userRating = new PlaylistRating();

      userRating.setUser(user);
      userRating.setPlaylist(playlist);
    }

    Integer ratingInt = Rating.getRatingFromLabel(rating);
    if (ratingInt != null) {
      userRating.setRating(ratingInt);
    }

    PlaylistRatingEvent event = new PlaylistRatingEvent(this, userRating);
    if (ratingInt == null) {
      playlistRatingDAO.delete(userRating);
      event.setActionValue(null);
    } else {
      playlistRatingDAO.save(userRating);
      event.setActionValue(Integer.toString(userRating.getRating()));
    }
    eventPublisher.publishEvent(event);
  }

  public PlaylistRating getRating(User user, Playlist playlist) {
    return playlistRatingDAO.getRating(user, playlist);
  }

  @Autowired
  public void setPlaylistRatingDAO(PlaylistRatingDAO playlistRatingDAO) {
    this.playlistRatingDAO = playlistRatingDAO;
  }

  @Override
  public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    eventPublisher = applicationEventPublisher;
  }
}
