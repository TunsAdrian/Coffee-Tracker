package com.example.coffeetracker2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInActivity extends AppCompatActivity {


    public static final String USER_NAME = "com.example.coffeetracker2.USER_NAME";
    public static final String USER_EMAIL = "com.example.coffeetracker2.USER_EMAIL";
    public static final String USER_PHOTO = "com.example.coffeetracker2.USER_PHOTO";
    public static final String GOOGLE_SIGN_OUT = "com.example.coffeetracker2.GOOGLE_SIGN_OUT";
    public static final int MAIN_ACTIVITY = 213;
    public static final int RC_SIGN_IN = 200;

    GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseGoogleAuth(account);

            } catch (ApiException e) {
                Toast.makeText(SignInActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == MAIN_ACTIVITY) {
            if (data != null) {
                boolean signOut = data.getBooleanExtra(GOOGLE_SIGN_OUT, false);

                if (signOut) {
                    Toast.makeText(SignInActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();

                    // Used for making the application ask to login again with an account, after logging out
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("Disconnected", "Disconnected");
                        }
                    });
                }
            }
        }
    }

    private void firebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential).
                addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(SignInActivity.this, "Signed in successfully", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(SignInActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            String email = user.getEmail();
            String name = user.getDisplayName();
            Uri image = user.getPhotoUrl();

            intent.putExtra(USER_NAME,name);
            intent.putExtra(USER_EMAIL,email);
            intent.putExtra(USER_PHOTO,image);
            startActivityForResult(intent, MAIN_ACTIVITY);
        }
    }
}
