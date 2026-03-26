package v1

import (
	"encoding/json"
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type OrderHandler struct {
	db *pg.DB
}

func NewOrderHandler(db *pg.DB) *OrderHandler {
	return &OrderHandler{db}
}

func (h *OrderHandler) List(w http.ResponseWriter, r *http.Request) {
	var items []models.OrderItem

	err := h.db.Model(&items).
		Relation("Order.User.Role").
		Relation("Batch.Drug.Type").
		Relation("Batch.Drug.Manufacturer").
		Select()

	if err != nil {
		http.Error(w, "error", 500)
		return
	}

	json.NewEncoder(w).Encode(items)
}
