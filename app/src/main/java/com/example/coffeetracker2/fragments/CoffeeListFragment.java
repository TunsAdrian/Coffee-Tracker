package com.example.coffeetracker2.fragments;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeetracker2.CoffeeRepository;
import com.example.coffeetracker2.R;
import com.example.coffeetracker2.RatingActivity;
import com.example.coffeetracker2.database.Coffee;
import com.example.coffeetracker2.recyclerView.CoffeeListAdapter;


import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CoffeeListFragment extends Fragment {

    public static final int RATE_COFFEE_PRODUCTIVITY = 1;
    CoffeeRepository coffeeRepository;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coffee_list, container, false);

        RecyclerView coffeeRecyclerView = root.findViewById(R.id.frag_coffee_recyclerView);
        coffeeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layout = new LinearLayoutManager(this.getContext());

        coffeeRecyclerView.setLayoutManager(layout);
        coffeeRecyclerView.setHasFixedSize(true);

        final CoffeeListAdapter adapter = new CoffeeListAdapter(this.getContext());
        coffeeRecyclerView.setAdapter(adapter);

        coffeeRepository = new ViewModelProvider(requireActivity()).get(CoffeeRepository.class);
        //final CoffeeRepository coffeeViewModel = ViewModelProviders.of(this).get(CoffeeRepository.class);
        coffeeRepository.getAllCoffees().observe(getViewLifecycleOwner(), new Observer<List<Coffee>>() {
            @Override
            public void onChanged(List<Coffee> coffees) {
                adapter.submitList(coffees);
            }
        });


        //FUNCTIONALITY TO SLIDE IN ORDER TO DELETE A COFFEE
        //SWIPE RIGHT TO DELETE A COFFEE
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                coffeeRepository.delete(adapter.getCoffeeAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Coffee deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(coffeeRecyclerView);


        adapter.setOnItemClickListener(new CoffeeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Coffee coffee) {

                if (coffee.getProductivityRating() == -1) {




                    Intent intent = new Intent(getContext(), RatingActivity.class);
                    intent.putExtra(RatingActivity.EXTRA_TYPE, coffee.getType());
                    intent.putExtra(RatingActivity.EXTRA_CAFFEINE, coffee.getCaffeine());
                    intent.putExtra(RatingActivity.EXTRA_DATE, coffee.getDate());
                    intent.putExtra(RatingActivity.EXTRA_PRODUCTIVITY, coffee.getProductivityRating());
                    //ROOM ALSO NEEDS THE ID TO FIGURE OUT WHICH CONTACT ARE WE TALKING ABOUT
                    intent.putExtra(RatingActivity.EXTRA_ID, coffee.getId());
                    startActivityForResult(intent, RATE_COFFEE_PRODUCTIVITY);
                }
            }
        });

        return root;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
//        //by this way, we find out which request we are handling here
//        if (requestCode == RATE_COFFEE_PRODUCTIVITY && resultCode == RESULT_OK) {
//            int id = data.getIntExtra(RatingActivity.EXTRA_ID, -1);
//
//            if(id == - 1){
//                Toast.makeText(getContext(),"Coffee cannot be rated", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            //if this is ok, then retrieve the extras we left over
//            String type = data.getStringExtra(RatingActivity.EXTRA_TYPE);
//            String caffeine = data.getStringExtra(RatingActivity.EXTRA_CAFFEINE);
//            String date = data.getStringExtra(RatingActivity.EXTRA_DATE);
//            String productivity = data.getStringExtra(RatingActivity.EXTRA_PRODUCTIVITY);
//            //TODO: Add calendar...
//
//            if (productivity != null) {
//                //Coffee contact = new Coffee(Integer.parseInt(productivity));
//                //contact.setId(id); // needed such that ROOM knows which contact is updated
//                Coffee coffee = new Coffee("small", -1, Calendar.getInstance().getTime(),-1);
//                coffee.setId(id);
//                coffee.setProductivityRating(Integer.parseInt(productivity));
//                coffeeRepository.update(coffee);
//
//                Toast.makeText(getContext(), "Coffee successfully rated", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//
//            Toast.makeText(getContext(), "Coffee NOT SAVED", Toast.LENGTH_SHORT).show();
//        }
//    }
}
