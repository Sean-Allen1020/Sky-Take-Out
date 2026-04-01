# Sky Take Out - 管理側API

# カテゴリ関連API

## カテゴリ更新

### 基本情報

**Path：** /admin/category

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考       | その他        |
| ------------ | ------- | ------ | ---------- | ------------- |
| id           | integer | 〇     | カテゴリid | format: int64 |
| name         | string  | 〇     | カテゴリ名 |               |
| sort         | integer | 〇     | ソート順   | format: int32 |
| type         | integer | 〇     | 分類タイプ | format: int32 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## カテゴリページネーション検索

### 基本情報

**Path：** /admin/category/page

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例       | 備考                                                   |
| ---------- | ------ | -------- | ------------------------------------------------------ |
| name       | ×      | 伝統主食 | カテゴリ名                                             |
| page       | 〇     | 1        | ページ番号                                             |
| pageSize   | 〇     | 10       | 1ページあたりの件数                                    |
| type       | ×      | 1        | 分類タイプ：1は料理カテゴリ、2はセットメニューカテゴリ |

**Response Body**

| フィールド名  | 型        | 必須か | 備考 | その他          |
| ------------- | --------- | ------ | ---- | --------------- |
| code          | number    | 〇     |      |                 |
| msg           | null      | ×      |      |                 |
| data          | object    | 〇     |      |                 |
| ├─ total      | number    | 〇     |      |                 |
| ├─ records    | object [] | 〇     |      | item 型: object |
| ├─ id         | number    | 〇     |      |                 |
| ├─ type       | number    | 〇     |      |                 |
| ├─ name       | string    | 〇     |      |                 |
| ├─ sort       | number    | 〇     |      |                 |
| ├─ status     | number    | 〇     |      |                 |
| ├─ createTime | string    | 〇     |      |                 |
| ├─ updateTime | string    | 〇     |      |                 |
| ├─ createUser | number    | 〇     |      |                 |
| ├─ updateUser | number    | 〇     |      |                 |

## カテゴリ有効化・無効化

### 基本情報

**Path：** /admin/category/status/{status}

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**パスパラメータ**

| パラメータ | 例  | 備考             |
| ---------- | --- | ---------------- |
| status     | 1   | 1は有効、0は無効 |

**Query**

| パラメータ | 必須か | 例  | 備考       |
| ---------- | ------ | --- | ---------- |
| id         | 〇     | 100 | カテゴリid |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## カテゴリ追加

### 基本情報

**Path：** /admin/category

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考                                                   | その他        |
| ------------ | ------- | ------ | ------------------------------------------------------ | ------------- |
| id           | integer | ×      |                                                        | format: int64 |
| name         | string  | 〇     | カテゴリ名                                             |               |
| sort         | integer | 〇     | ソート順（昇順）                                       | format: int32 |
| type         | integer | 〇     | 分類タイプ：1は料理カテゴリ、2はセットメニューカテゴリ | format: int32 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## IDによるカテゴリ削除

### 基本情報

**Path：** /admin/category

**Method：** DELETE


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考       |
| ---------- | ------ | --- | ---------- |
| id         | 〇     | 100 | カテゴリid |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## タイプによるカテゴリ検索

### 基本情報

**Path：** /admin/category/list

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考                                                   |
| ---------- | ------ | --- | ------------------------------------------------------ |
| type       | ×      | 2   | 分類タイプ：1は料理カテゴリ、2はセットメニューカテゴリ |

**Response Body**

| フィールド名  | 型        | 必須か | 備考 | その他            |
| ------------- | --------- | ------ | ---- | ----------------- |
| code          | integer   | 〇     |      | format: int32     |
| data          | object [] | ×      |      | item 型: object   |
| ├─ createTime | string    | ×      |      | format: date-time |
| ├─ createUser | integer   | ×      |      | format: int64     |
| ├─ id         | integer   | ×      |      | format: int64     |
| ├─ name       | string    | ×      |      |                   |
| ├─ sort       | integer   | ×      |      | format: int32     |
| ├─ status     | integer   | ×      |      | format: int32     |
| ├─ type       | integer   | ×      |      | format: int32     |
| ├─ updateTime | string    | ×      |      | format: date-time |
| ├─ updateUser | integer   | ×      |      | format: int64     |
| msg           | string    | ×      |      |                   |

# 従業員関連API

## パスワード変更

### 基本情報

**Path：** /admin/employee/editPassword

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考             | その他        |
| ------------ | ------- | ------ | ---------------- | ------------- |
| empId        | integer | 〇     | 従業員id         | format: int64 |
| newPassword  | string  | 〇     | 新しいパスワード |               |
| oldPassword  | string  | 〇     | 旧パスワード     |               |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 従業員アカウント有効化・無効化

### 基本情報

