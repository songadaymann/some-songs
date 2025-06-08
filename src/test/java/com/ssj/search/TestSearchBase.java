package com.ssj.search;

import junit.framework.TestCase;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class TestSearchBase extends TestCase {

  public void testSetStartResultNum() {
    SearchBase search = new SearchBaseImpl();

    assertEquals(search.getStartResultNum(), 0);

    search.setStartResultNum(-1);

    assertEquals(search.getStartResultNum(), 0);

    search.setStartResultNum(0);

    assertEquals(search.getStartResultNum(), 0);

    search.setStartResultNum(1);

    assertEquals(search.getStartResultNum(), 1);
  }

  public void testSetTotalResults() {
    SearchBase search = new SearchBaseImpl();

    assertEquals(search.getTotalResults(), 0);

    search.setTotalResults(-1);

    assertEquals(search.getTotalResults(), 0);

    search.setTotalResults(0);

    assertEquals(search.getTotalResults(), 0);

    search.setTotalResults(10);

    assertEquals(search.getTotalResults(), 10);
  }

  public void testGetEndResultNum() {
    SearchBase search = new SearchBaseImpl();
    search.setResultsPerPage(5);

    assertEquals(search.getEndResultNum(), 0);

    search.setTotalResults(1);

    assertEquals(search.getEndResultNum(), 0);

    search.setTotalResults(2);

    assertEquals(search.getEndResultNum(), 1);

    search.setTotalResults(5);

    assertEquals(search.getEndResultNum(), 4);

    search.setTotalResults(6);

    assertEquals(search.getEndResultNum(), 4);

    search.setTotalResults(10);

    assertEquals(search.getEndResultNum(), 4);

    search.setStartResultNum(5);

    assertEquals(search.getEndResultNum(), 9);
  }
}
