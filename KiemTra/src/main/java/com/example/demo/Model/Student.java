package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Long studentId;

	@NotBlank(message = "Tên đăng nhập không được để trống")
	@Size(min = 3, max = 50, message = "Tên đăng nhập phải từ 3-50 ký tự")
	@Column(nullable = false, unique = true)
	private String username;

	@NotBlank(message = "Mật khẩu không được để trống")
	@Size(min = 6, message = "Mật khẩu ít nhất 6 ký tự")
	@Column(nullable = false)
	private String password;

	@NotBlank(message = "Email không được để trống")
	@Email(message = "Email không hợp lệ")
	@Column(nullable = false, unique = true)
	private String email;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "student_roles", joinColumns = @JoinColumn(name = "student_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
}