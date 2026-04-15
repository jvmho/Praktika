from pydantic import BaseModel
from datetime import date, datetime
from typing import Optional, List


class RoleSchema(BaseModel):
    roleID: int
    userStatus: str
    
    class Config:
        from_attributes = True


class UserSchema(BaseModel):
    userID: Optional[int] = None
    login: str
    password: str
    roleID: Optional[int] = None
    name: str
    
    class Config:
        from_attributes = True


class UserDiscountSchema(BaseModel):
    discountID: Optional[int] = None
    userID: int
    percent: int
    validFrom: date
    validTo: date
    
    class Config:
        from_attributes = True


class ManufacturerSchema(BaseModel):
    manufacturerID: Optional[int] = None
    name: str
    country: Optional[str] = None
    contactNumber: Optional[str] = None
    
    class Config:
        from_attributes = True


class DrugTypeSchema(BaseModel):
    typeID: Optional[int] = None
    name: str
    parentID: Optional[int] = None
    
    class Config:
        from_attributes = True


class DrugSchema(BaseModel):
    drugID: Optional[int] = None
    name: str
    category: Optional[str] = None
    description: Optional[str] = None
    INN: Optional[str] = None
    typeID: Optional[int] = None
    dose: Optional[str] = None
    manufacturerID: Optional[int] = None
    barcode: Optional[int] = None
    
    class Config:
        from_attributes = True


class SupplierSchema(BaseModel):
    supplierID: Optional[int] = None
    name: str
    number: Optional[str] = None
    
    class Config:
        from_attributes = True


class SupplySchema(BaseModel):
    supplyID: Optional[int] = None
    supplierID: Optional[int] = None
    supplyDate: Optional[date] = None
    status: Optional[str] = None
    
    class Config:
        from_attributes = True


class BatchSchema(BaseModel):
    batchID: Optional[int] = None
    drugID: int
    supplyID: Optional[int] = None
    number: Optional[int] = None
    shelfLife: Optional[date] = None
    arrivalDate: Optional[date] = None
    price: Optional[int] = None
    
    class Config:
        from_attributes = True


class WarehouseSchema(BaseModel):
    warehouseID: Optional[int] = None
    name: str
    address: Optional[str] = None
    
    class Config:
        from_attributes = True


class StockSchema(BaseModel):
    stockID: Optional[int] = None
    warehouseID: int
    batchID: int
    amount: int
    
    class Config:
        from_attributes = True


class CartItemSchema(BaseModel):
    cartItemID: Optional[int] = None
    cartID: int
    drugID: int
    quantity: int
    
    class Config:
        from_attributes = True


class CartSchema(BaseModel):
    cartID: Optional[int] = None
    userID: int
    createdAt: Optional[datetime] = None
    updatedAt: Optional[datetime] = None
    status: Optional[str] = None
    
    class Config:
        from_attributes = True


class OrderItemSchema(BaseModel):
    orderItemID: Optional[int] = None
    orderID: int
    batchID: int
    quantity: int
    price: int
    
    class Config:
        from_attributes = True


class OrderSchema(BaseModel):
    orderID: Optional[int] = None
    userID: int
    discount: Optional[int] = None
    createdAt: Optional[datetime] = None
    totalAmount: Optional[int] = None
    status: Optional[str] = None
    deliveryType: Optional[str] = None
    
    class Config:
        from_attributes = True


class ReservationSchema(BaseModel):
    reservationID: Optional[int] = None
    batchID: int
    orderID: int
    quantity: int
    expiresAt: Optional[date] = None
    
    class Config:
        from_attributes = True