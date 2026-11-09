
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

    // CSV保存先
    @Value("${survey.csv.path:data/responses.csv}")
    private String csvFilePath;

    // アンケート回答データ
    public static class Answer {
        public String gender;
        public String ageGroup;
        public String favoriteGame;
        public String comment;
    }

    // アンケート回答を保存
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
                        "Game7", "Game8", "Game9")
                || (answer.comment != null
                        && answer.comment.length() > 500)) {

            return ResponseEntity.badRequest()
                    .body("Invalid answer");
        }

        try {
            // CSVファイルのパスを取得
            Path csvPath = Path.of(csvFilePath)
                    .toAbsolutePath()
                    .normalize();

            Path parent = csvPath.getParent();

            // 保存先フォルダがなければ作成
            if (parent != null) {
                Files.createDirectories(parent);
            }

            // 新規ファイルか確認
            boolean newFile = !Files.exists(csvPath)
                    || Files.size(csvPath) == 0;

            StringBuilder csv = new StringBuilder();

            if (newFile) {
                // Excel用UTF-8 BOM
                csv.append('\uFEFF');

                // CSVヘッダー
                csv.append(
                    "回答日時,性別,年代,一番面白かったゲーム,コメント\n"
                );
            }

            // 回答日時
            String timestamp =
                    LocalDateTime.now().format(FORMATTER);

            // CSVデータ作成
            csv.append(escapeCsv(timestamp)).append(',')
                    .append(escapeCsv(answer.gender)).append(',')
                    .append(escapeCsv(answer.ageGroup)).append(',')
                    .append(escapeCsv(answer.favoriteGame)).append(',')
                    .append(escapeCsv(answer.comment)).append('\n');

            // CSVファイルに保存
            Files.writeString(
                    csvPath,
                    csv.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            return ResponseEntity.ok("Saved");

        } catch (IOException | SecurityException e) {

            // 回答内容や絶対パスはログに出さない
            log.error("アンケート回答の保存に失敗しました");

            return ResponseEntity.status(
                    HttpStatus.INTERNAL_SERVER_ERROR
            ).body("Save failed");
        }
    }

    // 許可された値か確認
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

    // CSV用の文字列変換
    private String escapeCsv(String value) {

        if (value == null) {
            value = "";
        }

        // Excelで数式として解釈されることを防止
        if (value.matches("^[\\s\\u0000-\\u001F]*[=+\\-@].*")) {
            value = "'" + value;
        }

        // ダブルクォーテーションをエスケープ
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
