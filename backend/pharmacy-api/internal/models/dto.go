package models

type CreateUserRequest struct {
    Login    string `json:"login"`
    Password string `json:"password"`
    Name     string `json:"name"`
    RoleId   int    `json:"role_id"`
}
