package com.ssj.dao.user;

import com.ssj.dao.DAO;
import com.ssj.model.user.IgnoredUser;
import com.ssj.model.user.User;

import java.util.List;

public interface IgnoredUserDAO extends DAO<IgnoredUser> {

  List<IgnoredUser> findByUser(User user);
}
