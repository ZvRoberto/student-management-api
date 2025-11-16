package com.upana.studentmanagement.repository;

import com.upana.studentmanagement.model.Student;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class StudentRepository {

    private final Map<Long, Student> students = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    public Optional<Student> findByCorreo(String correo) {
        return students.values().stream()
                .filter(s -> s.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }

    public Student save(Student student) {
        if (student.getId() == null) {
            student.setId(idGenerator.getAndIncrement());
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student update(Student student) {
        students.put(student.getId(), student);
        return student;
    }

    public boolean deleteById(Long id) {
        return students.remove(id) != null;
    }

    public boolean existsByCorreo(String correo) {
        return students.values().stream()
                .anyMatch(s -> s.getCorreo().equalsIgnoreCase(correo));
    }

    public boolean existsByCorreoAndIdNot(String correo, Long excludeId) {
        return students.values().stream()
                .anyMatch(s -> s.getCorreo().equalsIgnoreCase(correo) && !s.getId().equals(excludeId));
    }

    public void deleteAll() {
        students.clear();
        idGenerator.set(1);
    }
}