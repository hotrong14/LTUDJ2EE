package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

	@Autowired
	private EnrollmentRepository enrollmentRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private CourseRepository courseRepository;

	public String enroll(String username, Long courseId) {
		Student student = studentRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new RuntimeException("Course not found"));

		if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
			return "Bạn đã đăng ký học phần này rồi!";
		}

		Enrollment enrollment = new Enrollment();
		enrollment.setStudent(student);
		enrollment.setCourse(course);
		enrollmentRepository.save(enrollment);
		return "Đăng ký học phần thành công!";
	}

	public List<Enrollment> getMyCourses(String username) {
		Student student = studentRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Student not found"));
		return enrollmentRepository.findByStudent(student);
	}

	public boolean isEnrolled(String username, Long courseId) {
		try {
			Student student = studentRepository.findByUsername(username).orElse(null);
			Course course = courseRepository.findById(courseId).orElse(null);
			if (student == null || course == null)
				return false;
			return enrollmentRepository.existsByStudentAndCourse(student, course);
		} catch (Exception e) {
			return false;
		}
	}
}