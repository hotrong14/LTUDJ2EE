package com.example.demo.config;

import com.example.demo.Model.*;
import com.example.demo.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private RoleRepository roleRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Create roles
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role(); r.setName("ADMIN"); return roleRepository.save(r);
        });
        Role studentRole = roleRepository.findByName("STUDENT").orElseGet(() -> {
            Role r = new Role(); r.setName("STUDENT"); return roleRepository.save(r);
        });

        // Create admin account
        if (!studentRepository.existsByUsername("admin")) {
            Student admin = new Student();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);
            studentRepository.save(admin);
        }

        // Create sample student
        if (!studentRepository.existsByUsername("student1")) {
            Student s = new Student();
            s.setUsername("student1");
            s.setPassword(passwordEncoder.encode("student123"));
            s.setEmail("student1@example.com");
            Set<Role> roles = new HashSet<>();
            roles.add(studentRole);
            s.setRoles(roles);
            studentRepository.save(s);
        }

        // Create categories
        Category cat1 = categoryRepository.findAll().stream()
            .filter(c -> c.getName().equals("Công nghệ thông tin")).findFirst()
            .orElseGet(() -> { Category c = new Category(); c.setName("Công nghệ thông tin"); return categoryRepository.save(c); });

        Category cat2 = categoryRepository.findAll().stream()
            .filter(c -> c.getName().equals("Toán - Khoa học")).findFirst()
            .orElseGet(() -> { Category c = new Category(); c.setName("Toán - Khoa học"); return categoryRepository.save(c); });

        Category cat3 = categoryRepository.findAll().stream()
            .filter(c -> c.getName().equals("Kinh tế")).findFirst()
            .orElseGet(() -> { Category c = new Category(); c.setName("Kinh tế"); return categoryRepository.save(c); });

        // Create sample courses
        if (courseRepository.count() == 0) {
            String[] names = {
                "Lập trình Java", "Cơ sở dữ liệu", "Mạng máy tính",
                "Trí tuệ nhân tạo", "Giải tích 1", "Đại số tuyến tính",
                "Kinh tế vi mô", "Lập trình Web", "An toàn thông tin", "Học máy"
            };
            String[] lecturers = {
                "TS. Nguyễn Văn A", "PGS. Trần Thị B", "TS. Lê Văn C",
                "GS. Phạm Thị D", "TS. Hoàng Văn E", "PGS. Ngô Thị F",
                "TS. Đặng Văn G", "TS. Vũ Thị H", "PGS. Bùi Văn I", "TS. Đỗ Thị K"
            };
            int[] credits = {3, 3, 3, 4, 3, 3, 3, 3, 3, 4};
            Category[] cats = {cat1, cat1, cat1, cat1, cat2, cat2, cat3, cat1, cat1, cat1};

            for (int i = 0; i < names.length; i++) {
                Course course = new Course();
                course.setName(names[i]);
                course.setLecturer(lecturers[i]);
                course.setCredits(credits[i]);
                course.setCategory(cats[i]);
                course.setImage("https://picsum.photos/seed/" + (i + 1) + "/400/250");
                courseRepository.save(course);
            }
        }
    }
}