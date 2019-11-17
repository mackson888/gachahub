package com.macksonlima.gachahub.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.macksonlima.gachahub.R;
import com.macksonlima.gachahub.objects.UserProfile;

public class ProfileCreationActivity extends AppCompatActivity {

    Button btSaveNewProfile;
    EditText editNickname;
    EditText editBirthday;

    DatabaseReference databaseUserProfile;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        databaseUserProfile = FirebaseDatabase.getInstance().getReference("user_profile");

        btSaveNewProfile = (Button) findViewById(R.id.buttonSaveNewProfile) ;
        editNickname = (EditText) findViewById(R.id.editNickname);
        editBirthday = (EditText) findViewById(R.id.editBirthday);

        btSaveNewProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String nick = editNickname.getText().toString().trim();
                String birthday = editBirthday.getText().toString().trim();

                if(!TextUtils.isEmpty(nick) || !TextUtils.isEmpty(birthday)){
                    String googleId = currentUser.getUid();
                    String userId = databaseUserProfile.push().getKey();

                    UserProfile userProfile = new UserProfile(userId, googleId, nick, birthday);

                    databaseUserProfile.child(userId).setValue(userProfile);

                    Toast.makeText(ProfileCreationActivity.this,"ProfileActivity created", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ProfileCreationActivity.this, HomeScreenActivity.class);
                    intent.putExtra("UserProfile", userProfile);
                    startActivity(intent);

                } else {
                    Toast.makeText(ProfileCreationActivity.this,"All fields are necessary", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
