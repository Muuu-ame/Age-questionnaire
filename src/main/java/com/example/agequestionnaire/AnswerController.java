
package com.example.agequestionnaire;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnswerController {

    private final SurveyAnswerService service;

    public AnswerController(SurveyAnswerService service) {
        this.service = service;
    }

    public static class Answer {
        public String gender;
        public String ageGroup;
        public String comment;
    }

    @PostMapping("/api/answers")
    public ResponseEntity<String> saveAnswer(
            @RequestBody(required = false) Answer answer) {

        if (answer == null
                || !isAllowed(answer.gender, "男性", "女性")
                || !isAllowed(
                        answer.ageGroup,
                        "10代以下",
                        "20代",
                        "30代",
                        "40代",
                        "50代",
                        "60代以上"
                )
                || (answer.comment != null
                        && answer.comment.length() > 500)) {

            return ResponseEntity.badRequest()
                    .body("Invalid answer");
        }

        try {
            service.saveAnswer(
                    answer.gender,
                    answer.ageGroup,
                    answer.comment
            );

            return ResponseEntity.ok("Saved");

        } catch (RuntimeException e) {
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Save failed");
        }
    }

    private boolean isAllowed(
            String value,
            String... allowed
    ) {
        if (value == null) {
            return false;
        }

        for (String item : allowed) {
            if (item.equals(value)) {
                return true;
            }
        }

        return false;
    }
}
