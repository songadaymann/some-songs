package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.song.SongService;
import com.ssj.model.song.Song;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class RandomSongController {

  private String viewName = "redirect:/songs/";
  private SongService songService;

  @RequestMapping("/random_song")
  protected String randomSongHandler() {
    Song randomSong = songService.getRandomSong();

    return viewName + randomSong.getTitleForUrl() + "-" + randomSong.getId();
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  public String getViewName() {
    return viewName;
  }
}
