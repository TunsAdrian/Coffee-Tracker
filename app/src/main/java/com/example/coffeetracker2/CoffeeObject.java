package com.example.coffeetracker2;

import java.util.Date;

public class CoffeeObject {
    private int type;
    private int caffeine;
    private Date date;
    private int productivityRating;

    public CoffeeObject(int type, int caffeine, Date date, int productivityRating) {
        this.type = type;
        this.caffeine = caffeine;
        this.productivityRating = productivityRating;
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCaffeine() {
        return caffeine;
    }

    public void setCaffeine(int caffeine) {
        this.caffeine = caffeine;
    }

    public int getProductivityRating() {
        return productivityRating;
    }

    public void setProductivityRating(int productivityRating) {
        this.productivityRating = productivityRating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
