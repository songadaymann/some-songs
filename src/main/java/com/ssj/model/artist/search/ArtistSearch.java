package com.ssj.model.artist.search;

import com.ssj.model.user.User;
import com.ssj.search.SearchBase;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class ArtistSearch extends SearchBase {

  public static final int ORDER_BY_NAME = 1;

  public static final int RESULTS_PER_PAGE_DEFAULT = 25;

  private String name;

  private boolean nameExactMatch = false;

  private String info;

  private boolean infoExactMatch = false;

  private String nameStartsWith;

  private boolean hasShownSongs = false;

  private User user;

  private Integer inUsersFavorites;

  public ArtistSearch() {
    super();
    setResultsPerPage(RESULTS_PER_PAGE_DEFAULT);
    setOrderBy(ORDER_BY_NAME);
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public boolean isInfoExactMatch() {
    return infoExactMatch;
  }

  public void setInfoExactMatch(boolean infoExactMatch) {
    this.infoExactMatch = infoExactMatch;
  }

  public boolean isHasShownSongs() {
    return hasShownSongs;
  }

  public void setHasShownSongs(boolean hasShownSongs) {
    this.hasShownSongs = hasShownSongs;
  }

  public boolean isNameExactMatch() {
    return nameExactMatch;
  }

  public void setNameExactMatch(boolean nameExactMatch) {
    this.nameExactMatch = nameExactMatch;
  }

  public String getNameStartsWith() {
    return nameStartsWith;
  }

  public void setNameStartsWith(String nameStartsWith) {
    this.nameStartsWith = nameStartsWith;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getUser() {
    return user;
  }

  public void setInUsersFavorites(Integer inUsersFavorites) {
    this.inUsersFavorites = inUsersFavorites;
  }

  public Integer getInUsersFavorites() {
    return inUsersFavorites;
  }
}
