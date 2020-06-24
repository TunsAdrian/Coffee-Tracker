package com.example.coffeetracker2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface CoffeeDAO {
    @Query("SELECT * FROM coffee ORDER BY date DESC")
    LiveData<List<Coffee>> getCoffeeList();

    @Insert
    void insert(Coffee coffee);

    @Query("SELECT COUNT(*) FROM coffee WHERE date BETWEEN :dayStart AND :dayEnd")
    LiveData<Integer> getCoffeeNr(Date dayStart, Date dayEnd);

    @Query("SELECT SUM(caffeine) FROM coffee WHERE date BETWEEN :dayStart AND :dayEnd")
    LiveData<Integer> getCaffeine(Date dayStart, Date dayEnd);

    @Query("UPDATE coffee SET productivity_rating =:true_productivity_rating WHERE id = :id")
    void updateProductivity(int id, int true_productivity_rating);

    @Update
    void update(Coffee coffee);

    @Delete
    void delete(Coffee coffee);
}
