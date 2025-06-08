package com.ssj.service.action.facebook;

import com.ssj.model.action.facebook.FacebookActionLog;
import com.ssj.model.user.UserEvent;

public interface FacebookActionService {

  public boolean isTimelineEvent(UserEvent event);

  public boolean isAlreadyPublished(UserEvent event);

  public void save(FacebookActionLog log);

  public Object getObjectUrl(UserEvent event);

}
