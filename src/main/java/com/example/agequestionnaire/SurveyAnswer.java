package com.example.agequestionnaire;

import java.time.Instant;

public class SurveyAnswer {

    private String id;
    private Instant submittedAt;
    private String gender;
    private String ageGroup;
    private String comment;

    public SurveyAnswer() {
    }

    public SurveyAnswer(
            String gender,
            String ageGroup,
            String comment
    ) {
        this.gender = gender;
        this.ageGroup = ageGroup;
        this.comment = comment;
        this.submittedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
