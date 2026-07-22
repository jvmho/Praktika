package v1

import (
    "encoding/json"
    "net/http"

    "github.com/go-pg/pg/v10"
    "pharmacy-api/internal/models"
)

type DrugHandler struct {
	BaseHandler
}

func NewDrugHandler(db *pg.DB) *DrugHandler {
	return &DrugHandler{BaseHandler{db}}
}

func (h *DrugHandler) List(w http.ResponseWriter, r *http.Request) {
	var drugs []models.Drug

	q := h.DB.Model(&drugs)

	if c := r.URL.Query().Get("category"); c != "" {
		q.Where("category = ?", c)
	}
	if t := r.URL.Query().Get("type"); t != "" {
		q.Where("type = ?", t)
	}

	q.Select()
	json.NewEncoder(w).Encode(drugs)
}

func (h *DrugHandler) Get(w http.ResponseWriter, r *http.Request) {
	drug := new(models.Drug)
	h.BaseHandler.GetByID(w, r, drug, "/api/v1/drugs/")
}
