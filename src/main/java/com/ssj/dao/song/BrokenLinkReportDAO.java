package com.ssj.dao.song;

import com.ssj.model.song.BrokenLinkReport;
import com.ssj.model.song.Song;
import com.ssj.dao.DAO;

import java.util.List;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public interface BrokenLinkReportDAO extends DAO<BrokenLinkReport> {

  public void updateJobId(int jobId);

  public List<BrokenLinkReport> findByJobId(int jobId);

  public void deleteReportsBeforeDate(Date date);

  public void updateStatusBySong(int status, Song song);

  public List<BrokenLinkReport> findNewReports(int start, int pageSize);
}
