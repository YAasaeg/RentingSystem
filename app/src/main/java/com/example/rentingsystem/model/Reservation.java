package com.example.rentingsystem.model;

public class Reservation {
    private int reservationId;
    private int tenantId;
    private int landlordId;
    private int houseId;
    private String reserveTime;
    private int status; // 0=Pending, 1=Approved, 2=Rejected
    private String createTime;

    // Helper fields for UI display (Join query results)
    private String houseTitle;
    private String tenantName;
    private String tenantPhone;

    public Reservation() {}

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }

    public int getHouseId() { return houseId; }
    public void setHouseId(int houseId) { this.houseId = houseId; }

    public String getReserveTime() { return reserveTime; }
    public void setReserveTime(String reserveTime) { this.reserveTime = reserveTime; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getHouseTitle() { return houseTitle; }
    public void setHouseTitle(String houseTitle) { this.houseTitle = houseTitle; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getTenantPhone() { return tenantPhone; }
    public void setTenantPhone(String tenantPhone) { this.tenantPhone = tenantPhone; }
}
