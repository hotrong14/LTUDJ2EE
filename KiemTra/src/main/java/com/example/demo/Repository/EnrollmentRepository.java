package com.example.demo.Repository;

import com.example.demo.Model.Enrollment;
import com.example.demo.Model.Student;
import com.example.demo.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
	List<Enrollment> findByStudent(Student student);

	Optional<Enrollment> findByStudentAndCourse(Student student, Course course);

	boolean existsByStudentAndCourse(Student student, Course course);
}