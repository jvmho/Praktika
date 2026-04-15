from sqlalchemy import Column, Integer, String, Text, Date, DateTime, BIGINT, ForeignKey, func
from sqlalchemy.orm import relationship
from datetime import datetime
from database import Base


class Role(Base):
    __tablename__ = "Role"
    
    roleID = Column(Integer, primary_key=True, index=True)
    userStatus = Column(String(50), nullable=False)
    
    users = relationship("User", back_populates="role")


class User(Base):
    __tablename__ = "User"
    
    userID = Column(Integer, primary_key=True, index=True)
    login = Column(String(100), unique=True, nullable=False, index=True)
    password = Column(String(255), nullable=False)
    roleID = Column(Integer, ForeignKey("Role.roleID"))
    name = Column(String(150), nullable=False)
    
    role = relationship("Role", back_populates="users")
    discounts = relationship("UserDiscount", back_populates="user", cascade="all, delete-orphan")
    carts = relationship("Cart", back_populates="user", cascade="all, delete-orphan")
    orders = relationship("Order", back_populates="user", cascade="all, delete-orphan")


class UserDiscount(Base):
    __tablename__ = "UserDiscount"
    
    discountID = Column(Integer, primary_key=True, index=True)
    userID = Column(Integer, ForeignKey("User.userID", ondelete="CASCADE"), nullable=False)
    percent = Column(Integer, nullable=False)
    validFrom = Column(Date, nullable=False)
    validTo = Column(Date, nullable=False)
    
    user = relationship("User", back_populates="discounts")


class Manufacturer(Base):
    __tablename__ = "Manufacturer"
    
    manufacturerID = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False, index=True)
    country = Column(String(100))
    contactNumber = Column(String(50))
    
    drugs = relationship("Drug", back_populates="manufacturer")


class DrugType(Base):
    __tablename__ = "DrugType"
    
    typeID = Column(Integer, primary_key=True, index=True)
    name = Column(String(100), nullable=False)
    parentID = Column(Integer, ForeignKey("DrugType.typeID"))
    
    drugs = relationship("Drug", back_populates="drug_type")


class Drug(Base):
    __tablename__ = "Drug"
    
    drugID = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False, index=True)
    category = Column(String(100))
    description = Column(Text)
    INN = Column(String(255))
    typeID = Column(Integer, ForeignKey("DrugType.typeID"))
    dose = Column(String(100))
    manufacturerID = Column(Integer, ForeignKey("Manufacturer.manufacturerID"))
    barcode = Column(BIGINT)
    
    drug_type = relationship("DrugType", back_populates="drugs")
    manufacturer = relationship("Manufacturer", back_populates="drugs")
    batches = relationship("Batch", back_populates="drug")
    cart_items = relationship("CartItem", back_populates="drug")


class Supplier(Base):
    __tablename__ = "Supplier"
    
    supplierID = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False, index=True)
    number = Column(String(50))
    
    supplies = relationship("Supply", back_populates="supplier")


class Supply(Base):
    __tablename__ = "Supply"
    
    supplyID = Column(Integer, primary_key=True, index=True)
    supplierID = Column(Integer, ForeignKey("Supplier.supplierID"))
    supplyDate = Column(Date)
    status = Column(String(50))
    
    supplier = relationship("Supplier", back_populates="supplies")
    batches = relationship("Batch", back_populates="supply")


class Batch(Base):
    __tablename__ = "Batch"
    
    batchID = Column(Integer, primary_key=True, index=True)
    drugID = Column(Integer, ForeignKey("Drug.drugID"))
    supplyID = Column(Integer, ForeignKey("Supply.supplyID"))
    number = Column(Integer)
    shelfLife = Column(Date)
    arrivalDate = Column(Date)
    price = Column(Integer)
    
    drug = relationship("Drug", back_populates="batches")
    supply = relationship("Supply", back_populates="batches")
    stocks = relationship("Stock", back_populates="batch")
    order_items = relationship("OrderItem", back_populates="batch")
    reservations = relationship("Reservation", back_populates="batch")


class Warehouse(Base):
    __tablename__ = "Warehouse"
    
    warehouseID = Column(Integer, primary_key=True, index=True)
    name = Column(String(255), nullable=False)
    address = Column(Text)
    
    stocks = relationship("Stock", back_populates="warehouse")


class Stock(Base):
    __tablename__ = "Stock"
    
    stockID = Column(Integer, primary_key=True, index=True)
    warehouseID = Column(Integer, ForeignKey("Warehouse.warehouseID"))
    batchID = Column(Integer, ForeignKey("Batch.batchID"))
    amount = Column(Integer, nullable=False)
    
    warehouse = relationship("Warehouse", back_populates="stocks")
    batch = relationship("Batch", back_populates="stocks")


class Cart(Base):
    __tablename__ = "Cart"
    
    cartID = Column(Integer, primary_key=True, index=True)
    userID = Column(Integer, ForeignKey("User.userID"))
    createdAt = Column(DateTime, nullable=False, default=datetime.utcnow)
    updatedAt = Column(DateTime)
    status = Column(String(50))
    
    user = relationship("User", back_populates="carts")
    items = relationship("CartItem", back_populates="cart", cascade="all, delete-orphan")


class CartItem(Base):
    __tablename__ = "CartItem"
    
    cartItemID = Column(Integer, primary_key=True, index=True)
    cartID = Column(Integer, ForeignKey("Cart.cartID", ondelete="CASCADE"))
    drugID = Column(Integer, ForeignKey("Drug.drugID"))
    quantity = Column(Integer, nullable=False)
    
    cart = relationship("Cart", back_populates="items")
    drug = relationship("Drug", back_populates="cart_items")


class Order(Base):
    __tablename__ = "Order"
    
    orderID = Column(Integer, primary_key=True, index=True)
    userID = Column(Integer, ForeignKey("User.userID"))
    discount = Column(Integer)
    createdAt = Column(DateTime, default=datetime.utcnow)
    totalAmount = Column(Integer)
    status = Column(String(50))
    deliveryType = Column(String(50))
    
    user = relationship("User", back_populates="orders")
    items = relationship("OrderItem", back_populates="order", cascade="all, delete-orphan")
    reservations = relationship("Reservation", back_populates="order")


class OrderItem(Base):
    __tablename__ = "OrderItem"
    
    orderItemID = Column(Integer, primary_key=True, index=True)
    orderID = Column(Integer, ForeignKey("Order.orderID", ondelete="CASCADE"))
    batchID = Column(Integer, ForeignKey("Batch.batchID"))
    quantity = Column(Integer, nullable=False)
    price = Column(Integer, nullable=False)
    
    order = relationship("Order", back_populates="items")
    batch = relationship("Batch", back_populates="order_items")


class Reservation(Base):
    __tablename__ = "Reservation"
    
    reservationID = Column(Integer, primary_key=True, index=True)
    batchID = Column(Integer, ForeignKey("Batch.batchID"))
    orderID = Column(Integer, ForeignKey("Order.orderID"))
    quantity = Column(Integer, nullable=False)
    expiresAt = Column(Date)
    
    batch = relationship("Batch", back_populates="reservations")
    order = relationship("Order", back_populates="reservations")
