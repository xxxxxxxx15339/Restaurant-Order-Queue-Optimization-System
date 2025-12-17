package com.restaurant.controllers;

import com.restaurant.algorithms.PriorityQueueScheduler;
import com.restaurant.algorithms.RoundRobinScheduler;
import com.restaurant.algorithms.SPTscheduler;
import com.restaurant.algorithms.SchedulingAlgorithm;
import com.restaurant.controllers.QueueController;
import com.restaurant.database.DatabaseConnection;
import com.restaurant.database.DatabaseManager;
import com.restaurant.models.Order;
import com.restaurant.models.OrderStatus;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML private TableView<Order> originalOrdersTable;
    @FXML private TableView<Order> optimizedOrdersTable;
    @FXML private ComboBox<String> algorithmCombo;
    @FXML private Button applyAlgorithmButton;
    @FXML private Button refreshOrdersButton;
    @FXML private Label originalStatsLabel;
    @FXML private Label optimizedStatsLabel;
    @FXML private Label statusMessageLabel;

    private final ObservableList<Order> originalOrders = FXCollections.observableArrayList();
    private final ObservableList<Order> optimizedOrders = FXCollections.observableArrayList();
    private SchedulingAlgorithm currentAlgorithm = new RoundRobinScheduler();
    private final QueueController queueController = new QueueController();

    @FXML
    public void initialize() {
        DatabaseConnection.setDatabaseUrl("jdbc:sqlite:database/restaurant.db");
        DatabaseManager.seedSampleData();

        setupOriginalOrdersTable();
        setupOptimizedOrdersTable();
        setupAlgorithmSelector();
        refreshAll();
    }

    private void setupAlgorithmSelector() {
        if (algorithmCombo != null) {
            algorithmCombo.setItems(FXCollections.observableArrayList(
                    "SPT (Shortest Processing Time)",
                    "Round Robin Scheduling",
                    "Priority Queue Scheduling"
            ));
            algorithmCombo.getSelectionModel().selectFirst();
        }

        if (applyAlgorithmButton != null) {
            applyAlgorithmButton.setOnAction(e -> handleApplyAlgorithm());
        }
    }

    private void setupOriginalOrdersTable() {
        originalOrdersTable.setItems(originalOrders);

        ((TableColumn<Order, Number>) originalOrdersTable.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ((TableColumn<Order, Number>) originalOrdersTable.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("tableId"));
        ((TableColumn<Order, Number>) originalOrdersTable.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("priority"));
        ((TableColumn<Order, Number>) originalOrdersTable.getColumns().get(3))
                .setCellValueFactory(new PropertyValueFactory<>("estimatedPrepTime"));
        ((TableColumn<Order, Number>) originalOrdersTable.getColumns().get(4))
                .setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        ((TableColumn<Order, OrderStatus>) originalOrdersTable.getColumns().get(5))
                .setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setupOptimizedOrdersTable() {
        optimizedOrdersTable.setItems(optimizedOrders);

        ((TableColumn<Order, Number>) optimizedOrdersTable.getColumns().get(0))
                .setCellValueFactory(new PropertyValueFactory<>("orderId"));
        ((TableColumn<Order, Number>) optimizedOrdersTable.getColumns().get(1))
                .setCellValueFactory(new PropertyValueFactory<>("tableId"));
        ((TableColumn<Order, Number>) optimizedOrdersTable.getColumns().get(2))
                .setCellValueFactory(new PropertyValueFactory<>("priority"));
        ((TableColumn<Order, Number>) optimizedOrdersTable.getColumns().get(3))
                .setCellValueFactory(new PropertyValueFactory<>("estimatedPrepTime"));
        ((TableColumn<Order, Number>) optimizedOrdersTable.getColumns().get(4))
                .setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        ((TableColumn<Order, OrderStatus>) optimizedOrdersTable.getColumns().get(5))
                .setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void refreshAll() {
        var pending = DatabaseManager.getOrdersByStatus(OrderStatus.PENDING);
        originalOrders.setAll(pending);
        optimizedOrders.setAll(currentAlgorithm.schedule(pending));
        updateStats("Loaded pending orders from database.");
    }

    @FXML
    private void handleAddOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/order-form.fxml"));
            Scene scene = new Scene(loader.load());

            Stage dialog = new Stage();
            dialog.setTitle("New Order");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(originalOrdersTable.getScene().getWindow());
            dialog.setScene(scene);

            OrderController controller = loader.getController();
            controller.setDialogStage(dialog);

            dialog.showAndWait();
            refreshAll();
            updateStats("New order added successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshOrders() {
        refreshAll();
    }

    @FXML
    private void handleApplyAlgorithm() {
        String selected = algorithmCombo != null ? algorithmCombo.getSelectionModel().getSelectedItem() : null;
        if (selected == null) {
            return;
        }

        if (selected.startsWith("SPT")) {
            currentAlgorithm = new SPTscheduler();
        } else if (selected.startsWith("Priority")) {
            currentAlgorithm = new PriorityQueueScheduler();
        } else {
            currentAlgorithm = new RoundRobinScheduler();
        }

        optimizedOrders.setAll(currentAlgorithm.schedule(originalOrders));
        String algoName = currentAlgorithm.getName();
        queueController.saveQueue(algoName, optimizedOrders);
        updateStats("Applied " + algoName + " to current orders and saved queue.");
    }

    private void updateStats(String message) {
        if (originalStatsLabel != null) {
            originalStatsLabel.setText(buildStatsText("Original", originalOrders));
        }
        if (optimizedStatsLabel != null) {
            optimizedStatsLabel.setText(buildStatsText("Optimized", optimizedOrders));
        }
        if (statusMessageLabel != null) {
            statusMessageLabel.setText(message != null ? message : "");
        }
    }

    private String buildStatsText(String prefix, ObservableList<Order> orders) {
        int count = orders.size();
        int totalTime = orders.stream()
                .map(Order::getEstimatedPrepTime)
                .filter(t -> t != null && t > 0)
                .mapToInt(Integer::intValue)
                .sum();
        double totalAmount = orders.stream()
                .map(Order::getTotalAmount)
                .filter(a -> a != null && a > 0)
                .mapToDouble(Double::doubleValue)
                .sum();
        double avgTime = count > 0 ? (double) totalTime / count : 0.0;

        return String.format(
                "%s: %d orders | Total Time: %d min | Avg Time: %.1f min | Total Amount: $%.2f",
                prefix, count, totalTime, avgTime, totalAmount
        );
    }
}
