package main

import (
	"net/http"

	"pharmacy-api/internal/api"
	"pharmacy-api/internal/logger"
	v1 "pharmacy-api/internal/api/v1"
	"pharmacy-api/internal/db"
)

func main() {
	database := db.New()
	_ = db.CreateSchema(database)

	base := "/api/v1"

	authHandler := v1.NewAuthHandler(database)
	userHandler := v1.NewUserHandler(database)
	roleHandler := v1.NewRoleHandler(database)
	drugHandler := v1.NewDrugHandler(database)
	batchHandler := v1.NewBatchHandler(database)
	stockHandler := v1.NewStockHandler(database)
	cartHandler := v1.NewCartHandler(database)
	orderHandler := v1.NewOrderHandler(database)
	supplyHandler := v1.NewSupplyHandler(database)
	reservationHandler := v1.NewReservationHandler(database)
	drugTypeHandler := v1.NewDrugTypeHandler(database)

	http.HandleFunc(base+"/login/auth", authHandler.Login)
	http.HandleFunc(base+"/users", api.Auth(userHandler.List))
	http.HandleFunc(base+"/users/", api.Auth(userHandler.Get))
	http.HandleFunc(base+"/users/create", userHandler.Create)
	http.HandleFunc(base+"/roles", api.Auth(roleHandler.List))
	http.HandleFunc(base+"/drugs", api.Auth(drugHandler.List))
	http.HandleFunc(base+"/drugs/", api.Auth(drugHandler.Get))
	http.HandleFunc(base+"/batches/", api.Auth(batchHandler.Get))
	http.HandleFunc(base+"/stock", api.Auth(stockHandler.List))
	http.HandleFunc(base+"/cart", api.Auth(cartHandler.Get))
	http.HandleFunc(base+"/cart/item", api.Auth(cartHandler.AddItem))
	http.HandleFunc(base+"/orders", api.Auth(orderHandler.Create))
	http.HandleFunc(base+"/orders/", api.Auth(orderHandler.Get))
	http.HandleFunc(base+"/supplies", api.Auth(supplyHandler.List))
	http.HandleFunc(base+"/reservations", api.Auth(reservationHandler.Create))
	http.HandleFunc(base+"/drugtypes", api.Auth(drugTypeHandler.List))
	http.HandleFunc(base+"/drugtypes/", api.Auth(drugTypeHandler.Get))
	
	logger.Info("Server running at http://localhost:8080")

	err := http.ListenAndServe(":8080", nil)
	if err != nil {
		logger.Error(err)
	}
}
