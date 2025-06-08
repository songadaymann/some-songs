package com.ssj.dao.messageboard;

import com.ssj.model.messageboard.search.MessageBoardPostSearch;
import com.ssj.model.user.IgnoredUser;
import com.ssj.dao.SearchCriteria;
import org.hibernate.criterion.*;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
public class MessageBoardPostCriteria extends SearchCriteria<MessageBoardPostSearch> {

  protected void populateCriteria(List<Criterion> criteriaList) {
    MessageBoardPostSearch search = getSearch();

    if (search.getOriginalPost() != null) {
      // ignore topic if original post id is set
      criteriaList.add(Restrictions.eq("originalPost", search.getOriginalPost()));
    } else if (search.getTopic() != null && search.getTopic().getId() != 0) {
      criteriaList.add(Restrictions.eq("topic", search.getTopic()));
    } else {
      // otherwise if no topic is set, don't show faq posts
      // show message board threads (with topic id > 0) and posts (topic is null)
      criteriaList.add(Restrictions.or(Restrictions.gt("topic.id", 0), Restrictions.isNull("topic")));
    }

    if (search.getOnlyThreads()) {
      criteriaList.add(Restrictions.isNull("originalPost"));
    }

    if (search.getNotByIgnoredUsers() != null) {
      // subselect to get ids of user's ignored users
      DetachedCriteria ignoredUserIds = DetachedCriteria.forClass(IgnoredUser.class);
      ignoredUserIds.setProjection(Property.forName("ignoredUser.id"));
      ignoredUserIds.add(Restrictions.eq("user.id", search.getNotByIgnoredUsers()));

      criteriaList.add(Subqueries.propertyNotIn("user.id", ignoredUserIds));
    }

    if (StringUtils.isNotBlank(search.getContent())) {
      criteriaList.add(
          Restrictions.or(
              Restrictions.ilike("content", search.getContent(), MatchMode.ANYWHERE),
              Restrictions.ilike("moreContent", search.getContent(), MatchMode.ANYWHERE)
          )
      );
    }

    if (StringUtils.isNotBlank(search.getAuthorName())) {
      addAlias("user", "u");
      criteriaList.add(Restrictions.ilike("u.displayName", search.getAuthorName(), MatchMode.ANYWHERE));
    }
  }

}
