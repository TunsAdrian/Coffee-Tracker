package com.example.coffeetracker2.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


import com.example.coffeetracker2.utils.Converters;

@Database(entities = Coffee.class, exportSchema = false, version = 1)
@TypeConverters(Converters.class)
public abstract class CoffeeDatabase extends RoomDatabase {

    public abstract CoffeeDAO coffeeDao();

    private static final String DB_NAME = "coffee_database";

    private static volatile CoffeeDatabase instance;

    public static CoffeeDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (CoffeeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), CoffeeDatabase.class, DB_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }
}
