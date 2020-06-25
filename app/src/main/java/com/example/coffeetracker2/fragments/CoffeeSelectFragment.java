package com.example.coffeetracker2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffeetracker2.R;

public class CoffeeSelectFragment extends Fragment {

    private static final String TAG = "CoffeeSelectFragment";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coffee_select, container, false);

        return root;
    }
}
