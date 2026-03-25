package main

import (
	"fmt"
	"time"

	"github.com/go-pg/pg/v10"
	"github.com/go-pg/pg/v10/orm"
)

/*
   --=== MODELS ===--
*/

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

/*
   --=== MAIN ===--
*/

func main() {
	db := pg.Connect(&pg.Options{
		User: "postgres",
	})
	defer db.Close()

	err := createSchema(db)
	if err != nil {
		panic(err)
	}

	err = seed(db)
	if err != nil {
		panic(err)
	}

	err = testQuery(db)
	if err != nil {
		panic(err)
	}
}

/*
   --=== SCHEMA ===--
*/

func createSchema(db *pg.DB) error {
	models := []interface{}{
		(*Role)(nil),
		(*User)(nil),
		(*UserDiscount)(nil),
		(*Manufacturer)(nil),
		(*DrugType)(nil),
		(*Drug)(nil),
		(*Supplier)(nil),
		(*Supply)(nil),
		(*Batch)(nil),
		(*Warehouse)(nil),
		(*Stock)(nil),
		(*Cart)(nil),
		(*CartItem)(nil),
		(*Order)(nil),
		(*OrderItem)(nil),
		(*Reservation)(nil),
	}

	for _, model := range models {
		err := db.Model(model).CreateTable(&orm.CreateTableOptions{
			Temp: true,
		})
		if err != nil {
			return err
		}
	}
	return nil
}

/*
   --=== DATA ===--
*/

func seed(db *pg.DB) error {
	role := &Role{UserStatus: "admin"}
	_, _ = db.Model(role).Insert()

	user := &User{
		Login:  "john",
		Name:   "John Doe",
		RoleId: role.Id,
	}
	_, _ = db.Model(user).Insert()

	manufacturer := &Manufacturer{Name: "Bayer", Country: "Germany"}
	_, _ = db.Model(manufacturer).Insert()

	dt := &DrugType{Name: "Antibiotic"}
	_, _ = db.Model(dt).Insert()

	drug := &Drug{
		Name:           "Amoxicillin",
		TypeId:         dt.Id,
		ManufacturerId: manufacturer.Id,
	}
	_, _ = db.Model(drug).Insert()

	order := &Order{
		UserId:    user.Id,
		CreatedAt: time.Now(),
	}
	_, _ = db.Model(order).Insert()

	batch := &Batch{
		DrugId:      drug.Id,
		SupplyId:    0,
		Number:      "B001",
		ArrivalDate: time.Now(),
		Price:       100,
	}
	_, _ = db.Model(batch).Insert()

	item := &OrderItem{
		OrderId:  order.Id,
		BatchId:  batch.Id,
		Quantity: 2,
		Price:    100,
	}
	_, _ = db.Model(item).Insert()

	return nil
}

/*
   --=== QUERY ===--
*/

func testQuery(db *pg.DB) error {
	var items []OrderItem

	err := db.Model(&items).
		Relation("Order.User.Role").
		Relation("Batch.Drug.Type").
		Relation("Batch.Drug.Manufacturer").
		Select()

	if err != nil {
		return err
	}

	for _, i := range items {
		fmt.Println(
			i.Order.User.Login,
			i.Order.User.Role.UserStatus,
			i.Batch.Drug.Name,
			i.Batch.Drug.Type.Name,
		)
	}

	return nil
}
