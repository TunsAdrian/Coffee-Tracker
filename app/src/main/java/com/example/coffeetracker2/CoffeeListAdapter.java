package com.example.coffeetracker2;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetracker2.database.Coffee;


import java.text.SimpleDateFormat;

public class CoffeeListAdapter extends ListAdapter<Coffee, CoffeeListAdapter.CoffeeHolder> {

    private OnItemClickListener listener; //used for tap to edit coffee

    public CoffeeListAdapter() {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Coffee> DIFF_CALLBACK = new DiffUtil.ItemCallback<Coffee>() {
        @Override
        public boolean areItemsTheSame(@NonNull Coffee oldItem, @NonNull Coffee newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Coffee oldItem, @NonNull Coffee newItem) {
            return oldItem.getType().equals(newItem.getType()) &&
                    oldItem.getCaffeine() == (newItem.getCaffeine()) &&
                    oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getProductivityRating() == newItem.getProductivityRating();
        }
    };

    @NonNull
    @Override
    public CoffeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_item, parent, false);
        return new CoffeeHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeHolder holder, int position) {

        String pattern = "dd/MMM/yy - HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Coffee currentCoffee = getItem(position);
        String setTextPlaceholder;

        setTextPlaceholder = "Coffee type: " + currentCoffee.getType();
        holder.coffeeType.setText(setTextPlaceholder);

        setTextPlaceholder = "Caffeine: " + currentCoffee.getCaffeine() + " mg";
        holder.caffeine.setText(setTextPlaceholder);

        setTextPlaceholder = "Date: " + simpleDateFormat.format(currentCoffee.getDate());
        holder.date.setText(setTextPlaceholder);

        if (currentCoffee.getProductivityRating() == -1) {

            holder.ratedImage.setImageResource(R.drawable.ic_star_border_black_24dp);
            setTextPlaceholder = "Productivity level: not rated";
        } else {

            holder.ratedImage.setImageResource(R.drawable.ic_star_black_24dp);
            setTextPlaceholder = "Productivity level: " + currentCoffee.getProductivityRating();
        }
        holder.rating.setText(setTextPlaceholder);
    }

    // Method used for delete on swipe
    public Coffee getCoffeeAt(int position) {
        return getItem(position);
    }

    class CoffeeHolder extends RecyclerView.ViewHolder {

        private TextView coffeeType;
        private TextView caffeine;
        private TextView date;
        private TextView rating;
        private ImageView ratedImage;

        CoffeeHolder(@NonNull final View item_view) {

            super(item_view);
            coffeeType = item_view.findViewById(R.id.itemCoffee_type);
            caffeine = item_view.findViewById(R.id.itemCoffee_caffeine);
            date = item_view.findViewById(R.id.itemCoffee_date);
            rating = item_view.findViewById(R.id.itemCoffee_rating);
            ratedImage = item_view.findViewById(R.id.itemCoffee_rating_checked);

            item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    // Used to rate coffee on tap
    public interface OnItemClickListener {
        void onItemClick(Coffee coffee);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
