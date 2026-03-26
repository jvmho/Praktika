package v1

import (
	"encoding/json"
	"net/http"
	"strconv"
	"strings"

	"github.com/go-pg/pg/v10"
)

type BaseHandler struct {
	DB *pg.DB
}

func (h *BaseHandler) List(w http.ResponseWriter, r *http.Request, model interface{}, relations ...string) {
	q := h.DB.Model(model)

	for _, rel := range relations {
		q.Relation(rel)
	}

	err := q.Select()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	json.NewEncoder(w).Encode(model)
}

func (h *BaseHandler) GetByID(w http.ResponseWriter, r *http.Request, model interface{}, prefix string, relations ...string) {
	idStr := strings.TrimPrefix(r.URL.Path, prefix)
	id, _ := strconv.Atoi(idStr)

	q := h.DB.Model(model).Where("id = ?", id)

	for _, rel := range relations {
		q.Relation(rel)
	}

	err := q.Select()
	if err != nil {
		http.Error(w, "not found", 404)
		return
	}

	json.NewEncoder(w).Encode(model)
}

func (h *BaseHandler) Create(w http.ResponseWriter, r *http.Request, model interface{}) {
	err := json.NewDecoder(r.Body).Decode(model)
	if err != nil {
		http.Error(w, "bad request", 400)
		return
	}

	_, err = h.DB.Model(model).Insert()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	json.NewEncoder(w).Encode(model)
}

func (h *BaseHandler) Patch(w http.ResponseWriter, r *http.Request, model interface{}, prefix string) {
	idStr := strings.TrimPrefix(r.URL.Path, prefix)
	id, _ := strconv.Atoi(idStr)

	err := json.NewDecoder(r.Body).Decode(model)
	if err != nil {
		http.Error(w, "bad request", 400)
		return
	}

	_, err = h.DB.Model(model).Where("id = ?", id).Update()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	json.NewEncoder(w).Encode(model)
}
