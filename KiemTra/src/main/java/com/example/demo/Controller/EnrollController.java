package com.example.demo.Controller;

import com.example.demo.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EnrollController {

	@Autowired
	private EnrollmentService enrollmentService;

	@PostMapping("/enroll/{courseId}")
	public String enroll(@PathVariable Long courseId,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		String message = enrollmentService.enroll(authentication.getName(), courseId);
		if (message.contains("thành công")) {
			redirectAttributes.addFlashAttribute("success", message);
		} else {
			redirectAttributes.addFlashAttribute("warning", message);
		}
		return "redirect:/home";
	}

	@GetMapping("/my-course")
	public String myCourses(Authentication authentication, Model model) {
		var enrollments = enrollmentService.getMyCourses(authentication.getName());
		int totalCredits = enrollments.stream()
				.mapToInt(e -> e.getCourse().getCredits())
				.sum();
		model.addAttribute("enrollments", enrollments);
		model.addAttribute("totalCredits", totalCredits);
		return "student/my-course";
	}
}