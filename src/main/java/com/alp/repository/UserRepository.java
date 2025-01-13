package com.alp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alp.entities.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

	Optional<User> findByUsername(String username);

}
