/*
 * Copyright 2002-2007 GeneticMail LLC, all rights reserved.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
License
 * on the World Wide Web for more details:
 * http://www.fsf.org/licensing/licenses/gpl.txt
 */

package com.ssj.search;

/**
 * User: sam
 * Date: Aug 20, 2007
 * Time: 10:42:37 PM
 * $Id$
 */
public abstract class SearchBase {

  public static final int RESULTS_PER_PAGE_DEFAULT = 10;
  public static final int RESULTS_PER_PAGE_MAX = 50;

  private int orderBy;

  private boolean descending = true;

  private int startResultNum = 0;

  private int resultsPerPage = RESULTS_PER_PAGE_DEFAULT;

  private int resultsPerNextPage;

  private boolean alwaysShowFullPage = true;

  private int totalResults;

  public SearchBase() {

  }

  public int getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(int orderBy) {
    this.orderBy = orderBy;
  }

  public boolean isDescending() {
    return descending;
  }

  public void setDescending(boolean descending) {
    this.descending = descending;
  }

  public int getStartResultNum() {
    return startResultNum;
  }

  public int getEndResultNum() {
    return Math.max(Math.min(totalResults - 1, startResultNum + resultsPerPage - 1), 0);
  }

  /**
   * Sets the 0-based index of the first result on the current page.
   * If the argument is less than zero, the start result num is set to zero.
   *
   * @param startResultNum the 0-based index of the first result on the current page
   */
  public void setStartResultNum(int startResultNum) {
    this.startResultNum = Math.max(startResultNum, 0);
  }

  public int getResultsPerPage() {
    return resultsPerPage;
  }

  /**
   * Sets the number of results to show on pages after the first page.
   * Lower bound is zero, upper bound is max results per page constant.
   *
   * @param resultsPerPage the number of results to show on pages after the first page
   */
  public void setResultsPerPage(int resultsPerPage) {
    this.resultsPerPage = Math.min(Math.max(resultsPerPage, 0), RESULTS_PER_PAGE_MAX);
  }

  public int getTotalResults() {
    return totalResults;
  }

  /**
   * Sets the total number of results of the search. Also updates the
   * 0-based start result index;
   * - if the total number of results less than the number of results to
   * show on the first page of results, sets start result num to 0
   * - otherwise sets the start result num to the lesser of the result
   * for the last page of results, or the current start result num
   * (set the results to the last page, if the original start result num
   * would show less than a full page of results)
   *
   * @param totalResults the total number of results of the search
   */
  public void setTotalResults(int totalResults) {
    this.totalResults = Math.max(0, totalResults);
    if (this.totalResults <= resultsPerPage) {
      setStartResultNum(0);
    } else {
      if (alwaysShowFullPage) {
        setStartResultNum(Math.min(startResultNum, getLastFullPageResultNum()));
      }
    }
  }

  public boolean hasNextPage() {
    return (startResultNum + resultsPerPage) < totalResults;
  }

  public boolean isNextPage() {
    return hasNextPage();
  }

  /**
   * Returns the row that starts the next page of results, or the row that
   * starts the last full page of results.
   *
   * @return the row that starts the next page of results, or the row that
   * starts the last full page of results
   */
  public int getNextPageStartNum() {
    int nextPageStartNum = startResultNum + resultsPerPage;
    if (alwaysShowFullPage) {
      nextPageStartNum = Math.min(nextPageStartNum, totalResults - resultsPerPage);
    }
    return nextPageStartNum;
  }

  public boolean hasPreviousPage() {
    return startResultNum > 0;
  }

  public boolean isPreviousPage() {
    return hasPreviousPage();
  }

  public int getPreviousPageStartNum() {
    return Math.max(0, startResultNum - resultsPerPage);
  }

  public int getNumPages() {
    int numPages;
    if (resultsPerNextPage > 0) {
      numPages = (int) Math.ceil(((double) totalResults / (double) resultsPerNextPage));
    } else {
      numPages = (int) Math.ceil(((double) totalResults / (double) resultsPerPage));
    }
    return numPages;
  }

  public int[] getPageStartNums() {
    int[] pageStartNums = new int[getNumPages()];
    for (int i = 0; i < pageStartNums.length; i++) {
      pageStartNums[i] = (i * (resultsPerNextPage > 0 ? resultsPerNextPage : resultsPerPage));
    }
    return pageStartNums;
  }

  public int getLastFullPageResultNum() {
    int lastPageResultNum = 0;
    if (totalResults > resultsPerPage) {
      lastPageResultNum = totalResults - resultsPerPage;
    }
    return lastPageResultNum;
  }

  public Integer getResultsPerNextPage() {
    return resultsPerNextPage;
  }

  public void setResultsPerNextPage(int resultsPerNextPage) {
    this.resultsPerNextPage = resultsPerNextPage;
  }

  public boolean isAlwaysShowFullPage() {
    return alwaysShowFullPage;
  }

  public void setAlwaysShowFullPage(boolean alwaysShowFullPage) {
    this.alwaysShowFullPage = alwaysShowFullPage;
  }
}
