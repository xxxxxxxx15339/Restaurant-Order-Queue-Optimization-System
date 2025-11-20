# SQL & Database Relationships Guide

## What is SQL?

**SQL (Structured Query Language)** is a programming language designed for managing and manipulating relational databases. It allows you to:
- Create and modify database structures (tables, indexes)
- Insert, update, and delete data
- Query and retrieve data
- Manage database security and permissions

SQL works with **relational databases** (like SQLite, MySQL, PostgreSQL) where data is stored in **tables** (like spreadsheets) with **rows** (records) and **columns** (fields).

---

## Database Relationships

In relational databases, tables can be connected through **relationships**. There are three main types:

### 1. One-to-One (1:1) Relationship
**Definition:** Each record in Table A relates to exactly ONE record in Table B, and vice versa.

**Example:**
- One `Staff` member has ONE `EmployeeProfile`
- One `Table` has ONE `CurrentOrder` (at a time)

```sql
-- Staff table
CREATE TABLE staff (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT UNIQUE
);

-- EmployeeProfile table (one-to-one with staff)
CREATE TABLE employee_profile (
    id INTEGER PRIMARY KEY,
    staff_id INTEGER UNIQUE,  -- UNIQUE ensures one-to-one
    phone TEXT,
    address TEXT,
    FOREIGN KEY (staff_id) REFERENCES staff(id)
);
```

**Visual:**
```
Staff (1) ────── (1) EmployeeProfile
```

---

### 2. One-to-Many (1:N) Relationship
**Definition:** One record in Table A can relate to MANY records in Table B, but each record in Table B relates to only ONE record in Table A.

**Example:**
- One `Table` can have MANY `Orders` (over time)
- One `Staff` member can handle MANY `Orders`
- One `Order` has MANY `OrderItems` (one order, multiple dishes)

```sql
-- Table table
CREATE TABLE tables (
    id INTEGER PRIMARY KEY,
    table_number INTEGER UNIQUE NOT NULL,
    capacity INTEGER NOT NULL,
    status TEXT DEFAULT 'AVAILABLE'
);

-- Orders table (many orders can belong to one table)
CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    table_id INTEGER NOT NULL,  -- Foreign key to tables
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TEXT DEFAULT 'PENDING',
    FOREIGN KEY (table_id) REFERENCES tables(id)
);
```

**Visual:**
```
Tables (1) ────── (Many) Orders
  │
  └── Table #1 has Orders: [Order 1, Order 5, Order 12]
```

**Key Point:** The "Many" side (Orders) has the **foreign key** (`table_id`) pointing to the "One" side (Tables).

---

### 3. Many-to-Many (M:N) Relationship
**Definition:** Records in Table A can relate to MANY records in Table B, and records in Table B can relate to MANY records in Table A.

**Example:**
- Many `Orders` can have many `MenuItems` (dishes)
- Many `Staff` members can handle many `Orders`

To implement this, you need a **junction/bridge table** (also called an association table):

```sql
-- Orders table
CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    table_id INTEGER,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES tables(id)
);

-- MenuItems table
CREATE TABLE menu_items (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category TEXT
);

-- Junction table for many-to-many relationship
CREATE TABLE order_items (
    id INTEGER PRIMARY KEY,
    order_id INTEGER NOT NULL,
    menu_item_id INTEGER NOT NULL,
    quantity INTEGER DEFAULT 1,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
    UNIQUE(order_id, menu_item_id)  -- Prevents duplicate entries
);
```

**Visual:**
```
Orders (Many) ──── OrderItems ──── (Many) MenuItems
       │                │                │
       └── Order #1     │                └── Pizza
       └── Order #2 ────┼─────────────────── Burger
                        │                └── Salad
                        └── Links them together
```

**Key Point:** The junction table contains **two foreign keys** - one pointing to each related table.

---

## Basic SQL Syntax

### 1. CREATE TABLE - Creating Tables

```sql
CREATE TABLE table_name (
    column1_name data_type constraints,
    column2_name data_type constraints,
    ...
    PRIMARY KEY (column_name),
    FOREIGN KEY (column_name) REFERENCES other_table(column_name)
);
```

