package com.upana.studentmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upana.studentmanagement.dto.StudentDTO;
import com.upana.studentmanagement.exception.DuplicateResourceException;
import com.upana.studentmanagement.exception.ResourceNotFoundException;
import com.upana.studentmanagement.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService service;

    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        testStudentDTO = new StudentDTO(1L, "Juan Pérez", "juan@example.com", "12345678", "español");
    }

    @Test
    @DisplayName("GET /api/students - Debe retornar todos los estudiantes")
    void testGetAllStudents() throws Exception {
        List<StudentDTO> students = Arrays.asList(
                new StudentDTO(1L, "Student 1", "s1@example.com", "11111111", "español"),
                new StudentDTO(2L, "Student 2", "s2@example.com", "22222222", "inglés")
        );
        when(service.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Student 1"))
                .andExpect(jsonPath("$[1].nombre").value("Student 2"));

        verify(service, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe retornar un estudiante por ID")
    void testGetStudentById() throws Exception {
        when(service.getStudentById(1L)).thenReturn(testStudentDTO);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.correo").value("juan@example.com"));

        verify(service, times(1)).getStudentById(1L);
    }

    @Test
    @DisplayName("GET /api/students/{id} - Debe retornar 404 cuando no existe")
    void testGetStudentByIdNotFound() throws Exception {
        when(service.getStudentById(999L)).thenThrow(new ResourceNotFoundException("Estudiante no encontrado"));

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getStudentById(999L);
    }

    @Test
    @DisplayName("POST /api/students - Debe crear un nuevo estudiante")
    void testCreateStudent() throws Exception {
        StudentDTO newStudent = new StudentDTO(null, "María García", "maria@example.com", "98765432", "inglés");
        StudentDTO createdStudent = new StudentDTO(1L, "María García", "maria@example.com", "98765432", "inglés");
        when(service.createStudent(any(StudentDTO.class))).thenReturn(createdStudent);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("María García"));

        verify(service, times(1)).createStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("POST /api/students - Debe retornar 400 con datos inválidos")
    void testCreateStudentInvalidData() throws Exception {
        StudentDTO invalidStudent = new StudentDTO(null, "", "maria@example.com", "98765432", "inglés");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("POST /api/students - Debe retornar 400 con teléfono inválido")
    void testCreateStudentInvalidPhone() throws Exception {
        StudentDTO invalidStudent = new StudentDTO(null, "Carlos", "carlos@example.com", "123", "español");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("POST /api/students - Debe retornar 400 con idioma inválido")
    void testCreateStudentInvalidLanguage() throws Exception {
        StudentDTO invalidStudent = new StudentDTO(null, "Ana", "ana@example.com", "12345678", "alemán");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("POST /api/students - Debe retornar 409 con correo duplicado")
    void testCreateStudentDuplicateEmail() throws Exception {
        StudentDTO newStudent = new StudentDTO(null, "Pedro", "duplicado@example.com", "12345678", "español");
        when(service.createStudent(any(StudentDTO.class)))
                .thenThrow(new DuplicateResourceException("Correo duplicado"));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isConflict());

        verify(service, times(1)).createStudent(any(StudentDTO.class));
    }

    @Test
    @DisplayName("PUT /api/students/{id} - Debe actualizar un estudiante")
    void testUpdateStudent() throws Exception {
        StudentDTO updateDTO = new StudentDTO(null, "Juan Actualizado", "juan.nuevo@example.com", "99999999", "francés");
        StudentDTO updatedStudent = new StudentDTO(1L, "Juan Actualizado", "juan.nuevo@example.com", "99999999", "francés");
        when(service.updateStudent(eq(1L), any(StudentDTO.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Actualizado"))
                .andExpect(jsonPath("$.correo").value("juan.nuevo@example.com"));

        verify(service, times(1)).updateStudent(eq(1L), any(StudentDTO.class));
    }

    @Test
    @DisplayName("PATCH /api/students/{id} - Debe actualizar parcialmente un estudiante")
    void testPatchStudent() throws Exception {
        StudentDTO patchDTO = new StudentDTO(null, "Nombre Actualizado", null, null, null);
        StudentDTO patchedStudent = new StudentDTO(1L, "Nombre Actualizado", "juan@example.com", "12345678", "español");
        when(service.patchStudent(eq(1L), any(StudentDTO.class))).thenReturn(patchedStudent);

        mockMvc.perform(patch("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nombre Actualizado"));

        verify(service, times(1)).patchStudent(eq(1L), any(StudentDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Debe eliminar un estudiante")
    void testDeleteStudent() throws Exception {
        doNothing().when(service).deleteStudent(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").exists());

        verify(service, times(1)).deleteStudent(1L);
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Debe retornar 404 si no existe")
    void testDeleteStudentNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Estudiante no encontrado")).when(service).deleteStudent(999L);

        mockMvc.perform(delete("/api/students/999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deleteStudent(999L);
    }

    @Test
    @DisplayName("GET /api/students - Debe retornar lista vacía cuando no hay estudiantes")
    void testGetAllStudentsEmpty() throws Exception {
        when(service.getAllStudents()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(service, times(1)).getAllStudents();
    }

    @Test
    @DisplayName("POST /api/students - Debe validar formato de correo")
    void testCreateStudentInvalidEmailFormat() throws Exception {
        StudentDTO invalidStudent = new StudentDTO(null, "Test", "correo-invalido", "12345678", "español");

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStudent)))
                .andExpect(status().isBadRequest());

        verify(service, never()).createStudent(any(StudentDTO.class));
    }
}