# Microservice Practice

Spring Bootを使用したマイクロサービスアーキテクチャの学習用プロジェクトです。

簡易EC（ネットショップ）を題材に、複数のサービスを独立して構築し、サービス間通信、API Gateway、Docker、データベース分離などを実践的に学習します。

## 概要

本プロジェクトでは、以下のマイクロサービスを構築しています。

* API Gateway
* Product Service
* Order Service

各サービスは独立したSpring Bootアプリケーションとして構成し、Docker Compose上でコンテナとして実行します。

また、サービスごとにデータベースを分離し、Order ServiceからProduct Serviceへの通信にはOpenFeignを使用しています。

## プロジェクト構成

```text
Microservice-Practice
│
├── api-gateway
│   └── 外部から各サービスへの入口・リクエストルーティング
│
├── product-service
│   └── 商品情報を管理するAPI
│
├── order-service
│   └── 注文情報を管理するAPI
│
└── docker-compose.yml
    └── 各サービス・データベースのコンテナ構成
````

## システム構成

```text
                           Client
                              |
                              | HTTP
                              v
                    +-------------------+
                    |    API Gateway    |
                    |      :8080        |
                    +---------+---------+
                              |
                 +------------+------------+
                 |                         |
          /products/**                /orders/**
                 |                         |
                 v                         v
       +-------------------+      +-------------------+
       |  Product Service  |      |   Order Service   |
       |      :8081        |      |      :8082        |
       +---------+---------+      +---------+---------+
                 |                          |
                 v                          |
       +-------------------+                |
       |    Product DB     |                |
       |    PostgreSQL     |                |
       +-------------------+                |
                                            |
                                            | OpenFeign
                                            | GET /products/{id}
                                            v
                                   +-------------------+
                                   |  Product Service  |
                                   |      :8081        |
                                   +-------------------+
                                            |
                                            v
                                   +-------------------+
                                   |    Product DB     |
                                   +-------------------+

       Order Service
             |
             v
        +-----------+
        | Order DB  |
        | PostgreSQL|
        +-----------+
```

## サービスの役割

### API Gateway

外部からマイクロサービスへアクセスするための入口です。

ポート:

```text
8080
```

ルーティング:

```text
/products/** → product-service:8081
/orders/**   → order-service:8082
```

ユーザーはProduct ServiceやOrder Serviceへ直接アクセスするのではなく、基本的にAPI Gatewayを経由してアクセスします。

### Product Service

商品情報を管理するサービスです。

ポート:

```text
8081
```

主なAPI:

```text
GET    /products
GET    /products/{id}
POST   /products
PUT    /products/{id}
DELETE /products/{id}
```

データはProduct DBに保存します。

### Order Service

注文情報を管理するサービスです。

ポート:

```text
8082
```

主なAPI:

```text
GET  /orders
GET  /orders/{id}
POST /orders
```

注文作成時には、Product Serviceへ商品情報を問い合わせます。

Order ServiceからProduct Serviceへの通信にはOpenFeignを使用しています。

```text
Order Service
      |
      | OpenFeign
      v
Product Service
      |
      v
Product DB
```

Order ServiceがProduct DBへ直接アクセスすることはありません。

## サービス間通信

本プロジェクトでは2種類の通信があります。

### API Gatewayによる外部からの通信

```text
Client
  |
  | http://localhost:8080/products
  v
API Gateway
  |
  v
Product Service
```

```text
Client
  |
  | http://localhost:8080/orders
  v
API Gateway
  |
  v
Order Service
```

### OpenFeignによるサービス間通信

Order Serviceが商品情報を確認する場合、

```text
Order Service
      |
      | GET /products/{id}
      | OpenFeign
      v
Product Service
```

という通信を行います。

API GatewayとOpenFeignの役割は異なります。

```text
API Gateway
外部 → マイクロサービス

OpenFeign
マイクロサービス → マイクロサービス
```

## データベース構成

サービスごとにデータベースを分離しています。

```text
Product Service
      |
      v
Product DB
PostgreSQL
```

```text
Order Service
      |
      v
Order DB
PostgreSQL
```

各サービスは自分のデータベースを管理し、他サービスのデータベースへ直接アクセスしない構成にしています。

## Docker構成

Docker Composeを使用して、各サービスとデータベースをコンテナとして起動します。

```text
Docker Compose
│
├── api-gateway
│
├── product-service
│
├── product-db
│
├── order-service
│
└── order-db
```

コンテナ間はDocker Composeのネットワークで通信します。

例えばAPI GatewayからProduct Serviceへアクセスする場合、

```text
http://product-service:8081
```

を使用します。

PCからAPI Gatewayへアクセスする場合は、

```text
http://localhost:8080
```

を使用します。

`localhost`はアクセス元の環境自身を意味するため、Dockerコンテナ間通信では`localhost`ではなくDocker Composeのサービス名を使用します。

## ポート構成

```text
API Gateway      8080
Product Service  8081
Order Service    8082
Product DB       5433 → コンテナ内5432
Order DB         5434 → コンテナ内5432
```

Docker Composeでは、

```text
ホスト側ポート : コンテナ側ポート
```

という形でポートを公開しています。

コンテナ間通信ではDockerネットワークを使用するため、サービス間通信だけであればホスト側へのポート公開は必須ではありません。

## 技術スタック

### Backend

* Java 17
* Spring Boot 4.0.7
* Spring MVC
* Spring Data JPA
* Spring Cloud Gateway
* Spring Cloud OpenFeign
* Maven
* Lombok

### Database

* PostgreSQL 15
* Docker Compose

### Infrastructure

* Docker
* Docker Compose

## 起動方法

プロジェクトルートで以下を実行します。

```powershell
docker compose up -d --build
```

起動確認:

```powershell
docker compose ps
```

以下の5つのコンテナが起動します。

```text
api-gateway
product-service
product-db
order-service
order-db
```

## API動作確認

### 商品一覧

```powershell
curl.exe http://localhost:8080/products
```

API Gatewayを経由してProduct Serviceへアクセスします。

```text
Client
  ↓
API Gateway :8080
  ↓
Product Service :8081
```

### 注文一覧

```powershell
curl.exe http://localhost:8080/orders
```

```text
Client
  ↓
API Gateway :8080
  ↓
Order Service :8082
```

### 商品作成

PowerShellではJSONのクォート処理に注意が必要です。

`Invoke-RestMethod`を使用する場合:

```powershell
$body = @{
    name  = "Test Product"
    price = 1000
    stock = 10
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/products" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

### 注文作成

```powershell
$body = @{
    productId = 1
    quantity  = 2
} | ConvertTo-Json

Invoke-RestMethod `
    -Uri "http://localhost:8080/orders" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

注文作成時には、

```text
API Gateway
    ↓
Order Service
    ↓
OpenFeign
    ↓
Product Service
```

というサービス間通信が発生します。

## 開発ステップ

### Step 1: Product Service

商品情報を管理するサービスを実装。

* Entity
* Repository
* Service
* Controller
* PostgreSQL

### Step 2: Order Service

注文情報を管理するサービスを実装。

* Entity
* Repository
* Service
* Controller
* PostgreSQL

### Step 3: Docker化

各サービスとデータベースをDocker Composeで起動できるようにします。

### Step 4: サービス間通信

Order ServiceからProduct Serviceへの通信を実装。

OpenFeignを利用して、

```text
Order Service
      ↓
Product Service
```

のHTTP通信を行います。

### Step 5: API Gateway

外部から各サービスへアクセスするためのAPI Gatewayを追加。

```text
Client
  ↓
API Gateway
  ├── Product Service
  └── Order Service
```

### Step 6: 動作確認・障害テスト

サービス停止時の挙動やサービス間依存を確認します。

```text
1. Product Service停止
   ↓
Gateway → Product Service
   ↓
500

Product Service復旧
   ↓
最終的に200

2. Order Service停止
   ↓
Gateway → Order Service
   ↓
500

Order Service復旧
   ↓
200

3. Product Service停止
   ↓
Order Service → OpenFeign → Product Service
   ↓
POST /orders
   ↓
Product not found

Product Service復旧
   ↓
正常
```

確認項目:

* サービス間通信
* エラーハンドリング
* タイムアウト
* サービス間依存
* API Gatewayのルーティング

## 学習目的

このプロジェクトでは、以下の理解を目的とします。

* モノリスとマイクロサービスの違い
* サービス単位での責任分離
* REST API
* API Gateway
* サービス間HTTP通信
* OpenFeign
* データベース分離
* Dockerコンテナ
* Docker Compose
* コンテナ間ネットワーク
* サービス障害時の挙動
* マイクロサービスにおけるサービス間依存
