package com.library.khanhnqph57627.repository;

import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    // Các phương thức hiện có
    List<Document> findByAuthorContainingIgnoreCase(String author);
    List<Document> findByTitleContainingIgnoreCase(String title);
    List<Document> findBySubjectId(Integer subjectId);
    List<Document> findByCategoriesId(Integer categoryId);
    List<Document> findByUploadedById(Integer userId);

    // PHƯƠNG THỨC MỚI CHO TÍNH NĂNG GỢI Ý

    // Tìm tài liệu cùng môn học (trừ tài liệu hiện tại)
    List<Document> findBySubjectIdAndIdNot(Integer subjectId, Integer documentId);

    // Tìm tài liệu cùng danh mục (trừ tài liệu hiện tại)
    @Query("SELECT DISTINCT d FROM Document d JOIN d.categories c WHERE c IN :categories AND d.id != :documentId")
    List<Document> findByCategoriesInAndIdNot(@Param("categories") List<Category> categories, @Param("documentId") Integer documentId);

    // Lấy tài liệu mới nhất (trừ tài liệu hiện tại)
    @Query(value = "SELECT * FROM documents WHERE id != :excludeId ORDER BY upload_date DESC LIMIT :limit", nativeQuery = true)
    List<Document> findTopByOrderByUploadDateDesc(@Param("limit") int limit, @Param("excludeId") Integer excludeId);

    // Lấy tài liệu mới nhất (không loại trừ)
    @Query(value = "SELECT * FROM documents ORDER BY upload_date DESC LIMIT :limit", nativeQuery = true)
    List<Document> findTopByOrderByUploadDateDesc(@Param("limit") int limit);

    // Tìm kiếm nâng cao
    List<Document> findByTitleContainingIgnoreCaseAndSubjectId(String title, Integer subjectId);

    @Query("SELECT d FROM Document d JOIN d.categories c WHERE d.title LIKE %:title% AND c.id = :categoryId")
    List<Document> findByTitleContainingIgnoreCaseAndCategoriesId(@Param("title") String title, @Param("categoryId") Integer categoryId);

    @Query("SELECT d FROM Document d JOIN d.categories c WHERE d.title LIKE %:title% AND d.subject.id = :subjectId AND c.id = :categoryId")
    List<Document> findByTitleContainingIgnoreCaseAndSubjectIdAndCategoriesId(@Param("title") String title, @Param("subjectId") Integer subjectId, @Param("categoryId") Integer categoryId);

    // Tìm kiếm theo tiêu đề hoặc tác giả
    @Query("SELECT d FROM Document d WHERE LOWER(d.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(d.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Document> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(@Param("keyword") String keyword);

    // Thống kê
    Long countBySubjectId(Integer subjectId);
    Long countByUploadedById(Integer userId);

    // Tìm tài liệu phổ biến (có nhiều comment nhất)
    @Query(value = "SELECT d.* FROM documents d LEFT JOIN comments c ON d.id = c.document_id WHERE d.id != :excludeId GROUP BY d.id ORDER BY COUNT(c.id) DESC LIMIT :limit", nativeQuery = true)
    List<Document> findPopularDocuments(@Param("limit") int limit, @Param("excludeId") Integer excludeId);

    // Tìm tài liệu theo subject và category
    @Query("SELECT d FROM Document d JOIN d.categories c WHERE d.subject.id = :subjectId AND c.id = :categoryId")
    List<Document> findBySubjectIdAndCategoriesId(@Param("subjectId") Integer subjectId, @Param("categoryId") Integer categoryId);
}