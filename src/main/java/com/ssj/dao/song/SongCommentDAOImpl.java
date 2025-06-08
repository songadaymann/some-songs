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
import com.ssj.dao.AbstractSpringSearchDAO;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * User: sam
 * Date: Sep 12, 2007
 * Time: 9:33:50 AM
 * $Id$
 */
@Repository
public class SongCommentDAOImpl extends AbstractSpringSearchDAO<SongComment, SongCommentSearch, SongCommentCriteria> implements SongCommentDAO {

  @Autowired
  public SongCommentDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  protected Order[] getOrderBy(SongCommentSearch search) {
    Order[] orderByArray = new Order[1];
    String orderByField;
    switch(search.getOrderBy()) {
      case SongCommentSearch.ORDER_BY_CREATE_DATE:
      default:
        orderByField = "createDate";
        break;
    }
    orderByArray[0] = search.isDescending() ? Order.desc(orderByField) : Order.asc(orderByField);
    return orderByArray;
  }

  public SongComment getComment(User user, Song song) {
    Criteria criteria = createCriteria();
    criteria.add(Restrictions.eq("user", user));
    criteria.add(Restrictions.eq("song", song));

    return (SongComment) criteria.uniqueResult();
  }

  private static final String COUNT_SHOWN_SONG_COMMENTS_HQL =
    "select count(id) from SongRating where song.showSong = true and user = :user";

  public int countComments(User user) {
    return ((Long) getCurrentSession().createQuery(COUNT_SHOWN_SONG_COMMENTS_HQL).setParameter("user", user).iterate().next()).intValue();
  }

  private String makeUniqueId(Date createDate, int id) {
    return (createDate.getTime() / 1000) + StringUtils.leftPad(Integer.toString(id), 6, '0');
  }

  private static final String NEWER_COMMENT_HQL =
    "from SongComment " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) > :uniqueId " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0'))";

  public SongComment getNewerComment(int commentId) {
    SongComment comment = get(commentId);
    String uniqueId = makeUniqueId(comment.getCreateDate(), comment.getId());
    return getNextSongComment(NEWER_COMMENT_HQL, uniqueId);
  }

  private static final String OLDER_COMMENT_HQL =
    "from SongComment " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) < :uniqueId " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0')) desc";

  public SongComment getOlderComment(int commentId) {
    SongComment comment = get(commentId);
    String uniqueId = makeUniqueId(comment.getCreateDate(), comment.getId());
    return getNextSongComment(OLDER_COMMENT_HQL, uniqueId);
  }

  private SongComment getNextSongComment(String HQL, String uniqueId) {
    List songs = getCurrentSession().createQuery(HQL).setParameter("uniqueId", uniqueId).setMaxResults(1).list();
    SongComment songComment = null;
    if (songs != null && !songs.isEmpty()) {
      songComment = (SongComment) songs.get(0);
    }
    return songComment;
  }

  private static final String NEWER_COMMENT_FOR_SONG_HQL =
    "from SongComment " +
    "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) > :uniqueId " +
    "and song.id = :songId " +
    "order by concat(unix_timestamp(createDate), lpad(id, 6, '0'))";

  public SongComment getNewerCommentForSong(int commentId, int songId) {
    SongComment comment = get(commentId);
    String uniqueId = makeUniqueId(comment.getCreateDate(), comment.getId());
    return getNextSongCommentForSong(NEWER_COMMENT_FOR_SONG_HQL, uniqueId, songId);
  }

  private static final String OLDER_COMMENT_FOR_SONG_HQL =
      "from SongComment " +
      "where concat(unix_timestamp(createDate), lpad(id, 6, '0')) < :uniqueId " +
      "and song.id = :songId " +
      "order by concat(unix_timestamp(createDate), lpad(id, 6, '0')) desc";

  public SongComment getOlderCommentForSong(int commentId, int songId) {
    SongComment comment = get(commentId);
    String uniqueId = makeUniqueId(comment.getCreateDate(), comment.getId());
    return getNextSongCommentForSong(OLDER_COMMENT_FOR_SONG_HQL, uniqueId, songId);
  }

  private SongComment getNextSongCommentForSong(String HQL, String uniqueId, int songId) {
    List songs = getCurrentSession().createQuery(HQL)
        .setParameter("uniqueId", uniqueId)
        .setParameter("songId", songId)
        .setMaxResults(1).list();
    SongComment songComment = null;
    if (songs != null && !songs.isEmpty()) {
      songComment = (SongComment) songs.get(0);
    }
    return songComment;
  }

  private static final String GET_SONG_COMMENT_POSITION_HQL =
    "select count(*) " +
    "from SongComment " +
    "where songId = :songId " +
    "and concat(unix_timestamp(createDate), lpad(id , 5, '0')) < ( " +
    "select concat(unix_timestamp(createDate), lpad(id, 5, '0')) " +
    " from SongComment " +
    " where id = :songCommentId " +
    ")";

  public int getCommentPosition(SongComment comment) {
    Long commentCount = (Long) getCurrentSession().createQuery(GET_SONG_COMMENT_POSITION_HQL)
        .setParameter("songId", comment.getSong().getId()).setParameter("songCommentId", comment.getId())
        .uniqueResult();

    return commentCount.intValue();
  }
}
