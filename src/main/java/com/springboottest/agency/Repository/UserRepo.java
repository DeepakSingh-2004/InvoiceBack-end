package com.springboottest.agency.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.springboottest.agency.Entity.UserEntity;


public interface UserRepo extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);

    boolean existsByUsername(String username); 
}

