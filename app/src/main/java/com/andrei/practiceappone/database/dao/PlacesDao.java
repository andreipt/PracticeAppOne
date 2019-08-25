package com.andrei.practiceappone.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.andrei.practiceappone.model.Place;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface PlacesDao {

    @Insert(onConflict = REPLACE)
    void insert(List<Place> places);

    @Query("DELETE FROM places_table")
    void deleteAll();

    @Query("SELECT * FROM places_table")
    LiveData<List<Place>> getPlaces();

    @Query("SELECT * FROM places_table WHERE id = :id LIMIT 1")
    LiveData<Place> getPlace(String id);
}
