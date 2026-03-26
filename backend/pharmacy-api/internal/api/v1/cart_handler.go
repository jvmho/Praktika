package v1

import (
	"encoding/json"
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type CartHandler struct {
	BaseHandler
}

func NewCartHandler(db *pg.DB) *CartHandler {
	return &CartHandler{BaseHandler{db}}
}

func (h *CartHandler) Get(w http.ResponseWriter, r *http.Request) {
	var cart []models.Cart
	h.BaseHandler.List(w, r, &cart)
}

func (h *CartHandler) AddItem(w http.ResponseWriter, r *http.Request) {
	item := new(models.CartItem)
	json.NewDecoder(r.Body).Decode(item)
	h.DB.Model(item).Insert()
	json.NewEncoder(w).Encode(item)
}
