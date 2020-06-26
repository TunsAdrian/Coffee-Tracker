package com.example.coffeetracker2.fragments;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coffeetracker2.AlertBroadcast;
import com.example.coffeetracker2.R;
import com.example.coffeetracker2.database.Coffee;
import com.example.coffeetracker2.utils.CoffeeRepository;

import java.util.Calendar;

public class CoffeeSelectFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "CoffeeSelectFragment";

    private NumberPicker smallCoffeePicker;
    private NumberPicker mediumCoffeePicker;
    private NumberPicker largeCoffeePicker;
    private CoffeeRepository coffeeViewModel;
    private Button smallCoffeeBtn;
    private Button mediumCoffeeBtn;
    private Button largeCoffeeBtn;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_coffee_select, container, false);
        createNotificationChannel();
        initView(root);
        coffeeViewModel = new ViewModelProvider(this).get(CoffeeRepository.class);

        int minPickerValue = 0;
        int maxPickerValue = 40;
        int stepSize = 5;

        String[] numberValues = new String[maxPickerValue - minPickerValue + 1];
        for (int i = 0; i <= maxPickerValue - minPickerValue; i++) {
            numberValues[i] = String.valueOf((minPickerValue + i) * stepSize);
        }

        smallCoffeeBtn.setOnClickListener(this);
        mediumCoffeeBtn.setOnClickListener(this);
        largeCoffeeBtn.setOnClickListener(this);

        smallCoffeePicker.setMinValue(0);
        smallCoffeePicker.setMaxValue(numberValues.length - 1);
        smallCoffeePicker.setValue(8);
        smallCoffeePicker.setDisplayedValues(numberValues);

        mediumCoffeePicker.setMinValue(0);
        mediumCoffeePicker.setMaxValue(numberValues.length - 1);
        mediumCoffeePicker.setValue(12);
        mediumCoffeePicker.setDisplayedValues(numberValues);

        largeCoffeePicker.setMinValue(0);
        largeCoffeePicker.setMaxValue(numberValues.length - 1);
        largeCoffeePicker.setValue(16);
        largeCoffeePicker.setDisplayedValues(numberValues);

        return root;
    }

    @Override
    public void onClick(View view) {
        registerCoffee(view);
    }

    private void registerCoffee(View view) {

        // Separate the cases for each type of coffee
        switch (view.getId()) {
            case R.id.frag_small_coffee:

                Coffee smallCoffee = new Coffee("Small", smallCoffeePicker.getValue(), Calendar.getInstance().getTime(), -1);
                coffeeViewModel.insert(smallCoffee);

                Toast.makeText(getContext(), "Small coffee registered", Toast.LENGTH_SHORT).show();
                break;
            case R.id.frag_medium_coffee:

                Coffee midCoffee = new Coffee("Medium", mediumCoffeePicker.getValue(), Calendar.getInstance().getTime(), -1);
                coffeeViewModel.insert(midCoffee);

                Toast.makeText(getContext(), "Medium coffee registered", Toast.LENGTH_SHORT).show();
                break;
            case R.id.frag_large_coffee:

                Coffee largeCoffee = new Coffee("Large", largeCoffeePicker.getValue(), Calendar.getInstance().getTime(), -1);
                coffeeViewModel.insert(largeCoffee);

                Toast.makeText(getContext(), "Large coffee registered", Toast.LENGTH_SHORT).show();
                break;
        }

        // Prepare intent to call in alarm manager
        Intent intent = new Intent(getContext(), AlertBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);

        // Get system time when button is clicked and number of seconds for when to show notification
        long timeButtonClicked = System.currentTimeMillis();
        long fiveSeconds = 1000 * 5;

        // Set the actual alarm
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeButtonClicked + fiveSeconds, pendingIntent);
        }
    }

    private void createNotificationChannel() {

        String CHANNEL_ID = "activity_channel";
        // Create the NotificationChannel, but only on API 26+ because the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = "Activity Channel";
            String description = "Notification channel for rating productivity after a coffee";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance or other notification behaviors after this
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void initView(View v) {

        smallCoffeeBtn = v.findViewById(R.id.frag_small_coffee);
        mediumCoffeeBtn = v.findViewById(R.id.frag_medium_coffee);
        largeCoffeeBtn = v.findViewById(R.id.frag_large_coffee);

        smallCoffeePicker = v.findViewById(R.id.frag_small_coffee_picker);
        mediumCoffeePicker = v.findViewById(R.id.frag_medium_coffee_picker);
        largeCoffeePicker = v.findViewById(R.id.frag_large_coffee_picker);
    }
}
