# Sky Take Out - ユーザー側API

# C側-カテゴリAPI

## 条件検索

### 基本情報

**Path：** /user/category/list

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考                                             |
| ---------- | ------ | --- | ------------------------------------------------ |
| type       | ×      | 1   | 分類：1は料理カテゴリ、2はセットメニューカテゴリ |

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

# C側-アドレス帳API

## 住所追加

### 基本情報

**Path：** /user/addressBook

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考     | その他        |
| ------------ | ------- | ------ | -------- | ------------- |
| cityCode     | string  | ×      |          |               |
| cityName     | string  | ×      |          |               |
| consignee    | string  | ×      |          |               |
| detail       | string  | 〇     | 詳細住所 |               |
| districtCode | string  | ×      |          |               |
| districtName | string  | ×      |          |               |
| id           | integer | ×      |          | format: int64 |
| isDefault    | integer | ×      |          | format: int32 |
| label        | string  | ×      |          |               |
| phone        | string  | 〇     | 電話番号 |               |
| provinceCode | string  | ×      |          |               |
| provinceName | string  | ×      |          |               |
| sex          | string  | 〇     |          |               |
| userId       | integer | ×      |          | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## 現在ログイン中のユーザーの全住所情報取得

### 基本情報

**Path：** /user/addressBook/list

**Method：** GET


**API説明：**

**Response Body**

| フィールド名    | 型      | 必須か | 備考 | その他        |
| --------------- | ------- | ------ | ---- | ------------- |
| code            | integer | 〇     |      | format: int32 |
| data            | object  | ×      |      |               |
| ├─ id           | number  | 〇     |      |               |
| ├─ userId       | number  | 〇     |      |               |
| ├─ consignee    | string  | 〇     |      |               |
| ├─ phone        | string  | 〇     |      |               |
| ├─ sex          | string  | 〇     |      |               |
| ├─ provinceCode | string  | 〇     |      |               |
| ├─ provinceName | string  | 〇     |      |               |
| ├─ cityCode     | string  | 〇     |      |               |
| ├─ cityName     | string  | 〇     |      |               |
| ├─ districtCode | string  | 〇     |      |               |
| ├─ districtName | string  | 〇     |      |               |
| ├─ detail       | string  | 〇     |      |               |
| ├─ label        | string  | 〇     |      |               |
| ├─ isDefault    | number  | 〇     |      |               |
| msg             | string  | ×      |      |               |

## デフォルト住所取得

### 基本情報

**Path：** /user/addressBook/default

**Method：** GET


**API説明：**

**Response Body**

| フィールド名    | 型      | 必須か | 備考 | その他        |
| --------------- | ------- | ------ | ---- | ------------- |
| code            | integer | 〇     |      | format: int32 |
| data            | object  | ×      |      |               |
| ├─ cityCode     | string  | ×      |      |               |
| ├─ cityName     | string  | ×      |      |               |
| ├─ consignee    | string  | ×      |      |               |
| ├─ detail       | string  | ×      |      |               |
| ├─ districtCode | string  | ×      |      |               |
| ├─ districtName | string  | ×      |      |               |
| ├─ id           | integer | ×      |      | format: int64 |
| ├─ isDefault    | integer | ×      |      | format: int32 |
| ├─ label        | string  | ×      |      |               |
| ├─ phone        | string  | ×      |      |               |
| ├─ provinceCode | string  | ×      |      |               |
| ├─ provinceName | string  | ×      |      |               |
| ├─ sex          | string  | ×      |      |               |
| ├─ userId       | integer | ×      |      | format: int64 |
| msg             | string  | ×      |      |               |

## IDによる住所更新

### 基本情報

**Path：** /user/addressBook

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考     | その他        |
| ------------ | ------- | ------ | -------- | ------------- |
| cityCode     | string  | ×      |          |               |
| cityName     | string  | ×      |          |               |
| consignee    | string  | ×      |          |               |
| detail       | string  | 〇     | 詳細住所 |               |
| districtCode | string  | ×      |          |               |
| districtName | string  | ×      |          |               |
| id           | integer | 〇     | 主キー値 | format: int64 |
| isDefault    | integer | ×      |          | format: int32 |
| label        | string  | ×      |          |               |
| phone        | string  | 〇     | 電話番号 |               |
| provinceCode | string  | ×      |          |               |
| provinceName | string  | ×      |          |               |
| sex          | string  | 〇     |          |               |
| userId       | integer | ×      |          | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## IDによる住所削除

### 基本情報

**Path：** /user/addressBook

**Method：** DELETE


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考   |
| ---------- | ------ | --- | ------ |
| id         | 〇     | 101 | 住所id |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

