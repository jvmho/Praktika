#!/usr/bin/env python
"""
Скрипт инициализации базы данных
Удаляет старые таблицы и создает новые в правильном порядке
"""

import os
from sqlalchemy import text
from dotenv import load_dotenv
from database import engine

load_dotenv()

def init_db():
    print("Инициализация базы данных...")
    
    with engine.begin() as conn:
        try:
            # Удаление всех таблиц (в обратном порядке зависимостей)
            print("Удаление старых таблиц...")
            tables_to_drop = [
                "Reservation", "OrderItem", "CartItem", "Order", "Cart",
                "Stock", "Warehouse", "Batch", "Supply", "Supplier",
                "Drug", "DrugType", "UserDiscount", "User", "Manufacturer", "Role"
            ]
            
            for table in tables_to_drop:
                try:
                    conn.execute(text(f'DROP TABLE IF EXISTS "{table}" CASCADE'))
                    print(f"  ✓ Удалена таблица {table}")
                except Exception as e:
                    print(f"  ✗ Ошибка удаления {table}: {e}")
            
            # Создание таблиц в правильном порядке
            print("\nСоздание новых таблиц...")
            
            # 1. Роли
            conn.execute(text("""
                CREATE TABLE "Role" (
                    "roleID" SERIAL PRIMARY KEY,
                    "userStatus" VARCHAR(50) NOT NULL
                )
            """))
            print("  ✓ Создана таблица Role")
            
            # 2. Производители
            conn.execute(text("""
                CREATE TABLE "Manufacturer" (
                    "manufacturerID" SERIAL PRIMARY KEY,
                    "name" VARCHAR(255) NOT NULL,
                    "country" VARCHAR(100),
                    "contactNumber" VARCHAR(50)
                )
            """))
            print("  ✓ Создана таблица Manufacturer")
            
            # 3. Типы препаратов
            conn.execute(text("""
                CREATE TABLE "DrugType" (
                    "typeID" SERIAL PRIMARY KEY,
                    "name" VARCHAR(100) NOT NULL,
                    "parentID" INT REFERENCES "DrugType"("typeID")
                )
            """))
            print("  ✓ Создана таблица DrugType")
            
            # 4. Пользователи
            conn.execute(text("""
                CREATE TABLE "User" (
                    "userID" SERIAL PRIMARY KEY,
                    "login" VARCHAR(100) UNIQUE NOT NULL,
                    "password" VARCHAR(255) NOT NULL,
                    "roleID" INT REFERENCES "Role"("roleID"),
                    "name" VARCHAR(150) NOT NULL
                )
            """))
            print("  ✓ Создана таблица User")
            
            # 5. Скидки
            conn.execute(text("""
                CREATE TABLE "UserDiscount" (
                    "discountID" SERIAL PRIMARY KEY,
                    "userID" INT NOT NULL REFERENCES "User"("userID") ON DELETE CASCADE,
                    "percent" INT CHECK (percent >= 0 AND percent <= 100),
                    "validFrom" DATE NOT NULL,
                    "validTo" DATE NOT NULL
                )
            """))
            print("  ✓ Создана таблица UserDiscount")
            
            # 6. Препараты
            conn.execute(text("""
                CREATE TABLE "Drug" (
                    "drugID" SERIAL PRIMARY KEY,
                    "name" VARCHAR(255) NOT NULL,
                    "category" VARCHAR(100),
                    "description" TEXT,
                    "INN" VARCHAR(255),
                    "typeID" INT REFERENCES "DrugType"("typeID"),
                    "dose" VARCHAR(100),
                    "manufacturerID" INT REFERENCES "Manufacturer"("manufacturerID"),
                    "barcode" BIGINT
                )
            """))
            print("  ✓ Создана таблица Drug")
            
            # 7. Поставщики
            conn.execute(text("""
                CREATE TABLE "Supplier" (
                    "supplierID" SERIAL PRIMARY KEY,
                    "name" VARCHAR(255) NOT NULL,
                    "number" VARCHAR(50)
                )
            """))
            print("  ✓ Создана таблица Supplier")
            
            # 8. Поставки
            conn.execute(text("""
                CREATE TABLE "Supply" (
                    "supplyID" SERIAL PRIMARY KEY,
                    "supplierID" INT REFERENCES "Supplier"("supplierID"),
                    "supplyDate" DATE,
                    "status" VARCHAR(50)
                )
            """))
            print("  ✓ Создана таблица Supply")
            
            # 9. Партии
            conn.execute(text("""
                CREATE TABLE "Batch" (
                    "batchID" SERIAL PRIMARY KEY,
                    "drugID" INT REFERENCES "Drug"("drugID"),
                    "supplyID" INT REFERENCES "Supply"("supplyID"),
                    "number" INT,
                    "shelfLife" DATE,
                    "arrivalDate" DATE,
                    "price" INT
                )
            """))
            print("  ✓ Создана таблица Batch")
            
            # 10. Склады
            conn.execute(text("""
                CREATE TABLE "Warehouse" (
                    "warehouseID" SERIAL PRIMARY KEY,
                    "name" VARCHAR(255) NOT NULL,
                    "address" TEXT
                )
            """))
            print("  ✓ Создана таблица Warehouse")
            
            # 11. Сток
            conn.execute(text("""
                CREATE TABLE "Stock" (
                    "stockID" SERIAL PRIMARY KEY,
                    "warehouseID" INT REFERENCES "Warehouse"("warehouseID"),
                    "batchID" INT REFERENCES "Batch"("batchID"),
                    "amount" INT NOT NULL CHECK (amount >= 0)
                )
            """))
            print("  ✓ Создана таблица Stock")
            
            # 12. Корзины
            conn.execute(text("""
                CREATE TABLE "Cart" (
                    "cartID" SERIAL PRIMARY KEY,
                    "userID" INT REFERENCES "User"("userID"),
                    "createdAt" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    "updatedAt" TIMESTAMP,
                    "status" VARCHAR(50)
                )
            """))
            print("  ✓ Создана таблица Cart")
            
            # 13. Товары в корзине
            conn.execute(text("""
                CREATE TABLE "CartItem" (
                    "cartItemID" SERIAL PRIMARY KEY,
                    "cartID" INT REFERENCES "Cart"("cartID") ON DELETE CASCADE,
                    "drugID" INT REFERENCES "Drug"("drugID"),
                    "quantity" INT NOT NULL CHECK (quantity > 0)
                )
            """))
            print("  ✓ Создана таблица CartItem")
            
            # 14. Заказы
            conn.execute(text("""
                CREATE TABLE "Order" (
                    "orderID" SERIAL PRIMARY KEY,
                    "userID" INT REFERENCES "User"("userID"),
                    "discount" INT CHECK (discount >= 0 AND discount <= 100),
                    "createdAt" TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    "totalAmount" INT,
                    "status" VARCHAR(50),
                    "deliveryType" VARCHAR(50)
                )
            """))
            print("  ✓ Создана таблица Order")
            
            # 15. Товары в заказе
            conn.execute(text("""
                CREATE TABLE "OrderItem" (
                    "orderItemID" SERIAL PRIMARY KEY,
                    "orderID" INT REFERENCES "Order"("orderID") ON DELETE CASCADE,
                    "batchID" INT REFERENCES "Batch"("batchID"),
                    "quantity" INT NOT NULL CHECK (quantity > 0),
                    "price" INT NOT NULL
                )
            """))
            print("  ✓ Создана таблица OrderItem")
            
            # 16. Резервации
            conn.execute(text("""
                CREATE TABLE "Reservation" (
                    "reservationID" SERIAL PRIMARY KEY,
                    "batchID" INT REFERENCES "Batch"("batchID"),
                    "orderID" INT REFERENCES "Order"("orderID"),
                    "quantity" INT NOT NULL CHECK (quantity > 0),
                    "expiresAt" DATE
                )
            """))
            print("  ✓ Создана таблица Reservation")
            
            print("\n✓ База данных успешно инициализирована!")
            
        except Exception as e:
            print(f"\n✗ Ошибка: {e}")
            raise

if __name__ == "__main__":
    init_db()
