package com.restaurant.database;

import com.restaurant.models.Order;
import com.restaurant.models.OrderStatus;
import com.restaurant.models.Staff;
import com.restaurant.models.StaffRole;
import com.restaurant.models.StaffStatus;
import com.restaurant.models.Table;
import com.restaurant.models.TableStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    /* Orders CRUD */

    public static boolean addOrder(Order order) {
        final String sql = """
            INSERT INTO orders (items, table_id, staff_id, priority,
                                estimated_prep_time, actual_prep_time,
                                total_amount, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, order.getItems());
            stmt.setInt(2, order.getTableId());
            setNullableInt(stmt, 3, order.getStaffId());
            stmt.setInt(4, order.getPriority() != null ? order.getPriority() : 5);
            setNullableInt(stmt, 5, order.getEstimatedPrepTime());
            setNullableInt(stmt, 6, order.getActualPrepTime());
            stmt.setDouble(7, order.getTotalAmount() != null ? order.getTotalAmount() : 0.0);
            stmt.setString(8, order.getStatus().name());

            return stmt.executeUpdate() == 1;
        } catch (SQLException ex) {
            System.err.println("addOrder failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static Order getOrderById(int orderId) {
        final String sql = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? mapOrder(rs) : null;

        } catch (SQLException ex) {
            System.err.println("getOrderById failed: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public static List<Order> getOrdersByStatus(OrderStatus status) {
        final String sql = """
            SELECT * FROM orders
            WHERE status = ?
            ORDER BY priority ASC, order_time ASC
        """;
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            System.err.println("getOrdersByStatus failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return orders;
    }

    public static boolean updateOrderStatus(int orderId, OrderStatus status) {
        final String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("updateOrderStatus failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteOrder(int orderId) {
        final String sql = "DELETE FROM orders WHERE order_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("deleteOrder failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /* Queue helpers */

    public static boolean insertQueueEntry(int orderId, int position, String algorithm) {
        final String sql = """
            INSERT INTO order_queue (order_id, position, algorithm_used)
            VALUES (?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            stmt.setInt(2, position);
            stmt.setString(3, algorithm);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("insertQueueEntry failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean clearQueue(String algorithm) {
        final String sql = "DELETE FROM order_queue WHERE algorithm_used = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, algorithm);
            stmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.err.println("clearQueue failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Order> getQueueByAlgorithm(String algorithm) {
        final String sql = """
            SELECT o.*
            FROM order_queue q
            JOIN orders o ON o.order_id = q.order_id
            WHERE q.algorithm_used = ?
            ORDER BY q.position ASC
        """;
        List<Order> queue = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, algorithm);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                queue.add(mapOrder(rs));
            }
        } catch (SQLException ex) {
            System.err.println("getQueueByAlgorithm failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return queue;
    }

    /* Tables CRUD */

    public static boolean addTable(Table table) {
        final String sql = "INSERT INTO tables (capacity, status) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, table.getCapacity());
            stmt.setString(2, table.getStatus().name());
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("addTable failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Table> getAllTables() {
        final String sql = "SELECT * FROM tables ORDER BY table_id ASC";
        List<Table> tables = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tables.add(mapTable(rs));
            }
        } catch (SQLException ex) {
            System.err.println("getAllTables failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return tables;
    }

    public static boolean updateTableStatus(int tableId, TableStatus status) {
        final String sql = "UPDATE tables SET status = ? WHERE table_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, tableId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("updateTableStatus failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTable(int tableId) {
        final String sql = "DELETE FROM tables WHERE table_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tableId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("deleteTable failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /* Staff CRUD */

    public static boolean addStaff(Staff staff) {
        final String sql = "INSERT INTO staff (name, role, status) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, staff.getName());
            stmt.setString(2, staff.getRole().name());
            stmt.setString(3, staff.getStatus().name());
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("addStaff failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static List<Staff> getAllStaff() {
        final String sql = "SELECT * FROM staff ORDER BY staff_id ASC";
        List<Staff> staffMembers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                staffMembers.add(mapStaff(rs));
            }
        } catch (SQLException ex) {
            System.err.println("getAllStaff failed: " + ex.getMessage());
            ex.printStackTrace();
        }
        return staffMembers;
    }

    public static boolean updateStaffStatus(int staffId, StaffStatus status) {
        final String sql = "UPDATE staff SET status = ? WHERE staff_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());
            stmt.setInt(2, staffId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("updateStaffStatus failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean updateStaffRole(int staffId, StaffRole role) {
        final String sql = "UPDATE staff SET role = ? WHERE staff_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, role.name());
            stmt.setInt(2, staffId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("updateStaffRole failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean deleteStaff(int staffId) {
        final String sql = "DELETE FROM staff WHERE staff_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, staffId);
            return stmt.executeUpdate() == 1;

        } catch (SQLException ex) {
            System.err.println("deleteStaff failed: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }

    /* Mapping helpers */

    private static Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setItems(rs.getString("items"));
        order.setTableId(rs.getInt("table_id"));
        order.setStaffId(getNullableInt(rs, "staff_id"));
        order.setOrderTime(rs.getString("order_time"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setPriority(rs.getInt("priority"));
        order.setEstimatedPrepTime(getNullableInt(rs, "estimated_prep_time"));
        order.setActualPrepTime(getNullableInt(rs, "actual_prep_time"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        return order;
    }

    private static Staff mapStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setStaffId(rs.getInt("staff_id"));
        staff.setName(rs.getString("name"));
        staff.setRole(StaffRole.valueOf(rs.getString("role")));
        staff.setStatus(StaffStatus.valueOf(rs.getString("status")));
        return staff;
    }

    private static Table mapTable(ResultSet rs) throws SQLException {
        Table table = new Table();
        table.setTableId(rs.getInt("table_id"));
        table.setCapacity(rs.getInt("capacity"));
        table.setStatus(TableStatus.valueOf(rs.getString("status")));
        return table;
    }

    private static void setNullableInt(PreparedStatement stmt, int index, Integer value) throws SQLException {
        if (value == null) {
            stmt.setNull(index, java.sql.Types.INTEGER);
        } else {
            stmt.setInt(index, value);
        }
    }

    private static Integer getNullableInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }

    /* Sample data seeding */

    public static void seedSampleData() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("seedSampleData skipped: no database connection");
            return;
        }

        try {
            if (getRowCount(conn, "tables") == 0) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO tables (capacity, status) VALUES (?, ?)")) {
                    insertTable(stmt, 2, TableStatus.AVAILABLE);
                    insertTable(stmt, 4, TableStatus.OCCUPIED);
                    insertTable(stmt, 6, TableStatus.RESERVED);
                }
            }

            if (getRowCount(conn, "staff") == 0) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO staff (name, role, status) VALUES (?, ?, ?)")) {
                    insertStaff(stmt, "Alice", StaffRole.WAITER, StaffStatus.ACTIVE);
                    insertStaff(stmt, "Ben", StaffRole.COOK, StaffStatus.ACTIVE);
                    insertStaff(stmt, "Clara", StaffRole.MANAGER, StaffStatus.INACTIVE);
                }
            }

            if (getRowCount(conn, "orders") == 0) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        """
                        INSERT INTO orders (items, table_id, staff_id, priority,
                                            estimated_prep_time, total_amount, status)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """)) {
                    insertOrder(stmt, "Burger & Fries", 1, 1, 2, 15, 18.50, OrderStatus.PENDING);
                    insertOrder(stmt, "Steak", 2, 2, 1, 25, 32.00, OrderStatus.IN_PROGRESS);
                    insertOrder(stmt, "Pasta", 3, 3, 3, 20, 22.75, OrderStatus.READY);
                }
            }

            System.out.println("Sample data ensured.");

        } catch (SQLException ex) {
            System.err.println("seedSampleData failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static int getRowCount(Connection conn, String table) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private static void insertTable(PreparedStatement stmt, int capacity, TableStatus status) throws SQLException {
        stmt.setInt(1, capacity);
        stmt.setString(2, status.name());
        stmt.executeUpdate();
    }

    private static void insertStaff(PreparedStatement stmt, String name, StaffRole role, StaffStatus status) throws SQLException {
        stmt.setString(1, name);
        stmt.setString(2, role.name());
        stmt.setString(3, status.name());
        stmt.executeUpdate();
    }

    private static void insertOrder(PreparedStatement stmt,
                                    String items,
                                    int tableId,
                                    int staffId,
                                    int priority,
                                    int estimatedPrep,
                                    double total,
                                    OrderStatus status) throws SQLException {
        stmt.setString(1, items);
        stmt.setInt(2, tableId);
        stmt.setInt(3, staffId);
        stmt.setInt(4, priority);
        stmt.setInt(5, estimatedPrep);
        stmt.setDouble(6, total);
        stmt.setString(7, status.name());
        stmt.executeUpdate();
    }
}

