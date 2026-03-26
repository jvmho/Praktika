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
			http.Error(w, "unauthorized", 401)
			return
		}

		tokenStr := strings.TrimPrefix(header, "Bearer ")

		_, err := jwt.Parse(tokenStr, func(t *jwt.Token) (interface{}, error) {
			return secret, nil
		})

		if err != nil {
			http.Error(w, "invalid token", 401)
			return
		}

		next(w, r)
	}
}
