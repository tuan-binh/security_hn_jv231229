package com.ra.repository;

import com.ra.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<Users,Long> {
	Optional<Users> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
}