**Path：** /admin/employee/status/{status}

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**パスパラメータ**

| パラメータ | 例  | 備考                   |
| ---------- | --- | ---------------------- |
| status     | 1   | 状態：1は有効、0は無効 |

**Query**

| パラメータ | 必須か | 例  | 備考     |
| ---------- | ------ | --- | -------- |
| id         | 〇     |     | 従業員id |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 従業員ページネーション検索

### 基本情報

**Path：** /admin/employee/page

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例       | 備考                |
| ---------- | ------ | -------- | ------------------- |
| name       | ×      | 山田太郎 | 従業員名            |
| page       | 〇     | 1        | ページ番号          |
| pageSize   | 〇     | 10       | 1ページあたりの件数 |

**Response Body**

| フィールド名  | 型          | 必須か | 備考 | その他          |
| ------------- | ----------- | ------ | ---- | --------------- |
| code          | number      | 〇     |      |                 |
| msg           | null        | ×      |      |                 |
| data          | object      | 〇     |      |                 |
| ├─ total      | number      | 〇     |      |                 |
| ├─ records    | object []   | 〇     |      | item 型: object |
| ├─ id         | number      | 〇     |      |                 |
| ├─ username   | string      | 〇     |      |                 |
| ├─ name       | string      | 〇     |      |                 |
| ├─ password   | string      | 〇     |      |                 |
| ├─ phone      | string      | 〇     |      |                 |
| ├─ sex        | string      | 〇     |      |                 |
| ├─ idNumber   | string      | 〇     |      |                 |
| ├─ status     | number      | 〇     |      |                 |
| ├─ createTime | string,null | 〇     |      |                 |
| ├─ updateTime | string      | 〇     |      |                 |
| ├─ createUser | number,null | 〇     |      |                 |
| ├─ updateUser | number      | 〇     |      |                 |

## 従業員ログイン

### 基本情報

**Path：** /admin/employee/login

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型     | 必須か | 備考       | その他 |
| ------------ | ------ | ------ | ---------- | ------ |
| password     | string | 〇     | パスワード |        |
| username     | string | 〇     | ユーザー名 |        |

**Response Body**

| フィールド名 | 型      | 必須か | 備考                             | その他        |
| ------------ | ------- | ------ | -------------------------------- | ------------- |
| code         | integer | 〇     |                                  | format: int32 |
| data         | object  | ×      | 従業員ログイン時の返却データ形式 |               |
| ├─ id        | integer | ×      | 主キー値                         | format: int64 |
| ├─ name      | string  | ×      | 氏名                             |               |
| ├─ token     | string  | ×      | JWTトークン                      |               |
| ├─ userName  | string  | ×      | ユーザー名                       |               |
| msg          | string  | ×      |                                  |               |

## 従業員追加

### 基本情報

**Path：** /admin/employee

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考       | その他        |
| ------------ | ------- | ------ | ---------- | ------------- |
| id           | integer | ×      | 従業員id   | format: int64 |
| idNumber     | string  | 〇     | 身分証     |               |
| name         | string  | 〇     | 氏名       |               |
| phone        | string  | 〇     | 携帯番号   |               |
| sex          | string  | 〇     | 性別       |               |
| username     | string  | 〇     | ユーザー名 |               |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## IDによる従業員検索

### 基本情報

**Path：** /admin/employee/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考     |
| ---------- | --- | -------- |
| id         | 100 | 従業員id |

**Response Body**

| フィールド名  | 型      | 必須か | 備考 | その他            |
| ------------- | ------- | ------ | ---- | ----------------- |
| code          | integer | 〇     |      | format: int32     |
| data          | object  | 〇     |      |                   |
| ├─ createTime | string  | ×      |      | format: date-time |
| ├─ createUser | integer | ×      |      | format: int64     |
| ├─ id         | integer | ×      |      | format: int64     |
| ├─ idNumber   | string  | ×      |      |                   |
| ├─ name       | string  | ×      |      |                   |
| ├─ password   | string  | ×      |      |                   |
| ├─ phone      | string  | ×      |      |                   |
| ├─ sex        | string  | ×      |      |                   |
| ├─ status     | integer | ×      |      | format: int32     |
| ├─ updateTime | string  | ×      |      | format: date-time |
| ├─ updateUser | integer | ×      |      | format: int64     |
| ├─ username   | string  | ×      |      |                   |
| msg           | string  | ×      |      |                   |

## 従業員情報編集

### 基本情報

**Path：** /admin/employee

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| id           | integer | 〇     |      | format: int64 |
| idNumber     | string  | 〇     |      |               |
| name         | string  | 〇     |      |               |
| phone        | string  | 〇     |      |               |
| sex          | string  | 〇     |      |               |
| username     | string  | 〇     |      |               |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## ログアウト

### 基本情報

