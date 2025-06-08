package com.ssj.hibernate;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class TablePrefixImprovedNamingStrategy extends ImprovedNamingStrategy {

  private String prefix;

  public TablePrefixImprovedNamingStrategy(String prefix) {
    super();
    this.prefix = (prefix == null ? "" : prefix);
  }

  @Override
  public String tableName(String tableName) {
    return prefix + super.tableName(tableName);
  }
}
