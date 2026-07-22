package v1

import (
	"encoding/json"
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type OrderHandler struct {
	BaseHandler
}

func NewOrderHandler(db *pg.DB) *OrderHandler {
	return &OrderHandler{BaseHandler{db}}
}

func (h *OrderHandler) Create(w http.ResponseWriter, r *http.Request) {
	order := new(models.Order)
	json.NewDecoder(r.Body).Decode(order)
	h.DB.Model(order).Insert()
	json.NewEncoder(w).Encode(order)
}

func (h *OrderHandler) Get(w http.ResponseWriter, r *http.Request) {
	order := new(models.Order)
	h.BaseHandler.GetByID(w, r, order, "/api/v1/orders/")
}
