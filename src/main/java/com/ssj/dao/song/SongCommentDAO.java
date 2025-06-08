/*
 * Copyright 2002-2007 GeneticMail LLC, all rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
License
 * on the World Wide Web for more details:
 * http://www.fsf.org/licensing/licenses/gpl.txt
 */

package com.ssj.dao.song;

import com.ssj.model.song.search.SongCommentSearch;
import com.ssj.model.song.SongComment;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.dao.SearchDAO;

/**
 * User: sam
 * Date: Sep 12, 2007
 * Time: 9:33:02 AM
 * $Id$
 */
public interface SongCommentDAO extends SearchDAO<SongComment, SongCommentSearch> {

  public SongComment getComment(User user, Song song);

  public int countComments(User user);

  public SongComment getNewerComment(int commentId);

  public SongComment getOlderComment(int commentId);

  public SongComment getNewerCommentForSong(int comentId, int songId);

  public SongComment getOlderCommentForSong(int comentId, int songId);

  public int getCommentPosition(SongComment comment);
}
