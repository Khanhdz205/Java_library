package com.library.khanhnqph57627.controller;

import com.library.khanhnqph57627.entity.Comment;
import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.service.CommentService;
import com.library.khanhnqph57627.service.DocumentService;
import com.library.khanhnqph57627.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    // Hiển thị danh sách bình luận
    @GetMapping
    public String listComments(Model model) {
        try {
            model.addAttribute("comments", commentService.getAllComments());
            return "comments/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách bình luận: " + e.getMessage());
            return "error";
        }
    }

    // Xử lý tạo bình luận mới
    @PostMapping("/create")
    public String createComment(@RequestParam("documentId") Integer documentId,
                                @RequestParam("userId") Integer userId,
                                @RequestParam("content") String content,
                                @RequestParam(value = "rating", required = false) Integer rating,
                                RedirectAttributes redirectAttributes) {
        try {
            // Tìm document
            Optional<Document> document = documentService.getDocumentById(documentId);
            if (!document.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Tài liệu không tồn tại!");
                return "redirect:/documents/" + documentId;
            }

            // Tìm user
            Optional<User> user = userService.getUserById(userId);
            if (!user.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại!");
                return "redirect:/documents/" + documentId;
            }

            // Tạo comment mới
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setRating(rating);
            comment.setDocument(document.get());
            comment.setUser(user.get());
            comment.setCreatedDate(LocalDateTime.now());

            commentService.saveComment(comment);
            redirectAttributes.addFlashAttribute("success", "Bình luận đã được thêm thành công!");
            return "redirect:/documents/" + documentId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm bình luận: " + e.getMessage());
            return "redirect:/documents/" + documentId;
        }
    }

    // Xóa bình luận
    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Comment> comment = commentService.getCommentById(id);
            if (comment.isPresent()) {
                Integer documentId = comment.get().getDocument().getId();
                commentService.deleteComment(id);
                redirectAttributes.addFlashAttribute("success", "Xóa bình luận thành công!");
                return "redirect:/documents/" + documentId;
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy bình luận!");
                return "redirect:/documents";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa bình luận: " + e.getMessage());
            return "redirect:/documents";
        }
    }

    // Form tạo bình luận (nếu cần)
    @GetMapping("/create")
    public String createCommentForm(@RequestParam("documentId") Integer documentId, Model model) {
        try {
            Optional<Document> document = documentService.getDocumentById(documentId);
            if (document.isPresent()) {
                model.addAttribute("document", document.get());
                model.addAttribute("users", userService.getAllUsers());
                model.addAttribute("comment", new Comment());
                return "comments/create";
            } else {
                return "redirect:/documents";
            }
        } catch (Exception e) {
            return "redirect:/documents";
        }
    }
}