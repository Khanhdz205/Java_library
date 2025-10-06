package com.library.khanhnqph57627.controller;

import com.library.khanhnqph57627.service.DocumentService;
import com.library.khanhnqph57627.service.SubjectService;
import com.library.khanhnqph57627.service.UserService;
import com.library.khanhnqph57627.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "Trang chủ Thư viện");

        // Thêm thống kê vào model
        model.addAttribute("totalDocuments", documentService.getAllDocuments().size());
        model.addAttribute("totalSubjects", subjectService.getAllSubjects().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalComments", commentService.getAllComments().size());

        // Thêm danh sách tài liệu mới nhất
        List<?> documents = documentService.getAllDocuments();
        if (documents.size() > 4) {
            documents = documents.subList(0, 4);
        }
        model.addAttribute("recentDocuments", documents);

        return "home";
    }
}