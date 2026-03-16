package com.example.demo.Service;

import com.example.demo.Model.Role;
import com.example.demo.Model.Student;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Student register(Student student) {
		// Encode password
		student.setPassword(passwordEncoder.encode(student.getPassword()));

		// Gán role ROLE_STUDENT mặc định
		Role studentRole = roleRepository.findByName("ROLE_STUDENT")
				.orElseThrow(() -> new RuntimeException(
						"Role ROLE_STUDENT chưa có trong DB. Hãy chạy data.sql để seed roles!"));

		Set<Role> roles = new HashSet<>();
		roles.add(studentRole);
		student.setRoles(roles);

		return studentRepository.save(student);
	}

	public Optional<Student> findByUsername(String username) {
		return studentRepository.findByUsername(username);
	}

	public Optional<Student> findByEmail(String email) {
		return studentRepository.findByEmail(email);
	}

	public Student save(Student student) {
		return studentRepository.save(student);
	}

	public boolean existsByUsername(String username) {
		return studentRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return studentRepository.existsByEmail(email);
	}
}