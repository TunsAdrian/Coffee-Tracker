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

    @Query("SELECT * FROM coffee WHERE productivity_rating != -1 ORDER BY date")
    List<Coffee> getCoffeeListWithProductivity();

    @Query("SELECT COUNT(*) FROM coffee WHERE date BETWEEN :dayStart AND :dayEnd")
    LiveData<Integer> getCoffeeNr(Date dayStart, Date dayEnd);

    @Query("SELECT SUM(caffeine) FROM coffee WHERE date BETWEEN :dayStart AND :dayEnd")
    LiveData<Integer> getCaffeine(Date dayStart, Date dayEnd);

    @Insert
    void insert(Coffee coffee);

    @Update
    void update(Coffee coffee);

    @Delete
    void delete(Coffee coffee);

    @Query("DELETE FROM coffee")
    void deleteAll();
}
