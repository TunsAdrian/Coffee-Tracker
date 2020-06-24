package com.example.coffeetracker2.recyclerView;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetracker2.R;
import com.example.coffeetracker2.database.Coffee;


import java.text.SimpleDateFormat;
import java.util.List;

public class CoffeeListAdapter extends ListAdapter<Coffee, CoffeeListAdapter.CoffeeHolder> {
    //private ArrayList<CoffeeObject> coffeeList;
    private List<Coffee> coffeeList;
    private Context context;
    private OnItemClickListener listener; //used for tap to edit contact

    public CoffeeListAdapter(Context context) {
        super(DIFF_CALLBACK);
    }

    private static DiffUtil.ItemCallback<Coffee> DIFF_CALLBACK= new DiffUtil.ItemCallback<Coffee>() {
        @Override
        public boolean areItemsTheSame(@NonNull Coffee oldItem, @NonNull Coffee newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Coffee oldItem, @NonNull Coffee newItem) {
            return oldItem.getType().equals(newItem.getType()) &&
                    oldItem.getCaffeine() == (newItem.getCaffeine()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };


    @NonNull
    @Override
    public CoffeeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coffee_item,parent,false);
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
            setTextPlaceholder = "Productivity level: no data" ;
        } else {
            setTextPlaceholder = "Productivity level: " + currentCoffee.getProductivityRating();
        }
        holder.rating.setText(setTextPlaceholder);

    }

    //method used for swipe to DELETE
    public Coffee getCoffeeAt(int position){
        return getItem(position);
    }

    public class CoffeeHolder extends RecyclerView.ViewHolder {

        private TextView coffeeType;
        private TextView caffeine;
        private TextView date;
        private TextView rating;
        private int boundPosition;
        private CheckBox ratingCheckBox;

        public CoffeeHolder(@NonNull final View item_view) {

            super(item_view);
            coffeeType = item_view.findViewById(R.id.itemCoffee_type);
            caffeine = item_view.findViewById(R.id.itemCoffee_caffeine);
            date = item_view.findViewById(R.id.itemCoffee_date);
            rating = item_view.findViewById(R.id.itemCoffee_rating);


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

    //use this to edit contact on tap
    public interface OnItemClickListener{
        void onItemClick(Coffee contact);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
