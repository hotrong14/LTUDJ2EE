package com.example.demo.Service;

import com.example.demo.Model.Course;
import com.example.demo.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;

	@Value("${app.upload.dir:uploads}")
	private String uploadDir;

	public Page<Course> getCourses(String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
		if (keyword != null && !keyword.isBlank()) {
			return courseRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable);
		}
		return courseRepository.findAll(pageable);
	}

	public Optional<Course> findById(Long id) {
		return courseRepository.findById(id);
	}

	public Course save(Course course, MultipartFile imageFile) throws IOException {
		if (imageFile != null && !imageFile.isEmpty()) {
			String filename = saveImage(imageFile);
			course.setImage(filename);
		}
		return courseRepository.save(course);
	}

	public void delete(Long id) {
		courseRepository.deleteById(id);
	}

	private String saveImage(MultipartFile file) throws IOException {
		Path uploadPath = Paths.get(uploadDir);
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
		Files.copy(file.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
		return filename;
	}
}