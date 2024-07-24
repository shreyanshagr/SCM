package com.scm.controller;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.payload.Message;
import com.scm.payload.MessageType;
import com.scm.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@Slf4j
public class PageController {

    @Autowired
    private UserService userService;

    /**
     * Handles the request for the home page.
     * @return the name of the view to render
     */

    @RequestMapping(value = {"/","/home"})
    public String home() {
        return "home";
    }

    /**
     * Handles the request for the about page.
     *
     * @param model the model to pass attributes to the view
     * @return the name of the view to render
     */
    @RequestMapping("/about")
    public String about(Model model) {
        System.out.println("About called !!");
        return "about";
    }

    /**
     * Handles the request for the services page.
     *
     * @param model the model to pass attributes to the view
     * @return the name of the view to render
     */
    @RequestMapping("/services")
    public String services(Model model) {
        System.out.println("Services called !!");
        return "services";
    }

    /**
     * Handles the request for the contact page.
     *
     * @param model the model to pass attributes to the view
     * @return the name of the view to render
     */
    @GetMapping("/contact")
    public String contact(Model model) {
        System.out.println("Contact called !!");
        return "contact";
    }

    /**
     * Handles the request for the login page.
     *
     * @param model the model to pass attributes to the view
     * @return the name of the view to render
     */
    @RequestMapping("/login")
    public String login(Model model) {
        System.out.println("Login called !!");
        return "login";
    }

    /**
     * Handles the request for the registration page.
     *
     * @param model the model to pass attributes to the view
     * @return the name of the view to render
     */
    @GetMapping("/register")
    public String signup(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        System.out.println("Register called !!");
        return "register";
    }

    /**
     * Processes the registration form submission.
     *
     * @param userForm       the form data submitted by the user
     * @param bindingResult  the result of validating the form data
     * @param session        the HTTP session to store messages
     * @return the name of the view to render or redirect
     */
    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult bindingResult, HttpSession session) {
        // Log the form data
        log.info(userForm.toString());

        // Validate form data
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Save form data to the database
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic("default.png");

        User savedUser = this.userService.saveUser(user);
        System.out.println("User saved!!");

        // Create a success message
        Message message = Message.builder()
                .type(MessageType.green)
                .content("Registration successful !!")
                .build();
        session.setAttribute("message", message);

        // Redirect to the registration page
        return "redirect:/register";
    }
}
