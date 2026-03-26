package v1

import (
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type UserHandler struct {
	BaseHandler
}

func NewUserHandler(db *pg.DB) *UserHandler {
	return &UserHandler{BaseHandler{db}}
}

func (h *UserHandler) List(w http.ResponseWriter, r *http.Request) {
	var users []models.User
	h.BaseHandler.List(w, r, &users)
}

func (h *UserHandler) Get(w http.ResponseWriter, r *http.Request) {
	user := new(models.User)
	h.BaseHandler.GetByID(w, r, user, "/api/v1/users/")
}

func (h *UserHandler) Create(w http.ResponseWriter, r *http.Request) {
	user := new(models.User)
	h.BaseHandler.Create(w, r, user)
}
