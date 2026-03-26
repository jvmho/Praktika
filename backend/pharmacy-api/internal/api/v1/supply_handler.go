package v1

import (
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type SupplyHandler struct {
	BaseHandler
}

func NewSupplyHandler(db *pg.DB) *SupplyHandler {
	return &SupplyHandler{BaseHandler{db}}
}

func (h *SupplyHandler) List(w http.ResponseWriter, r *http.Request) {
	var supplies []models.Supply
	h.BaseHandler.List(w, r, &supplies)
}
