package data_structures

import (
	"net/http"
)

/*
    --=== Информация о пользователе ===--
                                          */

// Роль
type Role struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
}

// Скидка(личная для каждого пользователя)
type Discount struct {
	Id        int    `json:"id"`
	Percent   int    `json:"percent"`
	ValidFrom string `json:"validFrom"`
	ValidTo   string `json:"validTo"`
}

// Пользователь
type User struct {
	Id    int    `json:"id"`
	Login string `json:"login"`
	Name  string `json:"name"`
	Role  Role   `json:"role"`
}

/*
   --=== Информация о препарате ===--
                                      */

// Производитель
type Manufacturer struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
}

// Тип препарата
type Type struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
}

// Препарат
type Drug struct {
	Id           int          `json:"id"`
	Name         string       `json:"name"`
	Category     string       `json:"category"`
	Inn          string       `json:"inn"`
	Dose         string       `json:"dose"`
	Manufacturer Manufacturer `json:"manufacturer"`
	Type         Type         `json:"type"`
}

/*
   --=== Склад и партии ===--
                              */

// Остаток
type Stock struct {
	WarehouseId int `json:"warehouseId"`
	Amount      int `json:"amount"`
}

// Склад
type Warehouse struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
}

// Партия
type Batch struct {
	Id        int    `json:"id"`
	DrugId    int    `json:"drugId"`
	Number    int    `json:"number"`
	Price     int    `json:"price"`
	ShelfLife string `json:"shelfLife"`
	Stock     Stock  `json:"stock"`
}

type Supplier struct {
	Id   int    `json:"id"`
	Name string `json:"name"`
}

type Supply struct {
	Id         int      `json:"id"`
	Supplier   Supplier `json:"supplier"`
	SupplyDate string   `json:"supplyDate"`
	Status     string   `json:"status"`
} 

/*
   --=== Корзина и заказы ===--
                               */

type CartItem struct {
	DrugId   int    `json:"drugid"`
	Name     string `json:"name"`
	Quantity int    `json:"quantity"`
}

type Cart struct {
	Id     int        `json:"id"`
	Status string     `json:"status"`
	Items  []CartItem `json:"items"`
}

type OrderItem struct {
	Drug     string `json:"drug"`
	Quantity int    `json:"quantity"`
	Price    int    `json:"price"`
}

type Order struct {
	Id           int         `json:"id"`
	Status       string      `json:"status"`
	DeliveryType string      `json:"deliveryType"`
	Items        []OrderItem `json:"items"`
	TotalAmount  *int        `json:"totalAmount"`
}

type Reservation struct {
	OrderId   int    `json:"orderId"`
	BatchId   int    `json:"BatchId"`
	Quantity  int    `json:"Quantity"`
	ExpiresAt string `json:"expiresAt"`
}
