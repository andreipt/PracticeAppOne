package com.andrei.practiceappone.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.andrei.practiceappone.database.dao.PlacesDao;
import com.andrei.practiceappone.model.Place;

@Database(entities = {Place.class}, version = 1, exportSchema = false)
public abstract class PlacesDatabase extends RoomDatabase {

    private static volatile PlacesDatabase databaseInstance;

    public abstract PlacesDao placesDao();

    public static PlacesDatabase getInstance(final Context context) {
        if (databaseInstance == null) {
            synchronized (PlacesDatabase.class) {
                if (databaseInstance == null) {
                    databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            PlacesDatabase.class, "places_database")
                            .build();
                }
            }
        }
        return databaseInstance;
    }
}
