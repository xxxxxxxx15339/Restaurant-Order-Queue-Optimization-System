package com.restaurant.controllers;

import com.restaurant.database.DatabaseManager;
import com.restaurant.database.DatabaseConnection;
import com.restaurant.models.Order;
import com.restaurant.models.OrderStatus;
import com.restaurant.models.Staff;
import com.restaurant.models.Table;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {

    @FXML private TableView<Order> ordersTable;
    @FXML private TableView<Table> tablesTable;
    @FXML private TableView<Staff> staffTable;

    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    private final ObservableList<Table> tables = FXCollections.observableArrayList();
    private final ObservableList<Staff> staff = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        DatabaseConnection.setDatabaseUrl("jdbc:sqlite:database/restaurant.db");
        DatabaseManager.seedSampleData();

        setupOrdersTable();
        setupTablesTable();
        setupStaffTable();

        refreshAll();
    }

    private void setupOrdersTable() {
        ordersTable.setItems(orders);
        // Example columns; ensure they match your FXML column ids
        ((TableColumn<Order, Number>) ordersTable.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ((TableColumn<Order, String>) ordersTable.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("items"));
        ((TableColumn<Order, OrderStatus>) ordersTable.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupTablesTable() {
        tablesTable.setItems(tables);
        ((TableColumn<Table, Number>) tablesTable.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("tableId"));
        ((TableColumn<Table, Number>) tablesTable.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("capacity"));
        ((TableColumn<Table, String>) tablesTable.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupStaffTable() {
        staffTable.setItems(staff);
        ((TableColumn<Staff, Number>) staffTable.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("staffId"));
        ((TableColumn<Staff, String>) staffTable.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("name"));
        ((TableColumn<Staff, String>) staffTable.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("role"));
        ((TableColumn<Staff, String>) staffTable.getColumns().get(3))
                .setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void refreshAll() {
        orders.setAll(DatabaseManager.getOrdersByStatus(OrderStatus.PENDING));
        tables.setAll(DatabaseManager.getAllTables());
        staff.setAll(DatabaseManager.getAllStaff());
    }
}
