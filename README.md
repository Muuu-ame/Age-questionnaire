# 年代・性別アンケート

## 概要

性別と年代を選択して回答できる、匿名アンケートアプリケーションです。

PC・タブレット・スマートフォンでの利用を想定しています。回答データはPostgreSQLデータベースに保存します。

## 主な機能

- 性別の選択（男性・女性）
- 年代の選択（10代・20代・30代・40代・50代・60代以上）
- 自由コメントの送信
- 回答データのPostgreSQLへの保存
- 回答後、自動的に最初の画面へ戻る
- 回答者の氏名など、個人を特定する情報を収集しない

## 使用技術

| 分類 | 技術 |
|---|---|
| 言語 | Java 21 |
| フレームワーク | Spring Boot |
| ビルドツール | Maven |
| データベース | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| フロントエンド | HTML / CSS / JavaScript |
| コンテナ | Docker |
| 公開環境（予定） | Google Cloud Run |
| データベース（公開環境予定） | Neon PostgreSQL |

## データベース構成

回答データは `answers` テーブルに保存されます。

| カラム | 内容 |
|---|---|
| id | 回答ID |
| submitted_at | 回答日時 |
| gender | 性別 |
| age_group | 年代 |
| comment | コメント |

テーブルはJPAの設定により自動生成・更新されます。

## ローカル環境での起動方法

### 必要な環境

- Java 21以上
- PostgreSQL 18（または対応するPostgreSQL）
- Windowsの場合はPowerShell

### 1. リポジトリのクローン

```bash
git clone https://github.com/Muuu-ame/Age-questionnaire.git
cd Age-questionnaire
```

### 2. データベースの作成

PostgreSQLで `age_questionnaire` というデータベースを作成します。

### 3. 環境変数の設定

PowerShellの場合：

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/age_questionnaire"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="データベースのパスワード"
```

### 4. アプリケーションの起動

```powershell
.\mvnw.cmd spring-boot:run
```

### 5. ブラウザでアクセス

```text
http://localhost:8080/
```

## Docker

プロジェクトのルートディレクトリで以下を実行します。

```bash
docker build -t age-questionnaire .
```

Dockerイメージのビルドにはマルチステージビルドを使用しています。

データベース接続情報は環境変数から設定します。

## 公開環境

公開環境では、以下の構成を予定しています。

- アプリケーション：Google Cloud Run
- データベース：Neon PostgreSQL
- コンテナイメージ：Google Artifact Registry
- リージョン：asia-northeast1（東京）

本番環境の接続情報は環境変数で管理し、ソースコードやリポジトリには認証情報を含めません。

## セキュリティ・運用上の注意

- データベースのパスワードなどの機密情報をGitHubに公開しないでください。
- `.env` などの環境設定ファイルはGit管理対象外とします。
- 本番環境ではデータベースへの接続情報を安全に管理してください。
- 回答データのバックアップと管理はデータベース側で行います。

## ライセンス

MIT License