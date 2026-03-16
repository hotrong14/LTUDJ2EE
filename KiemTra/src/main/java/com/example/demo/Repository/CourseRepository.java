package com.example.demo.Repository;

import com.example.demo.Model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
	Page<Course> findByNameContainingIgnoreCase(String name, Pageable pageable);

	Page<Course> findAll(Pageable pageable);
}