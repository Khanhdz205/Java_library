package com.library.khanhnqph57627.controller;

import com.library.khanhnqph57627.entity.StudyPreference;
import com.library.khanhnqph57627.service.StudyPreferenceService;
import com.library.khanhnqph57627.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/preferences")
public class StudyPreferenceController {

    @Autowired
    private StudyPreferenceService studyPreferenceService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getPreference(Model model) {
        Integer currentUserId = 1; // Tạm thời hardcode

        Optional<StudyPreference> preference = studyPreferenceService.getPreferenceByUserId(currentUserId);
        if (preference.isPresent()) {
            model.addAttribute("preference", preference.get());
        } else {
            model.addAttribute("preference", new StudyPreference());
        }

        model.addAttribute("users", userService.getAllUsers());
        return "preferences/edit";
    }

    @PostMapping("/save")
    public String savePreference(@ModelAttribute StudyPreference preference,
                                 @RequestParam Integer userId,
                                 RedirectAttributes redirectAttributes) {
        try {
            studyPreferenceService.createOrUpdatePreference(userId, preference);
            redirectAttributes.addFlashAttribute("success", "Lưu sở thích học tập thành công!");
            return "redirect:/recommendations";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu sở thích: " + e.getMessage());
            return "redirect:/preferences";
        }
    }
}