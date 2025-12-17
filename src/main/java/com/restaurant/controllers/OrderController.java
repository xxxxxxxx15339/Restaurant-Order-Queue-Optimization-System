package com.restaurant.controllers;

import com.restaurant.database.DatabaseManager;
import com.restaurant.models.Order;
import com.restaurant.models.OrderStatus;
import com.restaurant.models.Staff;
import com.restaurant.models.Table;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OrderController {

    @FXML private ComboBox<Table> tableCombo;
    @FXML private ComboBox<Staff> staffCombo;
    @FXML private TextArea itemsArea;
    @FXML private Spinner<Integer> prioritySpinner;
    @FXML private Spinner<Integer> estTimeSpinner;
    @FXML private TextField amountField;
    @FXML private Label errorLabel;

    private Stage dialogStage;

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    @FXML
    private void initialize() {
        tableCombo.setItems(FXCollections.observableArrayList(DatabaseManager.getAllTables()));
        staffCombo.setItems(FXCollections.observableArrayList(DatabaseManager.getAllStaff()));

        prioritySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 5));
        estTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 120, 15));
    }

    @FXML
    private void handleSave() {
        errorLabel.setText("");

        Table table = tableCombo.getValue();
        if (table == null) {
            errorLabel.setText("Please select a table.");
            return;
        }
        String items = itemsArea.getText() != null ? itemsArea.getText().trim() : "";
        if (items.isEmpty()) {
            errorLabel.setText("Please describe the order items.");
            return;
        }

        Double amount = null;
        String amountText = amountField.getText();
        if (amountText != null && !amountText.isBlank()) {
            try {
                amount = Double.parseDouble(amountText.replace(",", "."));
            } catch (NumberFormatException ex) {
                errorLabel.setText("Total amount must be a number.");
                return;
            }
        }

        Order order = new Order();
        order.setTableId(table.getTableId());
        Staff staff = staffCombo.getValue();
        if (staff != null) {
            order.setStaffId(staff.getStaffId());
        }
        order.setItems(items);
        order.setPriority(prioritySpinner.getValue());
        order.setEstimatedPrepTime(estTimeSpinner.getValue());
        if (amount != null) {
            order.setTotalAmount(amount);
        }
        order.setStatus(OrderStatus.PENDING);

        boolean ok = DatabaseManager.addOrder(order);
        if (!ok) {
            errorLabel.setText("Failed to save order. Check logs for details.");
            return;
        }

        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }
}
