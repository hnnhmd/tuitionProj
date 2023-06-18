package com.fdmgroup.TuitionProjectSpring;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Parent, Long>{
	Optional<Parent> findByUsername(String username);
}
