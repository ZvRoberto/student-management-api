package com.upana.studentmanagement.repository;

import com.upana.studentmanagement.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentRepositoryTest {

    private StudentRepository repository;

    @BeforeEach
    void setUp() {
        repository = new StudentRepository();
    }

    @Test
    @DisplayName("Debe guardar un estudiante y asignar ID automáticamente")
    void testSaveStudent() {
        Student student = new Student("Roberto Medrano", "medranor.roberto@.com", "12345678", "español");
        Student saved = repository.save(student);
        assertNotNull(saved.getId());
        assertEquals("Roberto Medrano", saved.getNombre());
    }

    @Test
    @DisplayName("Debe encontrar un estudiante por ID")
    void testFindById() {
        Student student = new Student("Maríana García", "mariana@gmail.com", "98765432", "inglés");
        Student saved = repository.save(student);
        Optional<Student> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("Debe retornar Optional vacío cuando no encuentra por ID")
    void testFindByIdNotFound() {
        Optional<Student> found = repository.findById(999L);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("Debe encontrar un estudiante por correo")
    void testFindByCorreo() {
        Student student = new Student("Juan jose", "JuanJose@gmails.com", "55555555", "francés");
        repository.save(student);
        Optional<Student> found = repository.findByCorreo("JuanJose@gmails.com");
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("Debe ser case insensitive al buscar por correo")
    void testFindByCorreoCaseInsensitive() {
        Student student = new Student("Jeferson Martinez", "Jefer@gmail.com", "44444444", "español");
        repository.save(student);
        Optional<Student> found = repository.findByCorreo("JEFER@GMAIL.COM");
        assertTrue(found.isPresent());
    }

    @Test
    @DisplayName("Debe obtener todos los estudiantes")
    void testFindAll() {
        repository.save(new Student("Student 1", "1@example.com", "11111111", "español"));
        repository.save(new Student("Student 2", "2@example.com", "22222222", "inglés"));
        List<Student> students = repository.findAll();
        assertEquals(2, students.size());
    }

    @Test
    @DisplayName("Debe actualizar un estudiante")
    void testUpdate() {
        Student student = new Student("Pedro Rivera", "pedro@gmail.com", "66666666", "español");
        Student saved = repository.save(student);
        saved.setNombre("Pedro Actualizado");
        Student updated = repository.update(saved);
        assertEquals("Pedro Actualizado", updated.getNombre());
    }

    @Test
    @DisplayName("Debe eliminar un estudiante por ID")
    void testDeleteById() {
        Student student = new Student("Laura Medrano", "laura@gmail.com", "77777777", "inglés");
        Student saved = repository.save(student);
        boolean deleted = repository.deleteById(saved.getId());
        assertTrue(deleted);
    }

    @Test
    @DisplayName("Debe retornar false al eliminar inexistente")
    void testDeleteByIdNotFound() {
        boolean deleted = repository.deleteById(999L);
        assertFalse(deleted);
    }

    @Test
    @DisplayName("Debe verificar si existe un correo")
    void testExistsByCorreo() {
        repository.save(new Student("Sofia Ruth", "sofia@gmail.com", "88888888", "francés"));
        assertTrue(repository.existsByCorreo("sofia@gmail.com"));
        assertFalse(repository.existsByCorreo("noexiste@gmail.com"));
    }

    @Test
    @DisplayName("Debe verificar correo excluyendo ID")
    void testExistsByCorreoAndIdNot() {
        Student s1 = repository.save(new Student("Diego", "diego@gmail.com", "99999999", "español"));
        repository.save(new Student("Elena", "elena@gmail.com", "10101010", "inglés"));
        assertFalse(repository.existsByCorreoAndIdNot("diego@gmail.com", s1.getId()));
        assertTrue(repository.existsByCorreoAndIdNot("elena@gmail.com", s1.getId()));
    }

    @Test
    @DisplayName("Debe generar IDs incrementales")
    void testIdGeneration() {
        Student s1 = repository.save(new Student("Test 1", "21@example.com", "11111111", "español"));
        Student s2 = repository.save(new Student("Test 2", "22@example.com", "22222222", "inglés"));
        assertEquals(1L, s1.getId());
        assertEquals(2L, s2.getId());
    }

    @Test
    @DisplayName("Debe limpiar todos los estudiantes")
    void testDeleteAll() {
        repository.save(new Student("Test 1", "11@example.com", "11111111", "español"));
        repository.deleteAll();
        assertEquals(0, repository.findAll().size());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay estudiantes")
    void testFindAllEmpty() {
        List<Student> students = repository.findAll();
        assertEquals(0, students.size());
    }

    @Test
    @DisplayName("Debe manejar múltiples estudiantes")
    void testMultipleStudents() {
        repository.save(new Student("Juan", "juan@gmial.com", "11111111", "español"));
        repository.save(new Student("Maríana", "maria@gmail.com", "22222222", "inglés"));
        assertEquals(2, repository.findAll().size());
    }
}