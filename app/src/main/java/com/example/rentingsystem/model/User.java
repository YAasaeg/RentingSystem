package com.example.rentingsystem.model;

public class User {
    private int userId;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private int identityType; // 1=Tenant, 2=Landlord
    private String createTime;

    public User() {}

    public User(int userId, String username, String password, String realName, String phone, int identityType, String createTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.phone = phone;
        this.identityType = identityType;
        this.createTime = createTime;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getIdentityType() { return identityType; }
    public void setIdentityType(int identityType) { this.identityType = identityType; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}
