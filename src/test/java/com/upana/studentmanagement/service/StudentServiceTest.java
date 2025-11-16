package com.upana.studentmanagement.service;

import com.upana.studentmanagement.dto.StudentDTO;
import com.upana.studentmanagement.exception.DuplicateResourceException;
import com.upana.studentmanagement.exception.ResourceNotFoundException;
import com.upana.studentmanagement.model.Student;
import com.upana.studentmanagement.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private Student testStudent;
    private StudentDTO testStudentDTO;

    @BeforeEach
    void setUp() {
        testStudent = new Student(1L, "Juan Pérez", "juan@example.com", "12345678", "español");
        testStudentDTO = new StudentDTO(null, "Juan Pérez", "juan@example.com", "12345678", "español");
    }

    @Test
    @DisplayName("Debe obtener todos los estudiantes")
    void testGetAllStudents() {
        List<Student> students = Arrays.asList(testStudent);
        when(repository.findAll()).thenReturn(students);
        List<StudentDTO> result = service.getAllStudents();
        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe obtener un estudiante por ID")
    void testGetStudentById() {
        when(repository.findById(1L)).thenReturn(Optional.of(testStudent));
        StudentDTO result = service.getStudentById(1L);
        assertNotNull(result);
        assertEquals("Juan Pérez", result.getNombre());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no existe")
    void testGetStudentByIdNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getStudentById(999L));
    }

    @Test
    @DisplayName("Debe crear un nuevo estudiante")
    void testCreateStudent() {
        when(repository.existsByCorreo(anyString())).thenReturn(false);
        when(repository.save(any(Student.class))).thenReturn(testStudent);
        StudentDTO result = service.createStudent(testStudentDTO);
        assertNotNull(result);
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción con correo duplicado")
    void testCreateStudentDuplicateEmail() {
        when(repository.existsByCorreo(anyString())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.createStudent(testStudentDTO));
    }

    @Test
    @DisplayName("Debe actualizar un estudiante")
    void testUpdateStudent() {
        StudentDTO updateDTO = new StudentDTO(null, "Juan Actualizado", "juan@example.com", "12345678", "inglés");
        when(repository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(repository.existsByCorreoAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(repository.update(any(Student.class))).thenReturn(testStudent);
        StudentDTO result = service.updateStudent(1L, updateDTO);
        assertNotNull(result);
        verify(repository, times(1)).update(any(Student.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al actualizar inexistente")
    void testUpdateStudentNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateStudent(999L, testStudentDTO));
    }

    @Test
    @DisplayName("Debe actualizar parcialmente")
    void testPatchStudent() {
        StudentDTO patchDTO = new StudentDTO(null, "Nuevo Nombre", null, null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(repository.update(any(Student.class))).thenReturn(testStudent);
        StudentDTO result = service.patchStudent(1L, patchDTO);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe eliminar un estudiante")
    void testDeleteStudent() {
        when(repository.deleteById(1L)).thenReturn(true);
        service.deleteStudent(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción al eliminar inexistente")
    void testDeleteStudentNotFound() {
        when(repository.deleteById(999L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteStudent(999L));
    }

    @Test
    @DisplayName("Debe retornar lista vacía")
    void testGetAllStudentsEmpty() {
        when(repository.findAll()).thenReturn(Arrays.asList());
        List<StudentDTO> result = service.getAllStudents();
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("Debe actualizar correo en patch")
    void testPatchStudentEmail() {
        StudentDTO patchDTO = new StudentDTO(null, null, "nuevo@example.com", null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(repository.existsByCorreoAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(repository.update(any(Student.class))).thenReturn(testStudent);
        StudentDTO result = service.patchStudent(1L, patchDTO);
        assertNotNull(result);
    }

    @Test
    @DisplayName("Debe validar correo duplicado en update")
    void testUpdateDuplicateEmail() {
        StudentDTO updateDTO = new StudentDTO(null, "Test", "duplicado@example.com", "12345678", "español");
        when(repository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(repository.existsByCorreoAndIdNot(anyString(), anyLong())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.updateStudent(1L, updateDTO));
    }
}