**Path：** /admin/employee/logout

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型  | 必須か | 備考 | その他 |
| ------------ | --- | ------ | ---- | ------ |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

# セットメニュー関連API

## セットメニュー更新

### 基本情報

**Path：** /admin/setmeal

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名  | 型        | 必須か | 備考                           | その他          |
| ------------- | --------- | ------ | ------------------------------ | --------------- |
| categoryId    | integer   | 〇     | カテゴリid                     | format: int64   |
| description   | string    | ×      | セットメニュー説明             |                 |
| id            | integer   | 〇     | セットメニューid               | format: int64   |
| image         | string    | 〇     | セットメニュー画像パス         |                 |
| name          | string    | 〇     | セットメニュー名               |                 |
| price         | number    | 〇     | セットメニュー価格             |                 |
| setmealDishes | object [] | 〇     | セットメニューと料理の関連関係 | item 型: object |
| ├─ copies     | integer   | 〇     | 料理の数量                     | format: int32   |
| ├─ dishId     | integer   | 〇     | 料理id                         | format: int64   |
| ├─ id         | integer   | ×      | 関連テーブル主キー値           | format: int64   |
| ├─ name       | string    | 〇     | 料理名                         |                 |
| ├─ price      | number    | 〇     | 料理価格                       |                 |
| ├─ setmealId  | integer   | ×      | セットメニューid               | format: int64   |
| status        | integer   | ×      | セットメニュー販売状態         | format: int32   |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | 〇     |      |               |
| msg          | string  | ×      |      |               |

## ページネーション検索

### 基本情報

**Path：** /admin/setmeal/page

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考                   |
| ---------- | ------ | --- | ---------------------- |
| categoryId | ×      |     | カテゴリid             |
| name       | ×      |     | セットメニュー名       |
| page       | 〇     |     | ページ番号             |
| pageSize   | 〇     |     | 1ページあたりの件数    |
| status     | ×      |     | セットメニュー販売状態 |

**Response Body**

| フィールド名    | 型        | 必須か | 備考 | その他          |
| --------------- | --------- | ------ | ---- | --------------- |
| code            | number    | 〇     |      |                 |
| msg             | null      | ×      |      |                 |
| data            | object    | ×      |      |                 |
| ├─ total        | number    | ×      |      |                 |
| ├─ records      | object [] | ×      |      | item 型: object |
| ├─ id           | number    | 〇     |      |                 |
| ├─ categoryId   | number    | 〇     |      |                 |
| ├─ name         | string    | 〇     |      |                 |
| ├─ price        | number    | 〇     |      |                 |
| ├─ status       | number    | 〇     |      |                 |
| ├─ description  | string    | 〇     |      |                 |
| ├─ image        | string    | 〇     |      |                 |
| ├─ updateTime   | string    | 〇     |      |                 |
| ├─ categoryName | string    | 〇     |      |                 |

## セットメニュー販売開始・停止

### 基本情報

**Path：** /admin/setmeal/status/{status}

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**パスパラメータ**

| パラメータ | 例  | 備考                                       |
| ---------- | --- | ------------------------------------------ |
| status     | 1   | セットメニュー状態：1は販売中、0は販売停止 |

**Query**

| パラメータ | 必須か | 例  | 備考             |
| ---------- | ------ | --- | ---------------- |
| id         | 〇     | 101 | セットメニューid |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## セットメニュー一括削除

### 基本情報

**Path：** /admin/setmeal

**Method：** DELETE


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考 |
| ---------- | ------ | --- | ---- |
| ids        | 〇     |     | ids  |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | ×      |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## セットメニュー追加

### 基本情報

**Path：** /admin/setmeal

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名  | 型        | 必須か | 備考                                       | その他          |
| ------------- | --------- | ------ | ------------------------------------------ | --------------- |
| categoryId    | integer   | 〇     | カテゴリid                                 | format: int64   |
| description   | string    | ×      | セットメニュー説明                         |                 |
| id            | integer   | ×      | セットメニューid                           | format: int64   |
| image         | string    | 〇     | セットメニュー画像                         |                 |
| name          | string    | 〇     | セットメニュー名                           |                 |
| price         | number    | 〇     | セットメニュー価格                         |                 |
| setmealDishes | object [] | 〇     | セットメニューに含まれる料理               | item 型: object |
| ├─ copies     | integer   | 〇     | 数量                                       | format: int32   |
| ├─ dishId     | integer   | 〇     | 料理id                                     | format: int64   |
| ├─ id         | integer   | ×      | セットメニュー・料理関連id                 | format: int64   |
| ├─ name       | string    | 〇     | 料理名                                     |                 |
| ├─ price      | number    | 〇     | 料理価格                                   |                 |
| ├─ setmealId  | integer   | 〇     | セットメニューid                           | format: int64   |
| status        | integer   | 〇     | セットメニュー状態：1は販売中、0は販売停止 | format: int32   |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## IDによるセットメニュー検索

