package db

import (
	"os"

	"github.com/go-pg/pg/v10"
)

func getEnv(key, fallback string) string {
	if v := os.Getenv(key); v != "" {
		return v
	}
	return fallback
}

func New() *pg.DB {
	return pg.Connect(&pg.Options{
		Addr:     getEnv("DB_ADDR", "localhost:5432"),
		User:     getEnv("DB_USER", "postgres"),
		Password: getEnv("DB_PASSWORD", "password"),
		Database: getEnv("DB_NAME", "postgres"),
	})
}
