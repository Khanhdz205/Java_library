package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.DocumentViewHistory;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.repository.DocumentViewHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentViewHistoryService {

    @Autowired
    private DocumentViewHistoryRepository documentViewHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DocumentService documentService;

    public DocumentViewHistory recordView(Integer userId, Integer documentId, Integer duration, boolean downloaded) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Document> document = documentService.getDocumentById(documentId);

        if (user.isPresent() && document.isPresent()) {
            DocumentViewHistory history = new DocumentViewHistory();
            history.setUser(user.get());
            history.setDocument(document.get());
            history.setViewDuration(duration);
            history.setDownloaded(downloaded);
            return documentViewHistoryRepository.save(history);
        }
        throw new RuntimeException("User hoặc Document không tồn tại");
    }

    public List<DocumentViewHistory> getUserViewHistory(Integer userId) {
        return documentViewHistoryRepository.findByUserIdOrderByViewedAtDesc(userId);
    }

    public List<DocumentViewHistory> getRecentUserViews(Integer userId, int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return documentViewHistoryRepository.findByUserIdAndViewedAtAfter(userId, since);
    }

    public List<Integer> getFrequentlyViewedDocumentIds(Integer userId, int limit) {
        return documentViewHistoryRepository.findFrequentlyViewedDocumentIds(userId, PageRequest.of(0, limit));
    }

    public List<Integer> getFrequentlyViewedSubjectIds(Integer userId) {
        return documentViewHistoryRepository.findFrequentlyViewedSubjectIds(userId);
    }

    public boolean hasUserViewedDocument(Integer userId, Integer documentId) {
        return documentViewHistoryRepository.existsByUserIdAndDocumentId(userId, documentId);
    }

    public Long getUserDownloadCount(Integer userId) {
        return documentViewHistoryRepository.countDownloadsByUserId(userId);
    }
}