### 基本情報

**Path：** /admin/setmeal/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考             |
| ---------- | --- | ---------------- |
| id         | 101 | セットメニューid |

**Response Body**

| フィールド名     | 型        | 必須か | 備考 | その他            |
| ---------------- | --------- | ------ | ---- | ----------------- |
| code             | integer   | 〇     |      | format: int32     |
| data             | object    | 〇     |      |                   |
| ├─ categoryId    | integer   | 〇     |      | format: int64     |
| ├─ categoryName  | string    | 〇     |      |                   |
| ├─ description   | string    | 〇     |      |                   |
| ├─ id            | integer   | 〇     |      | format: int64     |
| ├─ image         | string    | 〇     |      |                   |
| ├─ name          | string    | 〇     |      |                   |
| ├─ price         | number    | 〇     |      |                   |
| ├─ setmealDishes | object [] | 〇     |      | item 型: object   |
| ├─ copies        | integer   | 〇     |      | format: int32     |
| ├─ dishId        | integer   | 〇     |      | format: int64     |
| ├─ id            | integer   | 〇     |      | format: int64     |
| ├─ name          | string    | 〇     |      |                   |
| ├─ price         | number    | 〇     |      |                   |
| ├─ setmealId     | integer   | 〇     |      | format: int64     |
| ├─ status        | integer   | 〇     |      | format: int32     |
| ├─ updateTime    | string    | 〇     |      | format: date-time |
| msg              | string    | ×      |      |                   |

# ダッシュボードAPI

## 本日の運営データ照会

### 基本情報

**Path：** /admin/workspace/businessData

**Method：** GET


**API説明：**

**Response Body**

| フィールド名           | 型      | 必須か | 備考           | その他         |
| ---------------------- | ------- | ------ | -------------- | -------------- |
| code                   | integer | 〇     |                | format: int32  |
| data                   | object  | 〇     |                |                |
| ├─ newUsers            | integer | 〇     | 新規ユーザー数 | format: int32  |
| ├─ orderCompletionRate | number  | 〇     | 注文完了率     | format: double |
| ├─ turnover            | number  | 〇     | 売上高         | format: double |
| ├─ unitPrice           | number  | 〇     | 平均客単価     | format: double |
| ├─ validOrderCount     | integer | 〇     | 有効注文数     | format: int32  |
| msg                    | string  | ×      |                |                |

## セットメニュー概要照会

### 基本情報

**Path：** /admin/workspace/overviewSetmeals

**Method：** GET


**API説明：**

**Response Body**

| フィールド名    | 型      | 必須か | 備考                       | その他        |
| --------------- | ------- | ------ | -------------------------- | ------------- |
| code            | integer | 〇     |                            | format: int32 |
| data            | object  | 〇     |                            |               |
| ├─ discontinued | integer | 〇     | 販売停止中セットメニュー数 | format: int32 |
| ├─ sold         | integer | 〇     | 販売中セットメニュー数     | format: int32 |
| msg             | string  | ×      |                            |               |

## 料理概要照会

### 基本情報

**Path：** /admin/workspace/overviewDishes

**Method：** GET


**API説明：**

**Response Body**

| フィールド名    | 型      | 必須か | 備考             | その他        |
| --------------- | ------- | ------ | ---------------- | ------------- |
| code            | integer | 〇     |                  | format: int32 |
| data            | object  | 〇     |                  |               |
| ├─ discontinued | integer | 〇     | 販売停止中料理数 | format: int32 |
| ├─ sold         | integer | 〇     | 販売中料理数     | format: int32 |
| msg             | string  | ×      |                  |               |

## 注文管理データ照会

### 基本情報

**Path：** /admin/workspace/overviewOrders

**Method：** GET


**API説明：**

**Response Body**

| フィールド名       | 型      | 必須か | 備考               | その他        |
| ------------------ | ------- | ------ | ------------------ | ------------- |
| code               | integer | 〇     |                    | format: int32 |
| data               | object  | 〇     |                    |               |
| ├─ allOrders       | integer | 〇     | 全注文数           | format: int32 |
| ├─ cancelledOrders | integer | 〇     | キャンセル済み件数 | format: int32 |
| ├─ completedOrders | integer | 〇     | 完了件数           | format: int32 |
| ├─ deliveredOrders | integer | 〇     | 配送待ち件数       | format: int32 |
| ├─ waitingOrders   | integer | 〇     | 受付待ち件数       | format: int32 |
| msg                | string  | ×      |                    |               |

# 店舗操作API

## 営業状態取得

### 基本情報

**Path：** /admin/shop/status

**Method：** GET


**API説明：**

**Response Body**

