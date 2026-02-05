package com.example.rentingsystem.model;

public class RentApplication {
    private int applyId;
    private int tenantId;
    private int houseId;
    private String applyReason; // Renamed from remark
    private String startDate; // New
    private String endDate; // New
    private int status; // 1=Pending, 2=Passed, 3=Rejected
    private String refuseReason;
    private String createTime;

    // Extra fields for UI
    private String houseTitle;
    private String tenantName;
    private String tenantPhone;

    public RentApplication() {}

    public RentApplication(int applyId, int tenantId, int houseId, String applyReason, String startDate, String endDate, int status, String refuseReason, String createTime) {
        this.applyId = applyId;
        this.tenantId = tenantId;
        this.houseId = houseId;
        this.applyReason = applyReason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.refuseReason = refuseReason;
        this.createTime = createTime;
    }

    public int getApplyId() { return applyId; }
    public void setApplyId(int applyId) { this.applyId = applyId; }

    public int getTenantId() { return tenantId; }
    public void setTenantId(int tenantId) { this.tenantId = tenantId; }

    public int getHouseId() { return houseId; }
    public void setHouseId(int houseId) { this.houseId = houseId; }

    public String getApplyReason() { return applyReason; }
    public void setApplyReason(String applyReason) { this.applyReason = applyReason; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getRefuseReason() { return refuseReason; }
    public void setRefuseReason(String refuseReason) { this.refuseReason = refuseReason; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getHouseTitle() { return houseTitle; }
    public void setHouseTitle(String houseTitle) { this.houseTitle = houseTitle; }

    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }

    public String getTenantPhone() { return tenantPhone; }
    public void setTenantPhone(String tenantPhone) { this.tenantPhone = tenantPhone; }
}
