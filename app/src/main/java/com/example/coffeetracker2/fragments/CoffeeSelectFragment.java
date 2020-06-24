package com.example.coffeetracker2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffeetracker2.R;

public class CoffeeSelectFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coffee_select, container, false);
//        final CoffeeViewModel coffeeViewModel = new ViewModelProvider(this).get(CoffeeViewModel.class);
//        Button smallCoffee = root.findViewById(R.id.frag_small_coffee);
//        smallCoffee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Coffee smallCoffee = new Coffee("Small", 40, Calendar.getInstance().getTime(), -1);
//               coffeeViewModel.insert(smallCoffee);
//               Toast.makeText(getContext(), "Small coffee registered", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        Button mediumCoffee = root.findViewById(R.id.frag_medium_coffee);
//        mediumCoffee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Coffee mediumCoffee = new Coffee("Medium", 60, Calendar.getInstance().getTime(), -1);
//                coffeeViewModel.insert(mediumCoffee);
//                Toast.makeText(getContext(), "Medium coffee registered", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        Button largeCoffee = root.findViewById(R.id.frag_large_coffee);
//        largeCoffee.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Coffee largeCoffee = new Coffee("Large", 80, Calendar.getInstance().getTime(), -1);
//                coffeeViewModel.insert(largeCoffee);
//                Toast.makeText(getContext(), "Large coffee registered", Toast.LENGTH_LONG).show();
//            }
//        });

        return root;
    }
}