| フィールド名 | 型      | 必須か | 備考                             | その他        |
| ------------ | ------- | ------ | -------------------------------- | ------------- |
| code         | integer | 〇     |                                  | format: int32 |
| data         | integer | 〇     | 店舗営業状態：1は営業中、0は閉店 | format: int32 |
| msg          | string  | ×      |                                  |               |

## 営業状態設定

### 基本情報

**Path：** /admin/shop/{status}

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**パスパラメータ**

| パラメータ | 例  | 備考                             |
| ---------- | --- | -------------------------------- |
| status     | 1   | 店舗営業状態：1は営業中、0は閉店 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

# 統計関連API

## Excelレポート出力API

### 基本情報

**Path：** /admin/report/export

**Method：** GET


**API説明：**

**Response Body**

```
OK
```

## 販売数ランキングTop10照会API

### 基本情報

**Path：** /admin/report/top10

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例         | 備考   |
| ---------- | ------ | ---------- | ------ |
| begin      | 〇     | 2022-05-01 | 開始日 |
| end        | 〇     | 2022-05-31 | 終了日 |

**Response Body**

| フィールド名  | 型      | 必須か | 備考                       | その他        |
| ------------- | ------- | ------ | -------------------------- | ------------- |
| code          | integer | 〇     |                            | format: int32 |
| data          | object  | 〇     |                            |               |
| ├─ nameList   | string  | 〇     | 商品名一覧（カンマ区切り） |               |
| ├─ numberList | string  | 〇     | 販売数一覧（カンマ区切り） |               |
| msg           | string  | ×      |                            |               |

## ユーザー統計API

### 基本情報

**Path：** /admin/report/userStatistics

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例         | 備考   |
| ---------- | ------ | ---------- | ------ |
| begin      | 〇     | 2022-05-01 | 開始日 |
| end        | 〇     | 2022-05-31 | 終了日 |

**Response Body**

| フィールド名     | 型      | 必須か | 備考                               | その他        |
| ---------------- | ------- | ------ | ---------------------------------- | ------------- |
| code             | integer | 〇     |                                    | format: int32 |
| data             | object  | 〇     |                                    |               |
| ├─ dateList      | string  | 〇     | 日付一覧（カンマ区切り）           |               |
| ├─ newUserList   | string  | 〇     | 新規ユーザー数一覧（カンマ区切り） |               |
| ├─ totalUserList | string  | 〇     | 総ユーザー数一覧（カンマ区切り）   |               |
| msg              | string  | ×      |                                    |               |

## 売上高統計API

### 基本情報

**Path：** /admin/report/turnoverStatistics

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例         | 備考   |
| ---------- | ------ | ---------- | ------ |
| begin      | 〇     | 2022-05-01 | 開始日 |
| end        | 〇     | 2022-05-31 | 終了日 |

**Response Body**

| フィールド名    | 型      | 必須か | 備考                               | その他        |
| --------------- | ------- | ------ | ---------------------------------- | ------------- |
| code            | integer | 〇     |                                    | format: int32 |
| data            | object  | 〇     |                                    |               |
| ├─ dateList     | string  | 〇     | 日付一覧（日付はカンマ区切り）     |               |
| ├─ turnoverList | string  | 〇     | 売上高一覧（売上高はカンマ区切り） |               |
| msg             | string  | ×      |                                    |               |

## 注文統計API

### 基本情報

**Path：** /admin/report/ordersStatistics

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例         | 備考   |
| ---------- | ------ | ---------- | ------ |
| begin      | 〇     | 2022-05-01 | 開始日 |
| end        | 〇     | 2022-05-31 | 終了日 |

**Response Body**

| フィールド名           | 型      | 必須か | 備考                           | その他         |
| ---------------------- | ------- | ------ | ------------------------------ | -------------- |
| code                   | integer | 〇     |                                | format: int32  |
| data                   | object  | 〇     |                                |                |
| ├─ dateList            | string  | 〇     | 日付一覧（カンマ区切り）       |                |
| ├─ orderCompletionRate | number  | 〇     | 注文完了率                     | format: double |
| ├─ orderCountList      | string  | 〇     | 注文数一覧（カンマ区切り）     |                |
| ├─ totalOrderCount     | integer | 〇     | 総注文数                       | format: int32  |
| ├─ validOrderCount     | integer | 〇     | 有効注文数                     | format: int32  |
| ├─ validOrderCountList | string  | 〇     | 有効注文数一覧（カンマ区切り） |                |
| msg                    | string  | ×      |                                |                |

# 料理関連API

## 料理更新

### 基本情報

