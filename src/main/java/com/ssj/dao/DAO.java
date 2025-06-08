package com.ssj.dao;

import java.io.Serializable;
import java.util.List;

public interface DAO<T> {

  void save(T instance);

  T get(Serializable key);

  void delete(T instance);

  List<T> findAll();
}
