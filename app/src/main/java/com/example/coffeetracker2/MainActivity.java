package com.example.coffeetracker2;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.coffeetracker2.database.Coffee;
import com.example.coffeetracker2.fragments.CoffeeListFragment;
import com.example.coffeetracker2.fragments.CoffeeSelectFragment;
import com.example.coffeetracker2.fragments.StatisticsFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private CoffeeRepository coffeeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        coffeeViewModel = new ViewModelProvider(this).get(CoffeeRepository.class);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_placeholder, new CoffeeSelectFragment());
        transaction.commit();
    }

    public void registerCoffee(View view) {

        // Separate the cases for each type of coffee
        switch (view.getId()) {
            case R.id.frag_small_coffee:

                Coffee smallCoffee =  new Coffee("Small", 40, Calendar.getInstance().getTime(), -1);
                coffeeViewModel.insert(smallCoffee);

                Toast.makeText(MainActivity.this, "Small coffee registered", Toast.LENGTH_LONG).show();
                break;
            case R.id.frag_medium_coffee:

                Coffee midCoffee = new Coffee("Medium", 60, Calendar.getInstance().getTime(), -1);
                coffeeViewModel.insert(midCoffee);

                Toast.makeText(MainActivity.this, "Medium coffee registered", Toast.LENGTH_LONG).show();
                break;
            case R.id.frag_large_coffee:

                Coffee largeCoffee = new Coffee("Large", 80, Calendar.getInstance().getTime(), -1);
                coffeeViewModel.insert(largeCoffee);

                Toast.makeText(MainActivity.this, "Large coffee registered", Toast.LENGTH_LONG).show();
                break;
        }

        // Prepare intent to call in alarm manager
        Intent intent = new Intent(MainActivity.this, AlertBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Get system time when button is clicked and number of seconds for when to show notification
        long timeButtonClicked = System.currentTimeMillis();
        long threeSeconds = 1000 * 3;

        // Set the actual alarm
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeButtonClicked + threeSeconds, pendingIntent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (id == R.id.nav_home) {
            transaction.replace(R.id.fragment_placeholder, new CoffeeSelectFragment());
            transaction.commit();
        } else if (id == R.id.nav_coffee_list) {
            transaction.replace(R.id.fragment_placeholder, new CoffeeListFragment());
            transaction.commit();
        } else if (id == R.id.nav_statistics) {
            transaction.replace(R.id.fragment_placeholder, new StatisticsFragment());
            transaction.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}
