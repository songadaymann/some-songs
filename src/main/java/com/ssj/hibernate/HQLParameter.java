/**
 * All Information herein is confidential and proprietary property of ZipRealty, Inc. Any
 * unauthorized use, transmission, possession, or access through any means or via any medium
 * is strictly prohibited.
 *
 * All information is copyright 1999-2005 by ZipRealty, Inc. All rights reserved worldwide.
 */
package com.ssj.hibernate;

import org.hibernate.type.Type;

/**
 * Represents a parameter value in an HQL query.
 * Contains the Object value and hibernate Type
 * needed to set the parameter of the Query.
 * Used when we want to explicitly tell Hibernate
 * what Type implementation to use, rather than
 * letting Hibernate guess the Type.
 *
 * @version $Id: HQLParameter.java 19509 2005-12-02 02:14:15Z sam $
 */
public class HQLParameter {
  private Object value;
  private Type hibernateType;

  HQLParameter(Object value, Type hibernateType) {
    this.value = value;
    this.hibernateType = hibernateType;
  }

  public Object getValue() {
    return value;
  }

  public Type getHibernateType() {
    return hibernateType;
  }
}
