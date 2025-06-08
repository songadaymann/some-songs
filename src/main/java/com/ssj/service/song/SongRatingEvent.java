package com.ssj.service.song;

import com.ssj.model.base.RateEvent;
import com.ssj.model.song.Song;
import com.ssj.model.song.SongRating;

public class SongRatingEvent extends RateEvent {
  public SongRatingEvent(Object source, SongRating rating) {
    super(source, rating);
    setObjectId(rating.getSong().getId());
    setObjectType("song");
  }
}
