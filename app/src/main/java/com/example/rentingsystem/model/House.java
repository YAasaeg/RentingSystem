package com.example.rentingsystem.model;

public class House {
    private int houseId;
    private int landlordId;
    private String title;
    private String address;
    private double rent;
    private String houseType;
    private double area;
    private int status; // 1=Rentable, 2=Rented, 3=Off-shelf
    private String imagePath; // Path to house image
    private Integer currentContractId; // Can be null
    private String createTime;

    public House() {}

    public House(int houseId, int landlordId, String title, String address, double rent, String houseType, double area, int status, String imagePath, Integer currentContractId, String createTime) {
        this.houseId = houseId;
        this.landlordId = landlordId;
        this.title = title;
        this.address = address;
        this.rent = rent;
        this.houseType = houseType;
        this.area = area;
        this.status = status;
        this.imagePath = imagePath;
        this.currentContractId = currentContractId;
        this.createTime = createTime;
    }

    public int getHouseId() { return houseId; }
    public void setHouseId(int houseId) { this.houseId = houseId; }

    public int getLandlordId() { return landlordId; }
    public void setLandlordId(int landlordId) { this.landlordId = landlordId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public double getRent() { return rent; }
    public void setRent(double rent) { this.rent = rent; }

    public String getHouseType() { return houseType; }
    public void setHouseType(String houseType) { this.houseType = houseType; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public Integer getCurrentContractId() { return currentContractId; }
    public void setCurrentContractId(Integer currentContractId) { this.currentContractId = currentContractId; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}
