package com.scm.service.impl;

import com.scm.config.AppConstants;
import com.scm.entities.Roles;
import com.scm.entities.User;
import com.scm.exception.UsernameNotFoundException;
import com.scm.repository.RoleRepo;
import com.scm.repository.UserRepo;
import com.scm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Roles userRole = roleRepo.findByName(AppConstants.USER)
                .orElseThrow(() -> new RuntimeException("User Role not found"));

        // Assign the 'ROLE_USER' role to the user
        Set<Roles> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepo.save(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        User initialUser = userRepo.findById(user.getUserId()).orElseThrow(()->new RuntimeException("Resource Not Found !!"));
        initialUser.setName(user.getName());
        initialUser.setEmail(user.getEmail());
        initialUser.setPassword(user.getPassword());
        initialUser.setPhoneNumber(user.getPhoneNumber());
        initialUser.setAbout(user.getAbout());
        initialUser.setProfilePic(user.getProfilePic());
        initialUser.setEnabled(user.isEnabled());
        initialUser.setPhoneVerified(user.isPhoneVerified());
        initialUser.setEmailVerified(user.isEmailVerified());
        initialUser.setProvider(user.getProvider());
        initialUser.setProviderId(user.getProviderId());
        User updatedUser = this.userRepo.save(initialUser);
        return Optional.of(updatedUser);
    }

    @Override
    public Optional<User> getUserById(String userId) {
        return userRepo.findById(userId);
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new RuntimeException("Resource Not Found !!"));
        userRepo.delete(user);

    }

    @Override
    public boolean isUserExist(String userId) {
        User user = this.userRepo.findById(userId).orElse(null);

        return user != null;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        Optional<User> userByEmail = this.userRepo.findByEmail(email);
        return userByEmail.isPresent();
    }

    @Override
    public Optional<List<User>> getAllUsers() {
        List<User> users = this.userRepo.findAll();
        return Optional.of(users);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepo.findByEmail(email).orElse(null);
    }
}
