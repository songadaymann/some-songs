package com.ssj.model.song;

import com.ssj.model.base.Comment;
import com.ssj.model.base.CommentReply;

import javax.persistence.*;
import java.util.Set;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:04:07 PM
 * $Id$
 */
@Entity
@AttributeOverride(name = "id", column = @Column(name = "songCommentId"))
public class SongComment extends Comment<Song> {

  @ManyToOne
  @JoinColumn(name = "songId", nullable = false)
  private Song song;

  @OneToMany(mappedBy = "originalComment", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("createDate desc")
  private Set<SongCommentReply> replies;

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

  public Set<SongCommentReply> getReplies() {
    return replies;
  }

  public void setReplies(Set<SongCommentReply> replies) {
    this.replies = replies;
  }
}
