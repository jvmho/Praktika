# REST API. Схема эндпоинтов.

`/api/v1` - Корневой URL

---

### Аутентификация

**POST `/login/auth`:**

```json
{
  "login": "user1",
  "password": "secret"
}
```

---

### Пользователи и роли

**GET `/users`:**
```json
{
  "login": "user1",
  "password": "secret",
  "roleId": 2,
  "name": "John Doe"
}
```
**GET `/users/{id}`:**
```json
{
  "id": 1,
  "login": "user1",
  "name": "John Doe",
  "role": {
    "id": 2,
    "name": "admin"
  }
}
```

**POST `/users`:**
```json
{
  "login": "user1",
  "password": "secret",
  "roleId": 2,
  "name": "John Doe"
}
```

**GET `/roles`:**
```json
[
  { "id": 1, "name": "customer" },
  { "id": 2, "name": "admin" }
]
```

### Скидки

**GET `/users/{id}/discount`:**
```json
[
  {
    "id": 1,
    "percent": 10,
    "validFrom": "2026-01-01",
    "validTo": "2026-12-31"
  }
]
```

---

### Препараты

Фильтр: `/drugs?category=free&type=tablets`

**GET `/drugs`:**
```json
{
  "id": 1,
  "name": "Парацетамол",
  "category": "free",
  "inn": "Paracetamol",
  "dose": "500mg",
  "manufacturer": {
    "id": 1,
    "name": "Bayer"
  },
  "type": {
    "id": 3,
    "name": "tablets"
  }
}
```

**GET `/drugs/{id}`:**
```json
{
  "name": "Парацетамол",
  "category": "free",
  "description": "...",
  "inn": "Paracetamol",
  "typeId": 3,
  "dose": "500mg",
  "manufacturerId": 1,
  "barcode": 1234567890
}
```
  
---

### Склад и партии

**GET `/batches/{id}`:**
```json
{
  "id": 10,
  "drugId": 1,
  "number": 555,
  "price": 120,
  "shelfLife": "2027-01-01",
  "stock": [
    {
      "warehouseId": 1,
      "amount": 50
    }
  ]
}
```

**GET `/stock?drugId=1`:**
```json
[
  {
    "warehouse": {
      "id": 1,
      "name": "Основной склад"
    },
    "batchId": 10,
    "amount": 50
  }
]
```

---

### Корзина

**GET `/cart`:**
```json
{
  "id": 1,
  "status": "active",
  "items": [
    {
      "drugId": 1,
      "name": "Парацетамол",
      "quantity": 2
    }
  ]
}
```

**POST `/cart/item**
```json
{
  "drugId": 1,
  "quantity": 2
}
```

**DELETE `/cart/items/{id}`**

---

### Заказы

**POST `/orders`**
```json
{
  "deliveryType": "courier"
}
```
**Ответ:**
```json
{
  "id": 100,
  "status": "created",
  "totalAmount": 240
}
```

**GET `/orders/{id}`:**
```json
{
  "id": 100,
  "status": "paid",
  "deliveryType": "courier",
  "items": [
    {
      "drug": "Парацетамол",
      "quantity": 2,
      "price": 120
    }
  ]
}
```

**PATCH `/orders/{id}`:**
```json
{
  "status": "shipped"
}
```

### Резервации

**POST `/reservations`**
```json
{
  "orderId": 100,
  "batchId": 10,
  "quantity": 2,
  "expiresAt": "2026-03-30"
}
```

### Потавки

**GET `/supplies`:**
```json
[
  {
    "id": 1,
    "supplier": {
      "id": 2,
      "name": "ФармПоставщик"
    },
    "supplyDate": "2026-03-25",
    "status": "delivered"
  }
]
```

**POST `/supplies`:**
```json
{
  "supplierId": 1,
  "supplyDate": "2026-03-25",
  "status": "delivered"
}
```
