package com.scm.controller;

import com.scm.entities.User;
import com.scm.payload.Helper;
import com.scm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Slf4j
public class RootController {

    @Autowired
    private UserService userService;

    /*this function will be used for all the requests in the app, whether protected or not*/
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication){
        if(authentication == null) return;
        log.info("Running for all requests.");

        String username = Helper.getEmailOfLoggedInUser(authentication);
        log.info("Profile page requested and user with {} tried to login!!" , username);
        User user = this.userService.getUserByEmail(username);
        model.addAttribute("loggedInUser", user);
        log.info("All about user ->" + user.toString());
    }
}
