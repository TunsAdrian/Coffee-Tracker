package com.example.coffeetracker2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class RatingActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.coffeetracker2.ID";
    public static final String EXTRA_TYPE = "com.example.coffeetracker2.EXTRA_TYPE";
    public static final String EXTRA_CAFFEINE = "com.example.coffeetracker2.EXTRA_CAFFEINE";
    public static final String EXTRA_DATE = "com.example.coffeetracker2.EXTRA_DATE";
    public static final String EXTRA_PRODUCTIVITY = "com.example.coffeetracker2.EXTRA_PRODUCTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        setTitle("Productivity rating");
        final RatingBar ratingBar = findViewById(R.id.act_coffee_rating);
        final TextView ratingMessage = findViewById(R.id.act_rating_message);
        final Button getRatingBtn = findViewById(R.id.act_get_rating_btn);

        ratingBar.setRating(getIntent().getIntExtra(EXTRA_PRODUCTIVITY, 3));
        changeRatingMessage(ratingBar, ratingMessage);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingMessage.setText(String.valueOf(v));
                changeRatingMessage(ratingBar, ratingMessage);
            }
        });

        getRatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ratingBar.getRating() > 0) {

                    Intent data = new Intent();
                    data.putExtra(EXTRA_PRODUCTIVITY, (int) ratingBar.getRating());

                    int id = getIntent().getIntExtra(EXTRA_ID, -1);
                    String type = getIntent().getStringExtra(EXTRA_TYPE);
                    int caffeine = getIntent().getIntExtra(EXTRA_CAFFEINE, -1);
                    Long date = getIntent().getLongExtra(EXTRA_DATE, -1);

                    data.putExtra(EXTRA_TYPE, type);
                    data.putExtra(EXTRA_CAFFEINE, caffeine);
                    data.putExtra(EXTRA_DATE, date);

                    // For update operation the ID needs to be sent because an already existing coffee does have an ID
                    if (id != -1) {
                        data.putExtra(EXTRA_ID, id);
                    }

                    setResult(Activity.RESULT_OK, data);
                    finish();

                } else {
                    String rating = "Please rate your productivity";
                    Toast.makeText(RatingActivity.this, rating, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changeRatingMessage(RatingBar ratingBar, TextView ratingMessage) {
        switch ((int) ratingBar.getRating()) {
            case 1:
                ratingMessage.setText(R.string.very_unproductive_rating);
                break;
            case 2:
                ratingMessage.setText(R.string.unproductive_rating);
                break;
            case 3:
                ratingMessage.setText(R.string.neutral_rating);
                break;
            case 4:
                ratingMessage.setText(R.string.productive_rating);
                break;
            case 5:
                ratingMessage.setText(R.string.very_productive_rating);
                break;
            default:
                ratingMessage.setText("");
        }
    }
}

