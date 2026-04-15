# Database Viewer Application

## Описание
Приложение для просмотра и редактирования данных из базы данных с использованием:
- **Backend**: FastAPI (сервер)
- **Frontend**: Tkinter (настольное приложение)
- **Database**: SQLite (по умолчанию) или PostgreSQL

## Установка

### 1. Установите зависимости
```bash
pip install -r requirements.txt
```

### 2. Настройте базу данных в файле `.env`

**Для SQLite (по умолчанию):**
```
DATABASE_URL=sqlite:///./test.db
```

**Для PostgreSQL:**
```
DATABASE_URL=postgresql://user:password@localhost:5432/database_name
```

## Запуск

### Способ 1: Два отдельных терминала

**Терминал 1 - Запуск FastAPI сервера:**
```bash
uvicorn server:app --reload
```
Сервер запустится на `http://localhost:8000`

**Терминал 2 - Запуск Tkinter приложения:**
```bash
python app.py
```

### Способ 2: PowerShell скрипт для Windows
Создайте файл `start.ps1`:
```powershell
# Запуск сервера в фоне
Start-Process powershell -ArgumentList "-NoExit -Command & 'uvicorn server:app --reload'"

# Небольшая задержка для инициализации сервера
Start-Sleep -Seconds 3

# Запуск Tkinter приложения
python app.py
```

Запустите:
```bash
.\start.ps1
```

## Использование

1. Выберите таблицу из выпадающего меню (Users, Roles, Drugs, Supplies, Orders)
2. Нажмите "Загрузить" чтобы загрузить данные
3. Используйте кнопки:
   - **Добавить** - добавить новую запись
   - **Редактировать** - изменить выбранную запись
   - **Удалить** - удалить выбранную запись

## API Endpoints

- `GET /api/v1/users` - получить всех пользователей
- `POST /api/v1/users` - создать пользователя
- `PUT /api/v1/users/{user_id}` - обновить пользователя
- `DELETE /api/v1/users/{user_id}` - удалить пользователя

Аналогичные endpoints существуют для: roles, drugs, supplies, orders

## Структура проекта

```
├── server.py          # FastAPI приложение
├── app.py            # Tkinter клиент
├── models.py         # ORM модели SQLAlchemy
├── database.py       # Конфигурация подключения к БД
├── schemas.py        # Pydantic схемы
├── requirements.txt  # Зависимости
├── .env             # Конфигурация (DATABASE_URL)
└── test.db          # SQLite база данных (создается при первом запуске)
```

## Решение проблем

### Ошибка подключения к базе данных
- Проверьте значение `DATABASE_URL` в `.env`
- Для SQLite убедитесь что папка доступна для записи
- Для PostgreSQL проверьте что сервер запущен

### Порт 8000 уже в использовании
```bash
uvicorn server:app --port 8001 --reload
```

### Модули не найдены
```bash
pip install --upgrade -r requirements.txt
```
