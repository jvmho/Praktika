FROM golang:1.26.1-alpine AS builder

WORKDIR /app

COPY go.mod ./
COPY go.sum ./
RUN go mod download

COPY . .

RUN CGO_ENABLED=0 GOOS=linux go build -o /pharmacy-api ./cmd/app

FROM alpine:3.20

WORKDIR /app

COPY --from=builder /pharmacy-api /app/pharmacy-api

EXPOSE 8080

CMD ["./pharmacy-api"]
