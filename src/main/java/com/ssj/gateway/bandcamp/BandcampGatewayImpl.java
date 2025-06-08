package com.ssj.gateway.bandcamp;

import com.ssj.model.artist.Artist;
import com.ssj.model.song.Song;
import org.apache.commons.lang.StringUtils;
import org.bandcamp4j.client.BandcampClient;
import org.bandcamp4j.model.Album;
import org.bandcamp4j.model.Band;
import org.bandcamp4j.model.Track;
import org.bandcamp4j.model.UrlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BandcampGatewayImpl implements BandcampGateway {

  private BandcampClient bandcampClient;
  @Override
  public Song getSong(String bandcampUrl) {
    if (StringUtils.isBlank(bandcampUrl))
      throw new IllegalArgumentException("Please provide a Bandcamp URL");

    UrlInfo urlInfo = bandcampClient.urlInfo(bandcampUrl);

    if (urlInfo == null)
      throw new IllegalArgumentException("Bandcamp did not return track info, please check your URL");

    Long trackId = urlInfo.getTrackId();
    if (trackId == null)
      throw new IllegalArgumentException("Bandcamp did not return a track id, please check your URL");

    Track track = bandcampClient.trackInfo(trackId);

    if (track == null)
      throw new IllegalArgumentException("Bandcamp did not return a track for id " + trackId);

    String artistName = track.getArtist();
    if (artistName == null) {
      long bandId = track.getBandId();
      Band band = bandcampClient.bandInfo(bandId).get(bandId);
      if (band == null)
        throw new IllegalArgumentException("Bandcamp did not return a band for id " + bandId);

      artistName = band.getName();
    }
    String albumTitle = null;
    if (track.getAlbumId() > 0) {
      Album album = bandcampClient.albumInfo(track.getAlbumId());
      albumTitle = album.getTitle();
    }
    return makeSong(track, bandcampUrl, artistName, albumTitle);
  }

  public Song makeSong(Track track, String bandcampUrl, String artistName, String albumTitle) {
    Song song = new Song();
    Artist artist = new Artist();
    artist.setName(artistName);
    song.setArtist(artist);
    song.setTitle(track.getTitle());
    // can't use Track.getUrl() here, that returns a relative URL, due to a bug in the Bandcamp API
    song.setBandcampUrl(bandcampUrl);
    song.setBandcampTrackId(track.getTrackId());
    song.setUrl(track.getStreamingUrl());
    song.setAlbum(albumTitle);
    song.setDuration((long) (track.getDuration() * 1000));
    song.setAlbumTrackNumber(track.getNumber());
    String info = track.getAbout() == null ? "Song imported from Bandcamp." : track.getAbout();
    song.setInfo(info);
    song.setMoreInfo(track.getCredits());
    return song;
  }

  @Autowired
  public void setBandcampClient(BandcampClient bandcampClient) {
    this.bandcampClient = bandcampClient;
  }
}
