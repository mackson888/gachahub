package com.macksonlima.gachahub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.macksonlima.gachahub.R;
import com.macksonlima.gachahub.objects.UserProfile;

import org.w3c.dom.Text;

public class HomeScreenActivity extends AppCompatActivity {

    TextView firstName;
    TextView textNickHome;
    Button buttonGoToProfile;
    Button signOut;

    private FirebaseAuth mAuth;
    UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        Intent intent = getIntent();
        userProfile = (UserProfile) intent.getSerializableExtra("UserProfile");

        firstName = (TextView) findViewById(R.id.firstName);
        textNickHome = (TextView) findViewById(R.id.textNickHome);
        buttonGoToProfile = (Button)findViewById(R.id.buttonGoToProfile);
        signOut = (Button) findViewById(R.id.sign_out);

        if (currentUser != null) {
            String personName = currentUser.getDisplayName();
            String personEmail = currentUser.getEmail();
            String personId = currentUser.getUid();
            Uri personPhoto =  currentUser.getPhotoUrl();

            firstName.setText(personName);
            textNickHome.setText("Welcome, " + userProfile.getNickname());

        }

        buttonGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeScreenActivity.this, ProfileActivity.class);
                intent.putExtra("UserProfile", userProfile);
                startActivity(intent);
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(HomeScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
