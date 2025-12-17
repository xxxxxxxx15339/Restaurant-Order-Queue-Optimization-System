package com.restaurant.models;

public class Staff {

    private Integer staffId;
    private String name;
    private StaffRole role;
    private StaffStatus status;

    public Staff() {
        this.status = StaffStatus.ACTIVE;
    }

    public Staff(Integer staffId, String name, StaffRole role, StaffStatus status) {
        this.staffId = staffId;
        this.name = name;
        this.role = role;
        this.status = status != null ? status : StaffStatus.ACTIVE;
    }

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StaffRole getRole() {
        return role;
    }

    public void setRole(StaffRole role) {
        this.role = role;
    }

    public StaffStatus getStatus() {
        return status;
    }

    public void setStatus(StaffStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // Show "id - name" in ComboBoxes instead of the full object syntax
        String idPart = staffId != null ? String.valueOf(staffId) : "-";
        return name != null && !name.isBlank() ? idPart + " - " + name : idPart;
    }
}
