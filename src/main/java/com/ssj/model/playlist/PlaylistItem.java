package com.ssj.model.playlist;

import com.ssj.model.song.Song;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@SuppressWarnings({"RedundantIfStatement"})
@Entity
public class PlaylistItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "playlistItemId")
  private int id;
  @ManyToOne
  @JoinColumn(name = "playlistId", nullable = false)
  private Playlist playlist;
  @ManyToOne
  @JoinColumn(name = "songId", nullable = false)
  private Song song;

  @Length(max = 1000, message = "Note can not contain more than 1000 characters")
  @Lob
  private String note;
  private int ordinal;

  @Column(updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate = createDate;

  public PlaylistItem() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Playlist getPlaylist() {
    return playlist;
  }

  public void setPlaylist(Playlist playlist) {
    this.playlist = playlist;
  }

  public Song getSong() {
    return song;
  }

  public void setSong(Song song) {
    this.song = song;
  }

  public int getOrdinal() {
    return ordinal;
  }

  public void setOrdinal(int ordinal) {
    this.ordinal = ordinal;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlaylistItem that = (PlaylistItem) o;

    if (ordinal != that.ordinal) return false;
    if (playlist != null ? !playlist.equals(that.playlist) : that.playlist != null) return false;
    if (song != null ? !song.equals(that.song) : that.song != null) return false;

    return true;
  }

  public int hashCode() {
    int result;
    result = id;
    result = 31 * result + (playlist != null ? playlist.hashCode() : 0);
    result = 31 * result + (song != null ? song.hashCode() : 0);
    result = 31 * result + ordinal;
    result = 31 * result + (note != null ? note.hashCode() : 0);
    return result;
  }
}
