package v1

import (
    "encoding/json"
    "net/http"

    "github.com/go-pg/pg/v10"
    "pharmacy-api/internal/models"
)

type StockHandler struct {
	BaseHandler
}

func NewStockHandler(db *pg.DB) *StockHandler {
	return &StockHandler{BaseHandler{db}}
}

func (h *StockHandler) List(w http.ResponseWriter, r *http.Request) {
	var stock []models.Stock

	q := h.DB.Model(&stock)
	if drugID := r.URL.Query().Get("drugId"); drugID != "" {
		q.Where("drug_id = ?", drugID)
	}

	q.Select()
	json.NewEncoder(w).Encode(stock)
}
