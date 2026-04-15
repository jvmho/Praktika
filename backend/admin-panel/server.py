from fastapi import FastAPI, Depends, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session
from typing import List
from database import engine, get_db, Base
from models import (
    User, Role, UserDiscount, Manufacturer, DrugType, Drug, 
    Supplier, Supply, Batch, Warehouse, Stock, 
    Cart, CartItem, Order, OrderItem, Reservation
)
from schemas import (
    UserSchema, RoleSchema, UserDiscountSchema, ManufacturerSchema, 
    DrugTypeSchema, DrugSchema, SupplierSchema, SupplySchema, 
    BatchSchema, WarehouseSchema, StockSchema, 
    CartSchema, CartItemSchema, OrderSchema, OrderItemSchema, ReservationSchema
)

# Database tables are created by init_db.py
print("✓ Подключено к базе данных PostgreSQL")

app = FastAPI()

# Enable CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# ======================= USERS =======================

@app.get("/api/v1/users", response_model=List[UserSchema])
def get_users(db: Session = Depends(get_db)):
    try:
        users = db.query(User).all()
        return [UserSchema.model_validate(u) for u in users]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/users")
def create_user(data: UserSchema, db: Session = Depends(get_db)):
    try:
        user = User(**data.model_dump(exclude={"userID"}))
        db.add(user)
        db.commit()
        db.refresh(user)
        return {"status": "created", "data": UserSchema.model_validate(user)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.put("/api/v1/users/{user_id}")
def update_user(user_id: int, data: UserSchema, db: Session = Depends(get_db)):
    try:
        user = db.query(User).filter(User.userID == user_id).first()
        if not user:
            raise HTTPException(status_code=404, detail="Пользователь не найден")
        for key, value in data.model_dump(exclude={"userID"}).items():
            setattr(user, key, value)
        db.commit()
        db.refresh(user)
        return {"status": "updated", "data": UserSchema.model_validate(user)}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.delete("/api/v1/users/{user_id}")
def delete_user(user_id: int, db: Session = Depends(get_db)):
    try:
        user = db.query(User).filter(User.userID == user_id).first()
        if not user:
            raise HTTPException(status_code=404, detail="Пользователь не найден")
        db.delete(user)
        db.commit()
        return {"status": "deleted"}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= ROLES =======================

@app.get("/api/v1/roles", response_model=List[RoleSchema])
def get_roles(db: Session = Depends(get_db)):
    try:
        roles = db.query(Role).all()
        return [RoleSchema.model_validate(r) for r in roles]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/roles")
def create_role(data: RoleSchema, db: Session = Depends(get_db)):
    try:
        role = Role(**data.model_dump(exclude={"roleID"}))
        db.add(role)
        db.commit()
        db.refresh(role)
        return {"status": "created", "data": RoleSchema.model_validate(role)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= DRUGS =======================

@app.get("/api/v1/drugs", response_model=List[DrugSchema])
def get_drugs(db: Session = Depends(get_db)):
    try:
        drugs = db.query(Drug).all()
        return [DrugSchema.model_validate(d) for d in drugs]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/drugs")
def create_drug(data: DrugSchema, db: Session = Depends(get_db)):
    try:
        drug = Drug(**data.model_dump(exclude={"drugID"}))
        db.add(drug)
        db.commit()
        db.refresh(drug)
        return {"status": "created", "data": DrugSchema.model_validate(drug)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.put("/api/v1/drugs/{drug_id}")
def update_drug(drug_id: int, data: DrugSchema, db: Session = Depends(get_db)):
    try:
        drug = db.query(Drug).filter(Drug.drugID == drug_id).first()
        if not drug:
            raise HTTPException(status_code=404, detail="Препарат не найден")
        for key, value in data.model_dump(exclude={"drugID"}).items():
            setattr(drug, key, value)
        db.commit()
        db.refresh(drug)
        return {"status": "updated", "data": DrugSchema.model_validate(drug)}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.delete("/api/v1/drugs/{drug_id}")
def delete_drug(drug_id: int, db: Session = Depends(get_db)):
    try:
        drug = db.query(Drug).filter(Drug.drugID == drug_id).first()
        if not drug:
            raise HTTPException(status_code=404, detail="Препарат не найден")
        db.delete(drug)
        db.commit()
        return {"status": "deleted"}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= SUPPLIERS =======================

@app.get("/api/v1/suppliers", response_model=List[SupplierSchema])
def get_suppliers(db: Session = Depends(get_db)):
    try:
        suppliers = db.query(Supplier).all()
        return [SupplierSchema.model_validate(s) for s in suppliers]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/suppliers")
def create_supplier(data: SupplierSchema, db: Session = Depends(get_db)):
    try:
        supplier = Supplier(**data.model_dump(exclude={"supplierID"}))
        db.add(supplier)
        db.commit()
        db.refresh(supplier)
        return {"status": "created", "data": SupplierSchema.model_validate(supplier)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= SUPPLIES =======================

@app.get("/api/v1/supplies", response_model=List[SupplySchema])
def get_supplies(db: Session = Depends(get_db)):
    try:
        supplies = db.query(Supply).all()
        return [SupplySchema.model_validate(s) for s in supplies]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/supplies")
def create_supply(data: SupplySchema, db: Session = Depends(get_db)):
    try:
        supply = Supply(**data.model_dump(exclude={"supplyID"}))
        db.add(supply)
        db.commit()
        db.refresh(supply)
        return {"status": "created", "data": SupplySchema.model_validate(supply)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.put("/api/v1/supplies/{supply_id}")
def update_supply(supply_id: int, data: SupplySchema, db: Session = Depends(get_db)):
    try:
        supply = db.query(Supply).filter(Supply.supplyID == supply_id).first()
        if not supply:
            raise HTTPException(status_code=404, detail="Поставка не найдена")
        for key, value in data.model_dump(exclude={"supplyID"}).items():
            setattr(supply, key, value)
        db.commit()
        db.refresh(supply)
        return {"status": "updated", "data": SupplySchema.model_validate(supply)}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= MANUFACTURERS =======================

@app.get("/api/v1/manufacturers", response_model=List[ManufacturerSchema])
def get_manufacturers(db: Session = Depends(get_db)):
    try:
        manufacturers = db.query(Manufacturer).all()
        return [ManufacturerSchema.model_validate(m) for m in manufacturers]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/manufacturers")
def create_manufacturer(data: ManufacturerSchema, db: Session = Depends(get_db)):
    try:
        manufacturer = Manufacturer(**data.model_dump(exclude={"manufacturerID"}))
        db.add(manufacturer)
        db.commit()
        db.refresh(manufacturer)
        return {"status": "created", "data": ManufacturerSchema.model_validate(manufacturer)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= BATCHES =======================

@app.get("/api/v1/batches", response_model=List[BatchSchema])
def get_batches(db: Session = Depends(get_db)):
    try:
        batches = db.query(Batch).all()
        return [BatchSchema.model_validate(b) for b in batches]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/batches")
def create_batch(data: BatchSchema, db: Session = Depends(get_db)):
    try:
        batch = Batch(**data.model_dump(exclude={"batchID"}))
        db.add(batch)
        db.commit()
        db.refresh(batch)
        return {"status": "created", "data": BatchSchema.model_validate(batch)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= WAREHOUSES =======================

@app.get("/api/v1/warehouses", response_model=List[WarehouseSchema])
def get_warehouses(db: Session = Depends(get_db)):
    try:
        warehouses = db.query(Warehouse).all()
        return [WarehouseSchema.model_validate(w) for w in warehouses]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/warehouses")
def create_warehouse(data: WarehouseSchema, db: Session = Depends(get_db)):
    try:
        warehouse = Warehouse(**data.model_dump(exclude={"warehouseID"}))
        db.add(warehouse)
        db.commit()
        db.refresh(warehouse)
        return {"status": "created", "data": WarehouseSchema.model_validate(warehouse)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= STOCK =======================

@app.get("/api/v1/stocks", response_model=List[StockSchema])
def get_stocks(db: Session = Depends(get_db)):
    try:
        stocks = db.query(Stock).all()
        return [StockSchema.model_validate(s) for s in stocks]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/stocks")
def create_stock(data: StockSchema, db: Session = Depends(get_db)):
    try:
        stock = Stock(**data.model_dump(exclude={"stockID"}))
        db.add(stock)
        db.commit()
        db.refresh(stock)
        return {"status": "created", "data": StockSchema.model_validate(stock)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= ORDERS =======================

@app.get("/api/v1/orders", response_model=List[OrderSchema])
def get_orders(db: Session = Depends(get_db)):
    try:
        orders = db.query(Order).all()
        return [OrderSchema.model_validate(o) for o in orders]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/orders")
def create_order(data: OrderSchema, db: Session = Depends(get_db)):
    try:
        order = Order(**data.model_dump(exclude={"orderID"}))
        db.add(order)
        db.commit()
        db.refresh(order)
        return {"status": "created", "data": OrderSchema.model_validate(order)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.put("/api/v1/orders/{order_id}")
def update_order(order_id: int, data: OrderSchema, db: Session = Depends(get_db)):
    try:
        order = db.query(Order).filter(Order.orderID == order_id).first()
        if not order:
            raise HTTPException(status_code=404, detail="Заказ не найден")
        for key, value in data.model_dump(exclude={"orderID"}).items():
            setattr(order, key, value)
        db.commit()
        db.refresh(order)
        return {"status": "updated", "data": OrderSchema.model_validate(order)}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.delete("/api/v1/orders/{order_id}")
def delete_order(order_id: int, db: Session = Depends(get_db)):
    try:
        order = db.query(Order).filter(Order.orderID == order_id).first()
        if not order:
            raise HTTPException(status_code=404, detail="Заказ не найден")
        db.delete(order)
        db.commit()
        return {"status": "deleted"}
    except HTTPException:
        raise
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= CARTS =======================

@app.get("/api/v1/carts", response_model=List[CartSchema])
def get_carts(db: Session = Depends(get_db)):
    try:
        carts = db.query(Cart).all()
        return [CartSchema.model_validate(c) for c in carts]
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/v1/carts")
def create_cart(data: CartSchema, db: Session = Depends(get_db)):
    try:
        cart = Cart(**data.model_dump(exclude={"cartID"}))
        db.add(cart)
        db.commit()
        db.refresh(cart)
        return {"status": "created", "data": CartSchema.model_validate(cart)}
    except Exception as e:
        db.rollback()
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

# ======================= STATISTICS =======================

@app.get("/api/v1/statistics/stock")
def get_stock_statistics(db: Session = Depends(get_db)):
    """Подсчет позиций на складе"""
    try:
        stocks = db.query(Stock).all()
        
        total_items = 0
        stock_by_warehouse = {}
        stock_by_drug = {}
        
        for stock in stocks:
            total_items += stock.amount
            
            # По складам
            warehouse_name = stock.warehouse.name if stock.warehouse else f"Warehouse {stock.warehouseID}"
            if warehouse_name not in stock_by_warehouse:
                stock_by_warehouse[warehouse_name] = 0
            stock_by_warehouse[warehouse_name] += stock.amount
            
            # По препаратам
            drug_name = stock.batch.drug.name if stock.batch and stock.batch.drug else f"Drug {stock.batchID}"
            if drug_name not in stock_by_drug:
                stock_by_drug[drug_name] = 0
            stock_by_drug[drug_name] += stock.amount
        
        return {
            "total_items": total_items,
            "by_warehouse": stock_by_warehouse,
            "by_drug": stock_by_drug,
            "total_positions": len(stocks)
        }
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/api/v1/statistics/reservations")
def get_reservation_statistics(db: Session = Depends(get_db)):
    """Статистика по броням и пользователям"""
    try:
        reservations = db.query(Reservation).all()
        
        total_reservations = len(reservations)
        users_with_reservations = set()
        reservations_by_expiry = {}
        
        for reservation in reservations:
            # Добавляем пользователя
            if reservation.order and reservation.order.user:
                users_with_reservations.add(reservation.order.user.userID)
            
            # По дате истечения
            expiry_date = str(reservation.expiresAt) if reservation.expiresAt else "No expiry"
            if expiry_date not in reservations_by_expiry:
                reservations_by_expiry[expiry_date] = {
                    "count": 0,
                    "total_quantity": 0,
                    "users": set()
                }
            reservations_by_expiry[expiry_date]["count"] += 1
            reservations_by_expiry[expiry_date]["total_quantity"] += reservation.quantity
            if reservation.order and reservation.order.user:
                reservations_by_expiry[expiry_date]["users"].add(reservation.order.user.login)
        
        # Конвертируем sets в списки для JSON
        for key in reservations_by_expiry:
            reservations_by_expiry[key]["users"] = list(reservations_by_expiry[key]["users"])
        
        return {
            "total_reservations": total_reservations,
            "total_users": len(users_with_reservations),
            "by_expiry_date": reservations_by_expiry
        }
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/api/v1/statistics/references")
def get_references(table_name: str, db: Session = Depends(get_db)):
    """Получить список всех значений для внешнего ключа"""
    try:
        references = {}
        
        if table_name == "Role":
            roles = db.query(Role).all()
            references = [(r.roleID, r.userStatus) for r in roles]
        elif table_name == "Manufacturer":
            manufacturers = db.query(Manufacturer).all()
            references = [(m.manufacturerID, m.name) for m in manufacturers]
        elif table_name == "DrugType":
            types = db.query(DrugType).all()
            references = [(t.typeID, t.name) for t in types]
        elif table_name == "Drug":
            drugs = db.query(Drug).all()
            references = [(d.drugID, d.name) for d in drugs]
        elif table_name == "Supplier":
            suppliers = db.query(Supplier).all()
            references = [(s.supplierID, s.name) for s in suppliers]
        elif table_name == "Supply":
            supplies = db.query(Supply).all()
            references = [(s.supplyID, f"Supply {s.supplyID} - {s.status}") for s in supplies]
        elif table_name == "User":
            users = db.query(User).all()
            references = [(u.userID, u.login) for u in users]
        elif table_name == "Warehouse":
            warehouses = db.query(Warehouse).all()
            references = [(w.warehouseID, w.name) for w in warehouses]
        elif table_name == "Batch":
            batches = db.query(Batch).all()
            references = [(b.batchID, f"Batch {b.batchID}") for b in batches]
        else:
            raise HTTPException(status_code=400, detail="Unknown table")
        
        return {"data": references}
    except Exception as e:
        print(f"Ошибка: {e}")
        raise HTTPException(status_code=500, detail=str(e))
