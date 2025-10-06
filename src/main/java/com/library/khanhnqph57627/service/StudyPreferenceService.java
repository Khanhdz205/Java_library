package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.StudyPreference;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.repository.StudyPreferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudyPreferenceService {

    @Autowired
    private StudyPreferenceRepository studyPreferenceRepository;

    @Autowired
    private UserService userService;

    public Optional<StudyPreference> getPreferenceByUserId(Integer userId) {
        return studyPreferenceRepository.findByUserId(userId);
    }

    public StudyPreference savePreference(StudyPreference preference) {
        return studyPreferenceRepository.save(preference);
    }

    public StudyPreference createOrUpdatePreference(Integer userId, StudyPreference preferenceData) {
        Optional<User> user = userService.getUserById(userId);
        if (!user.isPresent()) {
            throw new RuntimeException("User không tồn tại");
        }

        Optional<StudyPreference> existingPreference = studyPreferenceRepository.findByUserId(userId);

        if (existingPreference.isPresent()) {
            // Update existing preference
            StudyPreference preference = existingPreference.get();
            updatePreferenceFields(preference, preferenceData);
            return studyPreferenceRepository.save(preference);
        } else {
            // Create new preference
            preferenceData.setUser(user.get());
            return studyPreferenceRepository.save(preferenceData);
        }
    }

    private void updatePreferenceFields(StudyPreference existing, StudyPreference newData) {
        existing.setPreferTextbooks(newData.isPreferTextbooks());
        existing.setPreferLectureNotes(newData.isPreferLectureNotes());
        existing.setPreferExercises(newData.isPreferExercises());
        existing.setPreferExamPapers(newData.isPreferExamPapers());
        existing.setPreferResearchPapers(newData.isPreferResearchPapers());
        existing.setPreferredStudyTime(newData.getPreferredStudyTime());
        existing.setPreferredDifficulty(newData.getPreferredDifficulty());
        existing.setFavoriteSubjects(newData.getFavoriteSubjects());
        existing.setLearningGoals(newData.getLearningGoals());
    }

    public boolean hasPreference(Integer userId) {
        return studyPreferenceRepository.existsByUserId(userId);
    }
}