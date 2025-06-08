package com.ssj.model.song;

import javax.persistence.*;
import java.util.Date;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Entity
public class BrokenLinkReport {

  public static final int STATUS_NOT_PROCESSED = 0;
  public static final int STATUS_PROCESSED = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "brokenLinkReportId")
  private int id;

  @ManyToOne
  @JoinColumn(name = "songId", nullable = false)
  private Song song;

  private Integer jobId;

  @Column(nullable = false)
  private int status = STATUS_NOT_PROCESSED;

  @Temporal(TemporalType.TIMESTAMP)
  private Date processedDate;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createDate = new Date();


  public BrokenLinkReport() {

  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Song getSong() {
    return song;
  }

  public void setSong(Song song) {
    this.song = song;
  }

  public Integer getJobId() {
    return jobId;
  }

  public void setJobId(Integer jobId) {
    this.jobId = jobId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Date getProcessedDate() {
    return processedDate;
  }

  public void setProcessedDate(Date processedDate) {
    this.processedDate = processedDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
