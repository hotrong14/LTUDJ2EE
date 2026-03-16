package com.example.demo.Controller;

import com.example.demo.Service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyCourseController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/my-courses")
    public String myCourses(Authentication authentication, Model model) {
        model.addAttribute("enrollments",
            enrollmentService.getEnrollmentsByUsername(authentication.getName()));
        return "student/my-courses";
    }
}