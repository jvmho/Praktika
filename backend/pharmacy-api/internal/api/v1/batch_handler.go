package v1

import (
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type BatchHandler struct {
	BaseHandler
}

func NewBatchHandler(db *pg.DB) *BatchHandler {
	return &BatchHandler{BaseHandler{db}}
}

func (h *BatchHandler) List(w http.ResponseWriter, r *http.Request) {
	var batches []models.Batch
	h.BaseHandler.List(w, r, &batches)
}

func (h *BatchHandler) Get(w http.ResponseWriter, r *http.Request) {
	batch := new(models.Batch)
	h.BaseHandler.GetByID(w, r, batch, "/api/v1/batches/")
}
