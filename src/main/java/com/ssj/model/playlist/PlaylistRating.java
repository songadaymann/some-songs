package com.ssj.model.playlist;

import com.ssj.model.base.Rating;

import javax.persistence.*;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "playlistRatingId"))
public class PlaylistRating extends Rating<Playlist> {

  @ManyToOne
  @JoinColumn(name = "playlistId", nullable = false)
  private Playlist playlist;

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }

  @Override
  public Playlist getItem() {
    return getPlaylist();
  }

  @Override
  public void setItem(Playlist item) {
    setPlaylist(item);
  }
}
