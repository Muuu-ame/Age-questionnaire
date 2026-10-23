package com.example.agequestionnaire;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger log =
            LoggerFactory.getLogger(AnswerController.class);

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${survey.csv.path:data/responses.csv}")
    private String csvFilePath;

    public static class Answer {
        public String gender;
        public String ageGroup;
        public String favoriteGame;
    }

    @PostMapping("/api/answers")
    public synchronized ResponseEntity<String> saveAnswer(
            @RequestBody(required = false) Answer answer) {

        // 入力チェック
        if (answer == null
                || !isAllowed(answer.gender,
                        "男性", "女性")
                || !isAllowed(answer.ageGroup,
                        "10代以下", "20代", "30代",
                        "40代", "50代", "60代以上")
                || !isAllowed(answer.favoriteGame,
                        "Game1", "Game2", "Game3",
                        "Game4", "Game5", "Game6",
                        "Game7", "Game8", "Game9")) {

            return ResponseEntity.badRequest()
                    .body("Invalid answer");
        }

        try {
            Path csvPath = Path.of(csvFilePath)
                    .toAbsolutePath()
                    .normalize();

            Path parent = csvPath.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            boolean newFile = !Files.exists(csvPath)
                    || Files.size(csvPath) == 0;

            StringBuilder csv = new StringBuilder();

            if (newFile) {
                // Excelで日本語を読みやすくするためのUTF-8 BOM
                csv.append('\uFEFF');
                csv.append("回答日時,性別,年代,一番面白かったゲーム\n");
            }

            String timestamp =
                    LocalDateTime.now().format(FORMATTER);

            csv.append(timestamp).append(',')
                    .append(answer.gender).append(',')
                    .append(answer.ageGroup).append(',')
                    .append(answer.favoriteGame).append('\n');

            Files.writeString(
                    csvPath,
                    csv.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            return ResponseEntity.ok("Saved");

        } catch (IOException | SecurityException e) {
            // 回答内容やファイルの絶対パスをログに出さない
            log.error("アンケート回答の保存に失敗しました");
            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Save failed");
        }
    }

    private boolean isAllowed(String value, String... allowed) {
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