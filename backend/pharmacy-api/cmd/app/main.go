package main

import (
	"net/http"

	"pharmacy-api/internal/api"
    v1  "pharmacy-api/internal/api/v1"
	"pharmacy-api/internal/db"
	"pharmacy-api/internal/logger"
	"pharmacy-api/internal/repo"
	"pharmacy-api/internal/service"
)

func main() {
	database := db.New()

	_ = db.CreateSchema(database)

	userRepo := repo.NewUserRepo(database)
	authService := service.NewAuthService(userRepo)

	authHandler := v1.NewAuthHandler(authService)
	orderHandler := v1.NewOrderHandler(database)

	const base = "/api/v1"

	http.HandleFunc(base+"/login", authHandler.Login)
	http.HandleFunc(base+"/orders", api.Auth(orderHandler.List))

	logger.Info("server started :8080")
	http.ListenAndServe(":8080", nil)
}
