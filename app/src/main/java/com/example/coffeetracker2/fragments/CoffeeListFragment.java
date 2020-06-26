package com.example.coffeetracker2.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetracker2.utils.CoffeeRepository;
import com.example.coffeetracker2.R;
import com.example.coffeetracker2.RatingActivity;
import com.example.coffeetracker2.database.Coffee;
import com.example.coffeetracker2.CoffeeListAdapter;


import java.sql.Date;
import java.util.List;

public class CoffeeListFragment extends Fragment {

    private static final String TAG = "CoffeeListFragment";

    public static final int RATE_COFFEE_PRODUCTIVITY = 1;

    private CoffeeRepository coffeeRepository;
    private CoffeeListAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coffee_list, container, false);

        RecyclerView coffeeRecyclerView = root.findViewById(R.id.frag_coffee_recyclerView);
        coffeeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layout = new LinearLayoutManager(this.getContext());
        coffeeRecyclerView.setLayoutManager(layout);
        coffeeRecyclerView.setHasFixedSize(true);

        adapter = new CoffeeListAdapter();
        coffeeRecyclerView.setAdapter(adapter);

        coffeeRepository = new ViewModelProvider(requireActivity()).get(CoffeeRepository.class);
        coffeeRepository.getAllCoffees().observe(getViewLifecycleOwner(), new Observer<List<Coffee>>() {
            @Override
            public void onChanged(List<Coffee> coffees) {
                adapter.submitList(coffees);
            }
        });


        // Functionality for coffee delete on right slide
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                coffeeRepository.delete(adapter.getCoffeeAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Coffee entry deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(coffeeRecyclerView);

        adapter.setOnItemClickListener(new CoffeeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Coffee coffee) {

                Intent intent = new Intent(getContext(), RatingActivity.class);

                intent.putExtra(RatingActivity.EXTRA_TYPE, coffee.getType());
                intent.putExtra(RatingActivity.EXTRA_CAFFEINE, coffee.getCaffeine());
                intent.putExtra(RatingActivity.EXTRA_DATE, coffee.getDate().getTime());
                intent.putExtra(RatingActivity.EXTRA_PRODUCTIVITY, coffee.getProductivityRating());

                // Android room needs ID to identify which item it is
                intent.putExtra(RatingActivity.EXTRA_ID, coffee.getId());
                startActivityForResult(intent, RATE_COFFEE_PRODUCTIVITY);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Find which request in handled by requestCode
        if (requestCode == RATE_COFFEE_PRODUCTIVITY && resultCode == Activity.RESULT_OK) {

            int id = data.getIntExtra(RatingActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Coffee cannot be rated", Toast.LENGTH_SHORT).show();
                return;
            }

            String type = data.getStringExtra(RatingActivity.EXTRA_TYPE);
            int caffeine = data.getIntExtra(RatingActivity.EXTRA_CAFFEINE, -1);
            Date date = new Date(data.getLongExtra(RatingActivity.EXTRA_DATE, -1));
            int productivity = data.getIntExtra(RatingActivity.EXTRA_PRODUCTIVITY, -1);

            Coffee coffee = new Coffee(type, caffeine, date, productivity);
            coffee.setId(id);
            coffeeRepository.update(coffee);

            Toast.makeText(getContext(), "Your rating has been registered", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "The rating was not changed", Toast.LENGTH_SHORT).show();
        }
    }
}
