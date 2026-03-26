package db

import (
	"github.com/go-pg/pg/v10"
	"github.com/go-pg/pg/v10/orm"
	"pharmacy-api/internal/models"
)

func CreateSchema(db *pg.DB) error {
	modelsList := []interface{}{
		(*models.Role)(nil),
		(*models.User)(nil),
		(*models.UserDiscount)(nil),
		(*models.Manufacturer)(nil),
		(*models.DrugType)(nil),
		(*models.Drug)(nil),
		(*models.Supplier)(nil),
		(*models.Supply)(nil),
		(*models.Batch)(nil),
		(*models.Warehouse)(nil),
		(*models.Stock)(nil),
		(*models.Cart)(nil),
		(*models.CartItem)(nil),
		(*models.Order)(nil),
		(*models.OrderItem)(nil),
		(*models.Reservation)(nil),
	}

	for _, m := range modelsList {
		err := db.Model(m).CreateTable(&orm.CreateTableOptions{
			Temp:        true,
			IfNotExists: true,
		})
		if err != nil {
			return err
		}
	}
	return nil
}
