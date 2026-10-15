package com.example.agequestionnaire;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class AnswerController {

    private static final Path CSV_PATH = Path.of("responses.csv");

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static class Answer {
        public String gender;
        public String ageGroup;
    }

    @PostMapping("/api/answers")
    public synchronized ResponseEntity<String> saveAnswer(
            @RequestBody Answer answer) {

        if (answer == null
                || !("男性".equals(answer.gender)
                || "女性".equals(answer.gender))
                || !("10代".equals(answer.ageGroup)
                || "20代".equals(answer.ageGroup)
                || "30代".equals(answer.ageGroup)
                || "40代".equals(answer.ageGroup)
                || "50代".equals(answer.ageGroup)
                || "60代以上".equals(answer.ageGroup))) {

            return ResponseEntity.badRequest().body("Invalid answer");
        }

        try {
            boolean newFile = !Files.exists(CSV_PATH)
                    || Files.size(CSV_PATH) == 0;

            StringBuilder csv = new StringBuilder();

            if (newFile) {
                // Excelで日本語を読みやすくするためのUTF-8 BOM
                csv.append('\uFEFF');
                csv.append("回答日時,性別,年代\n");
            }

            String timestamp = LocalDateTime.now().format(FORMATTER);

            csv.append(timestamp).append(',')
               .append(answer.gender).append(',')
               .append(answer.ageGroup).append('\n');

            Files.writeString(
                    CSV_PATH,
                    csv.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            return ResponseEntity.ok("Saved");

        } catch (IOException e) {
            e.printStackTrace();

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Save failed");
        }
    }
}
