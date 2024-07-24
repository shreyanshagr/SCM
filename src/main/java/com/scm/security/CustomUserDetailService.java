package com.scm.security;

import com.scm.entities.User;
import com.scm.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user = this.userRepo.findByEmail(username).orElseThrow(()->new com.scm.exception.UsernameNotFoundException("User","username",username));
       return  user;
    }
}
