package db

import (
	"time"

	"github.com/go-pg/pg/v10"
	"github.com/go-pg/pg/v10/orm"
	"pharmacy-api/internal/models"
)

func CreateSchema(db *pg.DB) error {
	modelsList := []interface{}{
		(*models.Role)(nil),
		(*models.User)(nil),
		(*models.UserDiscount)(nil),
		(*models.Manufacturer)(nil),
		(*models.DrugType)(nil),
		(*models.Drug)(nil),
		(*models.Supplier)(nil),
		(*models.Supply)(nil),
		(*models.Batch)(nil),
		(*models.Warehouse)(nil),
		(*models.Stock)(nil),
		(*models.Cart)(nil),
		(*models.CartItem)(nil),
		(*models.Order)(nil),
		(*models.OrderItem)(nil),
		(*models.Reservation)(nil),
	}

	for _, m := range modelsList {
		err := db.Model(m).CreateTable(&orm.CreateTableOptions{
			IfNotExists: true,
		})
		if err != nil {
			return err
		}
	}

	// ===== Seed data =====
	if err := seedRoles(db); err != nil {
		return err
	}
	if err := seedUsers(db); err != nil {
		return err
	}
	if err := seedUserDiscounts(db); err != nil {
		return err
	}
	if err := seedManufacturers(db); err != nil {
		return err
	}
	if err := seedDrugTypes(db); err != nil {
		return err
	}
	if err := seedDrugs(db); err != nil {
		return err
	}
	if err := seedSuppliers(db); err != nil {
		return err
	}
	if err := seedSupplies(db); err != nil {
		return err
	}
	if err := seedBatches(db); err != nil {
		return err
	}
	if err := seedWarehouses(db); err != nil {
		return err
	}
	if err := seedStocks(db); err != nil {
		return err
	}
	if err := seedCarts(db); err != nil {
		return err
	}
	if err := seedCartItems(db); err != nil {
		return err
	}
	if err := seedOrders(db); err != nil {
		return err
	}
	if err := seedOrderItems(db); err != nil {
		return err
	}
	if err := seedReservations(db); err != nil {
		return err
	}

	return nil
}

// Вспомогательные функции
func parseDate(dateStr string) time.Time {
	t, _ := time.Parse("2006-01-02", dateStr)
	return t
}

func parseDateTime(dateTimeStr string) time.Time {
	t, _ := time.Parse("2006-01-02 15:04:05", dateTimeStr)
	return t
}

func intPtr(i int) *int {
	return &i
}

// ===== Seed Functions =====

