package com.ssj.service.synch;

import com.ssj.model.artist.Artist;
import com.ssj.model.artist.ArtistBandcampAlbum;
import com.ssj.model.song.Song;
import com.ssj.model.user.User;
import com.ssj.service.artist.ArtistService;
import com.ssj.service.song.SongService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.bandcamp4j.client.BandcampClient;
import org.bandcamp4j.model.Album;
import org.bandcamp4j.model.Discography;
import org.bandcamp4j.model.Track;
import org.bandcamp4j.model.UrlInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("bandcampSynchService")
@Transactional
public class BandcampSynchServiceImpl implements BandcampSynchService {

  private static final Logger LOGGER = Logger.getLogger(BandcampSynchServiceImpl.class);

  private boolean enableSynchFromBandcamp = false;

  private SongService songService;

  private ArtistService artistService;

  private BandcampClient bandcampClient;

  public void synchFromBandcamp() {
    if (enableSynchFromBandcamp) {
      LOGGER.info("starting synch from bandcamp");

      List<Artist> artistsToSynch = artistService.findArtistsForBandcampSynch();

      LOGGER.info("# artists to synch: " + artistsToSynch.size());

      for (Artist artist : artistsToSynch) {
        try {
          String bandcampUrl = artist.getBandcampUrl();
          LOGGER.info("synching from " + bandcampUrl + " for artist " + artist.getName());

          User user = artist.getUser();
          if (!user.isCanSynchFromBandcamp()) {
            LOGGER.info("user for artist does not have permission to synch from Bandcamp, skipping");
            continue;
          }

          Long bandId = artist.getBandcampBandId();

          if (bandId == null) {
            LOGGER.debug("no band id for artist, retrieving from bandcamp");
            UrlInfo urlInfo = bandcampClient.urlInfo(bandcampUrl);
            bandId = urlInfo.getBandId();
            if (bandId != null) {
              LOGGER.debug("updating artist bandcamp band id to " + bandId);
              artist.setBandcampBandId(bandId);
              artistService.save(artist);
            } else {
              LOGGER.error("Could not get band id for bandcamp URL, skipping user");
              return;
            }
          }

          synchForArtist(artist);
        } catch (Exception e) {
          LOGGER.error(e);
        }
      }

      LOGGER.info("done!");
    } else {
      LOGGER.info("synch from Bandcamp disabled, not synching");
    }
  }

