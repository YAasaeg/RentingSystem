package com.example.rentingsystem.model;

public class Contract {
    private int contractId;
    private int tenantId;
    private int landlordId;
    private int houseId;
    private String startDate;
    private String endDate;
    private double rent;
    private int status; // 1=Signing, 2=Active, 3=Expired, 4=Terminated, 5=Invalid
    private String createTime;

    // Extra fields for UI
    private String houseTitle;
    private String tenantName;

    public Contract() {}

    public Contract(int contractId, int tenantId, int landlordId, int houseId, String startDate, String endDate, double rent, int status, String createTime) {
        this.contractId = contractId;
        this.tenantId = tenantId;
        this.landlordId = landlordId;
        this.houseId = houseId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rent = rent;
        this.status = status;
        this.createTime = createTime;
    }

    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }

    public int getHouseId() { return houseId; }
    public void setHouseId(int houseId) { this.houseId = houseId; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public double getRent() { return rent; }
    public void setRent(double rent) { this.rent = rent; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getHouseTitle() { return houseTitle; }
    public void setHouseTitle(String houseTitle) { this.houseTitle = houseTitle; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
}
