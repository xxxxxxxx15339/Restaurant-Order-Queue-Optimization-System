package com.restaurant.models;

import java.time.Instant;

public class Order {

    private Integer orderId;
    private String items;
    private Integer tableId;
    private Integer staffId;
    private String orderTime;
    private OrderStatus status;
    private Integer priority;
    private Integer estimatedPrepTime;
    private Integer actualPrepTime;
    private Double totalAmount;

    /**
     * Default constructor for new orders created in the UI.
     * Applies sensible defaults (current time, pending status, medium priority).
     */
    public Order() {
        this.orderTime = Instant.now().toString();
        this.status = OrderStatus.PENDING;
        this.priority = 5;
    }

    /**
     * Full constructor used when hydrating from the database.
     */
    public Order(
            Integer orderId,
            String items,
            Integer tableId,
            Integer staffId,
            String orderTime,
            OrderStatus status,
            Integer priority,
            Integer estimatedPrepTime,
            Integer actualPrepTime,
            Double totalAmount
    ) {
        this.orderId = orderId;
        this.items = items;
        this.tableId = tableId;
        this.staffId = staffId;
        this.orderTime = orderTime != null ? orderTime : Instant.now().toString();
        this.status = status != null ? status : OrderStatus.PENDING;
        this.priority = priority != null ? priority : 5;
        this.estimatedPrepTime = estimatedPrepTime;
        this.actualPrepTime = actualPrepTime;
        this.totalAmount = totalAmount;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getEstimatedPrepTime() {
        return estimatedPrepTime;
    }

    public void setEstimatedPrepTime(Integer estimatedPrepTime) {
        this.estimatedPrepTime = estimatedPrepTime;
    }

    public Integer getActualPrepTime() {
        return actualPrepTime;
    }

    public void setActualPrepTime(Integer actualPrepTime) {
        this.actualPrepTime = actualPrepTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
