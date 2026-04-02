package v1

import (
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type DrugTypeHandler struct {
	BaseHandler
}

func NewDrugTypeHandler(db *pg.DB) *DrugTypeHandler {
	return &DrugTypeHandler{BaseHandler{db}}
}

func (h *DrugTypeHandler) List(w http.ResponseWriter, r *http.Request) {
	var drugTypes []models.DrugType
	h.BaseHandler.List(w, r, &drugTypes)
}

func (h *DrugTypeHandler) Get(w http.ResponseWriter, r *http.Request) {
	drugType := new(models.DrugType)
	h.BaseHandler.GetByID(w, r, drugType, "/api/v1/drugtypes/")
}
