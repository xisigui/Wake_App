package com.bscs501.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Spot> spots;
    private Context mContext;

    public CardStackAdapter(Context context, List<Spot> spots) {
        this.inflater = LayoutInflater.from(context);
        this.spots = spots;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_spot, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Spot spot = spots.get(position);
        holder.name.setText(spot.name);
        holder.city.setText(spot.city);
        Glide.with(this.mContext)
                .load(spot.url)
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return spots.size();
    }

    public void addSpots(List<Spot> spots) {
        this.spots.addAll(spots);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView city;
        private ImageView image;

        ViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.item_name);
            this.city = view.findViewById(R.id.item_city);
            this.image = view.findViewById(R.id.item_image);
        }
    }

}
