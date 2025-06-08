package com.ssj.dao.song;

import com.ssj.model.song.BrokenLinkReport;
import com.ssj.model.song.Song;
import com.ssj.dao.AbstractSpringDAO;

import java.util.List;
import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Repository
public class BrokenLinkReportDAOImpl extends AbstractSpringDAO<BrokenLinkReport> implements BrokenLinkReportDAO {

  @Autowired
  public BrokenLinkReportDAOImpl(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  private static final String UPDATE_JOB_ID_AND_STATUS_HQL =
      "update BrokenLinkReport set jobId = :jobId where jobId is null";

  public void updateJobId(int jobId) {
    getCurrentSession().createQuery(UPDATE_JOB_ID_AND_STATUS_HQL).setParameter("jobId", jobId).executeUpdate();
  }

  public List<BrokenLinkReport> findByJobId(int jobId) {
    return getCurrentSession().createCriteria(BrokenLinkReport.class).add(Restrictions.eq("jobId", jobId)).add(Restrictions.eq("status", 0)).addOrder(Order.asc("createDate")).list();
  }

  private static final String DELETE_OLD_REPORT_HQL =
      "delete BrokenLinkReport where createDate < :date";

  public void deleteReportsBeforeDate(Date date) {
    getCurrentSession().createQuery(DELETE_OLD_REPORT_HQL).setParameter("date", date).executeUpdate();
  }

  private static final String UPDATE_STATUS_BY_SONG_HQL =
      "update BrokenLinkReport set processedDate = current_timestamp, status = :status where song = :song";

  public void updateStatusBySong(int status, Song song) {
    getCurrentSession().createQuery(UPDATE_STATUS_BY_SONG_HQL).setParameter("status", status).setParameter("song", song).executeUpdate();
  }

  public List<BrokenLinkReport> findNewReports(int start, int pageSize) {
    Criteria criteria = createCriteria();
    criteria.addOrder(Order.desc("createDate"));
    criteria.add(Restrictions.isNull("processedDate"));
    criteria.setFetchMode("song", FetchMode.JOIN);
    criteria.setFirstResult(start);
    criteria.setMaxResults(pageSize);
    return criteria.list();
  }
}
