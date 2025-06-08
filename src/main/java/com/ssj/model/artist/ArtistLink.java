package com.ssj.model.artist;

import com.ssj.model.artist.Artist;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:29:10 PM
 * $Id$
 */
@Entity
public class ArtistLink {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "artistLinkId")
  private int id;

  @ManyToOne
  @JoinColumn(name = "artistId", nullable = false)
  private Artist artist;

  @Length(min = 4, max = 128, message = "Label must be 4 to 128 characters")
  private String label;

  @NotEmpty(message = "URL is required")
  @Length(min = 4, max = 128, message = "URL must be 4 to 128 characters")
  @Column(nullable = false)
  private String url;

  @Length(min = 4, max = 256, message = "Notes must be 4 to 256 characters")
  @Lob
  private String notes;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public ArtistLink() {

  }

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

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }
}
