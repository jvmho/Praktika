package v1

import (
	"encoding/json"
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type ReservationHandler struct {
	BaseHandler
}

func NewReservationHandler(db *pg.DB) *ReservationHandler {
	return &ReservationHandler{BaseHandler{db}}
}

func (h *ReservationHandler) Create(w http.ResponseWriter, r *http.Request) {
	res := new(models.Reservation)
	json.NewDecoder(r.Body).Decode(res)
	h.DB.Model(res).Insert()
	json.NewEncoder(w).Encode(res)
}
