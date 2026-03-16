package com.example.demo.Controller;

import com.example.demo.Model.Course;
import com.example.demo.Repository.CategoryRepository;
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
	private CategoryRepository categoryRepository;

	@GetMapping({ "", "/", "/course" })
	public String adminCourses(@RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String keyword,
			Model model) {
		Page<Course> coursePage = courseService.getCourses(keyword, page, 10);
		model.addAttribute("coursePage", coursePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", coursePage.getTotalPages());
		return "admin/course";
	}

	@GetMapping("/course/new")
	public String newCourseForm(Model model) {
		model.addAttribute("course", new Course());
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin/course-form";
	}

	@GetMapping("/course/edit/{id}")
	public String editCourseForm(@PathVariable Long id, Model model) {
		Course course = courseService.findById(id)
				.orElseThrow(() -> new RuntimeException("Course not found"));
		model.addAttribute("course", course);
		model.addAttribute("categories", categoryRepository.findAll());
		return "admin/course-form";
	}

	@PostMapping("/course/save")
	public String saveCourse(@Valid @ModelAttribute("course") Course course,
			BindingResult result,
			@RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
			RedirectAttributes redirectAttributes,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("categories", categoryRepository.findAll());
			return "admin/course-form";
		}
		try {
			courseService.save(course, imageFile);
			redirectAttributes.addFlashAttribute("success",
					course.getId() == null ? "Thêm học phần thành công!" : "Cập nhật thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
		}
		return "redirect:/admin/course";
	}

	@PostMapping("/course/delete/{id}")
	public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			courseService.delete(id);
			redirectAttributes.addFlashAttribute("success", "Xóa học phần thành công!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Không thể xóa: " + e.getMessage());
		}
		return "redirect:/admin/course";
	}
}