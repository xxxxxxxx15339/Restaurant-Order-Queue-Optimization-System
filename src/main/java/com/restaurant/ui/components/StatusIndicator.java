package com.restaurant.ui.components;

import com.restaurant.models.OrderStatus;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

/**
 * Small pill‑shaped label that visually represents an {@link OrderStatus}.
 */
public class StatusIndicator extends Label {

    private final ObjectProperty<OrderStatus> status = new SimpleObjectProperty<>();

    public StatusIndicator() {
        getStyleClass().add("status-indicator");
        setAlignment(Pos.CENTER);

        status.addListener((obs, oldVal, newVal) -> applyStatus(newVal));
    }

    public ObjectProperty<OrderStatus> statusProperty() {
        return status;
    }

    public OrderStatus getStatus() {
        return status.get();
    }

    public void setStatus(OrderStatus value) {
        status.set(value);
    }

    private void applyStatus(OrderStatus s) {
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-pending"), false);
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-in-progress"), false);
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-ready"), false);
        pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-served"), false);

        if (s == null) {
            setText("");
            return;
        }

        setText(s.name());

        switch (s) {
            case PENDING -> pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-pending"), true);
            case IN_PROGRESS -> pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-in-progress"), true);
            case READY -> pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-ready"), true);
            case SERVED -> pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("status-served"), true);
            default -> {
            }
        }
    }
}
