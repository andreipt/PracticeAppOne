package com.andrei.practiceappone.view.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.andrei.practiceappone.R;
import com.andrei.practiceappone.database.PlacesDatabase;
import com.andrei.practiceappone.database.dao.PlacesDao;
import com.andrei.practiceappone.model.Place;
import com.andrei.practiceappone.network.RetrofitClient;
import com.andrei.practiceappone.network.endpoints.PlacesApiEndpoint;
import com.andrei.practiceappone.repsitory.PlacesRepository;
import com.andrei.practiceappone.viewmodel.PlaceDetailsViewModel;
import com.andrei.practiceappone.viewmodel.ViewModelFactory;
import com.bumptech.glide.Glide;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetailsActivity extends AppCompatActivity {

    public static final String PLACE_ID_EXTRA = "place_id";

    @BindView(R.id.place_details_toolbar) Toolbar toolbar;
    @BindView(R.id.place_image_view) ImageView placeImageView;
    @BindView(R.id.place_name_view) TextView placeNameView;

    private PlaceDetailsViewModel placeDetailsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final Bundle bundle = getIntent().getExtras();
        final String placeId = bundle != null
                ? bundle.getString(PLACE_ID_EXTRA, "")
                : "";

        setupViewModel();

        placeDetailsViewModel.getPlaceData(placeId).observe(this, new PlaceDataObserver());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle back click
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewModel() {
        final PlacesApiEndpoint placesApiEndpoint = RetrofitClient.getInstance().create(PlacesApiEndpoint.class);
        final PlacesDao placesDao = PlacesDatabase.getInstance(getApplicationContext()).placesDao();
        final Executor executor = Executors.newSingleThreadExecutor();
        final PlacesRepository placesRepository = PlacesRepository.getInstance(placesApiEndpoint, placesDao, executor);
        placeDetailsViewModel = ViewModelProviders
                .of(this, ViewModelFactory.getInstance(placesRepository))
                .get(PlaceDetailsViewModel.class);
    }

    private void populateViews(@NonNull Place place) {
        setTitle(place.getName());

        placeNameView.setText(place.getName());

        Glide.with(getApplicationContext())
                .load(place.getImageUrl())
                .into(placeImageView);
    }

    class PlaceDataObserver implements Observer<Place> {
        @Override
        public void onChanged(Place place) {
            populateViews(place);
        }
    }
}
