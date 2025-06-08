package com.ssj.web.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import com.ssj.service.song.SongSearchService;
import com.ssj.model.user.User;
import com.ssj.model.song.search.SongSearchSearch;
import com.ssj.web.spring.security.SecurityUtil;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class SavedSearchesController {
  private static final int DEFAULT_PER_PAGE = 20;

  private String viewName = "include/my_saved_searches";

  private SongSearchService songSearchService;

  @RequestMapping("/include/my_saved_searches")
  protected ModelAndView mySearches(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                    @RequestParam(value = "resultsPerPage", required = false) Integer resultsPerPage) {
    return new ModelAndView(getViewName(), populateModel(start, resultsPerPage));
  }

  @RequestMapping("/user/saved_searches")
  protected ModelAndView savedSearches(@RequestParam(value = "start", required = false, defaultValue = "0") int start,
                                       @RequestParam(value = "resultsPerPage", required = false) Integer resultsPerPage) {
    return new ModelAndView("user/saved_searches", populateModel(start, resultsPerPage));
  }

  public ModelMap populateModel(int start, Integer resultsPerPage) {
    User user = SecurityUtil.getUser();

    ModelMap model = new ModelMap();
    if (user != null) {
      SongSearchSearch searchSearch = new SongSearchSearch();
      searchSearch.setUser(user);

      searchSearch.setStartResultNum(start);

      int perPage = resultsPerPage == null ? DEFAULT_PER_PAGE : resultsPerPage;

      searchSearch.setResultsPerPage(perPage);

      List savedSearches = songSearchService.findSavedSearches(searchSearch);

      model.addAttribute("numSavedSearches", searchSearch.getTotalResults());
      model.addAttribute("savedSearches", savedSearches);
    }

    return model;
  }

  @Autowired
  public void setSongSearchService(SongSearchService songSearchService) {
    this.songSearchService = songSearchService;
  }

  public String getViewName() {
    return viewName;
  }
}
