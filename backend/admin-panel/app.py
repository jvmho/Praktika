import tkinter as tk
from tkinter import ttk, messagebox, simpledialog
import requests

BASE_URL = "http://localhost:8000/api/v1"

TABLES = {
    "Пользователи": "/users",
    "Роли": "/roles",
    "Препараты": "/drugs",
    "Производители": "/manufacturers",
    "Поставщики": "/suppliers",
    "Поставки": "/supplies",
    "Партии": "/batches",
    "Склады": "/warehouses",
    "Сток": "/stocks",
    "Заказы": "/orders",
    "Корзины": "/carts",
}

# Соответствие таблицы - её внешние ключи
FOREIGN_KEYS = {
    "/users": {"roleID": "Role"},
    "/roles": {},
    "/drugs": {"typeID": "DrugType", "manufacturerID": "Manufacturer"},
    "/manufacturers": {},
    "/suppliers": {},
    "/supplies": {"supplierID": "Supplier"},
    "/batches": {"drugID": "Drug", "supplyID": "Supply"},
    "/stocks": {"warehouseID": "Warehouse", "batchID": "Batch"},
    "/orders": {"userID": "User"},
    "/carts": {"userID": "User"},
}

class App:
    def __init__(self, root):
        self.root = root
        self.root.title("Система управления аптекой")
        self.root.geometry("1200x700")

        self.selected_table = None
        self.data = []
        self.references_cache = {}

        # Выбор таблицы
        label = tk.Label(root, text="Выберите таблицу:")
        label.pack(pady=5)
        
        self.combo = ttk.Combobox(root, values=list(TABLES.keys()), width=30)
        self.combo.pack(pady=5)

        self.load_btn = tk.Button(root, text="Загрузить", command=self.load_data, bg="lightgreen")
        self.load_btn.pack()

        # Таблица
        self.tree = ttk.Treeview(root)
        self.tree.pack(expand=True, fill='both', pady=10)

        # Кнопки CRUD и статистика
        btn_frame = tk.Frame(root)
        btn_frame.pack(pady=10)

        tk.Button(btn_frame, text="Добавить", command=self.add_item, bg="lightblue", width=12).grid(row=0, column=0, padx=3)
        tk.Button(btn_frame, text="Редактировать", command=self.edit_item, bg="lightyellow", width=12).grid(row=0, column=1, padx=3)
        tk.Button(btn_frame, text="Удалить", command=self.delete_item, bg="lightcoral", width=12).grid(row=0, column=2, padx=3)
        tk.Button(btn_frame, text="📊 Склад", command=self.show_stock_stats, bg="lightgray", width=12).grid(row=0, column=3, padx=3)
        tk.Button(btn_frame, text="📋 Брони", command=self.show_reservation_stats, bg="lightgray", width=12).grid(row=0, column=4, padx=3)

    def load_data(self):
        table_name = self.combo.get()
        if not table_name:
            messagebox.showwarning("Внимание", "Выберите таблицу")
            return

        endpoint = TABLES[table_name]
        self.selected_table = endpoint

        try:
            response = requests.get(BASE_URL + endpoint)
            self.data = response.json()

            if isinstance(self.data, dict):
                self.data = [self.data]

            self.render_table()
            messagebox.showinfo("Успех", f"Загружено {len(self.data)} записей")

        except Exception as e:
            messagebox.showerror("Ошибка", f"Ошибка загрузки: {str(e)}")

    def render_table(self):
        self.tree.delete(*self.tree.get_children())

        if not self.data:
            messagebox.showinfo("Информация", "Нет данных")
            return

        columns = list(self.data[0].keys())
        self.tree["columns"] = columns
        self.tree["show"] = "headings"

        for col in columns:
            self.tree.heading(col, text=col)
            self.tree.column(col, width=100)

        for row in self.data:
            values = [str(row.get(col, "")) for col in columns]
            self.tree.insert("", tk.END, values=values)

    def add_item(self):
        if not self.selected_table:
            messagebox.showwarning("Внимание", "Сначала загрузите таблицу")
            return

        self.load_references()
        data = self.prompt_fields()
        if not data:
            return

        try:
            response = requests.post(BASE_URL + self.selected_table, json=data)
            if response.status_code == 200:
                messagebox.showinfo("Успех", "Запись добавлена")
                self.load_data()
            else:
                messagebox.showerror("Ошибка", f"Ошибка сервера: {response.text}")
        except Exception as e:
            messagebox.showerror("Ошибка", str(e))

    def edit_item(self):
        selected = self.tree.selection()
        if not selected:
            messagebox.showwarning("Внимание", "Выберите запись для редактирования")
            return

        index = self.tree.index(selected[0])
        item = self.data[index]

        self.load_references()
        data = self.prompt_fields(item)
        if not data:
            return

        try:
            item_id = None
            for key in item.keys():
                if key.endswith("ID"):
                    item_id = item[key]
                    break
            
            if item_id is None:
                messagebox.showerror("Ошибка", "Не найден ID записи")
                return
            
            response = requests.put(f"{BASE_URL}{self.selected_table}/{item_id}", json=data)
            if response.status_code == 200:
                messagebox.showinfo("Успех", "Запись обновлена")
                self.load_data()
            else:
                messagebox.showerror("Ошибка", f"Ошибка сервера: {response.text}")
        except Exception as e:
            messagebox.showerror("Ошибка", str(e))

    def delete_item(self):
        selected = self.tree.selection()
        if not selected:
            messagebox.showwarning("Внимание", "Выберите запись для удаления")
            return

        if not messagebox.askyesno("Подтверждение", "Вы уверены?"):
            return

        index = self.tree.index(selected[0])
        item = self.data[index]

        try:
            item_id = None
            for key in item.keys():
                if key.endswith("ID"):
                    item_id = item[key]
                    break
            
            if item_id is None:
                messagebox.showerror("Ошибка", "Не найден ID записи")
                return
            
            response = requests.delete(f"{BASE_URL}{self.selected_table}/{item_id}")
            if response.status_code == 200:
                messagebox.showinfo("Успех", "Запись удалена")
                self.load_data()
            else:
                messagebox.showerror("Ошибка", f"Ошибка сервера: {response.text}")
        except Exception as e:
            messagebox.showerror("Ошибка", str(e))

    def load_references(self):
        """Загружает справочные данные для внешних ключей"""
        try:
            ref_tables = ["Role", "Manufacturer", "DrugType", "Drug", "Supplier", "Supply", "User", "Warehouse", "Batch"]
            
            for table in ref_tables:
                if table not in self.references_cache:
                    response = requests.get(f"{BASE_URL}/statistics/references?table_name={table}")
                    self.references_cache[table] = response.json()["data"]
        except Exception as e:
            print(f"Ошибка загрузки справочников: {e}")

    def prompt_fields(self, existing=None):
        """Открывает диалог для ввода данных с поддержкой внешних ключей"""
        if not self.data:
            return {}
        
        table_name = self.combo.get()
        endpoint = TABLES.get(table_name, self.selected_table)
        fk_map = FOREIGN_KEYS.get(endpoint, {})
        
        result = {}
        fields = list(self.data[0].keys())
        
        for field in fields:
            # Пропускаем ID поля, которые не являются внешними ключами
            if field.endswith("ID") and field not in fk_map:
                continue
            
            # Если это внешний ключ, создаем выпадающий список
            if field in fk_map:
                ref_table = fk_map[field]
                value = self.show_reference_dialog(ref_table, existing, field)
                if value is not None:
                    result[field] = value
            else:
                # Обычное текстовое поле
                value = simpledialog.askstring(
                    "Ввод", 
                    f"{field}:", 
                    initialvalue=str((existing or {}).get(field, ""))
                )
                if value is not None:
                    result[field] = value

        return result

    def show_reference_dialog(self, ref_table, existing, field_name):
        """Показывает диалог выбора из справочника"""
        if ref_table not in self.references_cache:
            messagebox.showerror("Ошибка", f"Справочник {ref_table} не загружен")
            return None
        
        refs = self.references_cache[ref_table]
        if not refs:
            messagebox.showwarning("Внимание", f"Нет данных в справочнике {ref_table}")
            return None
        
        # Создаем диалог выбора
        dialog = tk.Toplevel(self.root)
        dialog.title(f"Выберите {ref_table}")
        dialog.geometry("300x400")
        
        listbox = tk.Listbox(dialog)
        listbox.pack(fill=tk.BOTH, expand=True, padx=10, pady=10)
        
        ref_map = {}
        for ref_id, ref_name in refs:
            listbox.insert(tk.END, ref_name)
            ref_map[ref_name] = ref_id
        
        selected = [None]
        
        def on_select():
            idx = listbox.curselection()
            if idx:
                selected[0] = ref_map[listbox.get(idx[0])]
            dialog.destroy()
        
        tk.Button(dialog, text="Выбрать", command=on_select).pack(pady=10)
        
        self.root.wait_window(dialog)
        return selected[0]

    def show_stock_stats(self):
        """Показывает статистику по складу"""
        try:
            response = requests.get(f"{BASE_URL}/statistics/stock")
            stats = response.json()
            
            msg = f"""СТАТИСТИКА ПО СКЛАДУ

Всего позиций: {stats['total_items']}
Количество артикулов: {stats['total_positions']}

ПО СКЛАДАМ:
"""
            for warehouse, amount in stats['by_warehouse'].items():
                msg += f"\n  {warehouse}: {amount} шт."
            
            msg += f"\n\nПО ПРЕПАРАТАМ (первые 10):"
            for drug, amount in list(stats['by_drug'].items())[:10]:
                msg += f"\n  {drug}: {amount} шт."
            
            messagebox.showinfo("Склад", msg)
        except Exception as e:
            messagebox.showerror("Ошибка", f"Ошибка загрузки статистики: {str(e)}")

    def show_reservation_stats(self):
        """Показывает статистику по бронированиям"""
        try:
            response = requests.get(f"{BASE_URL}/statistics/reservations")
            stats = response.json()
            
            msg = f"""СТАТИСТИКА ПО БРОНИРОВАНИЯМ

Всего броней: {stats['total_reservations']}
Пользователей с бронями: {stats['total_users']}

ПО ДАТАМ ИСТЕЧЕНИЯ:
"""
            for date, data in stats['by_expiry_date'].items():
                msg += f"\n  {date}:"
                msg += f"\n    Броней: {data['count']}"
                msg += f"\n    Количество: {data['total_quantity']} шт."
                users_str = ', '.join(data['users'][:5])
                if len(data['users']) > 5:
                    users_str += f" и еще {len(data['users']) - 5}"
                msg += f"\n    Пользователи: {users_str}"
            
            messagebox.showinfo("Брони", msg)
        except Exception as e:
            messagebox.showerror("Ошибка", f"Ошибка загрузки статистики: {str(e)}")


root = tk.Tk()
app = App(root)
root.mainloop()