package com.ssj.web.spring.controllers;

import com.ssj.web.spring.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ssj.service.song.SongService;
import com.ssj.model.song.Song;

@Controller
public class NewerSongController {

  private SongService songService;

  @RequestMapping("/newer_song")
  protected String handleRequestInternal(@RequestParam("id") int id) {
    Song newerSong;
    if (id >= 0) {
      newerSong = songService.getNewerSong(id);

      if (newerSong == null) {
        newerSong = songService.getSong(id);
        if (newerSong == null) {
          throw new NotFoundException("Could not find song with id " + id);
        }
      }
    } else {
      // invalid input, send to random song
      newerSong = songService.getRandomSong();
    }
    return "redirect:/songs/" + newerSong.getTitleForUrl() + "-" + newerSong.getId();
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }
}
