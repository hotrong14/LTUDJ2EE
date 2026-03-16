package com.example.demo.config;

import com.example.demo.Model.Role;
import com.example.demo.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Override
	public void run(String... args) {
		// Tự động tạo roles nếu chưa có — fix lỗi "Role not found"
		if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
			Role admin = new Role();
			admin.setName("ROLE_ADMIN");
			roleRepository.save(admin);
			System.out.println("✅ Seeded: ROLE_ADMIN");
		}
		if (roleRepository.findByName("ROLE_STUDENT").isEmpty()) {
			Role student = new Role();
			student.setName("ROLE_STUDENT");
			roleRepository.save(student);
			System.out.println("✅ Seeded: ROLE_STUDENT");
		}
	}
}