package com.ssj.model.artist;

import javax.persistence.*;

/**
 * @author sam
 * @version $Id$
 */
@Entity
@Table(
    uniqueConstraints = @UniqueConstraint(name = "artistAlbumIdUniq", columnNames = {"artistId", "bandcampAlbumId"})
)
public class ArtistBandcampAlbum {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="artistId")
  private Artist artist;

  private long bandcampAlbumId;

//  private long bandcampAlbumTitle;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Artist getArtist() {
    return artist;
  }

  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  public long getBandcampAlbumId() {
    return bandcampAlbumId;
  }

  public void setBandcampAlbumId(long bandcampAlbumId) {
    this.bandcampAlbumId = bandcampAlbumId;
  }

/*
  public long getBandcampAlbumTitle() {
    return bandcampAlbumTitle;
  }

  public void setBandcampAlbumTitle(long bandcampAlbumTitle) {
    this.bandcampAlbumTitle = bandcampAlbumTitle;
  }
*/
}
