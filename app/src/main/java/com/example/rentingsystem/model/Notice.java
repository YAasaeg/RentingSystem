package com.example.rentingsystem.model;

public class Notice {
    private int noticeId;
    private int publisherId; // 0=Platform, >=1 Landlord
    private String title;
    private String content;
    private int receiverId; // Tenant ID
    private int isRead; // 0=Unread, 1=Read
    private String createTime;
    
    // Extra fields for UI
    private String publisherName;

    public Notice() {}

    public Notice(int noticeId, int publisherId, String title, String content, int receiverId, int isRead, String createTime) {
        this.noticeId = noticeId;
        this.publisherId = publisherId;
        this.title = title;
        this.content = content;
        this.receiverId = receiverId;
        this.isRead = isRead;
        this.createTime = createTime;
    }

    public int getNoticeId() { return noticeId; }
    public void setNoticeId(int noticeId) { this.noticeId = noticeId; }

    public int getPublisherId() { return publisherId; }
    public void setPublisherId(int publisherId) { this.publisherId = publisherId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public int getIsRead() { return isRead; }
    public void setIsRead(int isRead) { this.isRead = isRead; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }
}
