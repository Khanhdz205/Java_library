package com.library.khanhnqph57627.entity;

import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "study_preferences")
@Data
public class StudyPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Loại tài liệu ưa thích
    private boolean preferTextbooks = true;
    private boolean preferLectureNotes = true;
    private boolean preferExercises = true;
    private boolean preferExamPapers = true;
    private boolean preferResearchPapers = false;

    // Thời gian học ưa thích
    private String preferredStudyTime; // MORNING, AFTERNOON, EVENING, NIGHT

    // Mức độ khó ưa thích
    private String preferredDifficulty; // BEGINNER, INTERMEDIATE, ADVANCED

    // Các môn học ưa thích (có thể lưu dưới dạng JSON hoặc tạo bảng riêng)
    @Column(length = 1000)
    private String favoriteSubjects;

    @Column(length = 1000)
    private String learningGoals;
}