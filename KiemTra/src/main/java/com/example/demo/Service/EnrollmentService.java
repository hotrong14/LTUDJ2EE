package com.example.demo.Service;

import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Enrollment enroll(String username, Long courseId) {
        Student student = studentRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrollmentRepository.existsByStudentAndCourse(student, course)) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrollDate(LocalDate.now());

        return enrollmentRepository.save(enrollment);
    }

    public List<Enrollment> getEnrollmentsByUsername(String username) {
        Student student = studentRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Student not found"));
        return enrollmentRepository.findByStudent(student);
    }

    public boolean isEnrolled(String username, Long courseId) {
        return studentRepository.findByUsername(username)
            .flatMap(student -> courseRepository.findById(courseId)
                .map(course -> enrollmentRepository.existsByStudentAndCourse(student, course)))
            .orElse(false);
    }
}