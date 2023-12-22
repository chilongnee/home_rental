package com.example.myapplication.model;

public class Room {
    private String roomId;
    private String title;
    private String description;
    private double price;
    private String location;
    private String imageUrl;
    private String userUid;

    public Room() {

    }

    public Room(String roomId, String title, String description, double price, String location, String imageUrl, String userUid) {
        this.roomId = roomId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
        this.userUid = userUid;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
