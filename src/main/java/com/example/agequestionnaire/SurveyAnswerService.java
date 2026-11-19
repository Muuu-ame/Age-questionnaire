package com.example.agequestionnaire;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class SurveyAnswerService {

    private final Firestore firestore;

    public SurveyAnswerService(Firestore firestore) {
        this.firestore = firestore;
    }

    public SurveyAnswer saveAnswer(
            String gender,
            String ageGroup,
            String comment
    ) {
        SurveyAnswer answer = new SurveyAnswer(
                gender,
                ageGroup,
                comment
        );

        Map<String, Object> data = new HashMap<>();

        data.put("gender", answer.getGender());
        data.put("ageGroup", answer.getAgeGroup());
        data.put("comment", answer.getComment());
        data.put("submittedAt", answer.getSubmittedAt());

        CollectionReference collection =
                firestore.collection("answers");

        DocumentReference document = collection.document();

        try {
            WriteResult result = document.set(data).get();

            answer.setId(document.getId());

            return answer;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                    "Saving answer was interrupted", e
            );

        } catch (ExecutionException e) {
            throw new RuntimeException(
                    "Failed to save answer to Firestore", e
            );
        }
    }
}