## IDによる住所検索

### 基本情報

**Path：** /user/addressBook/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考   |
| ---------- | --- | ------ |
| id         | 101 | 住所id |

**Response Body**

| フィールド名    | 型     | 必須か | 備考 | その他 |
| --------------- | ------ | ------ | ---- | ------ |
| code            | number | 〇     |      |        |
| data            | object | 〇     |      |        |
| ├─ id           | number | ×      |      |        |
| ├─ phone        | string | ×      |      |        |
| ├─ consignee    | string | ×      |      |        |
| ├─ userId       | number | ×      |      |        |
| ├─ cityCode     | string | ×      |      |        |
| ├─ provinceName | string | ×      |      |        |
| ├─ provinceCode | string | ×      |      |        |
| ├─ sex          | string | ×      |      |        |
| ├─ districtName | string | ×      |      |        |
| ├─ districtCode | string | ×      |      |        |
| ├─ cityName     | string | ×      |      |        |
| ├─ isDefault    | number | ×      |      |        |
| ├─ label        | string | ×      |      |        |
| ├─ detail       | string | ×      |      |        |
| msg             | string | ×      |      |        |

## デフォルト住所設定

### 基本情報

**Path：** /user/addressBook/default

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考   | その他        |
| ------------ | ------- | ------ | ------ | ------------- |
| id           | integer | 〇     | 住所id | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

# C側-セットメニュー閲覧API

## カテゴリidによるセットメニュー検索

### 基本情報

**Path：** /user/setmeal/list

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

## セットメニューidに含まれる料理取得

### 基本情報

**Path：** /user/setmeal/dish/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考             |
| ---------- | --- | ---------------- |
| id         | 101 | セットメニューid |

**Response Body**

| フィールド名   | 型        | 必須か | 備考         | その他          |
| -------------- | --------- | ------ | ------------ | --------------- |
| code           | integer   | 〇     |              | format: int32   |
| data           | object [] | 〇     |              | item 型: object |
| ├─ copies      | integer   | 〇     | 数量         | format: int32   |
| ├─ description | string    | 〇     | 料理説明     |                 |
| ├─ image       | string    | 〇     | 料理画像パス |                 |
| ├─ name        | string    | 〇     | 料理名       |                 |
| msg            | string    | ×      |              |                 |

# C側-店舗操作API

## 営業状態取得

### 基本情報

**Path：** /user/shop/status

**Method：** GET


**API説明：**

**Response Body**

| フィールド名 | 型      | 必須か | 備考                         | その他        |
| ------------ | ------- | ------ | ---------------------------- | ------------- |
| code         | integer | 〇     |                              | format: int32 |
| data         | integer | 〇     | 店舗状態：1は営業中、0は閉店 | format: int32 |
| msg          | string  | ×      |                              |               |

# C側-ユーザーAPI

## ログイン

### 基本情報

**Path：** /user/user/login

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型     | 必須か | 備考             | その他 |
| ------------ | ------ | ------ | ---------------- | ------ |
| code         | string | 〇     | WeChat認証コード |        |

**Response Body**

| フィールド名 | 型      | 必須か | 備考                 | その他        |
| ------------ | ------- | ------ | -------------------- | ------------- |
| code         | integer | 〇     |                      | format: int32 |
| data         | object  | 〇     |                      |               |
| ├─ id        | integer | 〇     | ユーザーid           | format: int64 |
| ├─ openid    | string  | 〇     | WeChatユーザーopenid |               |
| ├─ token     | string  | 〇     | JWTトークン          |               |
| msg          | string  | ×      |                      |               |

## ログアウト

### 基本情報

**Path：** /user/user/logout

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
| data         | object  | ×      |      |               |
| msg          | string  | ×      |      |               |

# C側-料理閲覧API

## カテゴリidによる料理検索

### 基本情報

**Path：** /user/dish/list

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考       |
| ---------- | ------ | --- | ---------- |
| categoryId | 〇     | 101 | カテゴリid |

**Response Body**

