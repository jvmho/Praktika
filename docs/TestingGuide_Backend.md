

# Полное руководство по тестированию бэкенда

## Для тестировщиков проекта «Pharmacy API»

---

# Часть 1. Подготовка рабочего окружения

## 1.1 Установка Git

### Windows
1. Скачайте с [git-scm.com](https://git-scm.com/download/win)
2. Установите, оставляя все настройки по умолчанию
3. Проверьте:
```bash
git --version
# git version 2.47.1
```

### macOS
```bash
brew install git
```

### Linux (Ubuntu/Debian)
```bash
sudo apt update && sudo apt install git -y
```

---

## 1.2 Установка Docker

### Windows / macOS
1. Скачайте Docker Desktop: [docker.com/products/docker-desktop](https://www.docker.com/products/docker-desktop/)
2. Установите и перезагрузите компьютер
3. Запустите Docker Desktop
4. Дождитесь, пока иконка в трее станет стабильной (не анимируется)

### Linux (Ubuntu)
```bash
# Установка Docker
sudo apt update
sudo apt install docker.io docker-compose-v2 -y

# Добавить себя в группу docker (чтобы не писать sudo)
sudo usermod -aG docker $USER

# Перелогиньтесь или выполните:
newgrp docker
```

### Проверка
```bash
docker --version
# Docker version 27.x.x

docker compose version
# Docker Compose version v2.x.x
```

---

## 1.3 Установка PostgreSQL (для локальной работы с БД)

> Нужен для прямой проверки данных в базе, написания SQL-запросов.

### Windows
1. Скачайте с [postgresql.org/download/windows](https://www.postgresql.org/download/windows/)
2. При установке запомните пароль для пользователя `postgres`
3. Установите pgAdmin (идёт в комплекте)

### macOS
```bash
brew install postgresql@16
```

### Linux
```bash
sudo apt install postgresql-client -y
```

> **Важно:** нам нужен только **клиент** (`psql`). Сервер PostgreSQL будет работать в Docker.

---

## 1.4 Установка инструментов тестирования

### Postman (GUI для API-запросов)
1. Скачайте: [postman.com/downloads](https://www.postman.com/downloads/)
2. Установите, создайте бесплатный аккаунт

### cURL (командная строка)
- Windows: встроен в PowerShell и Git Bash
- Linux/Mac: уже установлен

### DBeaver (GUI для БД) — опционально
1. Скачайте: [dbeaver.io/download](https://dbeaver.io/download/)
2. Удобнее pgAdmin для просмотра данных

---

## 1.5 Получение проекта

```bash
# Клонировать репозиторий
git clone <ссылка-на-репозиторий>
cd <название-папки-проекта>

# Убедиться, что вы на нужной ветке
git checkout develop
git pull
```

---

# Часть 2. Запуск проекта

## 2.1 Перед запуском — освободить порты

```bash
# Проверить, занят ли порт 5432
# Windows (PowerShell):
netstat -ano | findstr :5432

# Linux/Mac:
sudo lsof -i :5432
```

Если занят — остановить локальный PostgreSQL:
```bash
# Windows: Службы → PostgreSQL → Остановить

# Linux:
sudo systemctl stop postgresql

# Mac:
brew services stop postgresql
```

## 2.2 Запуск

```bash
docker compose up --build
```

Ожидайте вывод:
```
pharmacy-db   | database system is ready to accept connections
pharmacy-api  | Server started on :8080
```

## 2.3 Проверка

```bash
# API отвечает?
curl http://localhost:8080/api/v1/drugs

# БД доступна?
docker exec -it pharmacy-db psql -U pharmacy_user -d pharmacy -c "\dt"
```

## 2.4 Остановка и перезапуск

```bash
# Остановить
docker compose down

# Остановить и удалить данные (чистый старт)
docker compose down -v
docker compose up --build
```

---

# Часть 3. Что тестировать

## 3.1 Карта тестирования

```
┌─────────────────────────────────────────────┐
│              Pharmacy API                    │
├──────────────┬──────────────────────────────┤
│  Модуль      │  Что проверяем              │
├──────────────┼──────────────────────────────┤
│  Auth        │  Регистрация, логин, JWT     │
│  Users       │  CRUD пользователей          │
│  Roles       │  Управление ролями           │
│  Drugs       │  Справочник лекарств         │
│  Stock       │  Остатки на складе           │
│  Batches     │  Партии товара               │
│  Supply      │  Поставки                    │
│  Cart        │  Корзина                     │
│  Orders      │  Заказы                      │
│  Reservation │  Бронирование                │
└──────────────┴──────────────────────────────┘
```

## 3.2 Типы тестов

| Тип | Что проверяем | Инструменты |
|-----|--------------|-------------|
| **Функциональные** | Эндпоинты работают по спецификации | Postman, cURL |
| **Негативные** | Невалидные данные, пустые поля, SQL-инъекции | Postman, cURL |
| **Авторизация** | JWT, доступ по ролям, истёкший токен | Postman |
| **Целостность БД** | Данные корректно сохраняются | psql, DBeaver |
| **Граничные** | Пустые строки, 0, отрицательные числа, MAX_INT | Postman |

---

# Часть 4. Тестирование REST API

## 4.1 Структура запроса

```
POST http://localhost:8080/api/v1/auth/register
                │              │           │
                │              │           └─ эндпоинт
                │              └─ версия API
                └─ базовый URL
```

## 4.2 Тестирование авторизации

### Тест 1: Регистрация нового пользователя (позитивный)

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test1234!",
    "email": "test@example.com"
  }'
```

**Ожидаемый результат:** Статус `201 Created`, тело содержит данные пользователя.

### Тест 2: Регистрация с существующим username (негативный)

```bash
# Повторить тот же запрос
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test1234!",
    "email": "test@example.com"
  }'
```

**Ожидаемый результат:** Статус `409 Conflict` или `400 Bad Request`.

### Тест 3: Логин

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "Test1234!"
  }'
```

**Ожидаемый результат:** Статус `200 OK`, тело содержит JWT-токен.

### Тест 4: Использование токена

```bash
# Сохраните токен из предыдущего ответа
TOKEN="eyJhbGciOi..."

# Запрос с токеном
curl -X GET http://localhost:8080/api/v1/drugs \
  -H "Authorization: Bearer $TOKEN"
```

### Тест 5: Запрос без токена к защищённому эндпоинту

```bash
curl -X GET http://localhost:8080/api/v1/orders
```

**Ожидаемый результат:** Статус `401 Unauthorized`.

### Тест 6: Невалидный токен

```bash
curl -X GET http://localhost:8080/api/v1/orders \
  -H "Authorization: Bearer invalid_token_here"
```

**Ожидаемый результат:** Статус `401 Unauthorized`.

---

## 4.3 Тестирование CRUD (на примере Drugs)

### Создание

```bash
curl -X POST http://localhost:8080/api/v1/drugs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Аспирин",
    "manufacturer": "Bayer",
    "dosage": "500mg",
    "price": 150.50
  }'
```

### Получение списка

```bash
curl -X GET http://localhost:8080/api/v1/drugs \
  -H "Authorization: Bearer $TOKEN"
```

### Получение по ID

```bash
curl -X GET http://localhost:8080/api/v1/drugs/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Обновление

```bash
curl -X PUT http://localhost:8080/api/v1/drugs/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "name": "Аспирин",
    "manufacturer": "Bayer",
    "dosage": "250mg",
    "price": 120.00
  }'
```

### Удаление

```bash
curl -X DELETE http://localhost:8080/api/v1/drugs/1 \
  -H "Authorization: Bearer $TOKEN"
```

### Получение удалённого (негативный)

```bash
curl -X GET http://localhost:8080/api/v1/drugs/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Ожидаемый результат:** `404 Not Found`.

---

## 4.4 Негативные тесты

```bash
# Пустое тело
curl -X POST http://localhost:8080/api/v1/drugs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{}'

# Отрицательная цена
curl -X POST http://localhost:8080/api/v1/drugs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "Test", "price": -100}'

# Несуществующий ID
curl -X GET http://localhost:8080/api/v1/drugs/999999 \
  -H "Authorization: Bearer $TOKEN"

# SQL-инъекция
curl -X GET "http://localhost:8080/api/v1/drugs/1;DROP TABLE drugs;" \
  -H "Authorization: Bearer $TOKEN"

# XSS в поле name
curl -X POST http://localhost:8080/api/v1/drugs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "<script>alert(1)</script>", "price": 100}'
```

---

# Часть 5. Тестирование базы данных

## 5.1 Подключение к БД

```bash
# Через Docker
docker exec -it pharmacy-db psql -U pharmacy_user -d pharmacy
```

Или через DBeaver:
```
Host:     localhost
Port:     5433       (если поменяли в compose, иначе 5432)
Database: pharmacy
User:     pharmacy_user
Password: pharmacy_secret
```

## 5.2 Полезные SQL-запросы для проверки

```sql
-- Список всех таблиц
\dt

-- Структура таблицы
\d users

-- Все пользователи
SELECT * FROM users;

-- Проверка: пароль НЕ хранится в открытом виде
SELECT username, password FROM users;
-- password должен быть хешом, НЕ читаемым текстом

-- Проверка уникальности username
SELECT username, COUNT(*)
FROM users
GROUP BY username
HAVING COUNT(*) > 1;
-- Должно вернуть 0 строк

-- Проверка внешних ключей
-- (заказ ссылается на существующего пользователя)
SELECT o.*
FROM orders o
LEFT JOIN users u ON o.user_id = u.id
WHERE u.id IS NULL;
-- Должно вернуть 0 строк (нет "висячих" заказов)

-- Проверка остатков (не отрицательные)
SELECT * FROM stock WHERE quantity < 0;
-- Должно вернуть 0 строк
```

## 5.3 Проверка целостности после API-операций

```bash
# 1. Создать лекарство через API
curl -X POST http://localhost:8080/api/v1/drugs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"name": "Тестовый препарат", "price": 200}'

# 2. Проверить, что оно появилось в БД
docker exec -it pharmacy-db psql -U pharmacy_user -d pharmacy \
  -c "SELECT * FROM drugs WHERE name = 'Тестовый препарат';"
```

---

# Часть 6. Автоматизация с помощью скриптов

## 6.1 Bash-скрипт для smoke-теста

Создайте файл `tests/smoke_test.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api/v1"
PASSED=0
FAILED=0

# Цвета
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

# Функция проверки
assert_status() {
    local test_name="$1"
    local expected_status="$2"
    local method="$3"
    local url="$4"
    local data="$5"
    local extra_headers="$6"

    if [ -n "$data" ]; then
        actual_status=$(curl -s -o /dev/null -w "%{http_code}" \
            -X "$method" "$url" \
            -H "Content-Type: application/json" \
            $extra_headers \
            -d "$data")
    else
        actual_status=$(curl -s -o /dev/null -w "%{http_code}" \
            -X "$method" "$url" \
            $extra_headers)
    fi

    if [ "$actual_status" -eq "$expected_status" ]; then
        echo -e "${GREEN}✅ PASS${NC}: $test_name (HTTP $actual_status)"
        ((PASSED++))
    else
        echo -e "${RED}❌ FAIL${NC}: $test_name (ожидали $expected_status, получили $actual_status)"
        ((FAILED++))
    fi
}

echo "========================================="
echo "  Smoke Test — Pharmacy API"
echo "========================================="
echo ""

# --- Auth ---
echo "--- Авторизация ---"

# Регистрация
assert_status \
    "Регистрация нового пользователя" \
    201 \
    "POST" \
    "$BASE_URL/auth/register" \
    '{"username":"smoketest_'$RANDOM'","password":"Test1234!","email":"smoke'$RANDOM'@test.com"}'

# Логин
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/auth/login" \
    -H "Content-Type: application/json" \
    -d '{"username":"testuser","password":"Test1234!"}')

TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

if [ -n "$TOKEN" ]; then
    echo -e "${GREEN}✅ PASS${NC}: Логин — токен получен"
    ((PASSED++))
else
    echo -e "${RED}❌ FAIL${NC}: Логин — токен не получен"
    ((FAILED++))
    echo "Ответ: $LOGIN_RESPONSE"
    echo ""
    echo "Дальнейшие тесты могут не работать без токена"
fi

AUTH_HEADER="-H \"Authorization: Bearer $TOKEN\""

# --- Drugs ---
echo ""
echo "--- Лекарства ---"

assert_status \
    "Получение списка лекарств" \
    200 \
    "GET" \
    "$BASE_URL/drugs" \
    "" \
    "-H \"Authorization: Bearer $TOKEN\""

assert_status \
    "Несуществующее лекарство" \
    404 \
    "GET" \
    "$BASE_URL/drugs/999999" \
    "" \
    "-H \"Authorization: Bearer $TOKEN\""

# --- Без авторизации ---
echo ""
echo "--- Без авторизации ---"

assert_status \
    "Заказы без токена → 401" \
    401 \
    "GET" \
    "$BASE_URL/orders"

# --- Итоги ---
echo ""
echo "========================================="
echo "  Результаты: ✅ $PASSED пройдено, ❌ $FAILED провалено"
echo "========================================="

exit $FAILED
```

### Запуск

```bash
chmod +x tests/smoke_test.sh
./tests/smoke_test.sh
```

---

## 6.2 Скрипт проверки БД

Создайте файл `tests/db_check.sh`:

```bash
#!/bin/bash

DB_CONTAINER="pharmacy-db"
DB_USER="pharmacy_user"
DB_NAME="pharmacy"

PASSED=0
FAILED=0

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m'

run_query() {
    docker exec -it "$DB_CONTAINER" psql -U "$DB_USER" -d "$DB_NAME" -t -A -c "$1" 2>/dev/null
}

assert_query() {
    local test_name="$1"
    local query="$2"
    local expected="$3"

    result=$(run_query "$query" | tr -d '[:space:]')

    if [ "$result" = "$expected" ]; then
        echo -e "${GREEN}✅ PASS${NC}: $test_name"
        ((PASSED++))
    else
        echo -e "${RED}❌ FAIL${NC}: $test_name (ожидали '$expected', получили '$result')"
        ((FAILED++))
    fi
}

echo "========================================="
echo "  DB Check — Pharmacy Database"
echo "========================================="
echo ""

# Таблицы существуют
assert_query \
    "Таблица users существует" \
    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'users');" \
    "t"

assert_query \
    "Таблица drugs существует" \
    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'drugs');" \
    "t"

assert_query \
    "Таблица orders существует" \
    "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'orders');" \
    "t"

# Целостность данных
assert_query \
    "Нет дублей username" \
    "SELECT COUNT(*) FROM (SELECT username FROM users GROUP BY username HAVING COUNT(*) > 1) t;" \
    "0"

assert_query \
    "Нет отрицательных остатков" \
    "SELECT COUNT(*) FROM stock WHERE quantity < 0;" \
    "0"

assert_query \
    "Пароли захешированы (не plaintext)" \
    "SELECT COUNT(*) FROM users WHERE LENGTH(password) < 20;" \
    "0"

echo ""
echo "========================================="
echo "  Результаты: ✅ $PASSED пройдено, ❌ $FAILED провалено"
echo "========================================="

exit $FAILED
```

---

# Часть 7. Документирование тестов

## 7.1 Шаблон тест-кейса

```markdown
### TC-001: Регистрация нового пользователя

| Поле            | Значение                                    |
|-----------------|---------------------------------------------|
| **Модуль**      | Auth                                        |
| **Приоритет**   | Высокий                                     |
| **Предусловия** | API запущен, пользователь не существует      |

**Шаги:**
1. Отправить POST-запрос на `/api/v1/auth/register`
2. Тело запроса:
   ```json
   {
     "username": "newuser",
     "password": "Secure123!",
     "email": "new@example.com"
   }
   ```

**Ожидаемый результат:**
- HTTP-статус: `201 Created`
- Тело ответа содержит `id`, `username`
- В БД появилась запись в таблице `users`
- Пароль в БД захеширован

**Фактический результат:** _заполняется при прохождении_

**Статус:** ✅ Пройден / ❌ Провален
```

## 7.2 Таблица тест-кейсов

```markdown
| ID      | Модуль | Тест                           | Метод  | Эндпоинт               | Статус |
|---------|--------|--------------------------------|--------|-------------------------|--------|
| TC-001  | Auth   | Регистрация                    | POST   | /auth/register          | ✅     |
| TC-002  | Auth   | Регистрация дубль              | POST   | /auth/register          | ✅     |
| TC-003  | Auth   | Логин                          | POST   | /auth/login             | ❌     |
| TC-004  | Auth   | Логин неверный пароль          | POST   | /auth/login             | ✅     |
| TC-005  | Auth   | Запрос без токена              | GET    | /orders                 | ✅     |
| TC-006  | Auth   | Запрос с невалидным токеном    | GET    | /orders                 | ✅     |
| TC-007  | Drugs  | Создание лекарства             | POST   | /drugs                  | ✅     |
| TC-008  | Drugs  | Получение списка               | GET    | /drugs                  | ✅     |
| TC-009  | Drugs  | Получение по ID                | GET    | /drugs/:id              | ✅     |
| TC-010  | Drugs  | Обновление                     | PUT    | /drugs/:id              | ✅     |
| TC-011  | Drugs  | Удаление                       | DELETE | /drugs/:id              | ✅     |
| TC-012  | Drugs  | Несуществующий ID              | GET    | /drugs/999999           | ✅     |
| TC-013  | Drugs  | Пустое тело                    | POST   | /drugs                  | ✅     |
| TC-014  | Drugs  | Отрицательная цена             | POST   | /drugs                  | ❌     |
| TC-015  | DB     | Пароли захешированы            | SQL    | —                       | ✅     |
| TC-016  | DB     | Нет дублей username            | SQL    | —                       | ✅     |
| TC-017  | DB     | Нет отрицательных остатков     | SQL    | —                       | ✅     |
```

---

# Часть 8. Баг-репорты

## 8.1 Шаблон баг-репорта

```markdown
## BUG-001: Сервер принимает отрицательную цену лекарства

| Поле              | Значение                           |
|-------------------|------------------------------------|
| **Серьёзность**   | Major                              |
| **Приоритет**     | High                               |
| **Модуль**        | Drugs                              |
| **Версия**        | commit abc1234                     |
| **Окружение**     | Docker, macOS 15.2                 |
| **Автор**         | Иванов И.И.                        |
| **Дата**          | 2025-01-15                         |
| **Статус**        | Open                               |
| **Назначен**      | —                                  |

### Описание
При создании лекарства с отрицательной ценой сервер
возвращает `201 Created` вместо ошибки валидации.

### Шаги воспроизведения
1. Запустить API (`docker compose up --build`)
2. Авторизоваться, получить токен
3. Отправить запрос:

```bash
curl -X POST http://localhost:8080/api/v1/drugs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"name": "Тест", "price": -500}'
```

### Ожидаемый результат
- HTTP `400 Bad Request`
- Сообщение: `"price must be positive"`

### Фактический результат
- HTTP `201 Created`
- Лекарство создано с ценой `-500`

### Скриншот / лог
```json
{
  "id": 15,
  "name": "Тест",
  "price": -500
}
```

### Дополнительно
Проверено в БД — запись действительно создана:
```sql
SELECT * FROM drugs WHERE price < 0;
-- 1 row
```
```

## 8.2 Классификация серьёзности

| Уровень      | Описание                               | Пример                              |
|-------------|----------------------------------------|--------------------------------------|
| **Blocker** | Система не работает                    | API не запускается                   |
| **Critical**| Основной функционал сломан             | Невозможно залогиниться              |
| **Major**   | Важный функционал работает неправильно | Отрицательная цена принимается       |
| **Minor**   | Незначительная проблема                | Неинформативное сообщение об ошибке  |
| **Trivial** | Косметика                              | Опечатка в ответе API                |

---

# Часть 9. Структура папки тестировщика

```
tests/
├── smoke_test.sh           # Быстрая проверка — всё ли живо
├── db_check.sh             # Проверка целостности БД
├── test_cases/
│   ├── TC-auth.md          # Тест-кейсы авторизации
│   ├── TC-drugs.md         # Тест-кейсы лекарств
│   ├── TC-orders.md        # Тест-кейсы заказов
│   └── TC-database.md      # Тест-кейсы БД
├── bug_reports/
│   ├── BUG-001.md
│   ├── BUG-002.md
│   └── ...
├── postman/
│   └── Pharmacy_API.postman_collection.json
└── README.md               # Эта инструкция
```

---

# Часть 10. Ежедневный процесс тестирования

```
1. Обновить код
   └─ git pull

2. Пересобрать
   └─ docker compose down -v && docker compose up --build

3. Запустить smoke-тест
   └─ ./tests/smoke_test.sh

4. Запустить проверку БД
   └─ ./tests/db_check.sh

5. Прогнать тест-кейсы по задаче
   └─ Вручную или через Postman

6. Если нашёл баг
   └─ Создать файл в tests/bug_reports/BUG-XXX.md
   └─ Завести задачу в трекере

7. Обновить таблицу тест-кейсов
   └─ Отметить статус: ✅ / ❌
```
