import os
from sqlalchemy import create_engine, event
from sqlalchemy.orm import sessionmaker, declarative_base
from dotenv import load_dotenv

load_dotenv()

DATABASE_URL = os.getenv("DATABASE_URL")

# Если DATABASE_URL не установлен, используем SQLite
if not DATABASE_URL:
    DATABASE_URL = "sqlite:///./test.db"

print(f"Подключение к БД: {DATABASE_URL.split('@')[0]}..." if '@' in DATABASE_URL else f"Подключение к БД: {DATABASE_URL}")

try:
    if "postgresql" in DATABASE_URL:
        engine = create_engine(DATABASE_URL, pool_pre_ping=True)
    else:
        engine = create_engine(DATABASE_URL, connect_args={"check_same_thread": False})
    
    # Проверка подключения
    with engine.connect() as conn:
        print("✓ Подключение к БД успешно")
except Exception as e:
    print(f"✗ Ошибка подключения к БД: {e}")
    raise

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
