package com.example.rentingsystem.model;

public class Message {
    private int msgId;
    private int senderId;
    private int receiverId;
    private String content;
    private int isRead; // 0=Unread, 1=Read
    private String createTime;

    public Message() {}

    public Message(int msgId, int senderId, int receiverId, String content, int isRead, String createTime) {
        this.msgId = msgId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.isRead = isRead;
        this.createTime = createTime;
    }

    public int getMsgId() { return msgId; }
    public void setMsgId(int msgId) { this.msgId = msgId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getIsRead() { return isRead; }
    public void setIsRead(int isRead) { this.isRead = isRead; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}
