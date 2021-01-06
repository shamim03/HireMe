package com.gamecodeschool.navigation;

/**
 * Created by Asif Sobhan on 10-12-17.
 */

public class Post {
    private String Title, Image, RentPerHour, Seats,userId,Contact_No;
    private boolean available;
    private String rate;

    public String getRate() {
        return rate;
    }

    public String getContact_No() {
        return Contact_No;
    }

    public void setContact_No(String contact_No) {
        Contact_No = contact_No;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Post() {

    }

    public Post(String title, String image, String rentPerHour, String seats) {
        Title = title;
        Image = image;
        RentPerHour = rentPerHour;
        Seats = seats;
    }


    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getRentPerHour() {
        return RentPerHour;
    }

    public void setRentPerHour(String rentPerHour) {
        RentPerHour = rentPerHour;
    }

    public String getSeats() {
        return Seats;
    }

    public void setSeats(String seats) {
        Seats = seats;
    }
}
