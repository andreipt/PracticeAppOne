package com.andrei.practiceappone.repsitory;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrei.practiceappone.database.dao.PlacesDao;
import com.andrei.practiceappone.model.Place;
import com.andrei.practiceappone.model.PlacesData;
import com.andrei.practiceappone.network.endpoints.PlacesApiEndpoint;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesRepository {

    private static PlacesRepository repositoryInstance;

    private LiveData<List<Place>> places;
    private MutableLiveData<Boolean> isPlacesListLoading;

    private PlacesApiEndpoint placesApiEndpoint;
    private PlacesDao placesDao;
    private Executor executor;

    private PlacesRepository(PlacesApiEndpoint placesApiEndpoint, PlacesDao placesDao, Executor executor) {
        this.placesApiEndpoint = placesApiEndpoint;
        this.placesDao = placesDao;
        this.executor = executor;

        places = placesDao.getPlaces();
        isPlacesListLoading = new MutableLiveData<>();
        isPlacesListLoading.setValue(false);
    }

    public static PlacesRepository getInstance(PlacesApiEndpoint placesApiEndpoint, PlacesDao placesDao, Executor executor) {
        if (repositoryInstance == null) {
            synchronized (PlacesRepository.class) {
                if (repositoryInstance == null) {
                    repositoryInstance = new PlacesRepository(placesApiEndpoint, placesDao, executor);
                }
            }
        }
        return repositoryInstance;
    }

    public LiveData<List<Place>> getPlaces() {
        // Reload data from the network
        reloadPlaces();
        return places;
    }

    public LiveData<Place> getPlace(String id) {
        return placesDao.getPlace(id);
    }

    public LiveData<Boolean> isPlacesListLoading() {
        return isPlacesListLoading;
    }

    private void reloadPlaces() {
        isPlacesListLoading.setValue(true);
        placesApiEndpoint.getPlacesData().enqueue(new Callback<PlacesData>() {
            @Override
            public void onResponse(@NonNull Call<PlacesData> call, @NonNull Response<PlacesData> response) {
                isPlacesListLoading.setValue(false);
                final PlacesData placesData = response.body();
                if (placesData != null) {
                    // Defer insertion to a background thread
                    executor.execute(() -> {
                        final List<Place> placeList = placesData.getPlaces();
                        placesDao.insert(placeList);
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlacesData> call, @NonNull Throwable t) {
                isPlacesListLoading.setValue(false);
            }
        });
    }
}
