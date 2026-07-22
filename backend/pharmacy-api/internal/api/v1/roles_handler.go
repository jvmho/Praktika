package v1

import (
	"net/http"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type RoleHandler struct {
	BaseHandler
}

func NewRoleHandler(db *pg.DB) *RoleHandler {
	return &RoleHandler{BaseHandler{db}}
}

func (h *RoleHandler) List(w http.ResponseWriter, r *http.Request) {
	var roles []models.Role
	h.BaseHandler.List(w, r, &roles)
}
