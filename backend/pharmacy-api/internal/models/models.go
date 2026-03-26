package models

import (
	"time"
)

type Role struct {
	tableName struct{} `pg:"roles"`
	Id         int
	UserStatus string
}

type User struct {
	tableName struct{} `pg:"users"`

	Id       int
	Login    string
	Password string
	Name     string

	RoleId int
	Role   *Role `pg:"rel:has-one"`
}

type UserDiscount struct {
	tableName struct{} `pg:"user_discounts"`

	Id      int
	UserId  int
	User    *User `pg:"rel:has-one"`
	Percent int

	ValidFrom time.Time
	ValidTo   time.Time
}

type Manufacturer struct {
	tableName struct{} `pg:"manufacturers"`

	Id            int
	Name          string
	Country       string
	ContactNumber string
}

type DrugType struct {
	tableName struct{} `pg:"drug_types"`

	Id       int
	Name     string
	ParentId *int
	Parent   *DrugType `pg:"rel:has-one"`
}

type Drug struct {
	tableName struct{} `pg:"drugs"`

	Id          int
	Name        string
	Category    string
	Description string
	INN         string
	Dose        string
	Barcode     int

	TypeId int
	Type   *DrugType `pg:"rel:has-one"`

	ManufacturerId int
	Manufacturer   *Manufacturer `pg:"rel:has-one"`
}

type Supplier struct {
	tableName struct{} `pg:"suppliers"`

	Id     int
	Name   string
	Number string
}

type Supply struct {
	tableName struct{} `pg:"supplies"`

	Id int

	SupplierId int
	Supplier   *Supplier `pg:"rel:has-one"`

	SupplyDate time.Time
	Status     string
}

type Batch struct {
	tableName struct{} `pg:"batches"`

	Id int

	DrugId int
	Drug   *Drug `pg:"rel:has-one"`

	SupplyId int
	Supply   *Supply `pg:"rel:has-one"`

	Number      string
	ShelfLife   time.Time
	ArrivalDate time.Time
	Price       int
}

type Warehouse struct {
	tableName struct{} `pg:"warehouses"`

	Id      int
	Name    string
	Address string
}

type Stock struct {
	tableName struct{} `pg:"stocks"`

	Id int

	WarehouseId int
	Warehouse   *Warehouse `pg:"rel:has-one"`

	BatchId int
	Batch   *Batch `pg:"rel:has-one"`

	Amount int
}

type Cart struct {
	tableName struct{} `pg:"carts"`

	Id int

	UserId int
	User   *User `pg:"rel:has-one"`

	CreatedAt time.Time
	UpdatedAt time.Time
	Status    string
}

type CartItem struct {
	tableName struct{} `pg:"cart_items"`

	Id int

	CartId int
	Cart   *Cart `pg:"rel:has-one"`

	DrugId int
	Drug   *Drug `pg:"rel:has-one"`

	Quantity int
}

type Order struct {
	tableName struct{} `pg:"orders"`

	Id int

	UserId int
	User   *User `pg:"rel:has-one"`

	Discount     int
	CreatedAt    time.Time
	TotalAmount  int
	Status       string
	DeliveryType string
}

type OrderItem struct {
	tableName struct{} `pg:"order_items"`

	Id int

	OrderId int
	Order   *Order `pg:"rel:has-one"`

	BatchId int
	Batch   *Batch `pg:"rel:has-one"`

	Quantity int
	Price    int
}

type Reservation struct {
	tableName struct{} `pg:"reservations"`

	Id int

	BatchId int
	Batch   *Batch `pg:"rel:has-one"`

	OrderId int
	Order   *Order `pg:"rel:has-one"`

	Quantity  int
	ExpiresAt time.Time
}
