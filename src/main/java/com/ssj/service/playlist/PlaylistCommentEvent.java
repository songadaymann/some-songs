package com.ssj.service.playlist;

import com.ssj.model.base.Comment;
import com.ssj.model.base.CommentEvent;
import com.ssj.model.playlist.PlaylistComment;

public class PlaylistCommentEvent extends CommentEvent {
  public PlaylistCommentEvent(Object source, PlaylistComment comment) {
    super(source, comment);
    setObjectId(comment.getPlaylist().getId());
    setObjectType("playlist");
  }
}
