package com.andrei.practiceappone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andrei.practiceappone.model.Place;
import com.andrei.practiceappone.repsitory.PlacesRepository;

public class PlaceDetailsViewModel extends ViewModel {

    private PlacesRepository placesRepository;
    private LiveData<Place> placeData;

    public PlaceDetailsViewModel(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
    }

    public LiveData<Place> getPlaceData(String id) {
        if (placeData == null) {
            placeData = placesRepository.getPlace(id);
        }
        return placeData;
    }
}
