package v1

import (
    "encoding/json"
    "net/http"

    "github.com/go-pg/pg/v10"
    "pharmacy-api/internal/models"
    "pharmacy-api/internal/logger"
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
    if r.Method != http.MethodPost {
        http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
        return
    }

    var req models.CreateUserRequest
    if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
        logger.Error(err)
        http.Error(w, "Invalid request body", http.StatusBadRequest)
        return
    }

    if req.Login == "" || req.Password == "" || req.Name == "" {
        http.Error(w, "Login, password and name are required", http.StatusBadRequest)
        return
    }

    exists, err := h.DB.Model(&models.User{}).
        Where("login = ?", req.Login).
        Exists()
    if err != nil {
        logger.Error(err)
        http.Error(w, "DB error", http.StatusInternalServerError)
        return
    }
    if exists {
        http.Error(w, "User with this login already exists", http.StatusConflict)
        return
    }

    user := &models.User{
        Login:    req.Login,
        Password: req.Password,
        Name:     req.Name,
        RoleId:   req.RoleId,
    }

    _, err = h.DB.Model(user).Insert()
    if err != nil {
        logger.Error(err)

        if pgErr, ok := err.(pg.Error); ok && pgErr.Field('C') == "23505" {
            http.Error(w, "User already exists (duplicate key)", http.StatusConflict)
            return
        }

        http.Error(w, "Failed to create user", http.StatusInternalServerError)
        return
    }

    w.Header().Set("Content-Type", "application/json")
    w.WriteHeader(http.StatusCreated)
    json.NewEncoder(w).Encode(user)
}

func (h *UserHandler) Delete(w http.ResponseWriter, r *http.Request) {
    user := new(models.User)
    h.BaseHandler.Delete(w, r, user, "/api/v1/users/")
}
