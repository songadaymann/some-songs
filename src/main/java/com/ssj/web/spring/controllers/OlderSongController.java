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
public class OlderSongController {

  private SongService songService;

  @RequestMapping("/older_song")
  protected String handleRequestInternal(@RequestParam("id") int id) {
    Song olderSong;
    if (id >= 0) {
      olderSong = songService.getOlderSong(id);

      if (olderSong == null) {
        olderSong = songService.getSong(id);
        if (olderSong == null) {
          throw new NotFoundException("Could not find song with id " + id);
        }
      }
    } else {
      // invalid input, send to random song
      olderSong = songService.getRandomSong();
    }
    return "redirect:/songs/" + olderSong.getTitleForUrl() + "-" + olderSong.getId();
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}