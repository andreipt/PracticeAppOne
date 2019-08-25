package com.andrei.practiceappone.network.endpoints;

import com.andrei.practiceappone.model.PlacesData;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PlacesApiEndpoint {

    @GET("/andreipt/mock_places_api/db")
    Call<PlacesData> getPlacesData();
}
