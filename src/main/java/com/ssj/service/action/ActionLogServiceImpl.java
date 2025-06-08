package com.ssj.service.action;

import com.ssj.dao.action.ActionLogDAO;
import com.ssj.model.action.ActionLog;
import com.ssj.model.user.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class ActionLogServiceImpl
implements ActionLogService, ApplicationListener<UserEvent> {

  private ActionLogDAO actionLogDAO;

  @Autowired
  public void setActionLogDAO(ActionLogDAO actionLogDAO) {
    this.actionLogDAO = actionLogDAO;
  }

  @Override
  @Transactional(readOnly = false)
  public void onApplicationEvent(UserEvent actionEvent) {
    ActionLog log = new ActionLog();
    log.setAction(actionEvent.getAction());
    log.setActionValue(actionEvent.getActionValue());
    log.setObjectId(actionEvent.getObjectId());
    log.setObjectType(actionEvent.getObjectType());
    log.setUser(actionEvent.getUser());
    log.setTime(new Date(actionEvent.getTimestamp()));
    actionLogDAO.save(log);
  }
}
