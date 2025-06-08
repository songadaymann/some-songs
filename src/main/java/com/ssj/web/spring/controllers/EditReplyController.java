package com.ssj.web.spring.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import com.ssj.service.user.UserException;
import com.ssj.service.messageboard.MessageBoardService;
import com.ssj.service.messageboard.MessageBoardException;
import com.ssj.model.messageboard.MessageBoardPost;
import com.ssj.model.messageboard.MessageBoardTopic;
import com.ssj.model.user.User;
import com.ssj.web.spring.security.SecurityUtil;

import javax.validation.Valid;
import java.util.*;

/**
 * Class javadoc comment here...
 *
 * @author sam
 * @version $Id$
 */
@Controller
public class EditReplyController {

  private static final Logger LOGGER = Logger.getLogger(EditReplyController.class);

  private String formView = "user/reply";
  private String successView = "redirect:/view_post";

  private MessageBoardService messageBoardService;

  @ModelAttribute
  protected void populateModel(WebRequest request,
                               Model model,
                               @RequestParam(value = "id", required = false) Integer postId,
                               @RequestParam(value = "threadId", required = false) Integer threadId,
                               @RequestParam(value = "quoteId", required = false) Integer quoteId) {

    MessageBoardPost post = null;
    if (postId != null) {
      post = messageBoardService.getPost(postId);
    } else if (threadId != null) {
      MessageBoardPost thread = messageBoardService.getPost(threadId);
      if (thread != null) {
        post = new MessageBoardPost();
        post.setOriginalPost(thread);
        post.setSubject(thread.getSubject());
      }
    }
    if (post == null) {
      throw new MessageBoardException("Could not find thread or reply");
    } else if (post.getId() != 0) {
      User user = SecurityUtil.getUser();
      if (user == null || (user.getId() != post.getUser().getId() && !user.isAdmin())) {
        throw new MessageBoardException("You do not have access to edit this post");
      }
    }
    post.mergeContent();

    model.addAttribute("reply", post);

    if (post.getId() == 0) {
      // adding a post, handle quoting (don't quote when editing)
      Set<MessageBoardPost> quotePosts = new HashSet<MessageBoardPost>();

      if (quoteId != null) {
        MessageBoardPost quotePost = messageBoardService.getPost(quoteId);
        if (quotePost != null) {
          quotePost.mergeContent();
          quotePosts.add(quotePost);
        }
      }

      Integer multiquoteThreadId = (Integer) request.getAttribute("multiquoteThreadId", WebRequest.SCOPE_SESSION);
      Set multiquotePostIds = (Set) request.getAttribute("multiquotePostIds", WebRequest.SCOPE_SESSION);

      if (multiquotePostIds != null && !multiquotePostIds.isEmpty() && multiquoteThreadId != null && multiquoteThreadId.equals(post.getOriginalPost().getId())) {
        List multiquotePosts = messageBoardService.getPosts(multiquotePostIds);
        for (Object multiquotePostObj : multiquotePosts) {
          MessageBoardPost multiquotePost = (MessageBoardPost) multiquotePostObj;
          multiquotePost.mergeContent();
          quotePosts.add(multiquotePost);
        }
      }

      model.addAttribute("quotePosts", quotePosts);
    }

    // get topic list for right nav
    List<MessageBoardTopic> topics = messageBoardService.findTopics();
    model.addAttribute("topics", topics);

    // get current topic here to keep this logic out of the JSP
    MessageBoardTopic currentTopic;
    if (post.getOriginalPost() != null) {
      currentTopic = post.getOriginalPost().getTopic();
      model.addAttribute("currentTopic", currentTopic);
    }
  }

  @RequestMapping(value = "/user/reply", method = RequestMethod.GET)
  public String formHandler() {
    return getFormView();
  }

  @RequestMapping(value = "/user/reply", method = RequestMethod.POST)
  public ModelAndView onSubmit(@ModelAttribute("reply") @Valid MessageBoardPost post, BindingResult errors,
                               WebRequest request) {
    if (!errors.hasErrors()) {
      try {
        User user = SecurityUtil.getUser();
        if (post.getId() == 0) {
          // set the user for new posts
          post.setUser(user);
        } else if (user == null || (user.getId() != post.getUser().getId() && !user.isAdmin())) {
          throw new MessageBoardException("You do not have access to edit this post.");
        }


        messageBoardService.savePost(post);

        request.removeAttribute("multiquoteThreadId", WebRequest.SCOPE_SESSION);
        request.removeAttribute("multiquotePostIds", WebRequest.SCOPE_SESSION);

      } catch (UserException e) {
        LOGGER.error(e);
        errors.reject("error.thread", e.getMessage());
      }
    }
    ModelAndView modelAndView;
    if (errors.hasErrors()) {
      modelAndView = new ModelAndView(getFormView(), errors.getModel());
    } else {
      modelAndView = new ModelAndView(getSuccessView(), "id", post.getId());
    }

    return modelAndView;
  }

  @Autowired
  public void setMessageBoardService(MessageBoardService messageBoardService) {
    this.messageBoardService = messageBoardService;
  }

  public String getFormView() {
    return formView;
  }

  public String getSuccessView() {
    return successView;
  }
}
