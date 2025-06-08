package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.Song;
import com.ssj.model.artist.search.ArtistSearch;
import com.ssj.model.user.User;
import com.ssj.service.song.SongService;
import com.ssj.service.song.SongRatingService;
import com.ssj.service.song.SongCommentService;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.user.UserService;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class StatisticsController extends ParameterizableViewController {
  private SongService songService;
  private ArtistService artistService;
  private UserService userService;
  private SongRatingService songRatingService;
  private SongCommentService songCommentService;
  private MessageBoardService messageBoardService;

  @RequestMapping("/statistics")
  protected ModelAndView handleRequestInternal() {
    ModelMap model = new ModelMap();

    SongSearch songSearch = new SongSearch();
    int numSongs = songService.countSongs(songSearch);

    model.addAttribute("numSongs", numSongs);

    ArtistSearch artistSearch = new ArtistSearch();
    artistSearch.setHasShownSongs(true);
    int numArtists = artistService.countArtists(artistSearch);

    model.addAttribute("numArtists", numArtists);

    int numRecentLogins = userService.countRecentLogins();

    model.addAttribute("numRecentLogins", numRecentLogins);

    float averageSongRating = songService.getAverageSongRating();
    String averageSongRatingString = Song.RATING_FORMAT.format(averageSongRating);

    model.addAttribute("averageSongRating", averageSongRatingString);

    int averageNumRatings = (int) songService.getAverageNumRatings();

    model.addAttribute("averageNumRatings", averageNumRatings);

    List topRaters = userService.getTopRaters();

    model.addAttribute("topRaters", topRaters);

    List topCommenters = userService.getTopCommenters();

    model.addAttribute("topCommenters", topCommenters);

    User user = SecurityUtil.getUser();

    if (user != null) {
      int nonHiddenSongRated = songRatingService.countNonHiddenRatedSongs(user);

      model.addAttribute("nonHiddenSongRated", nonHiddenSongRated);

      float userAverageRating = songRatingService.getAverageSongRating(user);
      String userAverageRatingString = Song.RATING_FORMAT.format(userAverageRating);

      model.addAttribute("userAverageRating", userAverageRatingString);

      int userNumComments = songCommentService.countComments(user);

      model.addAttribute("userNumComments", userNumComments);

      int numCommentReplies = songCommentService.countReplies(user);

      model.addAttribute("numCommentReplies", numCommentReplies);

      int numMessageBoardPosts = messageBoardService.countPosts(user);

      model.addAttribute("numMessageBoardPosts", numMessageBoardPosts);

      int numMessageBoardThreads = messageBoardService.countThreads(user);

      model.addAttribute("numMessageBoardThreads", numMessageBoardThreads);

      // TODO playlist statistics?
    }

    return new ModelAndView(getViewName(), model);
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Autowired
  public void setSongRatingService(SongRatingService songRatingService) {
    this.songRatingService = songRatingService;
  }

  @Autowired
  public void setSongCommentService(SongCommentService songCommentService) {
    this.songCommentService = songCommentService;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }
}
