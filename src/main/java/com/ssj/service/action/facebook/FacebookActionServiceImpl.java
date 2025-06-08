package com.ssj.service.action.facebook;

import com.ssj.dao.action.facebook.FacebookActionLogDAO;
import com.ssj.model.action.facebook.FacebookActionLog;
import com.ssj.model.song.Song;
import com.ssj.model.user.UserEvent;
import com.ssj.service.song.SongRatingEvent;
import com.ssj.service.song.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class FacebookActionServiceImpl
implements   FacebookActionService {

  private FacebookActionLogDAO facebookActionLogDAO;

  private SongService songService;

  private String siteUrl;

  @Transactional(readOnly = false)
  public void save(FacebookActionLog log) {
    log.setPublished(new Date());
    facebookActionLogDAO.save(log);
  }

  public boolean isTimelineEvent(UserEvent event) {
    // for now only publish song rating events
    return (event instanceof SongRatingEvent);
  }

  public boolean isAlreadyPublished(UserEvent event) {
    // check if this action has already been published to the user's timeline
    return (facebookActionLogDAO.find(event.getAction(), event.getObjectType(), event.getObjectId()) != null);
  }

  public Object getObjectUrl(UserEvent event) {
    // hard coding this for songs for now, will need to register code for different object types at some point
    StringBuilder builder = new StringBuilder(siteUrl);
    builder.append("/songs/");
    Song song = songService.getSong(event.getObjectId());
    builder.append(song.getTitleForUrl()).append('-').append(song.getId());
    return builder.toString();
  }

  @Autowired
  public void setFacebookActionLogDAO(FacebookActionLogDAO facebookActionLogDAO) {
    this.facebookActionLogDAO = facebookActionLogDAO;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Value("${site.url:http://somesongs.org}")
  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }
}
