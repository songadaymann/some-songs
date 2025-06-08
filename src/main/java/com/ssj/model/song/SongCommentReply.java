package com.ssj.model.song;

import com.ssj.model.base.CommentReply;

import javax.persistence.*;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:12:34 PM
 * $Id$
 */
@Entity
//@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "songCommentReplyId"))
public class SongCommentReply extends CommentReply<SongComment> {

  private SongComment songComment;

  private Song song;

/*
  public SongComment getSongComment() {
    return songComment;
  }

  public void setSongComment(SongComment songComment) {
    this.songComment = songComment;
  }
*/

  @ManyToOne(targetEntity = SongComment.class)
  @JoinColumn(name = "songCommentId", nullable = false)
  @Override
  public SongComment getOriginalComment() {
    return songComment;
  }

  @Override
  public void setOriginalComment(SongComment originalComment) {
    songComment = originalComment;
    if (originalComment != null) {
      song = originalComment.getSong();
    }
  }

  @ManyToOne
  @JoinColumn(name = "songId", nullable = false)
  public Song getSong() {
    return song;
  }

  public void setSong(Song song) {
    this.song = song;
  }
}
