package com.example.demo.Controller;

import com.example.demo.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enroll")
public class EnrollController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/{courseId}")
    public String enroll(@PathVariable Long courseId,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        try {
            enrollmentService.enroll(authentication.getName(), courseId);
            redirectAttributes.addFlashAttribute("success", "Đăng ký học phần thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/home";
    }

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        model.addAttribute("enrollments",
            enrollmentService.getEnrollmentsByUsername(authentication.getName()));
        return "student/my-courses";
    }
}