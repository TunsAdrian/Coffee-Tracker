package com.example.coffeetracker2.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "Coffee")
public class Coffee {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "caffeine")
    private int caffeine;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "productivity_rating")
    private int productivityRating;

    public Coffee(String type, int caffeine, Date date, int productivityRating) {

        this.type = type;
        this.caffeine = caffeine;
        this.date = date;
        this.productivityRating = productivityRating;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCaffeine() {
        return caffeine;
    }

    public void setCaffeine(int caffeine) {
        this.caffeine = caffeine;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getProductivityRating() {
        return productivityRating;
    }

    public void setProductivityRating(int productivityRating) {
        this.productivityRating = productivityRating;
    }

    @Override
    public String toString() {
        return "Coffee{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", caffeine=" + caffeine +
                ", date=" + date +
                ", productivityRating=" + productivityRating +
                '}';
    }
}
