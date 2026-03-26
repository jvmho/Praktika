package db

import (
	"github.com/go-pg/pg/v10"
)

func New() *pg.DB {
	return pg.Connect(&pg.Options{
		Addr:     "localhost:5432",
		User:     "postgres",
		Password: "password",
		Database: "postgres",
	})
}
