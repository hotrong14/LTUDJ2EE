package com.example.demo.Model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "enrollments", uniqueConstraints = @UniqueConstraint(columnNames = { "student_id", "course_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Enrollment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ManyToOne
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@Column(name = "enroll_date")
	private LocalDate enrollDate;

	@PrePersist
	public void prePersist() {
		this.enrollDate = LocalDate.now();
	}
}