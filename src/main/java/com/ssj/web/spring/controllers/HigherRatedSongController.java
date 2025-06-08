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
public class HigherRatedSongController {

  private SongService songService;

  @RequestMapping("/higher_rated_song")
  protected String handleRequestInternal(@RequestParam("id") int id) {
    Song higherRatedSong;
    if (id >= 0) {
      higherRatedSong = songService.getHigherRatedSong(id);
      if (higherRatedSong == null) {
        higherRatedSong = songService.getSong(id);
        if (higherRatedSong == null) {
          throw new NotFoundException("Could not find song with id " + id);
        }
      }
    } else {
      // invalid input, send to random song
      higherRatedSong = songService.getRandomSong();
    }


    return "redirect:/songs/" + higherRatedSong.getTitleForUrl() + "-" + higherRatedSong.getId();
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
