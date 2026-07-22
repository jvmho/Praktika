package api

import (
	"net/http"
	"strings"

	"github.com/golang-jwt/jwt/v5"
)

var secret = []byte("secret-key")

func Auth(next http.HandlerFunc) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		header := r.Header.Get("Authorization")
		if header == "" {
			http.Error(w, "unauthorized", http.StatusUnauthorized)
			return
		}

		// Проверка формата Bearer
		parts := strings.SplitN(header, " ", 2)
		if len(parts) != 2 || strings.ToLower(parts[0]) != "bearer" {
			http.Error(w, "invalid token - invalid input", http.StatusUnauthorized)
			return
		}

		tokenStr := parts[1]

		// Парсинг и проверка токена
		token, err := jwt.Parse(tokenStr, func(t *jwt.Token) (interface{}, error) {
			return secret, nil
		})
		if err != nil || !token.Valid {
			http.Error(w, "invalid token - invalid signature", http.StatusUnauthorized)
			return
		}

		// Всё ок — вызываем следующий хендлер
		next(w, r)
	}
}
