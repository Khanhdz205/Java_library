package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.Comment;
import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void initSampleData() {
        if (commentRepository.count() == 0) {
            // Lấy dữ liệu mẫu
            List<Document> documents = documentService.getAllDocuments();
            List<User> users = userService.getAllUsers();

            if (!documents.isEmpty() && !users.isEmpty()) {
                Comment comment1 = new Comment();
                comment1.setContent("Tài liệu rất hữu ích, giải thích chi tiết và dễ hiểu. Cảm ơn thầy!");
                comment1.setRating(5);
                comment1.setCreatedDate(LocalDateTime.now().minusDays(2));
                comment1.setUser(users.get(0)); // Sinh viên
                comment1.setDocument(documents.get(0));

                Comment comment2 = new Comment();
                comment2.setContent("Bài tập khá khó nhưng giúp nâng cao kỹ năng lập trình đáng kể");
                comment2.setRating(4);
                comment2.setCreatedDate(LocalDateTime.now().minusDays(1));
                comment2.setUser(users.get(0)); // Sinh viên
                comment2.setDocument(documents.get(1));

                Comment comment3 = new Comment();
                comment3.setContent("Đề thi sát với chương trình học, format rõ ràng");
                comment3.setRating(5);
                comment3.setCreatedDate(LocalDateTime.now());
                comment3.setUser(users.get(3)); // Sinh viên khác
                comment3.setDocument(documents.get(2));

                commentRepository.saveAll(Arrays.asList(comment1, comment2, comment3));
            }
        }
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Integer id) {
        return commentRepository.findById(id);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByDocument(Integer documentId) {
        return commentRepository.findByDocumentId(documentId);
    }

    public List<Comment> getCommentsByUser(Integer userId) {
        return commentRepository.findByUserId(userId);
    }

    public Double getAverageRatingByDocument(Integer documentId) {
        List<Comment> comments = commentRepository.findByDocumentId(documentId);
        if (comments.isEmpty()) return 0.0;

        return comments.stream()
                .mapToInt(Comment::getRating)
                .average()
                .orElse(0.0);
    }
}