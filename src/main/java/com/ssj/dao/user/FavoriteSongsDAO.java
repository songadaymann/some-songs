package com.ssj.dao.user;

import com.ssj.dao.DAO;
import com.ssj.model.user.FavoriteSong;
import com.ssj.model.user.User;

import java.util.List;

/**
 * @author sam
 * @version $Id$
 */
public interface FavoriteSongsDAO extends DAO<FavoriteSong> {
  List<FavoriteSong> findByUser(User user);
}
