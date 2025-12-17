package com.restaurant.ui.components;

import com.restaurant.models.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

/**
 * Simple vertical list of {@link OrderCard} components representing
 * the current optimized queue.
 *
 * This component is not wired into the main FXML yet, but is fully
 * functional and available for richer queue visualizations.
 */
public class QueuePanel extends VBox {

    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    private final ListView<Order> listView = new ListView<>(orders);

    public QueuePanel() {
        getStyleClass().add("queue-panel");
        listView.setCellFactory(view -> new ListCell<>() {
            private final OrderCard card = new OrderCard();

            @Override
            protected void updateItem(Order item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    card.setOrder(item);
                    setGraphic(card);
                }
            }
        });

        getChildren().add(listView);
    }

    public void setOrders(Iterable<Order> newOrders) {
        orders.clear();
        if (newOrders != null) {
            for (Order o : newOrders) {
                orders.add(o);
            }
        }
    }
}
