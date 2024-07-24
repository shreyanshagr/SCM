package com.scm.controller;

import com.scm.entities.User;
import com.scm.payload.Helper;
import com.scm.repository.UserRepo;
import com.scm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    // user dashboard page
    @RequestMapping("/dashboard")
    public String userDashboard(){
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping("/profile")
    public String userProfile(){
        return "user/profile";
    }

//    // user add contact page
//    @PostMapping("/contact")
//    public String addContact(){
//        log.info("Add contact page requested !!");
//        return "user/add_contact";
//    }

    // user view contact page

    // user edit contact page
    @PutMapping("/contact")
    public String updateContact(){
        log.info("Update contact page requested !!");
        return "user/update_contact_view";
    }

    // user delete contact page

    // user search contact page
    //
    @RequestMapping("/ty")
    public String userty(){
        return "user/ty";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    // contact page

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }


}
