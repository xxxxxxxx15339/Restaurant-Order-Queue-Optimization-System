package com.restaurant.models;

public class Table {

    private Integer tableId;
    private Integer capacity;
    private TableStatus status;

    public Table() {
        this.status = TableStatus.AVAILABLE;
    }

    public Table(Integer tableId, Integer capacity, TableStatus status) {
        this.tableId = tableId;
        this.capacity = capacity;
        this.status = status != null ? status : TableStatus.AVAILABLE;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }
}
