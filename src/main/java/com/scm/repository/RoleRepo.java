package com.scm.repository;

import com.scm.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Roles,Integer> {
    Optional<Roles> findByName(String name);
}
