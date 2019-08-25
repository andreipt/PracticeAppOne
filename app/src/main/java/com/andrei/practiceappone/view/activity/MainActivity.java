package com.andrei.practiceappone.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.andrei.practiceappone.R;
import com.andrei.practiceappone.adapter.PlacesAdapter;
import com.andrei.practiceappone.database.PlacesDatabase;
import com.andrei.practiceappone.database.dao.PlacesDao;
import com.andrei.practiceappone.model.Place;
import com.andrei.practiceappone.network.RetrofitClient;
import com.andrei.practiceappone.network.endpoints.PlacesApiEndpoint;
import com.andrei.practiceappone.repsitory.PlacesRepository;
import com.andrei.practiceappone.viewmodel.PlacesViewModel;
import com.andrei.practiceappone.viewmodel.ViewModelFactory;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();
    private static final String LINEAR_LAYOUT_MANAGER_STATE_KEY = "LinearLayoutManagerState";

    @BindView(R.id.places_toolbar) Toolbar placesToolbar;
    @BindView(R.id.places_swipe_refresh_layout) SwipeRefreshLayout placesSwipeRefreshLayout;
    @BindView(R.id.places_recycler_view) RecyclerView placesRecyclerView;

    private PlacesAdapter placesAdapter;
    private PlacesViewModel placesViewModel;
    private Parcelable layoutManagerState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(placesToolbar);

        setupViews();
        setupViewModel();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        final RecyclerView.LayoutManager layoutManager = placesRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            outState.putParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY, layoutManager.onSaveInstanceState());
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        layoutManagerState = savedInstanceState.getParcelable(LINEAR_LAYOUT_MANAGER_STATE_KEY);
        final RecyclerView.LayoutManager layoutManager = placesRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.onRestoreInstanceState(layoutManagerState);
        }
    }

    private void setupViewModel() {
        // Init injected dependencies
        final PlacesApiEndpoint placesApiEndpoint = RetrofitClient.getInstance().create(PlacesApiEndpoint.class);
        final PlacesDao placesDao = PlacesDatabase.getInstance(getApplicationContext()).placesDao();
        final Executor executor = Executors.newSingleThreadExecutor();
        final PlacesRepository placesRepository = PlacesRepository.getInstance(placesApiEndpoint, placesDao, executor);

        placesViewModel = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(placesRepository))
                .get(PlacesViewModel.class);

        // Add observer
        placesViewModel.getPlacesListData().observe(this, new PlacesObserver());
        placesViewModel.isDataLoading().observe(this, new PlacesLoadingObserver());
    }

    private void setupViews() {
        placesSwipeRefreshLayout.setOnRefreshListener(new OnPlacesRefreshListener());

        placesAdapter = new PlacesAdapter(getApplicationContext(), new OnPlaceClickListener());
        placesRecyclerView.setAdapter(placesAdapter);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    /**
     * Observer for the Places list
     */
    class PlacesObserver implements Observer<List<Place>> {
        @Override
        public void onChanged(List<Place> places) {
            placesAdapter.updatePlaces(places);

            final RecyclerView.LayoutManager layoutManager = placesRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.onRestoreInstanceState(layoutManagerState);
            }
        }
    }

    /**
     * Observer for the Places loading state value
     */
    class PlacesLoadingObserver implements Observer<Boolean> {
        @Override
        public void onChanged(Boolean aBoolean) {
            placesSwipeRefreshLayout.setRefreshing(aBoolean);
        }
    }

    /**
     * OnRefresh Listener for the Places SwipeRefreshLayout
     */
    class OnPlacesRefreshListener implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            placesViewModel.forceRefreshPlacesData();
        }
    }

    /**
     * RecyclerView Place item click listener
     */
    class OnPlaceClickListener implements PlacesAdapter.OnPlaceClickListener {
        @Override
        public void onPlaceClick(Place place) {
            if (place == null) return;
            final Intent placeDetailsIntent = new Intent(
                    MainActivity.this, PlaceDetailsActivity.class);
            placeDetailsIntent.putExtra(PlaceDetailsActivity.PLACE_ID_EXTRA, place.getId());
            startActivity(placeDetailsIntent);
        }
    }
}
