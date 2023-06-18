package com.fdmgroup.TuitionProjectSpring;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TuitionRepository extends JpaRepository<TuitionClass, Long> {
	Optional<TuitionClass> findByClassName(String className);
}
