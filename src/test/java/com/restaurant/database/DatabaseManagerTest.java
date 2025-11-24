package com.restaurant.database;

import com.restaurant.models.Order;
import com.restaurant.models.OrderStatus;
import com.restaurant.models.Staff;
import com.restaurant.models.StaffRole;
import com.restaurant.models.StaffStatus;
import com.restaurant.models.Table;
import com.restaurant.models.TableStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @TempDir
    Path tempDir;

    private Connection connection;

    @BeforeEach
    void setUp() throws Exception {
        Path dbPath = tempDir.resolve("test-db-" + System.nanoTime() + ".sqlite");
        String url = "jdbc:sqlite:" + dbPath.toAbsolutePath();
        DatabaseConnection.setDatabaseUrl(url);
        connection = DatabaseConnection.getConnection();
        enableForeignKeys(connection);
    }

    @AfterEach
    void tearDown() {
        DatabaseConnection.closeConnection();
    }

    @Test
    void orderCrudFlow() throws SQLException {
        int tableId = insertTableRow(4, TableStatus.AVAILABLE);
        int staffId = insertStaffRow("Test Waiter", StaffRole.WAITER, StaffStatus.ACTIVE);

        Order order = buildOrder(tableId, staffId);
        assertTrue(DatabaseManager.addOrder(order));

        var pending = DatabaseManager.getOrdersByStatus(OrderStatus.PENDING);
        assertEquals(1, pending.size());
        Order stored = pending.get(0);
        assertEquals("Test Dish", stored.getItems());
        assertEquals(staffId, stored.getStaffId());

        assertTrue(DatabaseManager.updateOrderStatus(stored.getOrderId(), OrderStatus.READY));
        Order refreshed = DatabaseManager.getOrderById(stored.getOrderId());
        assertNotNull(refreshed);
        assertEquals(OrderStatus.READY, refreshed.getStatus());

        assertTrue(DatabaseManager.deleteOrder(stored.getOrderId()));
        assertNull(DatabaseManager.getOrderById(stored.getOrderId()));
    }

    @Test
    void tableCrudFlow() {
        Table table = new Table();
        table.setCapacity(2);
        table.setStatus(TableStatus.AVAILABLE);
        assertTrue(DatabaseManager.addTable(table));

        var tables = DatabaseManager.getAllTables();
        assertEquals(1, tables.size());
        int tableId = tables.get(0).getTableId();

        assertTrue(DatabaseManager.updateTableStatus(tableId, TableStatus.OCCUPIED));
        assertEquals(TableStatus.OCCUPIED, DatabaseManager.getAllTables().get(0).getStatus());

        assertTrue(DatabaseManager.deleteTable(tableId));
        assertTrue(DatabaseManager.getAllTables().isEmpty());
    }

    @Test
    void staffCrudFlow() {
        Staff staff = new Staff();
        staff.setName("Alice");
        staff.setRole(StaffRole.WAITER);
        staff.setStatus(StaffStatus.ACTIVE);
        assertTrue(DatabaseManager.addStaff(staff));

        var staffList = DatabaseManager.getAllStaff();
        assertEquals(1, staffList.size());
        int staffId = staffList.get(0).getStaffId();

        assertTrue(DatabaseManager.updateStaffStatus(staffId, StaffStatus.INACTIVE));
        assertTrue(DatabaseManager.updateStaffRole(staffId, StaffRole.MANAGER));

        Staff updated = DatabaseManager.getAllStaff().get(0);
        assertEquals(StaffStatus.INACTIVE, updated.getStatus());
        assertEquals(StaffRole.MANAGER, updated.getRole());

        assertTrue(DatabaseManager.deleteStaff(staffId));
        assertTrue(DatabaseManager.getAllStaff().isEmpty());
    }

    @Test
    void queueOperations() throws SQLException {
        int tableId = insertTableRow(4, TableStatus.AVAILABLE);
        int staffId = insertStaffRow("Queue Tester", StaffRole.WAITER, StaffStatus.ACTIVE);

        Order order = buildOrder(tableId, staffId);
        assertTrue(DatabaseManager.addOrder(order));
        int orderId = DatabaseManager.getOrdersByStatus(OrderStatus.PENDING).get(0).getOrderId();

        assertTrue(DatabaseManager.insertQueueEntry(orderId, 1, "SPT"));
        var queue = DatabaseManager.getQueueByAlgorithm("SPT");
        assertEquals(1, queue.size());
        assertEquals(orderId, queue.get(0).getOrderId());

        assertTrue(DatabaseManager.clearQueue("SPT"));
        assertTrue(DatabaseManager.getQueueByAlgorithm("SPT").isEmpty());
    }

    private void enableForeignKeys(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");
        }
    }

    private Order buildOrder(int tableId, Integer staffId) {
        Order order = new Order();
        order.setItems("Test Dish");
        order.setTableId(tableId);
        order.setStaffId(staffId);
        order.setPriority(3);
        order.setEstimatedPrepTime(15);
        order.setTotalAmount(14.25);
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    private int insertTableRow(int capacity, TableStatus status) throws SQLException {
        final String sql = "INSERT INTO tables (capacity, status) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, capacity);
            stmt.setString(2, status.name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert table row");
    }

    private int insertStaffRow(String name, StaffRole role, StaffStatus status) throws SQLException {
        final String sql = "INSERT INTO staff (name, role, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, role.name());
            stmt.setString(3, status.name());
            stmt.executeUpdate();
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert staff row");
    }
}

