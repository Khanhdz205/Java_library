package com.library.khanhnqph57627.controller;

import com.library.khanhnqph57627.entity.Comment;
import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.entity.Subject;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.entity.Category;
import com.library.khanhnqph57627.service.DocumentService;
import com.library.khanhnqph57627.service.SubjectService;
import com.library.khanhnqph57627.service.UserService;
import com.library.khanhnqph57627.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.library.khanhnqph57627.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CommentService commentService;

    @GetMapping
    public String listDocuments(Model model) {
        try {
            model.addAttribute("documents", documentService.getAllDocuments());
            model.addAttribute("totalDocuments", documentService.getTotalDocuments());
            return "documents/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải danh sách tài liệu: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/create")
    public String createDocumentForm(Model model) {
        try {
            model.addAttribute("document", new Document());
            model.addAttribute("subjects", subjectService.getAllSubjects());
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "documents/create";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải form tạo tài liệu: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/create")
    public String createDocument(@ModelAttribute Document document,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            documentService.saveDocument(document, file);
            redirectAttributes.addFlashAttribute("success", "Tạo tài liệu thành công!");
            return "redirect:/documents";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo tài liệu: " + e.getMessage());
            return "redirect:/documents/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editDocumentForm(@PathVariable Integer id, Model model) {
        try {
            Optional<Document> document = documentService.getDocumentById(id);
            if (document.isPresent()) {
                model.addAttribute("document", document.get());
                model.addAttribute("subjects", subjectService.getAllSubjects());
                model.addAttribute("users", userService.getAllUsers());
                model.addAttribute("categories", categoryService.getAllCategories());
                return "documents/edit";
            } else {
                model.addAttribute("error", "Không tìm thấy tài liệu với ID: " + id);
                return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải form sửa tài liệu: " + e.getMessage());
            return "error";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateDocument(@PathVariable Integer id,
                                 @RequestParam String title,
                                 @RequestParam String author,
                                 @RequestParam(required = false) String description,
                                 @RequestParam Integer subject,
                                 @RequestParam(required = false) Integer uploadedBy,
                                 @RequestParam(value = "file", required = false) MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            Optional<Document> existingDoc = documentService.getDocumentById(id);
            if (existingDoc.isPresent()) {
                Document document = existingDoc.get();
                document.setTitle(title);
                document.setAuthor(author);
                document.setDescription(description);

                // Set subject
                Optional<Subject> subjectObj = subjectService.getSubjectById(subject);
                subjectObj.ifPresent(document::setSubject);

                // Set uploadedBy if provided
                if (uploadedBy != null) {
                    Optional<User> user = userService.getUserById(uploadedBy);
                    user.ifPresent(document::setUploadedBy);
                }

                documentService.saveDocument(document, file);
                redirectAttributes.addFlashAttribute("success", "Cập nhật tài liệu thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Không tìm thấy tài liệu!");
            }
            return "redirect:/documents";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi cập nhật tài liệu: " + e.getMessage());
            return "redirect:/documents/edit/" + id;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteDocument(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            documentService.deleteDocument(id);
            redirectAttributes.addFlashAttribute("success", "Xóa tài liệu thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi xóa tài liệu: " + e.getMessage());
        }
        return "redirect:/documents";
    }

    @GetMapping("/{id}")
    public String viewDocument(@PathVariable Integer id, Model model, HttpServletRequest request) {
        try {
            Optional<Document> document = documentService.getDocumentById(id);
            if (document.isPresent()) {
                model.addAttribute("document", document.get());

                // Add average rating
                Double averageRating = commentService.getAverageRatingByDocument(id);
                model.addAttribute("averageRating", averageRating != null ? averageRating : 0.0);

                // Add new comment form
                model.addAttribute("newComment", new Comment());

                // Add users for dropdown
                model.addAttribute("users", userService.getAllUsers());

                // TÍNH NĂNG MỚI: Thêm tài liệu liên quan
                List<Document> relatedDocuments = documentService.getRelatedDocuments(id, 4);
                model.addAttribute("relatedDocuments", relatedDocuments);

                // Kiểm tra flash attributes
                java.util.Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
                if (flashMap != null) {
                    model.addAttribute("success", flashMap.get("success"));
                    model.addAttribute("error", flashMap.get("error"));
                }

                return "documents/detail";
            } else {
                return "redirect:/documents";
            }
        } catch (Exception e) {
            return "redirect:/documents";
        }
    }

    @GetMapping("/search")
    public String searchDocuments(@RequestParam String author, Model model) {
        try {
            List<Document> documents = documentService.searchByAuthor(author);
            model.addAttribute("documents", documents);
            model.addAttribute("searchAuthor", author);
            model.addAttribute("totalDocuments", documents.size());
            return "documents/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tìm kiếm: " + e.getMessage());
            return "documents/list";
        }
    }

    @GetMapping("/subject/{subjectId}")
    public String getDocumentsBySubject(@PathVariable Integer subjectId, Model model) {
        try {
            List<Document> documents = documentService.getDocumentsBySubject(subjectId);
            model.addAttribute("documents", documents);
            Optional<Subject> subject = subjectService.getSubjectById(subjectId);
            subject.ifPresent(s -> model.addAttribute("subjectName", s.getName()));
            model.addAttribute("totalDocuments", documents.size());
            return "documents/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi lọc theo môn học: " + e.getMessage());
            return "documents/list";
        }
    }

    // TÌM KIẾM NÂNG CAO
    @GetMapping("/search/advanced")
    public String advancedSearch(@RequestParam(required = false) String keyword,
                                 @RequestParam(required = false) Integer subjectId,
                                 @RequestParam(required = false) Integer categoryId,
                                 Model model) {
        try {
            List<Document> documents = documentService.advancedSearch(keyword, subjectId, categoryId);
            model.addAttribute("documents", documents);
            model.addAttribute("searchKeyword", keyword);
            model.addAttribute("subjects", subjectService.getAllSubjects());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("selectedSubjectId", subjectId);
            model.addAttribute("selectedCategoryId", categoryId);
            model.addAttribute("totalDocuments", documents.size());
            return "documents/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tìm kiếm: " + e.getMessage());
            return "documents/list";
        }
    }

    // TÌM KIẾM THEO TỪ KHÓA
    @GetMapping("/search/keyword")
    public String searchByKeyword(@RequestParam String keyword, Model model) {
        try {
            List<Document> documents = documentService.searchByKeyword(keyword);
            model.addAttribute("documents", documents);
            model.addAttribute("searchKeyword", keyword);
            model.addAttribute("totalDocuments", documents.size());
            return "documents/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tìm kiếm: " + e.getMessage());
            return "documents/list";
        }
    }

    // TÀI LIỆU MỚI NHẤT
    @GetMapping("/recent")
    public String getRecentDocuments(Model model) {
        try {
            List<Document> recentDocuments = documentService.getRecentDocuments(10);
            model.addAttribute("documents", recentDocuments);
            model.addAttribute("totalDocuments", recentDocuments.size());
            model.addAttribute("isRecent", true);
            return "documents/list";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi tải tài liệu mới nhất: " + e.getMessage());
            return "documents/list";
        }
    }
}