package com.ssj.dao.action.facebook;

import com.ssj.dao.DAO;
import com.ssj.model.action.facebook.FacebookActionLog;

public interface FacebookActionLogDAO extends DAO<FacebookActionLog> {

  public FacebookActionLog find(String action, String object, int objectId);

}
