package com.example.demo.Controller;

import com.example.demo.Service.CourseService;
import com.example.demo.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.demo.Model.Course;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

	@Autowired
	private CourseService courseService;

	@Autowired
	private EnrollmentService enrollmentService;

	@GetMapping({ "/", "/home" })
	public String home(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String keyword,
			Authentication authentication,
			Model model) {

		Page<Course> coursePage = courseService.getCourses(keyword, page, 5);
		model.addAttribute("coursePage", coursePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", coursePage.getTotalPages());

		// Truyền set courseId đã enroll để ẩn/hiện nút
		if (authentication != null && authentication.isAuthenticated()) {
			boolean isStudent = authentication.getAuthorities()
					.contains(new SimpleGrantedAuthority("ROLE_STUDENT"));
			if (isStudent) {
				Set<Long> enrolledIds = enrollmentService
						.getMyCourses(authentication.getName())
						.stream()
						.map(e -> e.getCourse().getId())
						.collect(Collectors.toSet());
				model.addAttribute("enrolledIds", enrolledIds);
			}
		}

		return "Home";
	}
}