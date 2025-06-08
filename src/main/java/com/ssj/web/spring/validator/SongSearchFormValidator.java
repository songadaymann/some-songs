package com.ssj.web.spring.validator;

import com.ssj.model.song.search.SongSearch;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class SongSearchFormValidator implements Validator {
  public boolean supports(Class<?> clazz) {
    return SongSearch.class.isAssignableFrom(clazz);
  }

  public void validate(Object target, Errors errors) {
    
  }
}
