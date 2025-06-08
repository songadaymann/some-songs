package com.ssj.web.spring.controllers;

import com.ssj.model.user.IgnoredUser;
import com.ssj.model.user.PreferredUser;
import com.ssj.service.user.SocialUserService;
import com.ssj.service.user.UserEmailValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.apache.commons.lang.StringUtils;
import com.ssj.service.user.UserService;
import com.ssj.service.user.UserException;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.song.SongService;
import com.ssj.service.playlist.PlaylistService;
import com.ssj.model.user.User;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.artist.Artist;
import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.Song;
import com.ssj.model.playlist.search.PlaylistSearch;
import com.ssj.model.playlist.Playlist;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.ArrayList;

@Controller
public class MyInfoController {

  private static final int NUM_FAVORITE_SONGS = 8;
  private static final int NUM_FAVORITE_ARTISTS = 8;
  private static final int NUM_FAVORITE_PLAYLISTS = 8;
  private static final int NUM_PLAYLISTS = 8;

  private static final Logger LOGGER = Logger.getLogger(MyInfoController.class);

  private String formView = "user/my_info";
  private String successView = "redirect:/" + formView;

  private UserService userService;
  private AuthenticationManager authenticationManager;
  private ArtistService artistService;
  private SongService songService;
  private PlaylistService playlistService;
  private SocialUserService socialUserService;

  private UserEmailValidator userEmailValidator = new UserEmailValidator();

  public MyInfoController() {

  }

  @ModelAttribute()
  protected void referenceData(Model model, @RequestParam(value = "id", required = false, defaultValue = "0") int userId) {

    User user = SecurityUtil.getUser();

    if (user.isAdmin() && userId > 0) {
      User otherUser = userService.getUser(userId);
      if (otherUser != null) {
        user = otherUser;
      }
    }

    model.addAttribute("myInfoUser", user);

    // get my artists
    // still going to use Hibernate ORM for these for now
/*
    ArtistSearch artistSearch = new ArtistSearch();
    artistSearch.setUser(user);
    artistSearch.setResultsPerPage(8);
    artistService.findArtists(artistSearch);
    modelMap.put("myArtistSearch", artistSearch);
*/

    // get shared artists
    List sharedArtists = artistService.getArtistsByOtherUser(user);
    model.addAttribute("sharedArtists", sharedArtists);

    // get my favorite songs
    SongSearch songSearch = new SongSearch();
    songSearch.setInUsersFavorites(user.getId());
    songSearch.setResultsPerPage(NUM_FAVORITE_SONGS);
    List<Song> favoriteSongs = songService.findSongs(songSearch);
    model.addAttribute("favoriteSongSearch", songSearch);
    model.addAttribute("favoriteSongs", favoriteSongs);

    // get my favorite artists
    ArtistSearch favoriteArtistSearch = new ArtistSearch();
    favoriteArtistSearch.setInUsersFavorites(user.getId());
    favoriteArtistSearch.setResultsPerPage(NUM_FAVORITE_ARTISTS);
    List<Artist> favoriteArtists = artistService.findArtists(favoriteArtistSearch);
    model.addAttribute("favoriteArtistSearch", favoriteArtistSearch);
    model.addAttribute("favoriteArtists", favoriteArtists);

    // get my favorite playlists
    PlaylistSearch favoritePlaylistSearch = new PlaylistSearch();
    favoritePlaylistSearch.setInUsersFavorites(user.getId());
    favoritePlaylistSearch.setResultsPerPage(NUM_FAVORITE_PLAYLISTS);
    List<Playlist> favoritePlaylists = playlistService.findPlaylists(favoritePlaylistSearch);
    model.addAttribute("favoritePlaylistSearch", favoritePlaylistSearch);
    model.addAttribute("favoritePlaylists", favoritePlaylists);

    List<PreferredUser> preferredUsers = userService.getPreferredUsers(user);
    model.addAttribute("preferredUsers", preferredUsers);

    List<IgnoredUser> ignoredUsers = userService.getIgnoredUsers(user);
    model.addAttribute("ignoredUsers", ignoredUsers);

    // get my preferred users
    // just going to use Hibernate ORM for these for now

    // get my ignored users
    // just going to use Hibernate ORM for these for now

    // saved searches - using an include

    int numPlaylists = playlistService.countPlaylists(user);
    model.addAttribute("numPlaylists", numPlaylists);
    List playlists;
    if (numPlaylists > 0) {
      playlists = playlistService.getPlaylists(user, NUM_PLAYLISTS);
    } else {
      playlists = new ArrayList(0);
    }
    model.addAttribute("playlists", playlists);

    model.addAttribute("canAddArtist", userService.isAbleToAddArtist(user));

    // check if the account is connected to Facebook or TWitter accounts
    ConnectionRepository connectionRepository = socialUserService.createConnectionRepository(user.getUsername());

    List connections = connectionRepository.findConnections("twitter");
    if (!connections.isEmpty()) {
      model.addAttribute("twitterConnected", "true");
    }
    connections = connectionRepository.findConnections("facebook");
    if (!connections.isEmpty()) {
      model.addAttribute("facebookConnected", "true");
    }
    connections = connectionRepository.findConnections("soundcloud");
    if (!connections.isEmpty()) {
      model.addAttribute("soundcloudConnected", "true");
    }
  }