**Path：** /admin/dish

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型        | 必須か | 備考 | その他          |
| ------------ | --------- | ------ | ---- | --------------- |
| categoryId   | integer   | 〇     |      | format: int64   |
| description  | string    | ×      |      |                 |
| flavors      | object [] | ×      |      | item 型: object |
| ├─ dishId    | integer   | ×      |      | format: int64   |
| ├─ id        | integer   | ×      |      | format: int64   |
| ├─ name      | string    | 〇     |      |                 |
| ├─ value     | string    | 〇     |      |                 |
| id           | integer   | 〇     |      | format: int64   |
| image        | string    | 〇     |      |                 |
| name         | string    | 〇     |      |                 |
| price        | number    | 〇     |      |                 |
| status       | integer   | ×      |      | format: int32   |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 料理一括削除

### 基本情報

**Path：** /admin/dish

**Method：** DELETE


**API説明：**

**Query**

| パラメータ | 必須か | 例    | 備考                   |
| ---------- | ------ | ----- | ---------------------- |
| ids        | 〇     | 1,2,3 | 料理id（カンマ区切り） |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 料理追加

### 基本情報

**Path：** /admin/dish

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型        | 必須か | 備考                             | その他          |
| ------------ | --------- | ------ | -------------------------------- | --------------- |
| categoryId   | integer   | 〇     | カテゴリid                       | format: int64   |
| description  | string    | ×      | 料理説明                         |                 |
| flavors      | object [] | ×      | 味                               | item 型: object |
| ├─ dishId    | integer   | ×      | 料理id                           | format: int64   |
| ├─ id        | integer   | ×      | 味id                             | format: int64   |
| ├─ name      | string    | 〇     | 味名                             |                 |
| ├─ value     | string    | 〇     | 味の値                           |                 |
| id           | integer   | ×      | 料理id                           | format: int64   |
| image        | string    | 〇     | 料理画像パス                     |                 |
| name         | string    | 〇     | 料理名                           |                 |
| price        | number    | 〇     | 料理価格                         |                 |
| status       | integer   | ×      | 料理状態：1は販売中、0は販売停止 | format: int32   |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## IDによる料理検索

### 基本情報

**Path：** /admin/dish/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考   |
| ---------- | --- | ------ |
| id         | 101 | 料理id |

**Response Body**

| フィールド名    | 型        | 必須か | 備考 | その他            |
| --------------- | --------- | ------ | ---- | ----------------- |
| code            | integer   | 〇     |      | format: int32     |
| data            | object    | 〇     |      |                   |
| ├─ categoryId   | integer   | 〇     |      | format: int64     |
| ├─ categoryName | string    | 〇     |      |                   |
| ├─ description  | string    | 〇     |      |                   |
| ├─ flavors      | object [] | 〇     |      | item 型: object   |
| ├─ dishId       | integer   | 〇     |      | format: int64     |
| ├─ id           | integer   | 〇     |      | format: int64     |
| ├─ name         | string    | 〇     |      |                   |
| ├─ value        | string    | 〇     |      |                   |
| ├─ id           | integer   | 〇     |      | format: int64     |
| ├─ image        | string    | 〇     |      |                   |
| ├─ name         | string    | 〇     |      |                   |
| ├─ price        | number    | 〇     |      |                   |
| ├─ status       | integer   | 〇     |      | format: int32     |
| ├─ updateTime   | string    | 〇     |      | format: date-time |
| msg             | string    | ×      |      |                   |

## カテゴリidによる料理検索

### 基本情報

**Path：** /admin/dish/list

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考       |
| ---------- | ------ | --- | ---------- |
| categoryId | 〇     | 101 | カテゴリid |

**Response Body**

| フィールド名   | 型        | 必須か | 備考 | その他            |
| -------------- | --------- | ------ | ---- | ----------------- |
| code           | integer   | 〇     |      | format: int32     |
| data           | object [] | ×      |      | item 型: object   |
| ├─ categoryId  | integer   | ×      |      | format: int64     |
| ├─ createTime  | string    | ×      |      | format: date-time |
| ├─ createUser  | integer   | ×      |      | format: int64     |
| ├─ description | string    | ×      |      |                   |
| ├─ id          | integer   | ×      |      | format: int64     |
| ├─ image       | string    | ×      |      |                   |
| ├─ name        | string    | ×      |      |                   |
| ├─ price       | number    | ×      |      |                   |
| ├─ status      | integer   | ×      |      | format: int32     |
| ├─ updateTime  | string    | ×      |      | format: date-time |
| ├─ updateUser  | integer   | ×      |      | format: int64     |
| msg            | string    | ×      |      |                   |

## 料理ページネーション検索

### 基本情報

**Path：** /admin/dish/page

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例       | 備考                |
| ---------- | ------ | -------- | ------------------- |
| categoryId | ×      | 101      | カテゴリid          |
| name       | ×      | 宮保鶏丁 | 料理名              |
| page       | 〇     | 1        | ページ番号          |
| pageSize   | 〇     | 10       | 1ページあたりの件数 |
| status     | ×      | 1        | カテゴリ状態        |

