package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongException;
import com.ssj.service.artist.ArtistService;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SongInfoController {

  private String viewName = "include/song_info";

  private SongService songService;
  private ArtistService artistService;

  @RequestMapping("/song_info")
  protected ModelAndView handleRequestInternal(@RequestParam("id") int songId) {
    Song song = songService.getSong(songId);
    // TODO handle exception/error better (need new view to send people to)
    if (song == null) {
      throw new SongException("Unable to find song with id " + songId);
    }

    ModelAndView modelAndView = new ModelAndView(getViewName(), "song", song);

    try {
      User user = SecurityUtil.getUser();

      boolean canEdit = artistService.canEditArtist(song.getArtist(), user);
      modelAndView.addObject("canEdit", canEdit);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return modelAndView;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  public String getViewName() {
    return viewName;
  }
}
