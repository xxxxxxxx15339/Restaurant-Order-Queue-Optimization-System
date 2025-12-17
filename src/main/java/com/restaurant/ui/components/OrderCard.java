package com.restaurant.ui.components;

import com.restaurant.models.Order;
import com.restaurant.models.OrderStatus;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * Compact visual representation of a single order.
 * <p>
 * Not currently wired into the main screen, but kept as a reusable
 * building block for future dashboards or detail panes.
 */
public class OrderCard extends HBox {

    private final Label idLabel = new Label();
    private final Label itemsLabel = new Label();
    private final Label metaLabel = new Label();

    public OrderCard() {
        getStyleClass().add("order-card");
        setSpacing(8);
        setPadding(new Insets(6, 10, 6, 10));

        idLabel.getStyleClass().add("order-card-id");
        itemsLabel.getStyleClass().add("order-card-items");
        metaLabel.getStyleClass().add("order-card-meta");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(idLabel, itemsLabel, spacer, metaLabel);
    }

    public void setOrder(Order order) {
        if (order == null) {
            idLabel.setText("");
            itemsLabel.setText("");
            metaLabel.setText("");
            pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-pending"), false);
            pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-in-progress"), false);
            pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-ready"), false);
            return;
        }

        idLabel.setText("ORD-" + (order.getOrderId() != null ? order.getOrderId() : "-"));
        itemsLabel.setText(order.getItems() != null ? order.getItems() : "");

        String meta = String.format("Table %s • P%d • %s min",
                order.getTableId() != null ? order.getTableId() : "-",
                order.getPriority() != null ? order.getPriority() : 0,
                order.getEstimatedPrepTime() != null ? order.getEstimatedPrepTime() : 0);
        metaLabel.setText(meta);

        // Simple status colouring using pseudo‑classes
        OrderStatus status = order.getStatus();
        boolean pending = status == OrderStatus.PENDING;
        boolean inProgress = status == OrderStatus.IN_PROGRESS;
        boolean ready = status == OrderStatus.READY;

        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-pending"), pending);
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-in-progress"), inProgress);
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-ready"), ready);
    }
}
