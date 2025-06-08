package com.ssj.model.playlist;

import com.ssj.model.base.Comment;

import javax.persistence.*;
import java.util.Set;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "playlistCommentId"))
public class PlaylistComment extends Comment<Playlist> {

  @ManyToOne
  @JoinColumn(name = "playlistId", nullable = false)
  private Playlist playlist;

  @OneToMany(mappedBy = "originalComment", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  private Set<PlaylistCommentReply> replies;

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

  public Set<PlaylistCommentReply> getReplies() {
    return replies;
  }

  public void setReplies(Set<PlaylistCommentReply> replies) {
    this.replies = replies;
  }
}