| フィールド名    | 型        | 必須か | 備考         | その他            |
| --------------- | --------- | ------ | ------------ | ----------------- |
| code            | integer   | 〇     |              | format: int32     |
| data            | object [] | ×      |              | item 型: object   |
| ├─ categoryId   | integer   | ×      |              | format: int64     |
| ├─ categoryName | string    | ×      |              |                   |
| ├─ description  | string    | ×      |              |                   |
| ├─ flavors      | object [] | ×      | 料理の味     | item 型: object   |
| ├─ dishId       | integer   | ×      |              | format: int64     |
| ├─ id           | integer   | ×      |              | format: int64     |
| ├─ name         | string    | ×      |              |                   |
| ├─ value        | string    | ×      |              |                   |
| ├─ id           | integer   | ×      |              | format: int64     |
| ├─ image        | string    | ×      | 料理画像パス |                   |
| ├─ name         | string    | ×      | 料理名       |                   |
| ├─ price        | number    | ×      | 价格         |                   |
| ├─ status       | integer   | ×      |              | format: int32     |
| ├─ updateTime   | string    | ×      |              | format: date-time |
| msg             | string    | ×      |              |                   |

# C側-注文API

## 催促

### 基本情報

**Path：** /user/order/reminder/{id}

**Method：** GET


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

## 再注文

### 基本情報

**Path：** /user/order/repetition/{id}

**Method：** POST


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

## 注文履歴検索

### 基本情報

**Path：** /user/order/historyOrders

**Method：** GET


**API説明：**

**Query**

| パラメータ | 必須か | 例  | 備考                |
| ---------- | ------ | --- | ------------------- |
| page       | 〇     | 1   | ページ番号          |
| pageSize   | 〇     | 10  | 1ページあたりの件数 |
| status     | ×      |     | 注文状態            |

**Response Body**

| フィールド名             | 型          | 必須か | 備考 | その他          |
| ------------------------ | ----------- | ------ | ---- | --------------- |
| code                     | number      | ×      |      |                 |
| msg                      | null        | ×      |      |                 |
| data                     | object      | ×      |      |                 |
| ├─ total                 | number      | ×      |      |                 |
| ├─ records               | object []   | ×      |      | item 型: object |
| ├─ id                    | number      | ×      |      |                 |
| ├─ number                | string      | ×      |      |                 |
| ├─ status                | number      | ×      |      |                 |
| ├─ userId                | number      | ×      |      |                 |
| ├─ addressBookId         | number      | ×      |      |                 |
| ├─ orderTime             | string      | ×      |      |                 |
| ├─ checkoutTime          | string      | ×      |      |                 |
| ├─ payMethod             | number      | ×      |      |                 |
| ├─ payStatus             | number      | ×      |      |                 |
| ├─ amount                | number      | ×      |      |                 |
| ├─ remark                | string      | ×      |      |                 |
| ├─ userName              | null        | ×      |      |                 |
| ├─ phone                 | string      | ×      |      |                 |
| ├─ address               | string      | ×      |      |                 |
| ├─ consignee             | string      | ×      |      |                 |
| ├─ cancelReason          | null        | ×      |      |                 |
| ├─ rejectionReason       | null        | ×      |      |                 |
| ├─ cancelTime            | null        | ×      |      |                 |
| ├─ estimatedDeliveryTime | string      | ×      |      |                 |
| ├─ deliveryStatus        | number      | ×      |      |                 |
| ├─ deliveryTime          | null        | ×      |      |                 |
| ├─ packAmount            | number      | ×      |      |                 |
| ├─ tablewareNumber       | number      | ×      |      |                 |
| ├─ tablewareStatus       | number      | ×      |      |                 |
| ├─ orderDetailList       | object []   | ×      |      | item 型: object |
| ├─ id                    | number      | 〇     |      |                 |
| ├─ name                  | string      | 〇     |      |                 |
| ├─ orderId               | number      | 〇     |      |                 |
| ├─ dishId                | number      | 〇     |      |                 |
| ├─ setmealId             | null        | 〇     |      |                 |
| ├─ dishFlavor            | null,string | 〇     |      |                 |
| ├─ number                | number      | 〇     |      |                 |
| ├─ amount                | number      | 〇     |      |                 |
| ├─ image                 | string      | 〇     |      |                 |

## 注文キャンセル

### 基本情報

**Path：** /user/order/cancel/{id}

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

## 注文詳細検索

### 基本情報

**Path：** /user/order/orderDetail/{id}

**Method：** GET


**API説明：**

**パスパラメータ**

| パラメータ | 例  | 備考   |
| ---------- | --- | ------ |
| id         | 101 | 注文id |

**Response Body**

| フィールド名             | 型        | 必須か | 備考 | その他            |
| ------------------------ | --------- | ------ | ---- | ----------------- |
| code                     | integer   | ×      |      | format: int32     |
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

## ユーザー注文

### 基本情報