**Common Data Types:**
- `INTEGER` - Whole numbers (1, 2, -5, 100)
- `TEXT` - Text strings ('Hello', 'John Doe')
- `REAL` - Decimal numbers (3.14, 99.99)
- `DECIMAL(10,2)` - Precise decimal (99999999.99)
- `TIMESTAMP` - Date and time ('2025-11-20 14:30:00')
- `BOOLEAN` - True/False (SQLite uses INTEGER: 0 or 1)

**Common Constraints:**
- `PRIMARY KEY` - Unique identifier for each row
- `NOT NULL` - Column cannot be empty
- `UNIQUE` - Values must be unique
- `DEFAULT value` - Default value if not provided
- `FOREIGN KEY` - References another table's primary key

**Example:**
```sql
CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,  -- Auto-incrementing ID
    table_id INTEGER NOT NULL,
    staff_id INTEGER,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TEXT DEFAULT 'PENDING' NOT NULL,
    total_amount DECIMAL(10,2),
    FOREIGN KEY (table_id) REFERENCES tables(id),
    FOREIGN KEY (staff_id) REFERENCES staff(id)
);
```

---

### 2. INSERT - Adding Data

```sql
-- Insert single record
INSERT INTO table_name (column1, column2, ...) 
VALUES (value1, value2, ...);

-- Insert multiple records
INSERT INTO table_name (column1, column2) 
VALUES 
    (value1, value2),
    (value3, value4);
```

**Example:**
```sql
-- Insert a new tablei
INSERT INTO tables (table_number, capacity, status) 
VALUES (1, 4, 'AVAILABLE');

-- Insert an order
INSERT INTO orders (table_id, status, total_amount) 
VALUES (1, 'PENDING', 45.50);

-- Insert with auto-generated values (CURRENT_TIMESTAMP)
INSERT INTO orders (table_id, status) 
VALUES (1, 'PENDING');  -- order_time will be set automatically
```

---

### 3. SELECT - Retrieving Data

```sql
-- Select all columns
SELECT * FROM table_name;

-- Select specific columns
SELECT column1, column2 FROM table_name;

-- Select with conditions (WHERE)
SELECT * FROM table_name WHERE condition;

-- Select with ordering
SELECT * FROM table_name ORDER BY column_name ASC/DESC;

-- Select with limits
SELECT * FROM table_name LIMIT number;
```

**WHERE Clause Operators:**
- `=` - Equals
- `!=` or `<>` - Not equals
- `>` `<` `>=` `<=` - Comparison
- `LIKE` - Pattern matching ('John%' matches 'John Doe')
- `IN` - Match any value in list
- `AND` / `OR` - Logical operators
- `IS NULL` / `IS NOT NULL` - Check for null values

**Example:**
```sql
-- Get all orders
SELECT * FROM orders;

-- Get pending orders
SELECT * FROM orders WHERE status = 'PENDING';

-- Get orders from table 1
SELECT * FROM orders WHERE table_id = 1;

-- Get orders with amount > 50, ordered by time
SELECT * FROM orders 
WHERE total_amount > 50 
ORDER BY order_time DESC;

-- Get orders with complex conditions
SELECT * FROM orders 
WHERE status = 'PENDING' 
AND table_id IN (1, 2, 3)
ORDER BY order_time ASC;
```

---

### 4. UPDATE - Modifying Data

```sql
UPDATE table_name 
SET column1 = value1, column2 = value2, ...
WHERE condition;
```

**⚠️ WARNING:** Always use WHERE clause! Without it, ALL rows will be updated.

**Example:**
```sql
-- Update order status
UPDATE orders 
SET status = 'PREPARING' 
WHERE id = 1;

-- Update multiple columns
UPDATE orders 
SET status = 'READY', total_amount = 50.00 
WHERE id = 5;

-- Update all orders for a table
UPDATE orders 
SET status = 'CANCELLED' 
WHERE table_id = 3 AND status = 'PENDING';
```

---

### 5. DELETE - Removing Data

```sql
DELETE FROM table_name WHERE condition;
```

**⚠️ WARNING:** Always use WHERE clause! Without it, ALL rows will be deleted.

**Example:**
```sql
-- Delete specific order
DELETE FROM orders WHERE id = 1;

-- Delete cancelled orders older than 30 days
DELETE FROM orders 
WHERE status = 'CANCELLED' 
AND order_time < datetime('now', '-30 days');
```

---

### 6. JOIN - Combining Tables

