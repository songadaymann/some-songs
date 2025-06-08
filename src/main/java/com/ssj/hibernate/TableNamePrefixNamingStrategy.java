package com.ssj.hibernate;

import org.hibernate.cfg.DefaultNamingStrategy;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class TableNamePrefixNamingStrategy extends DefaultNamingStrategy {

  private String prefix;

  public TableNamePrefixNamingStrategy(String prefix) {
    super();
    this.prefix = (prefix == null ? "" : prefix);
  }

  @Override
  public String classToTableName(String className) {
    return prefix + super.classToTableName(className);
  }
}
