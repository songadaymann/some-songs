package com.ssj.service.song;

import com.ssj.model.song.search.SongSearch;
import com.ssj.model.song.Song;
import com.ssj.model.song.BrokenLinkReport;
import com.ssj.dao.song.SongDAO;
import com.ssj.dao.song.BrokenLinkReportDAO;
import com.ssj.web.util.URLChecker;
import com.ssj.service.user.UserService;

import java.util.*;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: sam
 * Date: Feb 27, 2007
 * Time: 11:49:14 PM
 * $Id$
 */
@Service
@Transactional(readOnly = true)
public class SongServiceImpl
implements   SongService, ApplicationListener<SongRatingEvent> {

  private static final Logger LOGGER = Logger.getLogger(SongServiceImpl.class);

  private SongDAO songDAO;
  private BrokenLinkReportDAO brokenLinkReportDAO;
  private UserService userService;

  public SongServiceImpl() {
  }

  public List<Song> findSongs(SongSearch search) {
    List<Song> songs = null;
    search.setTotalResults(songDAO.doCount(search));
    if (search.getTotalResults() > 0) {
      songs = songDAO.doSearch(search);
    }
    return songs;
  }

  public int countSongs(SongSearch search) {
    return songDAO.doCount(search);
  }

  @Transactional(readOnly = false)
  public void save(Song song) {
    songDAO.save(song);
  }

  public Song getSong(int songId) {
    return songDAO.get(songId);
  }

  public Song getRandomSong() {
    SongSearch search = new SongSearch();
    int numSongs = songDAO.doCount(search);
    int randomIndex = (int) (Math.random() * numSongs);
    search.setStartResultNum(randomIndex);
    search.setResultsPerPage(1);
    List<Song> results = songDAO.doSearch(search);
    return results.get(0);
  }

  public Song getNewerSong(int songId) {
    return songDAO.getNewerSong(songId);
  }

  public Song getOlderSong(int songId) {
    return songDAO.getOlderSong(songId);
  }

  public Song getHigherRatedSong(int songId) {
    return songDAO.getHigherRatedSong(songId);
  }

  public Song getLowerRatedSong(int songId) {
    return songDAO.getLowerRatedSong(songId);
  }

  public float getAverageSongRating() {
    return songDAO.getAverageSongRating();
  }

  public float getAverageNumRatings() {
    return songDAO.getAverageNumRatings();
  }

  @Transactional(readOnly = false)
  public void deleteSong(Song song) {
    songDAO.delete(song);
  }

  @Transactional(readOnly = false)
  public void updateRatingInfo(Song song) {
    songDAO.updateRatingInfo(song);
  }

  private static final int BROKEN_LINK_REPORT_EXPIRATION_DAYS = 7;
  private static int BROKEN_LINK_CHECK_COUNTER = 1;
  private static final int LINK_CHECK_INTERVAL_HOURS = 2;

  /**
   * Handles broken link reports. Should be called periodically by
   * a recurring process. (Set up as a Spring Quartz batch job.)
   */
  @Transactional(readOnly = false)
  public void checkSongLinks() {
    LOGGER.debug("Starting link checker");
    // delete broken link reports older than a couple days?
    Date expiryDate = DateUtils.addDays(new Date(), -BROKEN_LINK_REPORT_EXPIRATION_DAYS);
    LOGGER.debug("Deleting broken link reports older than " + DateFormatUtils.format(expiryDate, "HH:mm:ss MM/dd/yyyy"));
    brokenLinkReportDAO.deleteReportsBeforeDate(expiryDate);
    // update broken link reports to "in progress" status, or with thread id?
    // just use an incrementing static int for thread/job id
    int jobId = BROKEN_LINK_CHECK_COUNTER++;
    LOGGER.debug("Marking reports with job id " + jobId);
    brokenLinkReportDAO.updateJobId(jobId);
    // get all "in progress" status broken link reports, or those marked with this thread's id (with the associated songs)
    List<BrokenLinkReport> brokenLinkReports = brokenLinkReportDAO.findByJobId(jobId);
    LOGGER.debug("Found " + brokenLinkReports.size() + " broken link report(s)");    
    Set<Song> processedSongs = new HashSet<Song>();
    // for each broken link report
    for (BrokenLinkReport brokenLinkReport : brokenLinkReports) {
      Song song = brokenLinkReport.getSong();
      LOGGER.debug("Report for song " + song.getId());
      // if song has not yet been processed by this job and song is not hidden
      if (!processedSongs.contains(song)) {
        LOGGER.debug("Haven't processed this song yet");
        processedSongs.add(song);
        // if song last link check date time is less than system current time in millis minus millis in hour times 2
        if (song.getLastLinkCheckDate() == null || song.getLastLinkCheckDate().before(DateUtils.addHours(new Date(), -LINK_CHECK_INTERVAL_HOURS))) {
          LOGGER.debug("Song last link check date " + (song.getLastLinkCheckDate() == null ? "is null" : "is " + DateFormatUtils.format(song.getLastLinkCheckDate(), "HH:mm:ss: MM/dd/yyyy")));
          // make an http url connection to the song url
          URLChecker urlChecker = new URLChecker(song.getUrl());
          // if the status is not 2xx
          boolean isBroken = urlChecker.isBroken();
          LOGGER.debug("URL is broken = " + isBroken);
          if (isBroken) {
            // hide song
            song.setShowSong(false);
            // email owner, bcc admin
            LOGGER.debug("Sending broken link e-mail");
            userService.sendBrokenLinkEmail(song);
          }
          // update song last link check date
          song.setLastLinkCheckDate(new Date());
          songDAO.save(song);
          // update all broken link reports for this song with broken/working status as appropriate
          LOGGER.debug("Updating broken link report status");
          brokenLinkReportDAO.updateStatusBySong(BrokenLinkReport.STATUS_PROCESSED, song);
        }
      }
    }
  }

  @Transactional(readOnly = false)
  public void reportBrokenLink(Song song) {
    BrokenLinkReport report = new BrokenLinkReport();
    report.setSong(song);
    brokenLinkReportDAO.save(report);
  }

  public List<BrokenLinkReport> getNewBrokenLinkReports(int start, int pageSize) {
    return brokenLinkReportDAO.findNewReports(start, pageSize);
  }

  public BrokenLinkReport getBrokenLinkReport(int reportId) {
    return brokenLinkReportDAO.get(reportId);
  }

  @Transactional(readOnly = false)
  public void saveBrokenLinkReport(BrokenLinkReport report) {
    brokenLinkReportDAO.save(report);
  }

  public Song findSongByBandcampTrackId(long trackId) {
    return songDAO.findByBandcmapTrackId(trackId);
  }

  @Autowired
  public void setSongDAO(SongDAO songDAO) {
    this.songDAO = songDAO;
  }

  @Autowired
  public void setBrokenLinkReportDAO(BrokenLinkReportDAO brokenLinkReportDAO) {
    this.brokenLinkReportDAO = brokenLinkReportDAO;
  }

  @Autowired
  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(final SongRatingEvent event) {
    Song song = getSong(event.getObjectId());
    updateRatingInfo(song);
  }
}