**Response Body**

| フィールド名    | 型        | 必須か | 備考           | その他          |
| --------------- | --------- | ------ | -------------- | --------------- |
| code            | number    | 〇     |                |                 |
| msg             | null      | ×      |                |                 |
| data            | object    | ×      |                |                 |
| ├─ total        | number    | 〇     | 総件数         |                 |
| ├─ records      | object [] | 〇     | 当ページデータ | item 型: object |
| ├─ id           | number    | 〇     |                |                 |
| ├─ name         | string    | 〇     |                |                 |
| ├─ categoryId   | number    | 〇     |                |                 |
| ├─ price        | number    | 〇     |                |                 |
| ├─ image        | string    | 〇     |                |                 |
| ├─ description  | string    | 〇     |                |                 |
| ├─ status       | number    | 〇     |                |                 |
| ├─ updateTime   | string    | 〇     |                |                 |
| ├─ categoryName | string    | 〇     | カテゴリ名     |                 |

## 料理販売開始・停止

### 基本情報

**Path：** /admin/dish/status/{status}

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**パスパラメータ**

| パラメータ | 例  | 備考                             |
| ---------- | --- | -------------------------------- |
| status     | 1   | 料理状態：1は販売中、0は販売停止 |

**Query**

| パラメータ | 必須か | 例  | 備考   |
| ---------- | ------ | --- | ------ |
| id         | 〇     | 101 | 料理id |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

# 注文管理API

## 注文取消

### 基本情報

**Path：** /admin/order/cancel

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考         | その他        |
| ------------ | ------- | ------ | ------------ | ------------- |
| cancelReason | string  | 〇     | 注文取消理由 |               |
| id           | integer | 〇     | 注文id       | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## ステータス別注文数集計

### 基本情報

**Path：** /admin/order/statistics

**Method：** GET


**API説明：**

**Response Body**

| フィールド名          | 型      | 必須か | 備考         | その他        |
| --------------------- | ------- | ------ | ------------ | ------------- |
| code                  | integer | 〇     |              | format: int32 |
| data                  | object  | 〇     |              |               |
| ├─ confirmed          | integer | 〇     | 配送待ち件数 | format: int32 |
| ├─ deliveryInProgress | integer | 〇     | 配送中件数   | format: int32 |
| ├─ toBeConfirmed      | integer | 〇     | 受付待ち件数 | format: int32 |
| msg                   | string  | ×      |              |               |

## 注文完了

### 基本情報

**Path：** /admin/order/complete/{id}

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**パスパラメータ**

| パラメータ | 例  | 備考   |
| ---------- | --- | ------ |
| id         | 101 | 注文id |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 注文拒否

### 基本情報

**Path：** /admin/order/rejection

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名    | 型      | 必須か | 備考         | その他        |
| --------------- | ------- | ------ | ------------ | ------------- |
| id              | integer | 〇     | 注文id       | format: int64 |
| rejectionReason | string  | 〇     | 注文拒否原因 |               |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 注文受付

### 基本情報

**Path：** /admin/order/confirm

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考   | その他        |
| ------------ | ------- | ------ | ------ | ------------- |
| id           | integer | 〇     | 注文id | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 注文詳細照会

### 基本情報

**Path：** /admin/order/details/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考   |
| ---------- | --- | ------ |
| id         | 101 | 注文id |

**Response Body**

| フィールド名             | 型        | 必須か | 備考 | その他            |
| ------------------------ | --------- | ------ | ---- | ----------------- |
| code                     | integer   | 〇     |      | format: int32     |
| data                     | object    | ×      |      |                   |
| ├─ address               | string    | ×      |      |                   |
| ├─ addressBookId         | integer   | ×      |      | format: int64     |
| ├─ amount                | number    | ×      |      |                   |
| ├─ cancelReason          | string    | ×      |      |                   |
| ├─ cancelTime            | string    | ×      |      | format: date-time |
| ├─ checkoutTime          | string    | ×      |      | format: date-time |
| ├─ consignee             | string    | ×      |      |                   |
| ├─ deliveryStatus        | integer   | ×      |      | format: int32     |
| ├─ deliveryTime          | string    | ×      |      | format: date-time |
| ├─ estimatedDeliveryTime | string    | ×      |      | format: date-time |
| ├─ id                    | integer   | ×      |      | format: int64     |
| ├─ number                | string    | ×      |      |                   |
| ├─ orderDetailList       | object [] | ×      |      | item 型: object   |
| ├─ amount                | number    | ×      |      |                   |
| ├─ dishFlavor            | string    | ×      |      |                   |
| ├─ dishId                | integer   | ×      |      | format: int64     |
| ├─ id                    | integer   | ×      |      | format: int64     |
| ├─ image                 | string    | ×      |      |                   |
| ├─ name                  | string    | ×      |      |                   |
| ├─ number                | integer   | ×      |      | format: int32     |
| ├─ orderId               | integer   | ×      |      | format: int64     |
| ├─ setmealId             | integer   | ×      |      | format: int64     |
| ├─ orderDishes           | string    | ×      |      |                   |
| ├─ orderTime             | string    | ×      |      | format: date-time |
| ├─ packAmount            | integer   | ×      |      | format: int32     |
| ├─ payMethod             | integer   | ×      |      | format: int32     |
| ├─ payStatus             | integer   | ×      |      | format: int32     |
| ├─ phone                 | string    | ×      |      |                   |
| ├─ rejectionReason       | string    | ×      |      |                   |
| ├─ remark                | string    | ×      |      |                   |
| ├─ status                | integer   | ×      |      | format: int32     |
| ├─ tablewareNumber       | integer   | ×      |      | format: int32     |
| ├─ tablewareStatus       | integer   | ×      |      | format: int32     |
| ├─ userId                | integer   | ×      |      | format: int64     |
| ├─ userName              | string    | ×      |      |                   |
| msg                      | string    | ×      |      |                   |

