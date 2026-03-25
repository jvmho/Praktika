-- =========================
-- ИНИЦИАЛИЗАЦИЯ БД
-- PostgreSQL
-- =========================

-- ==== Регистрация и права доступа ====

-- Роли пользователей
CREATE TABLE Role (
    roleID      SERIAL PRIMARY KEY,
    userStatus  VARCHAR(50) NOT NULL
);

-- Пользователи
CREATE TABLE "User" (
    userID      SERIAL PRIMARY KEY,
    login       VARCHAR(100) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    roleID      INT REFERENCES Role(roleID),
    name        VARCHAR(150) NOT NULL
);

-- Скидки пользователей
CREATE TABLE UserDiscount (
    discountID  SERIAL PRIMARY KEY,
    userID      INT REFERENCES "User"(userID) ON DELETE CASCADE,
    percent     INT CHECK (percent >= 0 AND percent <= 100),
    validFrom   DATE NOT NULL,
    validTo     DATE NOT NULL
);

-- ==== Товар и информация о нем ====

-- Производители
CREATE TABLE Manufacturer (
    manufacturerID  SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    country         VARCHAR(100),
    contactNumber   VARCHAR(50)
);

-- Типы препаратов
CREATE TABLE DrugType (
    typeID   SERIAL PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    parentID INT REFERENCES DrugType(typeID)
);

-- Препараты
CREATE TABLE Drug (
    drugID          SERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    category        VARCHAR(100),
    description     TEXT,
    INN             VARCHAR(255),
    typeID          INT REFERENCES DrugType(typeID),
    dose            VARCHAR(100),
    manufacturerID  INT REFERENCES Manufacturer(manufacturerID),
    barcode         BIGINT
);

-- ==== Склад и кол-во товара ====

-- Поставщики
CREATE TABLE Supplier (
    supplierID  SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    number      VARCHAR(50)
);

-- Поставки
CREATE TABLE Supply (
    supplyID    SERIAL PRIMARY KEY,
    supplierID  INT REFERENCES Supplier(supplierID),
    supplyDate  DATE,
    status      VARCHAR(50)
);

-- Партии
CREATE TABLE Batch (
    batchID     SERIAL PRIMARY KEY,
    drugID      INT REFERENCES Drug(drugID),
    supplyID    INT REFERENCES Supply(supplyID),
    number      INT,
    shelfLife   DATE,
    arrivalDate DATE,
    price       INT
);

-- Склады
CREATE TABLE Warehouse (
    warehouseID SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    address     TEXT
);

-- Остатки на складе
CREATE TABLE Stock (
    stockID     SERIAL PRIMARY KEY,
    warehouseID INT REFERENCES Warehouse(warehouseID),
    batchID     INT REFERENCES Batch(batchID),
    amount      INT NOT NULL CHECK (amount >= 0)
);

-- ==== Заказ товаров и корзина ====

-- Корзины
CREATE TABLE Cart (
    cartID      SERIAL PRIMARY KEY,
    userID      INT REFERENCES "User"(userID),
    createdAt   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt   TIMESTAMP,
    status      VARCHAR(50)
);

-- Позиции корзины
CREATE TABLE CartItem (
    cartItemID  SERIAL PRIMARY KEY,
    cartID      INT REFERENCES Cart(cartID) ON DELETE CASCADE,
    drugID      INT REFERENCES Drug(drugID),
    quantity    INT NOT NULL CHECK (quantity > 0)
);

-- Заказы
CREATE TABLE Order (
    orderID      SERIAL PRIMARY KEY,
    userID       INT REFERENCES "User"(userID),
    discount     INT CHECK (discount >= 0 AND discount <= 100),
    createdAt    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    totalAmount  INT,
    status       VARCHAR(50),
    deliveryType VARCHAR(50)
);

-- Позиции заказа
CREATE TABLE OrderItem (
    orderItemID SERIAL PRIMARY KEY,
    orderID     INT REFERENCES "Order"(orderID) ON DELETE CASCADE,
    batchID     INT REFERENCES Batch(batchID),
    quantity    INT NOT NULL CHECK (quantity > 0),
    price       INT NOT NULL
);

-- Резервации
CREATE TABLE Reservation (
    reservationID SERIAL PRIMARY KEY,
    batchID       INT REFERENCES Batch(batchID),
    orderID       INT REFERENCES "Order"(orderID),
    quantity      INT NOT NULL CHECK (quantity > 0),
    expiresAt     DATE
);
