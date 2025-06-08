package com.ssj.service.song;

import com.ssj.model.base.Comment;
import com.ssj.model.base.CommentEvent;
import com.ssj.model.song.SongComment;

public class SongCommentEvent extends CommentEvent {
  public SongCommentEvent(Object source, SongComment comment) {
    super(source, comment);
    setObjectId(comment.getSong().getId());
    setObjectType("song");
  }
}
