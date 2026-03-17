package com.example.demo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Tên học phần không được để trống")
	@Column(nullable = false)
	private String name;

	private String image;

	@Min(value = 1, message = "Số tín chỉ phải ít nhất là 1")
	@Max(value = 10, message = "Số tín chỉ tối đa là 10")
	private int credits;

	@NotBlank(message = "Tên giảng viên không được để trống")
	private String lecturer;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;
}