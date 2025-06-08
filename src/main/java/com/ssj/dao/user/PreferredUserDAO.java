package com.ssj.dao.user;

import com.ssj.dao.DAO;
import com.ssj.model.user.PreferredUser;
import com.ssj.model.user.User;

import java.util.List;

/**
 * @author sam
 * @version $Id$
 */
public interface PreferredUserDAO extends DAO<PreferredUser> {

  List<PreferredUser> findByUser(User user);
}
