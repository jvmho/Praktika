package service

import (
	"context"
	"errors"
	"time"

	"github.com/golang-jwt/jwt/v5"
	"pharmacy-api/internal/repo"
)

var secret = []byte("secret-key")

type AuthService struct {
	repo *repo.UserRepo
}

func NewAuthService(r *repo.UserRepo) *AuthService {
	return &AuthService{r}
}

func (s *AuthService) Login(ctx context.Context, login, password string) (string, error) {
	user, err := s.repo.GetByLogin(ctx, login)
	if err != nil {
		return "", err
	}

	if user.Password != password {
		return "", errors.New("invalid credentials")
	}

	token := jwt.NewWithClaims(jwt.SigningMethodHS256, jwt.MapClaims{
		"user_id": user.Id,
		"exp":     time.Now().Add(24 * time.Hour).Unix(),
	})

	return token.SignedString(secret)
}
