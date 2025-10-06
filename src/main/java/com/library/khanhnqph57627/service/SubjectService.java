package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.Subject;
import com.library.khanhnqph57627.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @PostConstruct
    public void initSampleData() {
        if (subjectRepository.count() == 0) {
            Subject subject1 = new Subject();
            subject1.setName("Toán Cao Cấp");
            subject1.setCode("MATH101");
            subject1.setDescription("Môn học về toán cao cấp cho sinh viên kỹ thuật");
            subject1.setDepartment("Khoa Toán");

            Subject subject2 = new Subject();
            subject2.setName("Lập trình Java");
            subject2.setCode("PROG201");
            subject2.setDescription("Môn học lập trình hướng đối tượng với Java");
            subject2.setDepartment("Khoa Công nghệ Thông tin");

            Subject subject3 = new Subject();
            subject3.setName("Cơ sở dữ liệu");
            subject3.setCode("DB301");
            subject3.setDescription("Nguyên lý và thiết kế cơ sở dữ liệu");
            subject3.setDepartment("Khoa Công nghệ Thông tin");

            Subject subject4 = new Subject();
            subject4.setName("Mạng Máy Tính");
            subject4.setCode("NET401");
            subject4.setDescription("Nguyên lý mạng máy tính và truyền thông");
            subject4.setDepartment("Khoa Công nghệ Thông tin");

            subjectRepository.saveAll(Arrays.asList(subject1, subject2, subject3, subject4));
        }
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectById(Integer id) {
        return subjectRepository.findById(id);
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Integer id) {
        subjectRepository.deleteById(id);
    }

    public List<Subject> searchSubjects(String keyword) {
        return subjectRepository.findByNameContainingIgnoreCase(keyword);
    }
}
