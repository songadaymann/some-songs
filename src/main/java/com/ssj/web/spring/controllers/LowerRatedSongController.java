package com.ssj.web.spring.controllers;

import com.ssj.web.spring.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ssj.service.song.SongService;
import com.ssj.model.song.Song;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class LowerRatedSongController {

  private SongService songService;

  @RequestMapping("/lower_rated_song")
  protected String handleRequestInternal(@RequestParam("id") int id) {
    Song lowerRatedSong;
    if (id >= 0) {
      lowerRatedSong = songService.getLowerRatedSong(id);

      if (lowerRatedSong == null) {
        lowerRatedSong = songService.getSong(id);
        if (lowerRatedSong == null) {
          throw new NotFoundException("Could not find song with id " + id);
        }
      }
    } else {
      // invalid input, send to random song
      lowerRatedSong = songService.getRandomSong();
    }
    return "redirect:/songs/" + lowerRatedSong.getTitleForUrl() + "-" + lowerRatedSong.getId();
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}