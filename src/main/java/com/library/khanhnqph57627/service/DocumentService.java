package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.Document;
import com.library.khanhnqph57627.entity.Subject;
import com.library.khanhnqph57627.entity.User;
import com.library.khanhnqph57627.entity.Category;
import com.library.khanhnqph57627.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    private final String UPLOAD_DIR = "./uploads/";

    @PostConstruct
    public void initSampleData() {
        if (documentRepository.count() == 0) {
            // Lấy dữ liệu mẫu từ các service khác
            List<Subject> subjects = subjectService.getAllSubjects();
            List<User> users = userService.getAllUsers();
            List<Category> categories = categoryService.getAllCategories();

            if (!subjects.isEmpty() && !users.isEmpty() && !categories.isEmpty()) {
                List<Document> sampleDocuments = new ArrayList<>();

                // === TOÁN HỌC - 4 tài liệu ===
                Document doc1 = createDocument(
                        "Bài giảng Toán Cao Cấp chương 1 - Giới hạn và Liên tục",
                        "PGS.TS. Nguyễn Văn Toán",
                        "Bài giảng chi tiết về giới hạn, liên tục và ứng dụng trong toán cao cấp",
                        "toan_cao_cap_ch1.pdf",
                        "pdf",
                        2048576L,
                        LocalDateTime.now().minusDays(5),
                        getSafeElement(subjects, 0),
                        getSafeElement(users, 1),
                        Arrays.asList(getSafeElement(categories, 0), getSafeElement(categories, 3))
                );

                Document doc2 = createDocument(
                        "Toán Giải Tích - Tích phân và Ứng dụng",
                        "TS. Trần Minh Giải",
                        "Chuyên sâu về tích phân, vi phân và ứng dụng trong thực tế",
                        "toan_giai_tich_tich_phan.pdf",
                        "pdf",
                        1896543L,
                        LocalDateTime.now().minusDays(7),
                        getSafeElement(subjects, 0),
                        getSafeElement(users, 1),
                        Arrays.asList(getSafeElement(categories, 0), getSafeElement(categories, 1))
                );

                Document doc3 = createDocument(
                        "Đại số Tuyến tính - Ma trận và Định thức",
                        "PGS.TS. Lê Văn Đại",
                        "Lý thuyết và bài tập về ma trận, định thức, không gian vector",
                        "dai_so_tuyen_tinh.pdf",
                        "pdf",
                        2256891L,
                        LocalDateTime.now().minusDays(3),
                        getSafeElement(subjects, 0),
                        getSafeElement(users, 2),
                        Arrays.asList(getSafeElement(categories, 0))
                );

                Document doc4 = createDocument(
                        "Xác suất Thống kê - Lý thuyết và Bài tập",
                        "TS. Nguyễn Thị Xác Suất",
                        "Toàn bộ kiến thức xác suất thống kê từ cơ bản đến nâng cao",
                        "xa_suat_thong_ke.pdf",
                        "pdf",
                        1987654L,
                        LocalDateTime.now().minusDays(4),
                        getSafeElement(subjects, 0),
                        getSafeElement(users, 1),
                        Arrays.asList(getSafeElement(categories, 0), getSafeElement(categories, Math.min(3, categories.size()-1)))
                );

                // === LẬP TRÌNH - 6 tài liệu ===
                Document doc5 = createDocument(
                        "Bài tập Lập trình Java - OOP cơ bản",
                        "ThS. Lê Thị Lập trình",
                        "Tuyển tập bài tập lập trình hướng đối tượng với Java, có lời giải chi tiết",
                        "bai_tap_java_oop.pdf",
                        "pdf",
                        1572864L,
                        LocalDateTime.now().minusDays(3),
                        getSafeElement(subjects, Math.min(1, subjects.size()-1)),
                        getSafeElement(users, 1),
                        Arrays.asList(getSafeElement(categories, 1), getSafeElement(categories, Math.min(3, categories.size()-1)))
                );

                Document doc6 = createDocument(
                        "Spring Boot Framework - Từ cơ bản đến nâng cao",
                        "ThS. Phạm Văn Spring",
                        "Hướng dẫn xây dựng ứng dụng web với Spring Boot, REST API, Security",
                        "spring_boot_complete.pdf",
                        "pdf",
                        3256891L,
                        LocalDateTime.now().minusDays(2),
                        getSafeElement(subjects, Math.min(1, subjects.size()-1)),
                        getSafeElement(users, 2),
                        Arrays.asList(getSafeElement(categories, 1), getSafeElement(categories, 3))
                );

                Document doc7 = createDocument(
                        "JavaScript ES6+ - Modern JavaScript Development",
                        "TS. Nguyễn Hiện Đại",
                        "Các tính năng mới trong ES6+, async/await, modules, và best practices",
                        "javascript_es6_advanced.pdf",
                        "pdf",
                        2896543L,
                        LocalDateTime.now().minusDays(4),
                        getSafeElement(subjects, Math.min(1, subjects.size()-1)),
                        getSafeElement(users, 1),
                        Arrays.asList(getSafeElement(categories, 1))
                );

                Document doc8 = createDocument(
                        "Python cho Khoa học Dữ liệu",
                        "TS. Trần Data Science",
                        "Pandas, NumPy, Matplotlib và ứng dụng trong phân tích dữ liệu",
                        "python_data_science.pdf",
                        "pdf",
                        2987654L,
                        LocalDateTime.now().minusDays(6),
                        getSafeElement(subjects, Math.min(1, subjects.size()-1)),
                        getSafeElement(users, 0),
                        Arrays.asList(getSafeElement(categories, 1), getSafeElement(categories, Math.min(3, categories.size()-1)))
                );

                Document doc9 = createDocument(
                        "React.js - Xây dựng Ứng dụng Web Hiện đại",
                        "ThS. Trần React Developer",
                        "Hooks, Context API, Redux và các kỹ thuật nâng cao trong React",
                        "reactjs_modern_web.pdf",
                        "pdf",
                        2756891L,
                        LocalDateTime.now().minusDays(8),
                        getSafeElement(subjects, Math.min(1, subjects.size()-1)),
                        getSafeElement(users, 2),
                        Arrays.asList(getSafeElement(categories, 1), getSafeElement(categories, 3))
                );

                // === CƠ SỞ DỮ LIỆU - 3 tài liệu ===
                Document doc10 = createDocument(
                        "Đề thi Cơ sở dữ liệu - Học kỳ 1 2023",
                        "Khoa Công nghệ Thông tin",
                        "Đề thi cuối kỳ môn Cơ sở dữ liệu kèm đáp án tham khảo",
                        "de_thi_csdl_hk1_2023.pdf",
                        "pdf",
                        1048576L,
                        LocalDateTime.now().minusDays(1),
                        getSafeElement(subjects, Math.min(2, subjects.size()-1)),
                        getSafeElement(users, 0),
                        Arrays.asList(getSafeElement(categories, 2))
                );

                Document doc11 = createDocument(
                        "SQL Mastery - Truy vấn nâng cao và Optimization",
                        "ThS. Hoàng SQL Expert",
                        "Kỹ thuật viết truy vấn SQL phức tạp, indexing và performance tuning",
                        "sql_mastery_advanced.pdf",
                        "pdf",
                        2156891L,
                        LocalDateTime.now().minusDays(8),
                        getSafeElement(subjects, Math.min(2, subjects.size()-1)),
                        getSafeElement(users, 2),
                        Arrays.asList(getSafeElement(categories, 2), getSafeElement(categories, Math.min(3, categories.size()-1)))
                );

                Document doc12 = createDocument(
                        "MySQL vs PostgreSQL - So sánh và Lựa chọn",
                        "TS. Lý Cơ Sở",
                        "Phân tích điểm mạnh/yếu giữa MySQL và PostgreSQL cho dự án thực tế",
                        "mysql_vs_postgresql.pdf",
                        "pdf",
                        1896543L,
                        LocalDateTime.now().minusDays(10),
                        getSafeElement(subjects, Math.min(2, subjects.size()-1)),
                        getSafeElement(users, 1),
                        Arrays.asList(getSafeElement(categories, 2))
                );

                // === MẠNG MÁY TÍNH - 2 tài liệu ===
                if (subjects.size() > 3) {
                    Document doc13 = createDocument(
                            "Mạng máy tính căn bản - TCP/IP và OSI Model",
                            "PGS.TS. Võ Mạng Cơ Bản",
                            "Tổng quan về mô hình TCP/IP, OSI và các giao thức mạng",
                            "mang_may_tinh_co_ban.pdf",
                            "pdf",
                            2456891L,
                            LocalDateTime.now().minusDays(9),
                            getSafeElement(subjects, 3),
                            getSafeElement(users, 2),
                            Arrays.asList(getSafeElement(categories, 3))
                    );
                    sampleDocuments.add(doc13);
                }

                if (subjects.size() > 3) {
                    Document doc14 = createDocument(
                            "Bảo mật mạng - Firewall và VPN",
                            "ThS. Nguyễn Bảo Mật",
                            "Cấu hình firewall, VPN và các kỹ thuật bảo mật mạng cơ bản",
                            "bao_mat_mang_firewall.pdf",
                            "pdf",
                            1987654L,
                            LocalDateTime.now().minusDays(12),
                            getSafeElement(subjects, 3),
                            getSafeElement(users, 1),
                            Arrays.asList(getSafeElement(categories, 3), getSafeElement(categories, Math.min(3, categories.size()-1)))
                    );
                    sampleDocuments.add(doc14);
                }

                // === TRÍ TUỆ NHÂN TẠO - 3 tài liệu ===
                if (subjects.size() > 4) {
                    Document doc15 = createDocument(
                            "Machine Learning cơ bản với Python",
                            "TS. AI Researcher",
                            "Giới thiệu các thuật toán ML cơ bản: regression, classification, clustering",
                            "machine_learning_basic.pdf",
                            "pdf",
                            3256891L,
                            LocalDateTime.now().minusDays(5),
                            getSafeElement(subjects, 4),
                            getSafeElement(users, 2),
                            Arrays.asList(getSafeElement(categories, Math.min(3, categories.size()-1)))
                    );
                    sampleDocuments.add(doc15);
                }

                if (subjects.size() > 4) {
                    Document doc16 = createDocument(
                            "Deep Learning - Neural Networks và CNN",
                            "GS.TS. Nguyễn Deep Learning",
                            "Mạng neural, convolutional neural networks và ứng dụng trong computer vision",
                            "deep_learning_neural_networks.pdf",
                            "pdf",
                            4256891L,
                            LocalDateTime.now().minusDays(7),
                            getSafeElement(subjects, 4),
                            getSafeElement(users, 1),
                            Arrays.asList(getSafeElement(categories, Math.min(3, categories.size()-1)), getSafeElement(categories, 1))
                    );
                    sampleDocuments.add(doc16);
                }

                if (subjects.size() > 4) {
                    Document doc17 = createDocument(
                            "Xử lý ngôn ngữ tự nhiên với Transformers",
                            "ThS. Trần NLP Engineer",
                            "BERT, GPT và các mô hình transformer trong xử lý ngôn ngữ tự nhiên",
                            "nlp_transformers.pdf",
                            "pdf",
                            3896543L,
                            LocalDateTime.now().minusDays(3),
                            getSafeElement(subjects, 4),
                            getSafeElement(users, 0),
                            Arrays.asList(getSafeElement(categories, Math.min(3, categories.size()-1)))
                    );
                    sampleDocuments.add(doc17);
                }

                // Thêm các document cố định (không phụ thuộc vào số lượng subjects)
                sampleDocuments.addAll(Arrays.asList(doc1, doc2, doc3, doc4, doc5, doc6, doc7, doc8, doc9,
                        doc10, doc11, doc12));

                documentRepository.saveAll(sampleDocuments);
                System.out.println("Đã tạo " + sampleDocuments.size() + " tài liệu mẫu");
            }
        }
    }

    // Phương thức helper để lấy phần tử an toàn
    private <T> T getSafeElement(List<T> list, int index) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (index >= list.size()) {
            return list.get(list.size() - 1);
        }
        return list.get(index);
    }

    private Document createDocument(String title, String author, String description, String filePath,
                                    String fileType, Long fileSize, LocalDateTime uploadDate,
                                    Subject subject, User uploadedBy, List<Category> categories) {
        Document doc = new Document();
        doc.setTitle(title);
        doc.setAuthor(author);
        doc.setDescription(description);
        doc.setFilePath(filePath);
        doc.setFileType(fileType);
        doc.setFileSize(fileSize);
        doc.setUploadDate(uploadDate);
        doc.setSubject(subject);
        doc.setUploadedBy(uploadedBy);

        // Lọc bỏ các category null
        if (categories != null) {
            categories = categories.stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        doc.setCategories(categories);
        return doc;
    }

    // CÁC PHƯƠNG THỨC CƠ BẢN - GIỮ NGUYÊN
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Optional<Document> getDocumentById(Integer id) {
        return documentRepository.findById(id);
    }

    public Document saveDocument(Document document, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, file.getBytes());

            document.setFilePath(fileName);
            document.setFileType(file.getContentType());
            document.setFileSize(file.getSize());
        }
        return documentRepository.save(document);
    }

    public Document saveDocument(Document document) {
        return documentRepository.save(document);
    }

    public void deleteDocument(Integer id) {
        Optional<Document> document = documentRepository.findById(id);
        if (document.isPresent() && document.get().getFilePath() != null) {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR + document.get().getFilePath()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        documentRepository.deleteById(id);
    }

    public List<Document> searchByAuthor(String author) {
        return documentRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<Document> getDocumentsBySubject(Integer subjectId) {
        return documentRepository.findBySubjectId(subjectId);
    }

    public List<Document> getDocumentsByCategory(Integer categoryId) {
        return documentRepository.findByCategoriesId(categoryId);
    }

    public List<Document> getDocumentsByUser(Integer userId) {
        return documentRepository.findByUploadedById(userId);
    }

    // TÍNH NĂNG MỚI: GỢI Ý TÀI LIỆU LIÊN QUAN
    public List<Document> getRelatedDocuments(Integer documentId, int limit) {
        Optional<Document> currentDocument = documentRepository.findById(documentId);
        if (!currentDocument.isPresent()) {
            return new ArrayList<>();
        }

        Document doc = currentDocument.get();
        List<Document> relatedDocs = new ArrayList<>();

        try {
            // Ưu tiên 1: Cùng môn học
            if (doc.getSubject() != null) {
                List<Document> sameSubject = documentRepository.findBySubjectIdAndIdNot(
                        doc.getSubject().getId(), documentId);
                relatedDocs.addAll(sameSubject.stream()
                        .limit(limit)
                        .collect(Collectors.toList()));
            }

            // Ưu tiên 2: Cùng danh mục (nếu chưa đủ)
            if (relatedDocs.size() < limit && doc.getCategories() != null && !doc.getCategories().isEmpty()) {
                List<Document> sameCategory = documentRepository.findByCategoriesInAndIdNot(
                        doc.getCategories(), documentId);
                sameCategory.stream()
                        .filter(d -> !relatedDocs.contains(d))
                        .limit(limit - relatedDocs.size())
                        .forEach(relatedDocs::add);
            }

            // Ưu tiên 3: Tài liệu phổ biến (nếu vẫn chưa đủ)
            if (relatedDocs.size() < limit) {
                List<Document> popularDocs = documentRepository.findPopularDocuments(
                        limit - relatedDocs.size(), documentId);
                if (popularDocs != null) {
                    popularDocs.stream()
                            .filter(d -> !relatedDocs.contains(d))
                            .forEach(relatedDocs::add);
                }
            }

            // Ưu tiên 4: Tài liệu mới nhất (nếu vẫn chưa đủ)
            if (relatedDocs.size() < limit) {
                List<Document> recentDocs = documentRepository.findTopByOrderByUploadDateDesc(
                        limit - relatedDocs.size(), documentId);
                if (recentDocs != null) {
                    recentDocs.stream()
                            .filter(d -> !relatedDocs.contains(d))
                            .forEach(relatedDocs::add);
                }
            }

        } catch (Exception e) {
            // Fallback: lấy tài liệu mới nhất nếu có lỗi
            System.err.println("Lỗi khi lấy tài liệu liên quan: " + e.getMessage());
            return documentRepository.findTopByOrderByUploadDateDesc(limit, documentId);
        }

        return relatedDocs.stream().limit(limit).collect(Collectors.toList());
    }

    // TÌM KIẾM NÂNG CAO
    public List<Document> advancedSearch(String keyword, Integer subjectId, Integer categoryId) {
        try {
            boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
            boolean hasSubject = subjectId != null;
            boolean hasCategory = categoryId != null;

            if (hasKeyword) {
                if (hasSubject && hasCategory) {
                    return documentRepository.findByTitleContainingIgnoreCaseAndSubjectIdAndCategoriesId(
                            keyword, subjectId, categoryId);
                } else if (hasSubject) {
                    return documentRepository.findByTitleContainingIgnoreCaseAndSubjectId(keyword, subjectId);
                } else if (hasCategory) {
                    return documentRepository.findByTitleContainingIgnoreCaseAndCategoriesId(keyword, categoryId);
                } else {
                    return documentRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword);
                }
            } else {
                if (hasSubject && hasCategory) {
                    return documentRepository.findBySubjectIdAndCategoriesId(subjectId, categoryId);
                } else if (hasSubject) {
                    return documentRepository.findBySubjectId(subjectId);
                } else if (hasCategory) {
                    return documentRepository.findByCategoriesId(categoryId);
                } else {
                    return documentRepository.findAll();
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong tìm kiếm nâng cao: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // LẤY TÀI LIỆU MỚI NHẤT
    public List<Document> getRecentDocuments(int limit) {
        return documentRepository.findTopByOrderByUploadDateDesc(limit);
    }

    // THỐNG KÊ
    public Long getTotalDocuments() {
        return documentRepository.count();
    }

    public Long getTotalDocumentsBySubject(Integer subjectId) {
        return documentRepository.countBySubjectId(subjectId);
    }

    public Long getTotalDocumentsByUser(Integer userId) {
        return documentRepository.countByUploadedById(userId);
    }

    // TÌM KIẾM THEO TỪ KHÓA
    public List<Document> searchByKeyword(String keyword) {
        return documentRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword);
    }
}