package com.library.khanhnqph57627.repository;

import com.library.khanhnqph57627.entity.DocumentViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentViewHistoryRepository extends JpaRepository<DocumentViewHistory, Integer> {

    List<DocumentViewHistory> findByUserIdOrderByViewedAtDesc(Integer userId);

    List<DocumentViewHistory> findByUserIdAndViewedAtAfter(Integer userId, LocalDateTime date);

    @Query("SELECT dvh.document.id FROM DocumentViewHistory dvh WHERE dvh.user.id = :userId GROUP BY dvh.document.id ORDER BY COUNT(dvh.id) DESC")
    List<Integer> findFrequentlyViewedDocumentIds(@Param("userId") Integer userId, org.springframework.data.domain.Pageable pageable);

    @Query("SELECT dvh.document.subject.id FROM DocumentViewHistory dvh WHERE dvh.user.id = :userId GROUP BY dvh.document.subject.id ORDER BY COUNT(dvh.id) DESC")
    List<Integer> findFrequentlyViewedSubjectIds(@Param("userId") Integer userId);

    boolean existsByUserIdAndDocumentId(Integer userId, Integer documentId);

    @Query("SELECT COUNT(dvh) FROM DocumentViewHistory dvh WHERE dvh.user.id = :userId AND dvh.downloaded = true")
    Long countDownloadsByUserId(@Param("userId") Integer userId);
}