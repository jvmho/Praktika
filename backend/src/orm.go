package pg_orm

import (
	"fmt"
	"github.com/go-pg/pg/v10"
    "github.com/go-pg/pg/v10/orm"
)

/*
   --=== Structures ===--
                          */
                          
type Role struct {
	RoleID     int
	UserStatus string
}

func (r Role) String() string {
	return fmt.Sprintf("Role<%d %s>", r.RoleID, r.UserStatus)
}

type User struct {
	UserId   int
	Login    string
    Password string
	RoleID   int
	Name     string	
}

func (u User) String() string {
	return fmt.Sprintf("User<%d %s %s $d %s>", u.UserID, u.Login, u.Password, u.RoleID, u.Name)
}

type UserDiscount struct {
	DiscountID int
	UserId     int
	Percent    int
	ValidFrom  string
	ValidTo    string
}

func (ud UserDiscount) String() string {
	return fmt.Sprintf("UserDiscount<%d %d %d %s %s>", ud.DiscountID, us.UserID, ud.Percent, ud.ValidFrom, ud.ValidTo)
}

type Manufacturer struct {
	ManufacturerId int
	Name           string
	Country        string
	ContactNumber  int
}

func (m Manufacturer) String() string {
	return fmt.Sprintf("Manufacturer<%d %s %s %d>", m.ManufacturerID, m.Name, m.Country, m.ContactNumber)
}

type DrugType struct {
	TypeId   int
	Name     string
	ParentId int
}

func (dt DrugType) String() string {
	return fmt.Sprintf("DrugType<%d %s %d>", dt.TypeID, dt.Name, dt.ParentID)
}

type Drug struct {
	DrugId         int
	Name           string
	Category       string
	Description    string
	INN            string
	TypeID         int
	Dose           string
	ManufacturerID int 
	Barcode        int
}

func (d.Drug) String() string {
	return fmt.Sprintf("Drug<%d %s %s %s %s %d %s %d %d>", d.DrugID, d.Name, d.Category, d.Description, d.INN, d.TypeID, d.Dose, d.ManufacturerID, d.Barcode)
}

type Supplier struct {
	SupplierID int
	Name       string
	Number     string
}

func (s.Supplier) String() string {
	return fmt.Sprintf("Supplier<%d %s %s>", s.SupplierID, s.Name, s.Number)
}

type Supply struct {
	SupplyID   int
	SupplierID int
	SupplyDate string
	Status     string
}

func (s.Supply) String() string {
	return fmt.Sprintf("Supply<%d %d %s %s>", s.SupplyID, s.SupplierID, s.SupplyDate, s.Status)
}

type Batch struct {
	BatchID     int
	DrugID      int 
	SupplyID    int 
	Number      string
	ShelfLife   string
	ArrivalDate string
	Price       int
}

func (b.Batch) String() string {
	return fmt.Sprintf("Batch<%d %d %d %s %s %s %d>", b.BatchID, b.DrugID, b.SupplyID, b.Number, b.ShelfLife, b.ArrivalDate, b.Price)
}

type Warehouse struct {
	WarehouseID int
	Name        string
	Address     string
}

func (w.Warehouse) String() string {
	return fmt.Sprintf("Warehouse<%d %s %s>", w.WarehouseID, w.Name, w.Address)
}

type Stock struct {
	StockID     int
	WarehouseID int
	BatchID     int
	Amount      int
}

func (s.Stock) String() string {
	return fmt.Sprintf("Stock<%d %d %d %d>", s.StockID, s.WarehouseID, s.BatchID, s.Amount)
}

type Cart struct {
	CartID    int
	UserID    int
	CreatedAt string
	UpdatedAt string
	Status    string
}

func (c.Cart) String() string {
	return fmt.Sprintf("Cart<%d %d %s %s %s>", c.CartID, c.UserID, c.CreatedAt, c.UpdatedAt, c.Status)
}

type CartItem struct {
	CartItemID int
	CartID     int
	DrugID     int
	Quantity   int
}

func (ci.CartItem) String() string {
	return fmt.Sprintf("CartItem<%d %d %d %d>", ci.CartItemID, ci.CartID, ci.DrugID, ci.Quantity)
}

type Order struct {
	OrderID      int
	UserID       int
	Discount     int
	CreatedAt    string
	TotalAmount  int
	Status       string
	DeliveryType string
}

func (o.Order) String() string {
	return fmt.Sprintf("Order<%d %d %d %s %d %s %s>", o.OrderID, o.UserID, o.Discount, o.CreatedAt, o.TotalAmount, o.Status, o.DeliveryType)
}

type OrderItem struct {
	OrderItemID int
	OrderID     int 
	BatchID     int
	Quantity    int 
	Price       int
}

func (oi.OrderItem) String() string {
	return fmt.Sprintf("OrderItem<%d %d %d %d %d>",oi.OrderItemID, oi.OrderID, oi.BatchID, oi.Quantity, oi.Price )
}

type Reservation struct {
	ReservationID int
	BatchID       int
	OrderID       int
	Quantity      int
	ExpiresAt     string
}

func (r.Reservation) String() string {
	return fmt.Sprintf("Reservation<%d %d %d %d %s>", r.ReservationID, r.BatchID, r.OrderID, r.Quantity, r.ExpiresAt)
}
