package com.example.agequestionnaire;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
public class SurveyAnswer {

    // 回答ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 回答日時
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    // 性別
    @Column(nullable = false, length = 20)
    private String gender;

    // 年代
    @Column(name = "age_group", nullable = false, length = 20)
    private String ageGroup;

    // コメント
    @Column(columnDefinition = "TEXT")
    private String comment;

    // 回答日時を自動設定
    @PrePersist
    protected void onCreate() {
        if (submittedAt == null) {
            submittedAt = LocalDateTime.now();
        }
    }

    // Getter
    public Long getId() {
        return id;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getGender() {
        return gender;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public String getComment() {
        return comment;
    }

    // Setter
    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
