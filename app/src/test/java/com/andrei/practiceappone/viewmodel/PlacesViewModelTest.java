package com.andrei.practiceappone.viewmodel;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.andrei.practiceappone.model.Place;
import com.andrei.practiceappone.repsitory.PlacesRepository;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class PlacesViewModelTest {

    @Mock
    private PlacesRepository placesRepository;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetPalaces_returnsCorrectDataFromRepository() {
        // GIVEN:
        final List<Place> places = new ArrayList<>();
        places.add(new Place("place_1", "Test Place 1", "https://testurl.com/image.jpg"));
        final MutableLiveData<List<Place>> expectedData = new MutableLiveData<>();
        expectedData.postValue(places);

        // WHEN:
        Mockito.when(placesRepository.getPlaces()).thenReturn(expectedData);
        final PlacesViewModel placesViewModel = new PlacesViewModel(placesRepository);
        final LiveData<List<Place>> actualData = placesViewModel.getPlacesListData();

        // THEN:
        assertThat(actualData, is(expectedData));
    }

}