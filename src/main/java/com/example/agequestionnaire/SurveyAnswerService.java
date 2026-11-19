package com.example.agequestionnaire;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SurveyAnswerService {

    private final SurveyAnswerRepository repository;

    public SurveyAnswerService(SurveyAnswerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public SurveyAnswer saveAnswer(String gender, String ageGroup, String comment) {
        SurveyAnswer answer = new SurveyAnswer();

        answer.setGender(gender);
        answer.setAgeGroup(ageGroup);
        answer.setComment(comment);

        return repository.save(answer);
    }
}