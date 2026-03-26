package v1

import (
	"encoding/json"
	"net/http"

	"github.com/golang-jwt/jwt/v5"
	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

var secret = []byte("secret")

type AuthHandler struct {
	db *pg.DB
}

func NewAuthHandler(db *pg.DB) *AuthHandler {
	return &AuthHandler{db}
}

func (h *AuthHandler) Login(w http.ResponseWriter, r *http.Request) {
	var req struct {
		Login    string
		Password string
	}

	json.NewDecoder(r.Body).Decode(&req)

	user := new(models.User)
	err := h.db.Model(user).
		Where("login = ?", req.Login).
		Select()

	if err != nil || user.Password != req.Password {
		http.Error(w, "invalid credentials", 401)
		return
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"user_id": user.Id,
	})

	tokenStr, _ := token.SignedString(secret)

	json.NewEncoder(w).Encode(map[string]string{
		"token": tokenStr,
	})
}
