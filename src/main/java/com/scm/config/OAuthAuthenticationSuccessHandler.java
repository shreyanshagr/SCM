package com.scm.config;

import com.scm.entities.Providers;
import com.scm.entities.Roles;
import com.scm.entities.User;
import com.scm.repository.RoleRepo;
import com.scm.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {


              /*To check the attributes and  their properties for each type of provider */
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        oAuth2User.getAttributes().forEach((key,value)->{
            log.info("{} :: {}",key,value);
        });


                                        /*IDENTIFY THE PROVIDER*/
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        String authorizedClientRegistrationId= oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        log.info("Provider -->> " + authorizedClientRegistrationId );

                                        /*CREATE USER*/
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setEmailVerified(true);
        user.setEnabled(true);
        user.setPassword(AppConstants.DEFAULT_PASSWORD);
        user.setPhoneNumber(AppConstants.DEFAULT_PHONE_NUMBER);


                                        /* Setting roles */
        Roles userRole = roleRepo.findByName(AppConstants.USER)
                .orElseThrow(() -> new RuntimeException("User Role not found"));

        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
                                        /* Done setting roles */

                                    /*CUSTOMIZE USER AS PER PROVIDER*/
        if(authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            String email = oAuth2User.getAttribute("email").toString();
            String name  = oAuth2User.getAttribute("name").toString();
            String picture = oAuth2User.getAttribute("picture").toString();
            user.setName(name);
            user.setEmail(email);
            user.setProfilePic(picture);
            user.setAbout("User authenticated using Google OAuth");
            user.setProvider(Providers.GOOGLE);

        } else if(authorizedClientRegistrationId.equalsIgnoreCase("github")){
            String email = oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString() + "@gmail.com";
            String name  = oAuth2User.getAttribute("login").toString();
            String picture = oAuth2User.getAttribute("avatar_url").toString();
            user.setName(name);
            user.setEmail(email);
            user.setProfilePic(picture);
            user.setAbout("User authenticated using Github OAuth");
            user.setProvider(Providers.GITHUB);
        }
                                /*SAVE USER IN DB IF ALREADY NOT EXITS.*/
        User isAlreadyExists = this.userRepo.findByEmail(user.getEmail()).orElse(null);
        if(isAlreadyExists == null){
            User savedUser = this.userRepo.save(user);
            log.info("User saved with username -> " + user.getEmail()) ;
        } else log.info("User already available !! ");

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }
}
