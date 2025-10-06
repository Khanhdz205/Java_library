package com.library.khanhnqph57627.controller;

import com.library.khanhnqph57627.entity.PersonalSchedule;
import com.library.khanhnqph57627.service.PersonalScheduleService;
import com.library.khanhnqph57627.service.UserService;
import com.library.khanhnqph57627.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/schedule")
public class PersonalScheduleController {

    @Autowired
    private PersonalScheduleService personalScheduleService;

    @Autowired
    private UserService userService;

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public String listSchedules(Model model) {
        // Trong thực tế, lấy userId từ session/authentication
        Integer currentUserId = 1; // Tạm thời hardcode
        model.addAttribute("schedules", personalScheduleService.getSchedulesByUser(currentUserId));
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "schedule/list";
    }

    @GetMapping("/create")
    public String createScheduleForm(Model model) {
        model.addAttribute("schedule", new PersonalSchedule());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("subjects", subjectService.getAllSubjects());
        return "schedule/create";
    }

    @PostMapping("/create")
    public String createSchedule(@ModelAttribute PersonalSchedule schedule,
                                 @RequestParam Integer userId,
                                 @RequestParam Integer subjectId,
                                 RedirectAttributes redirectAttributes) {
        try {
            personalScheduleService.createSchedule(userId, subjectId, schedule);
            redirectAttributes.addFlashAttribute("success", "Thêm lịch học thành công!");
            return "redirect:/schedule";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm lịch học: " + e.getMessage());
            return "redirect:/schedule/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editScheduleForm(@PathVariable Integer id, Model model) {
        Optional<PersonalSchedule> schedule = personalScheduleService.getScheduleById(id);
        if (schedule.isPresent()) {
            model.addAttribute("schedule", schedule.get());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            return "schedule/edit";
        } else {
            return "redirect:/schedule";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateSchedule(@PathVariable Integer id,
                                 @ModelAttribute PersonalSchedule schedule,
                                 RedirectAttributes redirectAttributes) {
        try {
            schedule.setId(id);
            personalScheduleService.saveSchedule(schedule);
            redirectAttributes.addFlashAttribute("success", "Cập nhật lịch học thành công!");
            return "redirect:/schedule";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật lịch học: " + e.getMessage());
            return "redirect:/schedule/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSchedule(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            personalScheduleService.deleteSchedule(id);
            redirectAttributes.addFlashAttribute("success", "Xóa lịch học thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa lịch học: " + e.getMessage());
        }
        return "redirect:/schedule";
    }

    @GetMapping("/toggle/{id}")
    public String toggleScheduleStatus(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            personalScheduleService.toggleScheduleStatus(id);
            redirectAttributes.addFlashAttribute("success", "Thay đổi trạng thái thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thay đổi trạng thái!");
        }
        return "redirect:/schedule";
    }
}
