package com.ssj.model.artist;

import javax.persistence.*;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(columnNames={"artistId", "relatedArtistId"})
    }
)
public class RelatedArtist {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private int id;

  @ManyToOne
  @JoinColumn(name = "artistId", nullable = false)
  private Artist artist;
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "relatedArtistId", nullable = false)
  private Artist relatedArtist;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

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

  public Artist getRelatedArtist() {
    return relatedArtist;
  }

  public void setRelatedArtist(Artist relatedArtist) {
    this.relatedArtist = relatedArtist;
  }

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changeDate) {
    this.changeDate = changeDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