## 注文配送

### 基本情報

**Path：** /admin/order/delivery/{id}

**Method：** PUT


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考   |
| ---------- | --- | ------ |
| id         | 101 | 注文id |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 注文検索

### 基本情報

**Path：** /admin/order/conditionSearch

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考      |
| ---------- | ------ | --- | --------- |
| beginTime  | ×      |     | beginTime |
| endTime    | ×      |     | endTime   |
| number     | ×      |     | number    |
| page       | 〇     |     | page      |
| pageSize   | 〇     |     | pageSize  |
| phone      | ×      |     | phone     |
| status     | ×      |     | status    |

**Response Body**

| フィールド名             | 型          | 必須か | 備考                                   | その他          |
| ------------------------ | ----------- | ------ | -------------------------------------- | --------------- |
| code                     | number      | 〇     |                                        |                 |
| msg                      | null        | ×      |                                        |                 |
| data                     | object      | ×      |                                        |                 |
| ├─ total                 | number      | ×      |                                        |                 |
| ├─ records               | object []   | ×      |                                        | item 型: object |
| ├─ id                    | number      | 〇     |                                        |                 |
| ├─ number                | string      | 〇     |                                        |                 |
| ├─ status                | number      | 〇     |                                        |                 |
| ├─ userId                | number      | 〇     |                                        |                 |
| ├─ addressBookId         | number      | 〇     |                                        |                 |
| ├─ orderTime             | string      | 〇     |                                        |                 |
| ├─ checkoutTime          | null,string | 〇     |                                        |                 |
| ├─ payMethod             | number      | 〇     |                                        |                 |
| ├─ payStatus             | number      | 〇     |                                        |                 |
| ├─ amount                | number      | 〇     |                                        |                 |
| ├─ remark                | string      | 〇     |                                        |                 |
| ├─ userName              | string      | 〇     |                                        |                 |
| ├─ phone                 | string      | 〇     |                                        |                 |
| ├─ address               | string      | 〇     |                                        |                 |
| ├─ consignee             | string      | 〇     |                                        |                 |
| ├─ cancelReason          | string      | 〇     |                                        |                 |
| ├─ rejectionReason       | string      | 〇     |                                        |                 |
| ├─ cancelTime            | string      | 〇     |                                        |                 |
| ├─ estimatedDeliveryTime | string      | 〇     |                                        |                 |
| ├─ deliveryStatus        | number      | 〇     |                                        |                 |
| ├─ deliveryTime          | string      | 〇     |                                        |                 |
| ├─ packAmount            | number      | 〇     |                                        |                 |
| ├─ tablewareNumber       | number      | 〇     |                                        |                 |
| ├─ tablewareStatus       | number      | 〇     |                                        |                 |
| ├─ orderDishes           | string      | 〇     | 注文に含まれる料理（文字列形式で表示） |                 |

# 共通API

## ファイルアップロード

### 基本情報

**Path：** /admin/common/upload

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値              | 必須か | 例  | 備考 |
| ------------ | ------------------- | ------ | --- | ---- |
| Content-Type | multipart/form-data | 〇     |     |      |

**Request Body**

| パラメータ | 型   | 必須か | 例  | 備考     |
| ---------- | ---- | ------ | --- | -------- |
| file       | file | 〇     |     | ファイル |

**Response Body**

| フィールド名 | 型      | 必須か | 備考                     | その他        |
| ------------ | ------- | ------ | ------------------------ | ------------- |
| code         | integer | 〇     |                          | format: int32 |
| data         | string  | 〇     | ファイルアップロードパス |               |
| msg          | string  | ×      |                          |               |