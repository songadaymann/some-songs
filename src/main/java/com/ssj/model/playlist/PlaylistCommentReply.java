package com.ssj.model.playlist;

import com.ssj.model.base.CommentReply;

import javax.persistence.*;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
//@Access(AccessType.PROPERTY)
@AttributeOverride(name = "id", column = @Column(name = "playlistCommentReplyId"))
public class PlaylistCommentReply extends CommentReply<PlaylistComment> {

  private PlaylistComment playlistComment;

/*
  public PlaylistComment getPlaylistComment() {
    return playlistComment;
  }

  public void setPlaylistComment(PlaylistComment playlistComment) {
    this.playlistComment = playlistComment;
  }
*/

  @ManyToOne(targetEntity = PlaylistComment.class)
  @JoinColumn(name = "playlistCommentId", nullable = false)
  @Override
  public PlaylistComment getOriginalComment() {
    return playlistComment;
  }

  @Override
  public void setOriginalComment(PlaylistComment originalComment) {
    playlistComment = originalComment;
  }
}
