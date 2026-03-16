package com.example.demo.Repository;

import com.example.demo.Model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Page<Course> findAll(Pageable pageable);
    Page<Course> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}   