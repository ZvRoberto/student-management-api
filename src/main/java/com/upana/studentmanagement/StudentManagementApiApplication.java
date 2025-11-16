package com.upana.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagementApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentManagementApiApplication.class, args);
		System.out.println("API de gestión de estudiantes iniciada :D");
		System.out.println("Accede a: http://localhost:8080");
	}
}