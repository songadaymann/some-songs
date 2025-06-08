package com.ssj.model.song;

import com.ssj.model.base.Rating;

import javax.persistence.*;

/**
 * User: sam
 * Date: Mar 7, 2007
 * Time: 11:57:44 PM
 * @version $Id$
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "songRatingId"))
public class SongRating extends Rating<Song> {

  @ManyToOne
  @JoinColumn(name = "songId", nullable = false)
  private Song song;

  public Song getSong() {
    return song;
  }

  public void setSong(Song song) {
    this.song = song;
  }

  @Override
  public Song getItem() {
    return getSong();
  }

  @Override
  public void setItem(Song item) {
    setSong(item);
  }
}
