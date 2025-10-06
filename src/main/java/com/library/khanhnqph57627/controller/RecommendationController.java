package com.library.khanhnqph57627.controller;

import com.library.khanhnqph57627.service.RecommendationService;
import com.library.khanhnqph57627.service.StudyPreferenceService;
import com.library.khanhnqph57627.service.PersonalScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/recommendations")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private StudyPreferenceService studyPreferenceService;

    @Autowired
    private PersonalScheduleService personalScheduleService;

    @GetMapping
    public String getRecommendations(Model model) {
        // Trong thực tế, lấy userId từ session/authentication
        Integer currentUserId = 1; // Tạm thời hardcode

        Map<String, Object> recommendations = recommendationService.getPersonalizedRecommendations(currentUserId, 12);
        boolean hasPreference = studyPreferenceService.hasPreference(currentUserId);
        boolean hasSchedule = !personalScheduleService.getSchedulesByUser(currentUserId).isEmpty();

        model.addAttribute("recommendations", recommendations.get("recommendations"));
        model.addAttribute("scheduleBasedCount", recommendations.get("scheduleBasedCount"));
        model.addAttribute("preferenceBasedCount", recommendations.get("preferenceBasedCount"));
        model.addAttribute("historyBasedCount", recommendations.get("historyBasedCount"));
        model.addAttribute("hasPreference", hasPreference);
        model.addAttribute("hasSchedule", hasSchedule);
        model.addAttribute("userSubjects", recommendations.get("userSubjects"));

        return "recommendations/list";
    }

    @GetMapping("/time-based")
    public String getTimeBasedRecommendations(Model model) {
        Integer currentUserId = 1; // Tạm thời hardcode

        model.addAttribute("recommendations", recommendationService.getTimeBasedRecommendations(currentUserId));
        return "recommendations/time-based";
    }

    @GetMapping("/schedule-based")
    public String getScheduleBasedRecommendations(Model model) {
        Integer currentUserId = 1; // Tạm thời hardcode

        model.addAttribute("recommendations", recommendationService.getScheduleBasedRecommendations(currentUserId, 10));
        return "recommendations/schedule-based";
    }

    @GetMapping("/preference-based")
    public String getPreferenceBasedRecommendations(Model model) {
        Integer currentUserId = 1; // Tạm thời hardcode

        model.addAttribute("recommendations", recommendationService.getPreferenceBasedRecommendations(currentUserId, 10));
        return "recommendations/preference-based";
    }
}