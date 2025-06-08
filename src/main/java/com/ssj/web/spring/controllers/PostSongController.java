package com.ssj.web.spring.controllers;

import com.ssj.service.user.SocialUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.soundcloud.api.SoundCloud;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ssj.service.user.UserService;
import com.ssj.service.user.UserException;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.song.SongService;
import com.ssj.model.user.User;
import com.ssj.model.artist.Artist;
import com.ssj.model.artist.comparator.ArtistNameComparator;
import com.ssj.model.song.Song;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.*;

@Controller
public class PostSongController {

  private static final Logger LOGGER = Logger.getLogger(PostSongController.class);

  private String formView = "user/post_song";
  private String successView = "redirect:/songs/";

  protected final Log logger = LogFactory.getLog(getClass());

  private UserService userService;
  private ArtistService artistService;
  private SongService songService;
  private SocialUserService socialUserService;

  @ModelAttribute
  protected void formBackingObject(Model model) {
    Song song = new Song();
    // add an artist to hold the artist id/name data
    song.setArtist(new Artist());

    model.addAttribute(song);

    // only allow users to add songs to their own artists
    User user = SecurityUtil.getUser();
    Set<Artist> artists = user.getArtists();
    List<Artist> allArtists = new ArrayList<Artist>();//artistService.getAllArtistsForUser(user);
    allArtists.addAll(artists);
    allArtists.addAll(artistService.getArtistsByOtherUser(user));
    Collections.sort(allArtists, new ArtistNameComparator());
    model.addAttribute("artists", allArtists);

    // limit users to posting 1 every 24 hours 
    try {
      userService.isAbleToPostSong(user);
    } catch (UserException e) {
      LOGGER.error(e);
//      errors.reject("error.user", e.getMessage());
      model.addAttribute("postError", e.getMessage());
    }

    if (socialUserService.getApi(user, SoundCloud.class) != null) {
      model.addAttribute("soundCloudConnected", "true");
    }
  }

  @RequestMapping(value = "/user/post_song", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/post_song", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute @Valid Song song, BindingResult errors) {
    if (song.getArtist().getId() < 0) {
      errors.rejectValue("artist.id", "", "Select an artist, or select &quot;New artist...&quot; and enter the name of the new artist");
    } else if (song.getArtist().getId() == 0) {
      if (!StringUtils.hasText(song.getArtist().getName())) {
        errors.rejectValue("artist.name", "", "Please provide a name for the new artist");
      } else if (song.getArtist().getName().length() > 128) {
        errors.rejectValue("artist.name", "", "Artist names cannot be longer than 128 characters");
      }
    }
    if (!errors.hasErrors()) {
      User user = SecurityUtil.getUser();


      Artist artist = song.getArtist();
      if (artist.getUser() == null) {
        artist.setUser(user);
      }

      try {
        userService.isAbleToPostSong(user);

        if (artist.getId() == 0) {
          if (artistService.findArtistByName(artist.getName()) != null) {
            throw new UserException("There is already an artist with that name, please pick a different name");
          }
          artistService.save(artist);
        } else if (!artistService.canEditArtist(artist, user)) {
          throw new UserException("You do not have permission to post a song with this artist");
        }

        songService.save(song);
        user.setLastSongPostDate(song.getCreateDate());
        userService.save(user);

      } catch (UserException e) {
        LOGGER.error(e);
        errors.reject("error.song", e.getMessage());
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    } else {
      modelAndView = new ModelAndView(getSuccessView() + song.getTitleForUrl() + "-" + song.getId());
    }
    return modelAndView;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setSocialUserService(SocialUserService socialUserService) {
    this.socialUserService = socialUserService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}