**Path：** /user/order/submit

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名          | 型      | 必須か | 備考                                                             | その他        |
| --------------------- | ------- | ------ | ---------------------------------------------------------------- | ------------- |
| addressBookId         | integer | 〇     | アドレス帳id                                                     | format: int64 |
| amount                | number  | 〇     | 合計金額                                                         |               |
| deliveryStatus        | integer | 〇     | 配送状態：1は即時配送、0は時間指定                               | format: int32 |
| estimatedDeliveryTime | string  | 〇     | 予定配達時刻                                                     |               |
| packAmount            | integer | 〇     | 包装代                                                           | format: int32 |
| payMethod             | integer | 〇     | 支払方法                                                         | format: int32 |
| remark                | string  | 〇     | 備考                                                             |               |
| tablewareNumber       | integer | 〇     | カトラリー数                                                     | format: int32 |
| tablewareStatus       | integer | 〇     | カトラリー数の状態：1は料理数に応じて提供、0は具体的な数量を選択 | format: int32 |

**Response Body**

| フィールド名   | 型      | 必須か | 備考     | その他            |
| -------------- | ------- | ------ | -------- | ----------------- |
| code           | integer | 〇     |          | format: int32     |
| data           | object  | 〇     |          |                   |
| ├─ id          | integer | 〇     | 注文id   | format: int64     |
| ├─ orderAmount | number  | 〇     | 注文金額 |                   |
| ├─ orderNumber | string  | 〇     | 注文番号 |                   |
| ├─ orderTime   | string  | 〇     | 注文時刻 | format: date-time |
| msg            | string  | ×      |          |                   |

## 注文支払い

### 基本情報

**Path：** /user/order/payment

**Method：** PUT


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考     | その他        |
| ------------ | ------- | ------ | -------- | ------------- |
| orderNumber  | string  | 〇     | 注文番号 |               |
| payMethod    | integer | 〇     | 支払方法 | format: int32 |

**Response Body**

| フィールド名             | 型      | 必須か | 備考         | その他            |
| ------------------------ | ------- | ------ | ------------ | ----------------- |
| code                     | integer | 〇     |              | format: int32     |
| data                     | object  | 〇     |              |                   |
| ├─ estimatedDeliveryTime | string  | 〇     | 予定配達時刻 | format: date-time |
| msg                      | string  | ×      |              |                   |

# C側-ショッピングカートAPI

## カート内商品の削除

### 基本情報

**Path：** /user/shoppingCart/sub

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考             | その他        |
| ------------ | ------- | ------ | ---------------- | ------------- |
| dishFlavor   | string  | ×      | 味               |               |
| dishId       | integer | ×      | 料理id           | format: int64 |
| setmealId    | integer | ×      | セットメニューid | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## カート確認

### 基本情報

**Path：** /user/shoppingCart/list

**Method：** GET


**API説明：**

**Response Body**

| フィールド名  | 型        | 必須か | 備考 | その他            |
| ------------- | --------- | ------ | ---- | ----------------- |
| code          | integer   | 〇     |      | format: int32     |
| data          | object [] | ×      |      | item 型: object   |
| ├─ amount     | number    | ×      |      |                   |
| ├─ createTime | string    | ×      |      | format: date-time |
| ├─ dishFlavor | string    | ×      |      |                   |
| ├─ dishId     | integer   | ×      |      | format: int64     |
| ├─ id         | integer   | ×      |      | format: int64     |
| ├─ image      | string    | ×      |      |                   |
| ├─ name       | string    | ×      |      |                   |
| ├─ number     | integer   | ×      |      | format: int32     |
| ├─ setmealId  | integer   | ×      |      | format: int64     |
| ├─ userId     | integer   | ×      |      | format: int64     |
| msg           | string    | ×      |      |                   |

## カート追加

### 基本情報

**Path：** /user/shoppingCart/add

**Method：** POST


**API説明：**

**Headers**

| ヘッダー名   | 設定値           | 必須か | 例  | 備考 |
| ------------ | ---------------- | ------ | --- | ---- |
| Content-Type | application/json | 〇     |     |      |

**Request Body**

| フィールド名 | 型      | 必須か | 備考             | その他        |
| ------------ | ------- | ------ | ---------------- | ------------- |
| dishFlavor   | string  | ×      | 味               |               |
| dishId       | integer | ×      | 料理id           | format: int64 |
| setmealId    | integer | ×      | セットメニューid | format: int64 |

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |

## カートクリア

### 基本情報

**Path：** /user/shoppingCart/clean

**Method：** DELETE


**API説明：**

**Response Body**

| フィールド名 | 型      | 必須か | 備考 | その他        |
| ------------ | ------- | ------ | ---- | ------------- |
| code         | integer | 〇     |      | format: int32 |
| data         | string  | ×      |      |               |
| msg          | string  | ×      |      |               |