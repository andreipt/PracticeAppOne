package com.andrei.practiceappone.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.andrei.practiceappone.model.Place;
import com.andrei.practiceappone.repsitory.PlacesRepository;

import java.util.List;

public class PlacesViewModel extends ViewModel {

    private PlacesRepository placesRepository;
    private LiveData<List<Place>> placesListData;
    private LiveData<Boolean> isDataLoading;

    public PlacesViewModel(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;

        placesListData = placesRepository.getPlaces();
        isDataLoading = placesRepository.isPlacesListLoading();
    }

    public LiveData<List<Place>> getPlacesListData() {
        return placesListData;
    }

    public void forceRefreshPlacesData() {
        placesListData = placesRepository.getPlaces();
    }

    public LiveData<Boolean> isDataLoading() {
        return isDataLoading;
    }
}
