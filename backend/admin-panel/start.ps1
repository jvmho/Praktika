# Скрипт для запуска приложения на Windows
# Запуск: .\start.ps1

# Запуск FastAPI сервера в новом окне PowerShell
Write-Host "Запуск FastAPI сервера..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit -Command `"cd '$PSScriptRoot'; python -m uvicorn server:app --reload`""

# Задержка чтобы сервер успел запуститься
Write-Host "Ожидание инициализации сервера..." -ForegroundColor Yellow
Start-Sleep -Seconds 8

# Запуск Tkinter приложения
Write-Host "Запуск Tkinter приложения..." -ForegroundColor Green
python app.py
