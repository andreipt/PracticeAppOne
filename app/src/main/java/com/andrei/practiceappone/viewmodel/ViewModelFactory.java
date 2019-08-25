package com.andrei.practiceappone.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.andrei.practiceappone.repsitory.PlacesRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private static ViewModelFactory factoryInstance;
    private PlacesRepository placesRepository;

    private ViewModelFactory(PlacesRepository placesRepository) {
        this.placesRepository = placesRepository;
    }

    public static ViewModelFactory getInstance(final PlacesRepository placesRepository) {
        if (factoryInstance == null) {
            synchronized (ViewModelFactory.class) {
                if (factoryInstance == null) {
                    factoryInstance = new ViewModelFactory(placesRepository);
                }
            }
        }
        return factoryInstance;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PlacesViewModel.class)) {
            return (T) new PlacesViewModel(placesRepository);
        }
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel.class)) {
            return (T) new PlaceDetailsViewModel(placesRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
