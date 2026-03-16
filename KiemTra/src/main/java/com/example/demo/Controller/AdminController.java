package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Service.CategoryService;
import com.example.demo.Service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping({"", "/", "/courses"})
    public String listCourses(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "") String keyword,
                              Model model) {
        Page<Course> coursePage = courseService.searchCourses(keyword, page, 10);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("keyword", keyword);
        return "admin/courses";
    }

    @GetMapping("/courses/add")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/course-form";
    }

    @PostMapping("/courses/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                            BindingResult result,
                            @RequestParam("imageFile") MultipartFile imageFile,
                            RedirectAttributes redirectAttributes,
                            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/course-form";
        }
        try {
            courseService.saveWithImage(course, imageFile);
            redirectAttributes.addFlashAttribute("success", "Thêm học phần thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.findById(id)
            .orElseThrow(() -> new RuntimeException("Course not found: " + id));
        model.addAttribute("course", course);
        model.addAttribute("categories", categoryService.findAll());
        return "admin/course-form";
    }

    @PostMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable Long id,
                             @Valid @ModelAttribute("course") Course course,
                             BindingResult result,
                             @RequestParam("imageFile") MultipartFile imageFile,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/course-form";
        }
        try {
            course.setId(id);
            // Keep existing image if no new file uploaded
            if (imageFile.isEmpty()) {
                courseService.findById(id).ifPresent(existing -> course.setImage(existing.getImage()));
                courseService.save(course);
            } else {
                courseService.saveWithImage(course, imageFile);
            }
            redirectAttributes.addFlashAttribute("success", "Cập nhật học phần thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Xóa học phần thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
        }
        return "redirect:/admin/courses";
    }
}