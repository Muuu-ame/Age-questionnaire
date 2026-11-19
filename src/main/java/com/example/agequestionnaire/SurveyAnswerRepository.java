package com.example.agequestionnaire;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyAnswerRepository
        extends JpaRepository<SurveyAnswer, Long> {

}