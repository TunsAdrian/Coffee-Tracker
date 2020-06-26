package com.example.coffeetracker2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.coffeetracker2.fragments.CoffeeListFragment;
import com.example.coffeetracker2.fragments.CoffeeSelectFragment;
import com.example.coffeetracker2.fragments.StatisticsFragment;
import com.example.coffeetracker2.utils.CoffeeRepository;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CoffeeRepository coffeeViewModel;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        coffeeViewModel = new ViewModelProvider(this).get(CoffeeRepository.class);
        initView();

        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        boolean fromNotification = false;
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        TextView userEmail = headerView.findViewById(R.id.user_email);
        CircleImageView userPhoto = headerView.findViewById(R.id.user_photo);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            fromNotification = bundle.getBoolean(AlertBroadcast.FROM_NOTIFICATION);

            userName.setText(bundle.getString(SignInActivity.USER_NAME));
            userEmail.setText(bundle.getString(SignInActivity.USER_EMAIL));
            Picasso.get().load((Uri) bundle.get(SignInActivity.USER_PHOTO))
                    .placeholder(R.mipmap.ic_launcher_round)
                    .resize(124, 124)
                    .transform(new CircleTransform())
                    .centerCrop()
                    .into(userPhoto);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fromNotification) {

            navigationView.getMenu().getItem(2).setChecked(true);
            transaction.replace(R.id.fragment_placeholder, new CoffeeListFragment());
            transaction.commit();
        } else {

            navigationView.getMenu().getItem(0).setChecked(true);
            transaction.replace(R.id.fragment_placeholder, new CoffeeSelectFragment());
            transaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getSupportFragmentManager();

        if (id == R.id.nav_home) {

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_placeholder, new CoffeeSelectFragment(), "CoffeeSelectFragment");
            transaction.commit();
        } else if (id == R.id.nav_coffee_list) {

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_placeholder, new CoffeeListFragment(), "CoffeeListFragment");
            transaction.commit();
        } else if (id == R.id.nav_statistics) {

            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_placeholder, new StatisticsFragment(), "StatisticsFragment");
            transaction.commit();
        } else if (id == R.id.sign_out_button) {

            signOut();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
    }

    private void signOut() {
        firebaseAuth.signOut();
        drawer.closeDrawer(GravityCompat.START);

        Intent intent = new Intent();
        intent.putExtra(SignInActivity.GOOGLE_SIGN_OUT, true);
        setResult(RESULT_OK, intent);

        finish();
    }

    // Method used to convert the google user image in a circle image
    public static class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
