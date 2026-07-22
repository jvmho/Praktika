package v1

import (
	"encoding/json"
	"net/http"
	"strings"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type CartHandler struct {
	BaseHandler
}

func NewCartHandler(db *pg.DB) *CartHandler {
	return &CartHandler{BaseHandler{db}}
}

func (h *CartHandler) List(w http.ResponseWriter, r *http.Request) {
	var carts []models.Cart

	userId := r.URL.Query().Get("userId")
	if userId != "" {
		err := h.DB.Model(&carts).
			Where("user_id = ?", userId).
			Select()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
	} else {
		err := h.DB.Model(&carts).Select()
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(carts)
}

// GET /cart/{id} - конкретная корзина
func (h *CartHandler) Get(w http.ResponseWriter, r *http.Request) {
	cartId := strings.TrimPrefix(r.URL.Path, "/api/v1/cart/")

	cart := new(models.Cart)
	err := h.DB.Model(cart).
		Where("id = ?", cartId).
		Select()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(cart)
}

// GET /cart/{id}/items - элементы корзины
func (h *CartHandler) GetItems(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/cart/")
	cartId := strings.TrimSuffix(path, "/items")

	var items []models.CartItem
	err := h.DB.Model(&items).
		Where("cart_id = ?", cartId).
		Select()
	if err != nil {
		http.Error(w, err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(items)
}

// POST /cart/item - добавление элемента в корзину
func (h *CartHandler) AddItem(w http.ResponseWriter, r *http.Request) {
	item := new(models.CartItem)
	json.NewDecoder(r.Body).Decode(item)
	h.DB.Model(item).Insert()
	json.NewEncoder(w).Encode(item)
}

// DELETE /cart/items/{id} - удаление элемента корзины
func (h *CartHandler) DeleteItem(w http.ResponseWriter, r *http.Request) {
	item := new(models.CartItem)
	h.BaseHandler.Delete(w, r, item, "api/v1/cart/items/")
}

// Router для /cart/ - разбирает путь и вызывает нужный обработчик
func (h *CartHandler) Router(w http.ResponseWriter, r *http.Request) {
	path := strings.TrimPrefix(r.URL.Path, "/api/v1/cart/")
	// Убираем trailing slash
	path = strings.TrimSuffix(path, "/")
	parts := strings.Split(path, "/")

	switch {
	// /cart/{id}/items
	case len(parts) == 2 && parts[1] == "items":
		h.GetItems(w, r)
	// /cart/{id}
	case len(parts) == 1 && parts[0] != "":
		h.Get(w, r)
	default:
		http.NotFound(w, r)
	}
}
