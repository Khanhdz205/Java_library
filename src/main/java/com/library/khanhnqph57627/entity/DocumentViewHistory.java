package com.library.khanhnqph57627.entity;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "document_view_history")
@Data
public class DocumentViewHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(nullable = false)
    private LocalDateTime viewedAt;

    private Integer viewDuration; // thời gian xem (giây)
    private boolean downloaded = false;
    private Integer rating; // đánh giá ngầm định từ 1-5

    @PrePersist
    protected void onCreate() {
        viewedAt = LocalDateTime.now();
    }
}
