package com.upana.studentmanagement.service;

import com.upana.studentmanagement.dto.StudentDTO;
import com.upana.studentmanagement.exception.DuplicateResourceException;
import com.upana.studentmanagement.exception.ResourceNotFoundException;
import com.upana.studentmanagement.model.Student;
import com.upana.studentmanagement.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    public List<StudentDTO> getAllStudents() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public StudentDTO getStudentById(Long id) {
        Student student = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante con ID " + id + " no encontrado"));
        return convertToDTO(student);
    }

    public StudentDTO createStudent(StudentDTO studentDTO) {
        if (repository.existsByCorreo(studentDTO.getCorreo())) {
            throw new DuplicateResourceException("Ya existe un estudiante con el correo: " + studentDTO.getCorreo());
        }

        Student student = convertToEntity(studentDTO);
        Student savedStudent = repository.save(student);
        return convertToDTO(savedStudent);
    }

    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante con ID " + id + " no encontrado"));

        if (repository.existsByCorreoAndIdNot(studentDTO.getCorreo(), id)) {
            throw new DuplicateResourceException("Ya existe otro estudiante con el correo: " + studentDTO.getCorreo());
        }

        existingStudent.setNombre(studentDTO.getNombre());
        existingStudent.setCorreo(studentDTO.getCorreo());
        existingStudent.setNumeroTelefono(studentDTO.getNumeroTelefono());
        existingStudent.setIdioma(studentDTO.getIdioma());

        Student updatedStudent = repository.update(existingStudent);
        return convertToDTO(updatedStudent);
    }

    public StudentDTO patchStudent(Long id, StudentDTO studentDTO) {
        Student existingStudent = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante con ID " + id + " no encontrado"));

        if (studentDTO.getNombre() != null) {
            existingStudent.setNombre(studentDTO.getNombre());
        }

        if (studentDTO.getCorreo() != null) {
            if (repository.existsByCorreoAndIdNot(studentDTO.getCorreo(), id)) {
                throw new DuplicateResourceException("Ya existe otro estudiante con el correo: " + studentDTO.getCorreo());
            }
            existingStudent.setCorreo(studentDTO.getCorreo());
        }

        if (studentDTO.getNumeroTelefono() != null) {
            existingStudent.setNumeroTelefono(studentDTO.getNumeroTelefono());
        }

        if (studentDTO.getIdioma() != null) {
            existingStudent.setIdioma(studentDTO.getIdioma());
        }

        Student updatedStudent = repository.update(existingStudent);
        return convertToDTO(updatedStudent);
    }

    public void deleteStudent(Long id) {
        if (!repository.deleteById(id)) {
            throw new ResourceNotFoundException("Estudiante con ID " + id + " no encontrado");
        }
    }

    private StudentDTO convertToDTO(Student student) {
        return new StudentDTO(
                student.getId(),
                student.getNombre(),
                student.getCorreo(),
                student.getNumeroTelefono(),
                student.getIdioma()
        );
    }

    private Student convertToEntity(StudentDTO dto) {
        return new Student(
                dto.getNombre(),
                dto.getCorreo(),
                dto.getNumeroTelefono(),
                dto.getIdioma()
        );
    }
}