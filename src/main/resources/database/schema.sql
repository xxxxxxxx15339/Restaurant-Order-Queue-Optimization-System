CREATE TABLE order_queue (
    queue_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    position INTEGER NOT NULL CHECK(position > 0),
    algorithm_used TEXT NOT NULL CHECK(algorithm_used IN ('SPT', 'ROUND_ROBIN', 'PRIORITY')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);  

CREATE TABLE orders (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    items TEXT NOT NULL,
    table_id INTEGER NOT NULL,
    staff_id INTEGER,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status TEXT DEFAULT 'PENDING' CHECK(status IN ('PENDING', 'IN_PROGRESS', 'READY', 'SERVED', 'CANCELLED')),
    priority INTEGER DEFAULT 5 CHECK(priority BETWEEN 1 AND 5),
    estimated_prep_time INTEGER CHECK(estimated_prep_time IS NULL OR (estimated_prep_time > 0 AND estimated_prep_time <= 120)),
    actual_prep_time INTEGER CHECK(actual_prep_time IS NULL OR (actual_prep_time > 0 AND actual_prep_time <= 120)),
    total_amount DECIMAL(10, 2) CHECK(total_amount >= 0),
    FOREIGN KEY (table_id) REFERENCES tables(table_id),
    FOREIGN KEY (staff_id) REFERENCES staff(staff_id)
);

CREATE TABLE staff (
    staff_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('WAITER', 'COOK', 'MANAGER')),
    status TEXT NOT NULL DEFAULT 'ACTIVE' CHECK(status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE tables (
    table_id INTEGER PRIMARY KEY AUTOINCREMENT,
    capacity INTEGER NOT NULL DEFAULT 4 CHECK(capacity BETWEEN 1 AND 8),
    status TEXT NOT NULL DEFAULT 'AVAILABLE' CHECK(status IN ('AVAILABLE', 'OCCUPIED', 'RESERVED'))
);



