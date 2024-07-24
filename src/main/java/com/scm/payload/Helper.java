package com.scm.payload;

import com.scm.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Slf4j
public class Helper {
    public static String getEmailOfLoggedInUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2AuthenticatedPrincipal) {
            // OAuth logged in user
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
            String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
            log.info("Provider -->> " + authorizedClientRegistrationId);

            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;

            if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
                log.info("Sending username from google");
                return oAuth2User.getAttribute("email").toString();
            } else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
                log.info("Sending username from github");
                return oAuth2User.getAttribute("email") != null ? oAuth2User.getAttribute("email").toString() : oAuth2User.getAttribute("login").toString() + "@gmail.com";
            }
        } else if (principal instanceof User) {
            // SELF login user
            User user = (User) principal;
            log.info("Provider -->> " + "SELF");
            return user.getEmail();
        }

        // If the principal is not of any expected type, return null or throw an exception
        log.warn("Unrecognized principal type: " + principal.getClass());
        return null;
    }
}