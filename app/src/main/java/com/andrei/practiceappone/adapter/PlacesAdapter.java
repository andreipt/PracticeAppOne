package com.andrei.practiceappone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrei.practiceappone.R;
import com.andrei.practiceappone.model.Place;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    public interface OnPlaceClickListener {
        void onPlaceClick(Place place);
    }

    private Context context;
    private OnPlaceClickListener onPlaceClickListener;
    private List<Place> places;

    public PlacesAdapter(Context context, OnPlaceClickListener onPlaceClickListener) {
        this.context = context;
        this.onPlaceClickListener = onPlaceClickListener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.place_item_view, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        final Place place = places.get(position);

        holder.placeNameTextView.setText(place.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlaceClickListener.onPlaceClick(place);
            }
        });

        // Load image
        Glide.with(context)
                .load(place.getImageUrl())
                .into(holder.placeImageView);
    }

    @Override
    public int getItemCount() {
        return places != null ? places.size() : 0;
    }

    public void updatePlaces(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_image_view) ImageView placeImageView;
        @BindView(R.id.place_name_view) TextView placeNameTextView;

        PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
