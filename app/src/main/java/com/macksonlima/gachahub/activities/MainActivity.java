package com.macksonlima.gachahub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.macksonlima.gachahub.R;
import com.macksonlima.gachahub.objects.UserProfile;

public class MainActivity extends AppCompatActivity {

    private SignInButton signIn;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        signIn = findViewById(R.id.sign_in_button);
        signIn.setSize(SignInButton.SIZE_WIDE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("user_profile");

            try {

                myRef.orderByChild("googleId").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

                    Intent intent;
                    UserProfile userProfile;

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot userProfileSnapshot : dataSnapshot.getChildren()){
                            userProfile = userProfileSnapshot.getValue(UserProfile.class);
                        }

                        if (userProfile != null) {

                            intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                            intent.putExtra("UserProfile", userProfile);
                            startActivity(intent);

                            Log.d("Logged", "Logged as: " + userProfile.getNickname());
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                        Log.w("Error", "Failed to read value.", error.toException());

                    }
                });

            } catch (Exception e) {

            }

        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Error", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Auth", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        //mAuth.signInWithCredential(credential)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Error", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("user_profile");

                            myRef.orderByChild("googleId").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                Intent intent;
                                UserProfile userProfile;
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for(DataSnapshot userProfileSnapshot : dataSnapshot.getChildren()){
                                        userProfile = userProfileSnapshot.getValue(UserProfile.class);
                                    }

                                    if (userProfile != null) {
                                        intent = new Intent(MainActivity.this, HomeScreenActivity.class);
                                        intent.putExtra("UserProfile", userProfile);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(MainActivity.this, ProfileCreationActivity.class);
                                        startActivity(intent);
                                    }
                                    Log.d("Logged", "Logged as: " + userProfile.getNickname());
                                }
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Log.w("Error", "Failed to read value.", error.toException());
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Error", "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Failed to sign in.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }
}