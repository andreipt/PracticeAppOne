package com.andrei.practiceappone.model;

import java.util.List;

public class PlacesData {

    private List<Place> places;

    public PlacesData() {
        // Default constructor
    }

    public PlacesData(List<Place> places) {
        this.places = places;
    }

    public List<Place> getPlaces() {
        return places;
    }

    public void setPlaces(List<Place> places) {
        this.places = places;
    }
}
