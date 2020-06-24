package com.example.coffeetracker2;


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

        setTitle("Productivity");
        final RatingBar ratingBar = findViewById(R.id.act_ratingbar);
        final TextView ratingMessage = findViewById(R.id.act_rating_message);
        Button getRating = findViewById(R.id.act_get_rating_btn);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ratingMessage.setText(String.valueOf(v));
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
        });

        getRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((int) ratingBar.getRating() > 0) {

                    Intent data = new Intent();
                    data.putExtra(EXTRA_PRODUCTIVITY, (int) ratingBar.getRating());



                    //this is the case where we Edit a contact we also needs to send the ID
                    //because an already existing contact does have an ID
                    int id = getIntent().getIntExtra(EXTRA_ID, -1);
                    if( id != -1 ){
                        data.putExtra(EXTRA_ID, id);
                    }
                    setResult(RESULT_OK, data);

                    String rating = "Your rating has been registered.";
                    Toast.makeText(RatingActivity.this, rating, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    String rating = "Please rate your productivity.";
                    Toast.makeText(RatingActivity.this, rating, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

