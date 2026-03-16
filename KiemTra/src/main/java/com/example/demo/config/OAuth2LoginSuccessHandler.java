package com.example.demo.config;

import com.example.demo.Model.Role;
import com.example.demo.Model.Student;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.StudentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Lazy
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication) throws IOException {
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String email = oAuth2User.getAttribute("email");
		String name = oAuth2User.getAttribute("name");

		// Tạo account nếu chưa tồn tại
		if (!studentRepository.findByEmail(email).isPresent()) {
			Student student = new Student();
			// Tạo username từ email (lấy phần trước @)
			String baseUsername = email.split("@")[0].replaceAll("[^a-zA-Z0-9]", "");
			String username = baseUsername;
			int count = 1;
			while (studentRepository.existsByUsername(username)) {
				username = baseUsername + count++;
			}
			student.setUsername(username);
			student.setEmail(email);
			student.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

			Role studentRole = roleRepository.findByName("ROLE_STUDENT")
					.orElseThrow(() -> new RuntimeException("ROLE_STUDENT not found"));
			Set<Role> roles = new HashSet<>();
			roles.add(studentRole);
			student.setRoles(roles);

			studentRepository.save(student);
		}

		getRedirectStrategy().sendRedirect(request, response, "/home");
	}
}