package com.ssj.model.artist.comparator;

import com.ssj.model.artist.Artist;

import java.util.Comparator;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class ArtistNameComparator implements Comparator<Artist> {
  public int compare(Artist artist, Artist artist1) {
    return artist.getName().compareTo(artist1.getName());
  }
}