  public void synchForArtist(Artist artist) {
    LOGGER.info("synch for artist " + artist.getName());

    Map<Long, Discography> discographies = bandcampClient.bandDiscography(artist.getBandcampBandId());

    Discography discography = discographies.get(artist.getBandcampBandId());

    LOGGER.debug("# of albums: " + discography.getAlbums().size());
    LOGGER.debug("# of tracks: " + discography.getTracks().size());

    int songsAdded = 0;
    int songsAlreadyImported = 0;
    int songsMissingStreamingUrl = 0;
    int songsWithErrors = 0;

    List<Album> allTracksByAlbum = new ArrayList<Album>(discography.getAlbums().size() + 1);
    allTracksByAlbum.addAll(discography.getAlbums());
    if (!discography.getTracks().isEmpty()) {
      Album tracksWithoutAlbum = new Album();
      tracksWithoutAlbum.setTracks(discography.getTracks());
      tracksWithoutAlbum.setTitle("(songs not in albums)");
      allTracksByAlbum.add(tracksWithoutAlbum);
    }

    List<ArtistBandcampAlbum> bandcampAlbums = artist.getBandcampAlbums();
    Set<Long> bandcampAlbumIds = new HashSet<Long>();
    for (ArtistBandcampAlbum bandcampAlbum : bandcampAlbums) {
      bandcampAlbumIds.add(bandcampAlbum.getBandcampAlbumId());
    }

    for (Album album : allTracksByAlbum) {

      if (album.getAlbumId() > 0) {
        if (!bandcampAlbumIds.isEmpty() && !bandcampAlbumIds.contains(album.getAlbumId())) {
          LOGGER.info("skipping album, not selected for synch");
          // don't process the album if just synching specific albums and this isn't one of them
          continue;
        }
        // load tracks for albums
        album = bandcampClient.albumInfo(album.getAlbumId());
      } else {
        if (!bandcampAlbumIds.isEmpty()) {
          LOGGER.info("skipping tracks not in an album, user has selected specific albums to synch");
          // don't process songs that are not in an album if just synching specific albums
          continue;
        }
      }

      LOGGER.debug("album \"" + album.getTitle() + "\" # of tracks: " + album.getTracks().size());

      for (Track track : album.getTracks()) {
        Song song = songService.findSongByBandcampTrackId(track.getTrackId());

        if (song != null) {
          LOGGER.debug("found existing song with that track id, skipping track");
          songsAlreadyImported++;
          continue;
        }

        if (track.getStreamingUrl() == null) {
          LOGGER.error("No streaming URL for track, skipping " + track.getUrl());
          songsMissingStreamingUrl++;
          continue;
        }

        LOGGER.debug("adding track \"" + track.getTitle() + "\"");

        song = new Song();
        song.setAlbum(StringUtils.abbreviate(album.getTitle(), 256));
        copySongData(song, track, artist.getBandcampUrl());

        String trackArtistName = track.getArtist();
        if (trackArtistName != null && !artist.getName().equals(trackArtistName)) {
          // create new artist
          Artist trackArtist = artistService.findArtistByName(trackArtistName);
          if (trackArtist == null) {
            trackArtist = new Artist();
            trackArtist.setName(trackArtistName);
            trackArtist.setUser(artist.getUser());
            LOGGER.debug("adding artist " + trackArtistName);
            artistService.save(trackArtist);
          }
          song.setArtist(trackArtist);
        } else {
          song.setArtist(artist);
        }


        try {
          songService.save(song);

          songsAdded++;
        } catch (Exception e) {
          LOGGER.error("error saving song for track \"" + track.getTitle() + "\"", e);

          songsWithErrors++;
        }
      }
    }

    LOGGER.debug("# of songs added: " + songsAdded);
    LOGGER.debug("# of songs already imported: " + songsAlreadyImported);
    LOGGER.debug("# of songs with no streaming url: " + songsMissingStreamingUrl);
    LOGGER.debug("# of songs with exceptions: " + songsWithErrors);

    LOGGER.info("done with artist " + artist.getName());
  }

  private void copySongData(Song song, Track track, String artistBandcampUrl) {
    song.setTitle(StringUtils.abbreviate(track.getTitle(), 256));
    song.setDuration((long) (track.getDuration() * 1000));
    song.setAlbumTrackNumber(track.getNumber());
    // TODO find better way to deal with super long URLs :( - sam 040514
    song.setUrl(StringUtils.abbreviate(track.getStreamingUrl(), 256));
    String fullBandcampUrl = artistBandcampUrl;
    if (fullBandcampUrl.endsWith("/")) {
      fullBandcampUrl += track.getUrl().substring(1);
    } else {
      fullBandcampUrl += track.getUrl();
    }
    // TODO find better way to deal with super long BandCamp URLs, though, doubtful this limit will be hit - sam 040514
    song.setBandcampUrl(StringUtils.abbreviate(fullBandcampUrl, 2000));
    song.setBandcampTrackId(track.getTrackId());
    String trackAbout = track.getAbout();
    if (trackAbout != null) {
      if (trackAbout.length() <= 1000) {
        song.setInfo(trackAbout);
        song.setMoreInfo(StringUtils.abbreviate(track.getCredits(), 3000));
      } else {
        song.setInfo(trackAbout.substring(0, 1000));
        String moreInfo = trackAbout.substring(1000) + track.getCredits();
        song.setMoreInfo(StringUtils.abbreviate(moreInfo, 3000));
      }
    }
    if (song.getInfo() == null) {
      // info is required
      song.setInfo("Song imported from Bandcamp.");
    }
  }

  @Value("${enableSynchFromBandcamp:false}")
  public void setEnableSynchFromBandcamp(boolean enableSynchFromBandcamp) {
    this.enableSynchFromBandcamp = enableSynchFromBandcamp;
  }

  @Autowired
  public void setBandcampClient(BandcampClient bandcampClient) {
    this.bandcampClient = bandcampClient;
  }

  @Autowired
  public void setSongService(SongService songService) {
    this.songService = songService;
  }

  @Autowired
  public void setArtistService(ArtistService artistService) {
    this.artistService = artistService;
  }
}
