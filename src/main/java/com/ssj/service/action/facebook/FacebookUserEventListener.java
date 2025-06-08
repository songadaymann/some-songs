package com.ssj.service.action.facebook;

import com.ssj.model.action.facebook.FacebookActionLog;
import com.ssj.model.user.User;
import com.ssj.model.user.UserEvent;
import com.ssj.service.user.SocialUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.core.task.TaskExecutor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class FacebookUserEventListener implements ApplicationListener<UserEvent> {

  private static final Logger LOGGER = Logger.getLogger(FacebookActionServiceImpl.class);

  private FacebookActionService facebookActionService;
  private SocialUserService socialUserService;

  private String facebookAppNamespace;

  private TaskExecutor executor;

  @Override
  public void onApplicationEvent(UserEvent event) {
    User user = event.getUser();
    LOGGER.info("FacebookUserEventListener received user event");
    try {
      if (user.isPublishToTimeline() &&
          facebookActionService.isTimelineEvent(event) &&
          !facebookActionService.isAlreadyPublished(event)) {

        LOGGER.info("FacebookUserEventListener event should be published");

        ConnectionRepository connectionRepository = socialUserService.createConnectionRepository(user.getUsername());
        Connection<Facebook> facebookConnection = connectionRepository.findPrimaryConnection(Facebook.class);
        // make everything final that will be needed in the asynch call
        final Facebook facebook = facebookConnection.getApi();
        final MultiValueMap<String, Object> data = new LinkedMultiValueMap<String, Object>(1);
        data.set(event.getObjectType(), facebookActionService.getObjectUrl(event));
        final String connection = facebookAppNamespace + ":" + event.getAction();
        final FacebookActionLog log = new FacebookActionLog();
        log.setFacebookUserId(facebookConnection.createData().getProviderUserId());
        log.setAction(event.getAction());
        log.setObject(event.getObjectType());
        log.setObjectId(event.getObjectId());
        LOGGER.info("FacebookUserEventListener scheduling publishing");
        executor.execute(new Runnable() {
          @Override
          public void run() {
            try {
              LOGGER.info("FacebookUserEventListener publishing event");
              String graphId = facebook.publish("me", connection, data);//"12345";
              log.setFacebookGraphId(graphId);
              // have to make this a call to a different service of transaction proxy stuff doesn't work
              facebookActionService.save(log);
              LOGGER.info("FacebookUserEventListener posted action, got id " + graphId);
            } catch (Exception e) {
              LOGGER.error("FacebookUserEventListener error posting OGP action", e);
            }
          }
        });
      }
    } catch (Exception e) {
      LOGGER.error("FacebookUserEventListener handling event", e);
    }
  }

  @Autowired
  public void setFacebookActionService(FacebookActionService facebookActionService) {
    this.facebookActionService = facebookActionService;
  }

  @Autowired
  public void setSocialUserService(SocialUserService socialUserService) {
    this.socialUserService = socialUserService;
  }

  @Autowired
  @Qualifier("taskExecutor")
  public void setExecutor(TaskExecutor executor) {
    this.executor = executor;
  }

  @Value("${facebook.somesongs.app.namespace:}")
  public void setFacebookAppNamespace(String facebookAppNamespace) {
    this.facebookAppNamespace = facebookAppNamespace;
  }
}