  @RequestMapping(value = "/user/my_info", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/my_info", method = RequestMethod.POST)
  protected ModelAndView onSubmit(@ModelAttribute("myInfoUser") @Valid User myInfoUser, BindingResult errors,
                                  @RequestParam(value = "newPassword", required = false) String newPassword,
                                  @RequestParam(value = "confirmPassword", required = false) String confirmPassword) {

    ModelAndView modelAndView = new ModelAndView(getFormView());

    // email validation
    // require email for people who signed up via the registration form
    if (myInfoUser.getSignupPath() == null || "form".equals(myInfoUser.getSignupPath())) {
      userEmailValidator.validate(myInfoUser, errors);
    }

    // check basic and email (if applicable) validation
    if (errors.hasErrors()) {
      return modelAndView;
    }

    // password change validation
    if (StringUtils.isNotBlank(newPassword)) {
      if (StringUtils.isBlank(confirmPassword)) {
        errors.reject("missing_new_password", "You must also enter your new password in the 'Confirm New Password' field.");
      } else if (!newPassword.equals(confirmPassword)) {
        errors.reject("incorrect_new_password", "You must enter the same password in the 'Confirm New Password' field.");
      } else if (newPassword.length() < 6 || newPassword.length() > 32) {
        errors.reject("new_password_length", "Passwords must be 6 to 32 characters long");
      } else {
        myInfoUser.setPassword(newPassword);
      }
      if (errors.hasErrors()) {
        return modelAndView;
      }
    }

    // try to update
    try {
      userService.updateMyInfo(myInfoUser);

      ModelMap model = new ModelMap();
      model.addAttribute("saved", "true");

      User loggedInUser = SecurityUtil.getUser();

      if (loggedInUser.getId() == myInfoUser.getId()) {
        SecurityUtil.updateAuthentication(myInfoUser, authenticationManager);
      } else {
        model.addAttribute("id", myInfoUser.getId());
      }

      modelAndView.setViewName(getSuccessView());
      modelAndView.addAllObjects(model);
    } catch (UserException e) {
      LOGGER.error(e);
      errors.reject("error.myUserInfo", e.getMessage());
//      modelAndView.addAllObjects(errors.getModel());
    }

    return modelAndView;
  }

  @RequestMapping(value = "/user/my_info/timeline", method = RequestMethod.POST)
  protected String onSubmit() {
    User user = SecurityUtil.getUser();
    user.setPublishToTimeline(false);
    userService.save(user);
    return "redirect:/user/my_info";
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
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
  public void setPlaylistService(PlaylistService playlistService) {
    this.playlistService = playlistService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }

  @Autowired
  public void setSocialUserService(SocialUserService socialUserService) {
    this.socialUserService = socialUserService;
  }
}