func seedRoles(db *pg.DB) error {
	roles := []models.Role{
		{Id: 1, UserStatus: "admin"},
		{Id: 2, UserStatus: "customer"},
	}

	for _, role := range roles {
		_, err := db.Model(&role).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedUsers(db *pg.DB) error {
	users := []models.User{
		{Id: 1, Login: "admin", Password: "admin", RoleId: 1, Name: "Администратор Системы"},
		{Id: 2, Login: "ivanov@mail.ru", Password: "hashed_pass_456", RoleId: 2, Name: "Иванов Иван Иванович"},
		{Id: 3, Login: "petrova@gmail.com", Password: "hashed_pass_789", RoleId: 2, Name: "Петрова Мария Сергеевна"},
		{Id: 4, Login: "sidorov@yandex.ru", Password: "hashed_pass_012", RoleId: 2, Name: "Сидоров Алексей Петрович"},
		{Id: 5, Login: "kozlova@mail.ru", Password: "hashed_pass_345", RoleId: 2, Name: "Козлова Анна Викторовна"},
		{Id: 6, Login: "manager@pharmacy.ru", Password: "hashed_manager_678", RoleId: 1, Name: "Менеджер Ольга Николаевна"},
		{Id: 7, Login: "novikov@inbox.ru", Password: "hashed_pass_901", RoleId: 2, Name: "Новиков Дмитрий Андреевич"},
		{Id: 8, Login: "smirnova@gmail.com", Password: "hashed_pass_234", RoleId: 2, Name: "Смирнова Елена Игоревна"},
	}

	for _, user := range users {
		_, err := db.Model(&user).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedUserDiscounts(db *pg.DB) error {
	discounts := []models.UserDiscount{
		{Id: 1, UserId: 2, Percent: 5, ValidFrom: parseDate("2024-01-01"), ValidTo: parseDate("2024-12-31")},
		{Id: 2, UserId: 3, Percent: 10, ValidFrom: parseDate("2024-06-01"), ValidTo: parseDate("2024-08-31")},
		{Id: 3, UserId: 4, Percent: 15, ValidFrom: parseDate("2024-01-01"), ValidTo: parseDate("2025-01-01")},
		{Id: 4, UserId: 5, Percent: 7, ValidFrom: parseDate("2024-03-01"), ValidTo: parseDate("2024-12-31")},
		{Id: 5, UserId: 7, Percent: 3, ValidFrom: parseDate("2024-05-01"), ValidTo: parseDate("2024-07-31")},
	}

	for _, discount := range discounts {
		_, err := db.Model(&discount).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedManufacturers(db *pg.DB) error {
	manufacturers := []models.Manufacturer{
		{Id: 1, Name: "Фармстандарт", Country: "Россия", ContactNumber: "+7-495-970-00-30"},
		{Id: 2, Name: "KRKA", Country: "Словения", ContactNumber: "+386-7-331-2111"},
		{Id: 3, Name: "Bayer", Country: "Германия", ContactNumber: "+49-214-30-1"},
		{Id: 4, Name: "Pfizer", Country: "США", ContactNumber: "+1-212-733-2323"},
		{Id: 5, Name: "Тева", Country: "Израиль", ContactNumber: "+972-3-926-7267"},
		{Id: 6, Name: "Санофи", Country: "Франция", ContactNumber: "+33-1-53-77-40-00"},
		{Id: 7, Name: "Новартис", Country: "Швейцария", ContactNumber: "+41-61-324-1111"},
		{Id: 8, Name: "Биокад", Country: "Россия", ContactNumber: "+7-812-380-49-33"},
		{Id: 9, Name: "Герофарм", Country: "Россия", ContactNumber: "+7-812-703-79-75"},
		{Id: 10, Name: "АстраЗенека", Country: "Великобритания", ContactNumber: "+44-20-7604-8000"},
	}

	for _, manufacturer := range manufacturers {
		_, err := db.Model(&manufacturer).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedDrugTypes(db *pg.DB) error {
	drugTypes := []models.DrugType{
		{Id: 1, Name: "Лекарственные средства", ParentId: nil},
		{Id: 2, Name: "БАДы", ParentId: nil},
		{Id: 3, Name: "Медицинские изделия", ParentId: nil},
		{Id: 4, Name: "Обезболивающие", ParentId: intPtr(1)},
		{Id: 5, Name: "Антибиотики", ParentId: intPtr(1)},
		{Id: 6, Name: "Противовирусные", ParentId: intPtr(1)},
		{Id: 7, Name: "Сердечно-сосудистые", ParentId: intPtr(1)},
		{Id: 8, Name: "Витамины", ParentId: intPtr(2)},
		{Id: 9, Name: "Пробиотики", ParentId: intPtr(2)},
		{Id: 10, Name: "Бинты и пластыри", ParentId: intPtr(3)},
		{Id: 11, Name: "НПВС", ParentId: intPtr(4)},
		{Id: 12, Name: "Анальгетики", ParentId: intPtr(4)},
		{Id: 13, Name: "Пенициллины", ParentId: intPtr(5)},
		{Id: 14, Name: "Макролиды", ParentId: intPtr(5)},
	}

	for _, drugType := range drugTypes {
		_, err := db.Model(&drugType).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedDrugs(db *pg.DB) error {
	drugs := []models.Drug{
		{Id: 1, Name: "Нурофен Экспресс", Category: "Безрецептурный", Description: "Обезболивающее и жаропонижающее средство", INN: "Ибупрофен", TypeId: 11, Dose: "200мг", ManufacturerId: 3, Barcode: 4607027762145},
		{Id: 2, Name: "Парацетамол", Category: "Безрецептурный", Description: "Жаропонижающее и анальгезирующее средство", INN: "Парацетамол", TypeId: 12, Dose: "500мг", ManufacturerId: 1, Barcode: 4602193000123},
		{Id: 3, Name: "Амоксициллин", Category: "Рецептурный", Description: "Антибиотик широкого спектра действия", INN: "Амоксициллин", TypeId: 13, Dose: "500мг", ManufacturerId: 2, Barcode: 3838989512347},
		{Id: 4, Name: "Азитромицин", Category: "Рецептурный", Description: "Антибиотик группы макролидов", INN: "Азитромицин", TypeId: 14, Dose: "250мг", ManufacturerId: 5, Barcode: 4603988012589},
		{Id: 5, Name: "Арбидол", Category: "Безрецептурный", Description: "Противовирусное средство", INN: "Умифеновир", TypeId: 6, Dose: "100мг", ManufacturerId: 1, Barcode: 4602193005678},
		{Id: 6, Name: "Кардиомагнил", Category: "Безрецептурный", Description: "Антиагрегантное средство", INN: "Ацетилсалициловая кислота + Магния гидроксид", TypeId: 7, Dose: "75мг", ManufacturerId: 6, Barcode: 4607027767894},
		{Id: 7, Name: "Витамин D3", Category: "Безрецептурный", Description: "Витаминный препарат", INN: "Колекальциферол", TypeId: 8, Dose: "1000МЕ", ManufacturerId: 7, Barcode: 7613035799851},
		{Id: 8, Name: "Линекс", Category: "Безрецептурный", Description: "Пробиотик для нормализации микрофлоры", INN: "Лебенин", TypeId: 9, Dose: "280мг", ManufacturerId: 2, Barcode: 3838989535261},
		{Id: 9, Name: "Омепразол", Category: "Рецептурный", Description: "Ингибитор протонной помпы", INN: "Омепразол", TypeId: 1, Dose: "20мг", ManufacturerId: 5, Barcode: 4603988045123},
		{Id: 10, Name: "Лоратадин", Category: "Безрецептурный", Description: "Антигистаминное средство", INN: "Лоратадин", TypeId: 1, Dose: "10мг", ManufacturerId: 1, Barcode: 4602193008521},
		{Id: 11, Name: "Но-шпа", Category: "Безрецептурный", Description: "Спазмолитическое средство", INN: "Дротаверин", TypeId: 1, Dose: "40мг", ManufacturerId: 6, Barcode: 5997001336014},
		{Id: 12, Name: "Мезим форте", Category: "Безрецептурный", Description: "Ферментный препарат", INN: "Панкреатин", TypeId: 1, Dose: "10000ЕД", ManufacturerId: 3, Barcode: 4607027764312},
		{Id: 13, Name: "Эссенциале форте Н", Category: "Безрецептурный", Description: "Гепатопротектор", INN: "Фосфолипиды", TypeId: 1, Dose: "300мг", ManufacturerId: 6, Barcode: 4607027769587},
		{Id: 14, Name: "Ингавирин", Category: "Безрецептурный", Description: "Противовирусное средство", INN: "Имидазолилэтанамид пентандиовой кислоты", TypeId: 6, Dose: "90мг", ManufacturerId: 8, Barcode: 4602193012456},
		{Id: 15, Name: "Терафлю", Category: "Безрецептурный", Description: "Комбинированное противопростудное средство", INN: "Парацетамол + Фенирамин + Фенилэфрин", TypeId: 6, Dose: "325мг", ManufacturerId: 7, Barcode: 7613035012345},
	}

	for _, drug := range drugs {
		_, err := db.Model(&drug).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedSuppliers(db *pg.DB) error {
	suppliers := []models.Supplier{
		{Id: 1, Name: "ФармЛогистик", Number: "+7-495-123-45-67"},
		{Id: 2, Name: "МедСнаб", Number: "+7-812-987-65-43"},
		{Id: 3, Name: "Протек", Number: "+7-495-737-35-00"},
		{Id: 4, Name: "Катрен", Number: "+7-383-363-09-50"},
		{Id: 5, Name: "СИА Интернейшнл", Number: "+7-495-961-14-61"},
		{Id: 6, Name: "Пульс", Number: "+7-495-787-62-62"},
	}

	for _, supplier := range suppliers {
		_, err := db.Model(&supplier).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedSupplies(db *pg.DB) error {
	supplies := []models.Supply{
		{Id: 1, SupplierId: 1, SupplyDate: parseDate("2024-05-01"), Status: "Завершена"},
		{Id: 2, SupplierId: 2, SupplyDate: parseDate("2024-05-10"), Status: "Завершена"},
		{Id: 3, SupplierId: 3, SupplyDate: parseDate("2024-05-15"), Status: "Завершена"},
		{Id: 4, SupplierId: 1, SupplyDate: parseDate("2024-06-01"), Status: "Завершена"},
		{Id: 5, SupplierId: 4, SupplyDate: parseDate("2024-06-10"), Status: "Завершена"},
		{Id: 6, SupplierId: 2, SupplyDate: parseDate("2024-06-15"), Status: "В обработке"},
		{Id: 7, SupplierId: 5, SupplyDate: parseDate("2024-06-18"), Status: "В пути"},
		{Id: 8, SupplierId: 3, SupplyDate: parseDate("2024-06-20"), Status: "Оформлена"},
	}

	for _, supply := range supplies {
		_, err := db.Model(&supply).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedBatches(db *pg.DB) error {
	batches := []models.Batch{
		// Поставка 1 (01.05.2024)
		{Id: 1, DrugId: 1, SupplyId: 1, Number: "100", ShelfLife: parseDate("2026-05-01"), ArrivalDate: parseDate("2024-05-01"), Price: 350  },
		{Id: 2, DrugId: 2, SupplyId: 1, Number: "200", ShelfLife: parseDate("2026-03-15"), ArrivalDate: parseDate("2024-05-01"), Price: 45   },
		{Id: 3, DrugId: 5, SupplyId: 1, Number: "150", ShelfLife: parseDate("2025-12-01"), ArrivalDate: parseDate("2024-05-01"), Price: 520  },

		// Поставка 2 (10.05.2024)
		{Id: 4, DrugId: 3, SupplyId: 2, Number: "80", ShelfLife: parseDate("2025-08-20"), ArrivalDate: parseDate("2024-05-10"), Price: 180   },
		{Id: 5, DrugId: 4, SupplyId: 2, Number: "60", ShelfLife: parseDate("2025-10-10"), ArrivalDate: parseDate("2024-05-10"), Price: 290   },
		{Id: 6, DrugId: 8, SupplyId: 2, Number: "120", ShelfLife: parseDate("2026-01-15"), ArrivalDate: parseDate("2024-05-10"), Price: 680  },

		// Поставка 3 (15.05.2024)
		{Id: 7, DrugId: 6, SupplyId: 3, Number: "90", ShelfLife: parseDate("2026-06-30"), ArrivalDate: parseDate("2024-05-15"), Price: 220   },
		{Id: 8, DrugId: 7, SupplyId: 3, Number: "200", ShelfLife: parseDate("2027-01-01"), ArrivalDate: parseDate("2024-05-15"), Price: 410  },
		{Id: 9, DrugId: 9, SupplyId: 3, Number: "100", ShelfLife: parseDate("2025-11-20"), ArrivalDate: parseDate("2024-05-15"), Price: 85   },

		// Поставка 4 (01.06.2024)
		{Id: 10, DrugId: 10, SupplyId: 4, Number: "150", ShelfLife: parseDate("2026-04-01"), ArrivalDate: parseDate("2024-06-01"), Price: 65 },
		{Id: 11, DrugId: 11, SupplyId: 4, Number: "180", ShelfLife: parseDate("2026-09-15"), ArrivalDate: parseDate("2024-06-01"), Price: 190},
		{Id: 12, DrugId: 1, SupplyId: 4, Number: "120", ShelfLife: parseDate("2026-08-01"), ArrivalDate: parseDate("2024-06-01"), Price: 355 },

		// Поставка 5 (10.06.2024)
		{Id: 13, DrugId: 12, SupplyId: 5, Number: "100", ShelfLife: parseDate("2025-12-01"), ArrivalDate: parseDate("2024-06-10"), Price: 320},
		{Id: 14, DrugId: 13, SupplyId: 5, Number: "80", ShelfLife: parseDate("2026-02-28"), ArrivalDate: parseDate("2024-06-10"), Price: 1250},
		{Id: 15, DrugId: 14, SupplyId: 5, Number: "90", ShelfLife: parseDate("2025-09-01"), ArrivalDate: parseDate("2024-06-10"), Price: 580 },
		{Id: 16, DrugId: 15, SupplyId: 5, Number: "110", ShelfLife: parseDate("2025-07-15"), ArrivalDate: parseDate("2024-06-10"), Price: 450},
	}

	for _, batch := range batches {
		_, err := db.Model(&batch).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedWarehouses(db *pg.DB) error {
	warehouses := []models.Warehouse{
		{Id: 1, Name: "Центральный склад", Address: "г. Москва, ул. Складская, д. 15"},
		{Id: 2, Name: "Северный филиал", Address: "г. Санкт-Петербург, пр. Ленина, д. 42"},
		{Id: 3, Name: "Южный филиал", Address: "г. Ростов-на-Дону, ул. Промышленная, д. 8"},
		{Id: 4, Name: "Уральский филиал", Address: "г. Екатеринбург, ул. Логистическая, д. 23"},
	}

	for _, warehouse := range warehouses {
		_, err := db.Model(&warehouse).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedStocks(db *pg.DB) error {
	stocks := []models.Stock{
		// Центральный склад (основной)
		{Id: 1, WarehouseId: 1, BatchId: 1, Amount: 45},
		{Id: 2, WarehouseId: 1, BatchId: 2, Amount: 120},
		{Id: 3, WarehouseId: 1, BatchId: 3, Amount: 80},
		{Id: 4, WarehouseId: 1, BatchId: 4, Amount: 35},
		{Id: 5, WarehouseId: 1, BatchId: 5, Amount: 28},
		{Id: 6, WarehouseId: 1, BatchId: 6, Amount: 65},
		{Id: 7, WarehouseId: 1, BatchId: 7, Amount: 50},
		{Id: 8, WarehouseId: 1, BatchId: 8, Amount: 95},
		{Id: 9, WarehouseId: 1, BatchId: 9, Amount: 55},
		{Id: 10, WarehouseId: 1, BatchId: 10, Amount: 88},
		{Id: 11, WarehouseId: 1, BatchId: 11, Amount: 100},
		{Id: 12, WarehouseId: 1, BatchId: 12, Amount: 70},
		{Id: 13, WarehouseId: 1, BatchId: 13, Amount: 60},
		{Id: 14, WarehouseId: 1, BatchId: 14, Amount: 45},
		{Id: 15, WarehouseId: 1, BatchId: 15, Amount: 50},
		{Id: 16, WarehouseId: 1, BatchId: 16, Amount: 65},

		// Северный филиал
		{Id: 17, WarehouseId: 2, BatchId: 1, Amount: 20},
		{Id: 18, WarehouseId: 2, BatchId: 2, Amount: 50},
		{Id: 19, WarehouseId: 2, BatchId: 3, Amount: 35},
		{Id: 20, WarehouseId: 2, BatchId: 6, Amount: 30},
		{Id: 21, WarehouseId: 2, BatchId: 8, Amount: 55},
		{Id: 22, WarehouseId: 2, BatchId: 11, Amount: 40},
		{Id: 23, WarehouseId: 2, BatchId: 15, Amount: 25},

		// Южный филиал
		{Id: 24, WarehouseId: 3, BatchId: 2, Amount: 30},
		{Id: 25, WarehouseId: 3, BatchId: 4, Amount: 25},
		{Id: 26, WarehouseId: 3, BatchId: 7, Amount: 20},
		{Id: 27, WarehouseId: 3, BatchId: 9, Amount: 30},
		{Id: 28, WarehouseId: 3, BatchId: 10, Amount: 35},
		{Id: 29, WarehouseId: 3, BatchId: 12, Amount: 25},

		// Уральский филиал
		{Id: 30, WarehouseId: 4, BatchId: 1, Amount: 15},
		{Id: 31, WarehouseId: 4, BatchId: 3, Amount: 20},
		{Id: 32, WarehouseId: 4, BatchId: 5, Amount: 18},
		{Id: 33, WarehouseId: 4, BatchId: 8, Amount: 40},
		{Id: 34, WarehouseId: 4, BatchId: 11, Amount: 25},
		{Id: 35, WarehouseId: 4, BatchId: 14, Amount: 20},
	}

	for _, stock := range stocks {
		_, err := db.Model(&stock).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedCarts(db *pg.DB) error {
	carts := []models.Cart{
		{Id: 1, UserId: 2, CreatedAt: parseDateTime("2024-06-18 10:30:00"), UpdatedAt: parseDateTime("2024-06-18 10:45:00"), Status: "Активна"},
		{Id: 2, UserId: 3, CreatedAt: parseDateTime("2024-06-17 14:00:00"), UpdatedAt: parseDateTime("2024-06-17 14:30:00"), Status: "Оформлена"},
		{Id: 3, UserId: 4, CreatedAt: parseDateTime("2024-06-18 09:00:00"), UpdatedAt: time.Time{}, Status: "Активна"},
		{Id: 4, UserId: 5, CreatedAt: parseDateTime("2024-06-16 18:20:00"), UpdatedAt: parseDateTime("2024-06-16 18:45:00"), Status: "Брошена"},
		{Id: 5, UserId: 7, CreatedAt: parseDateTime("2024-06-18 11:00:00"), UpdatedAt: parseDateTime("2024-06-18 11:15:00"), Status: "Активна"},
	}

	for _, cart := range carts {
		_, err := db.Model(&cart).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedCartItems(db *pg.DB) error {
	cartItems := []models.CartItem{
		// Корзина Иванова
		{Id: 1, CartId: 1, DrugId: 1, Quantity: 2},
		{Id: 2, CartId: 1, DrugId: 7, Quantity: 1},
		{Id: 3, CartId: 1, DrugId: 10, Quantity: 3},

		// Корзина Петровой (оформлена в заказ)
		{Id: 4, CartId: 2, DrugId: 5, Quantity: 1},
		{Id: 5, CartId: 2, DrugId: 15, Quantity: 2},

		// Корзина Сидорова
		{Id: 6, CartId: 3, DrugId: 6, Quantity: 1},
		{Id: 7, CartId: 3, DrugId: 13, Quantity: 1},

		// Брошенная корзина Козловой
		{Id: 8, CartId: 4, DrugId: 2, Quantity: 2},
		{Id: 9, CartId: 4, DrugId: 11, Quantity: 1},

		// Корзина Новикова
		{Id: 10, CartId: 5, DrugId: 8, Quantity: 2},
		{Id: 11, CartId: 5, DrugId: 12, Quantity: 1},
	}

	for _, cartItem := range cartItems {
		_, err := db.Model(&cartItem).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedOrders(db *pg.DB) error {
	orders := []models.Order{
		{Id: 1, UserId: 3, Discount: 10, CreatedAt: parseDateTime("2024-06-17 14:35:00"), TotalAmount: 1395, Status: "Доставлен", DeliveryType: "Курьер"},
		{Id: 2, UserId: 2, Discount: 5, CreatedAt: parseDateTime("2024-06-15 12:00:00"), TotalAmount: 855, Status: "Доставлен", DeliveryType: "Самовывоз"},
		{Id: 3, UserId: 4, Discount: 15, CreatedAt: parseDateTime("2024-06-16 10:20:00"), TotalAmount: 2125, Status: "В обработке", DeliveryType: "Курьер"},
		{Id: 4, UserId: 5, Discount: 7, CreatedAt: parseDateTime("2024-06-14 16:45:00"), TotalAmount: 520, Status: "Доставлен", DeliveryType: "Самовывоз"},
		{Id: 5, UserId: 7, Discount: 3, CreatedAt: parseDateTime("2024-06-17 09:30:00"), TotalAmount: 1680, Status: "Собирается", DeliveryType: "Курьер"},
		{Id: 6, UserId: 2, Discount: 5, CreatedAt: parseDateTime("2024-06-10 11:15:00"), TotalAmount: 410, Status: "Доставлен", DeliveryType: "Самовывоз"},
		{Id: 7, UserId: 8, Discount: 0, CreatedAt: parseDateTime("2024-06-18 08:00:00"), TotalAmount: 580, Status: "Оформлен", DeliveryType: "Курьер"},
		{Id: 8, UserId: 3, Discount: 10, CreatedAt: parseDateTime("2024-06-05 13:00:00"), TotalAmount: 935, Status: "Доставлен", DeliveryType: "Самовывоз"},
	}

	for _, order := range orders {
		_, err := db.Model(&order).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedOrderItems(db *pg.DB) error {
	orderItems := []models.OrderItem{
		// Заказ 1 (Петрова) - Арбидол + Терафлю
		{Id: 1, OrderId: 1, BatchId: 3, Quantity: 1, Price: 520},
		{Id: 2, OrderId: 1, BatchId: 16, Quantity: 2, Price: 450},

		// Заказ 2 (Иванов) - Нурофен + Витамин D3 + Омепразол
		{Id: 3, OrderId: 2, BatchId: 1, Quantity: 1, Price: 350},
		{Id: 4, OrderId: 2, BatchId: 8, Quantity: 1, Price: 410},
		{Id: 5, OrderId: 2, BatchId: 9, Quantity: 1, Price: 85},

		// Заказ 3 (Сидоров) - Эссенциале + Но-шпа + Кардиомагнил
		{Id: 6, OrderId: 3, BatchId: 14, Quantity: 1, Price: 1250},
		{Id: 7, OrderId: 3, BatchId: 11, Quantity: 2, Price: 190},
		{Id: 8, OrderId: 3, BatchId: 7, Quantity: 2, Price: 220},

		// Заказ 4 (Козлова) - Арбидол
		{Id: 9, OrderId: 4, BatchId: 3, Quantity: 1, Price: 520},

		// Заказ 5 (Новиков) - Линекс + Мезим + Ингавирин
		{Id: 10, OrderId: 5, BatchId: 6, Quantity: 1, Price: 680},
		{Id: 11, OrderId: 5, BatchId: 13, Quantity: 1, Price: 320},
		{Id: 12, OrderId: 5, BatchId: 15, Quantity: 1, Price: 580},

		// Заказ 6 (Иванов) - Витамин D3
		{Id: 13, OrderId: 6, BatchId: 8, Quantity: 1, Price: 410},

		// Заказ 7 (Смирнова) - Ингавирин
		{Id: 14, OrderId: 7, BatchId: 15, Quantity: 1, Price: 580},

		// Заказ 8 (Петрова) - Парацетамол + Но-шпа + Арбидол
		{Id: 15, OrderId: 8, BatchId: 2, Quantity: 3, Price: 45},
		{Id: 16, OrderId: 8, BatchId: 11, Quantity: 1, Price: 190},
		{Id: 17, OrderId: 8, BatchId: 3, Quantity: 1, Price: 520},
	}

	for _, orderItem := range orderItems {
		_, err := db.Model(&orderItem).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}

func seedReservations(db *pg.DB) error {
	reservations := []models.Reservation{
		// Резервации для заказа 3 (В обработке)
		{Id: 1, BatchId: 14, OrderId: 3, Quantity: 1, ExpiresAt: parseDate("2024-06-19")},
		{Id: 2, BatchId: 11, OrderId: 3, Quantity: 2, ExpiresAt: parseDate("2024-06-19")},
		{Id: 3, BatchId: 7, OrderId: 3, Quantity: 2, ExpiresAt: parseDate("2024-06-19")},

		// Резервации для заказа 5 (Собирается)
		{Id: 4, BatchId: 6, OrderId: 5, Quantity: 1, ExpiresAt: parseDate("2024-06-20")},
		{Id: 5, BatchId: 13, OrderId: 5, Quantity: 1, ExpiresAt: parseDate("2024-06-20")},
		{Id: 6, BatchId: 15, OrderId: 5, Quantity: 1, ExpiresAt: parseDate("2024-06-20")},

		// Резервации для заказа 7 (Оформлен)
		{Id: 7, BatchId: 15, OrderId: 7, Quantity: 1, ExpiresAt: parseDate("2024-06-21")},
	}

	for _, reservation := range reservations {
		_, err := db.Model(&reservation).OnConflict("DO NOTHING").Insert()
		if err != nil {
			return err
		}
	}
	return nil
}
