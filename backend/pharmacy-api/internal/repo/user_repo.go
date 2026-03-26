package repo

import (
	"context"

	"github.com/go-pg/pg/v10"
	"pharmacy-api/internal/models"
)

type UserRepo struct {
	db *pg.DB
}

func NewUserRepo(db *pg.DB) *UserRepo {
	return &UserRepo{db}
}

func (r *UserRepo) GetByLogin(ctx context.Context, login string) (*models.User, error) {
	user := new(models.User)

	err := r.db.ModelContext(ctx, user).
		Where("login = ?", login).
		Relation("Role").
		Select()

	return user, err
}
