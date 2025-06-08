package com.ssj.service.playlist;

import com.ssj.model.base.RateEvent;
import com.ssj.model.playlist.Playlist;
import com.ssj.model.playlist.PlaylistRating;

public class PlaylistRatingEvent extends RateEvent {
  public PlaylistRatingEvent(Object source, PlaylistRating rating) {
    super(source, rating);
    setObjectId(rating.getPlaylist().getId());
    setObjectType("playlist");
  }
}