JOINs allow you to combine data from multiple tables.

#### INNER JOIN
Returns only rows that have matching values in both tables.

```sql
SELECT columns
FROM table1
INNER JOIN table2 ON table1.column = table2.column;
```

**Example:**
```sql
-- Get orders with table information
SELECT orders.id, orders.status, tables.table_number
FROM orders
INNER JOIN tables ON orders.table_id = tables.id;
```

#### LEFT JOIN
Returns all rows from the left table, and matched rows from the right table. If no match, NULL values are returned.

```sql
SELECT columns
FROM table1
LEFT JOIN table2 ON table1.column = table2.column;
```

**Example:**
```sql
-- Get all tables, even if they have no orders
SELECT tables.table_number, orders.id as order_id, orders.status
FROM tables
LEFT JOIN orders ON tables.id = orders.table_id;
```

#### Multiple JOINs
```sql
-- Join multiple tables
SELECT 
    orders.id,
    orders.status,
    tables.table_number,
    staff.name as staff_name
FROM orders
INNER JOIN tables ON orders.table_id = tables.id
LEFT JOIN staff ON orders.staff_id = staff.id;
```

---

### 7. Aggregate Functions

Functions that perform calculations on groups of rows.

- `COUNT()` - Count rows
- `SUM()` - Sum values
- `AVG()` - Average values
- `MAX()` / `MIN()` - Maximum/Minimum values

```sql
-- Count all orders
SELECT COUNT(*) FROM orders;

-- Count pending orders
SELECT COUNT(*) FROM orders WHERE status = 'PENDING';

-- Sum total amount for a table
SELECT SUM(total_amount) FROM orders WHERE table_id = 1;

-- Average order amount
SELECT AVG(total_amount) FROM orders;

-- Group by status
SELECT status, COUNT(*) as count 
FROM orders 
GROUP BY status;
```

---

### 8. ALTER TABLE - Modifying Table Structure

```sql
-- Add a column
ALTER TABLE table_name ADD COLUMN column_name data_type;

-- Rename table
ALTER TABLE old_name RENAME TO new_name;
```

**Example:**
```sql
ALTER TABLE orders ADD COLUMN notes TEXT;
```

---

### 9. DROP TABLE - Deleting Tables

```sql
-- Delete a table (⚠️ removes all data)
DROP TABLE IF EXISTS table_name;
```

---

## Example: Complete Restaurant Schema

```sql
-- Tables table
CREATE TABLE tables (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    table_number INTEGER UNIQUE NOT NULL,
    capacity INTEGER NOT NULL,
    status TEXT DEFAULT 'AVAILABLE' CHECK(status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED'))
);

-- Staff table
CREATE TABLE staff (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    role TEXT NOT NULL,
    status TEXT DEFAULT 'ACTIVE'
);

-- Orders table (One-to-Many with tables and staff)
CREATE TABLE orders (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    table_id INTEGER NOT NULL,
    staff_id INTEGER,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'PREPARING', 'READY', 'SERVED', 'CANCELLED')),
    priority INTEGER DEFAULT 5,
    estimated_time INTEGER,  -- minutes
    total_amount DECIMAL(10,2),
    FOREIGN KEY (table_id) REFERENCES tables(id),
    FOREIGN KEY (staff_id) REFERENCES staff(id)
);

-- Menu items
CREATE TABLE menu_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    category TEXT,
    preparation_time INTEGER  -- minutes
);

-- Order items (Junction table for many-to-many: orders <-> menu_items)
CREATE TABLE order_items (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    menu_item_id INTEGER NOT NULL,
    quantity INTEGER DEFAULT 1,
    notes TEXT,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
    UNIQUE(order_id, menu_item_id)
);
```

---

## Key Takeaways for Your Project

1. **Primary Keys:** Every table needs a unique identifier (usually `id`)
2. **Foreign Keys:** Connect related tables (orders.table_id → tables.id)
3. **Relationships:**
   - One-to-Many: Foreign key on the "many" side
   - Many-to-Many: Use a junction table
4. **Constraints:** Use NOT NULL, UNIQUE, CHECK for data integrity
5. **CASCADE:** Use `ON DELETE CASCADE` to automatically delete related records

---

## Next Steps

Now you can create your `schema.sql` file with these concepts in mind! 🎯

