# Microservice Practice

Spring Bootを使用したマイクロサービスアーキテクチャの練習用プロジェクトです。

## 概要

簡易EC（ネットショップ）を題材に、以下のマイクロサービス構成を学習します。

* 商品サービス（Product Service）
* 注文サービス（Order Service）
* サービス間HTTP通信
* Docker Composeによるコンテナ化
* データベース分離

## プロジェクト構成

```
Microservice-Practice
│
├── product-service
│   └── 商品情報を管理するAPI
│
└── order-service
    └── 注文情報を管理するAPI
```

## 技術スタック

### Backend

* Java 17
* Spring Boot 4
* Spring MVC
* Maven

### Database（予定）

* PostgreSQL
* Docker Compose

## サービス構成

```
             Client
                |
                |
        +-------+-------+
        |               |
        v               v
 Product Service   Order Service
    :8081              :8082
        |               |
        v               v
 Product DB        Order DB
```

## 開発ステップ

### Step 1: Product Service作成

商品APIを作成します。

予定API:

```
GET /products/{id}
```

例:

```
GET http://localhost:8081/products/1
```

レスポンス例:

```json
{
  "id": 1,
  "name": "Coffee",
  "price": 500
}
```

---

### Step 2: Order Service作成

注文受付APIを作成します。

予定API:

```
POST /orders
```

注文時の商品確認は、Product ServiceへHTTPリクエストを送信します。

```
Order Service
      |
      | HTTP Request
      v
Product Service
```

他サービスのDBへ直接アクセスしない設計にします。

---

### Step 3: Docker化

各サービスをコンテナ化します。

予定構成:

```
docker-compose.yml

product-service
product-db

order-service
order-db
```

---

### Step 4: 障害テスト

Product Serviceを停止し、Order Serviceがどのような動作になるか確認します。

例:

```
docker compose stop product-service
```

確認項目:

* エラーハンドリング
* タイムアウト
* サービス間依存

---

## 起動方法

### Product Service

```
cd product-service
./mvnw spring-boot:run
```

起動確認:

```
http://localhost:8081
```

---

### Order Service

```
cd order-service
./mvnw spring-boot:run
```

起動確認:

```
http://localhost:8082
```

---

## 学習目的

このプロジェクトでは以下を理解することを目的とします。

* モノリスとマイクロサービスの違い
* サービス単位での責任分離
* REST API通信
* データベース分離
* コンテナ環境
* 障害に強い設計

```
```
