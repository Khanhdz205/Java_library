package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.entity.PersonalSchedule;
import com.library.khanhnqph57627.entity.StudyPreference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private PersonalScheduleService personalScheduleService;

    @Autowired
    private StudyPreferenceService studyPreferenceService;

    @Autowired
    private DocumentViewHistoryService documentViewHistoryService;

    // Đề xuất tài liệu dựa trên lịch học hiện tại
    public List<Document> getScheduleBasedRecommendations(Integer userId, int limit) {
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();
        List<PersonalSchedule> todaySchedules = personalScheduleService.getTodaySchedule(userId, today);

        List<Document> recommendations = new ArrayList<>();

        for (PersonalSchedule schedule : todaySchedules) {
            // Lấy tài liệu cùng môn học
            List<Document> subjectDocs = documentService.getDocumentsBySubject(schedule.getSubject().getId());
            recommendations.addAll(subjectDocs);
        }

        // Loại bỏ trùng lặp và giới hạn số lượng
        return recommendations.stream()
                .distinct()
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Đề xuất tài liệu dựa trên sở thích học tập
    public List<Document> getPreferenceBasedRecommendations(Integer userId, int limit) {
        Optional<StudyPreference> preferenceOpt = studyPreferenceService.getPreferenceByUserId(userId);
        if (!preferenceOpt.isPresent()) {
            return new ArrayList<>();
        }

        StudyPreference preference = preferenceOpt.get();
        List<Document> allDocuments = documentService.getAllDocuments();
        List<Document> filteredDocs = new ArrayList<>();

        for (Document doc : allDocuments) {
            if (matchesPreference(doc, preference)) {
                filteredDocs.add(doc);
            }
        }

        // Sắp xếp theo mức độ phù hợp
        filteredDocs.sort((d1, d2) -> {
            int score1 = calculatePreferenceScore(d1, preference);
            int score2 = calculatePreferenceScore(d2, preference);
            return Integer.compare(score2, score1);
        });

        return filteredDocs.stream().limit(limit).collect(Collectors.toList());
    }

    // Đề xuất tài liệu dựa trên lịch sử xem
    public List<Document> getHistoryBasedRecommendations(Integer userId, int limit) {
        List<Integer> frequentSubjectIds = documentViewHistoryService.getFrequentlyViewedSubjectIds(userId);
        List<Integer> frequentDocIds = documentViewHistoryService.getFrequentlyViewedDocumentIds(userId, 10);

        List<Document> recommendations = new ArrayList<>();

        // Đề xuất từ các môn học thường xem
        for (Integer subjectId : frequentSubjectIds) {
            List<Document> subjectDocs = documentService.getDocumentsBySubject(subjectId);
            recommendations.addAll(subjectDocs);
        }

        // Đề xuất các tài liệu tương tự với những tài liệu đã xem
        for (Integer docId : frequentDocIds) {
            Optional<Document> doc = documentService.getDocumentById(docId);
            if (doc.isPresent()) {
                List<Document> similarDocs = documentService.getRelatedDocuments(docId, 3);
                recommendations.addAll(similarDocs);
            }
        }

        return recommendations.stream()
                .distinct()
                .filter(doc -> !frequentDocIds.contains(doc.getId()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Đề xuất tổng hợp - kết hợp tất cả các yếu tố
    public Map<String, Object> getPersonalizedRecommendations(Integer userId, int limit) {
        Map<String, Object> result = new HashMap<>();

        // Lấy các loại đề xuất
        List<Document> scheduleBased = getScheduleBasedRecommendations(userId, limit);
        List<Document> preferenceBased = getPreferenceBasedRecommendations(userId, limit);
        List<Document> historyBased = getHistoryBasedRecommendations(userId, limit);

        // Kết hợp và sắp xếp
        List<Document> combined = new ArrayList<>();
        combined.addAll(scheduleBased);
        combined.addAll(preferenceBased);
        combined.addAll(historyBased);

        // Loại bỏ trùng lặp và sắp xếp theo điểm số
        List<Document> finalRecommendations = combined.stream()
                .distinct()
                .sorted((d1, d2) -> {
                    int score1 = calculateOverallScore(d1, userId);
                    int score2 = calculateOverallScore(d2, userId);
                    return Integer.compare(score2, score1);
                })
                .limit(limit)
                .collect(Collectors.toList());

        result.put("recommendations", finalRecommendations);
        result.put("scheduleBasedCount", scheduleBased.size());
        result.put("preferenceBasedCount", preferenceBased.size());
        result.put("historyBasedCount", historyBased.size());
        result.put("userSubjects", personalScheduleService.getUserSubjectIds(userId));

        return result;
    }

    // Đề xuất theo thời gian trong ngày
    public List<Document> getTimeBasedRecommendations(Integer userId) {
        LocalTime now = LocalTime.now();
        String timeCategory = getTimeCategory(now);

        Optional<StudyPreference> preference = studyPreferenceService.getPreferenceByUserId(userId);
        List<Document> recommendations = new ArrayList<>();

        // Thêm đề xuất dựa trên thời gian
        if (preference.isPresent() && timeCategory.equals(preference.get().getPreferredStudyTime())) {
            recommendations.addAll(getPreferenceBasedRecommendations(userId, 5));
        }

        // Thêm đề xuất từ lịch học
        recommendations.addAll(getScheduleBasedRecommendations(userId, 5));

        return recommendations.stream()
                .distinct()
                .limit(8)
                .collect(Collectors.toList());
    }

    // Helper methods
    private boolean matchesPreference(Document doc, StudyPreference preference) {
        // Logic kiểm tra tài liệu có phù hợp với sở thích không
        String title = doc.getTitle().toLowerCase();
        String description = doc.getDescription() != null ? doc.getDescription().toLowerCase() : "";

        if (preference.isPreferTextbooks() && (title.contains("textbook") || title.contains("sách giáo khoa"))) {
            return true;
        }
        if (preference.isPreferLectureNotes() && (title.contains("lecture") || title.contains("bài giảng"))) {
            return true;
        }
        if (preference.isPreferExercises() && (title.contains("exercise") || title.contains("bài tập"))) {
            return true;
        }
        if (preference.isPreferExamPapers() && (title.contains("exam") || title.contains("đề thi"))) {
            return true;
        }

        return false;
    }

    private int calculatePreferenceScore(Document doc, StudyPreference preference) {
        int score = 0;
        String title = doc.getTitle().toLowerCase();

        if (preference.isPreferTextbooks() && title.contains("textbook")) score += 3;
        if (preference.isPreferLectureNotes() && title.contains("lecture")) score += 3;
        if (preference.isPreferExercises() && title.contains("exercise")) score += 3;
        if (preference.isPreferExamPapers() && title.contains("exam")) score += 3;
        if (preference.isPreferResearchPapers() && title.contains("research")) score += 3;

        return score;
    }

    private int calculateOverallScore(Document doc, Integer userId) {
        int score = 0;

        // Điểm cho môn học trong lịch
        if (personalScheduleService.hasSubjectInSchedule(userId, doc.getSubject().getId())) {
            score += 10;
        }

        // Điểm cho sở thích
        Optional<StudyPreference> preference = studyPreferenceService.getPreferenceByUserId(userId);
        if (preference.isPresent()) {
            score += calculatePreferenceScore(doc, preference.get());
        }

        // Điểm cho độ mới
        if (doc.getUploadDate().isAfter(LocalDateTime.now().minusDays(30))) {
            score += 5;
        }

        // Điểm cho lượt tải
        score += Math.min(doc.getDownloadCount() / 10, 5);

        return score;
    }

    private String getTimeCategory(LocalTime time) {
        if (time.isBefore(LocalTime.NOON)) {
            return "MORNING";
        } else if (time.isBefore(LocalTime.of(17, 0))) {
            return "AFTERNOON";
        } else if (time.isBefore(LocalTime.of(22, 0))) {
            return "EVENING";
        } else {
            return "NIGHT";
        }
    }
}