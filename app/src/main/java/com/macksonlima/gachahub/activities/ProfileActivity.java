package com.macksonlima.gachahub.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.macksonlima.gachahub.R;
import com.macksonlima.gachahub.objects.Game;
import com.macksonlima.gachahub.objects.GamesList;
import com.macksonlima.gachahub.objects.UserGame;
import com.macksonlima.gachahub.objects.UserProfile;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    TextView textViewNameProfile;
    Spinner spinnerGameList;
    EditText editTextGameCode;
    Button buttonAddGame;

    DatabaseReference databaseUserProfile;

    ListView listViewGames;

    List<UserGame> gameList;

    UserProfile userProfile;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (userProfile == null) {
            Intent intent = getIntent();
            userProfile = (UserProfile) intent.getSerializableExtra("UserProfile");
        }

        databaseUserProfile = FirebaseDatabase.getInstance().getReference("user_games").child(userProfile.getUserId());

        textViewNameProfile = (TextView) findViewById(R.id.textViewNameProfile) ;
        spinnerGameList = (Spinner) findViewById(R.id.spinnerGameList);
        editTextGameCode = (EditText) findViewById(R.id.editTextGameCode);
        buttonAddGame = (Button) findViewById(R.id.buttonAddGame);

        textViewNameProfile.setText(userProfile.getNickname() + "'s Profile");

        listViewGames = (ListView) findViewById(R.id.listViewGames);

        gameList = new ArrayList<>();

        buttonAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gameName = spinnerGameList.getSelectedItem().toString().trim();
                String gameCode = editTextGameCode.getText().toString().trim();

                Game game = new Game();

                game.setGameId(gameName);
                game.setGameName(gameName);
                game.setGameCode(gameCode);

                if(!TextUtils.isEmpty(gameName) || !TextUtils.isEmpty(gameCode)){
                    String userGameId = databaseUserProfile.push().getKey();

                    UserGame userGame = new UserGame(userGameId,game);

                    databaseUserProfile.child(userGameId).setValue(userGame);

                    Toast.makeText(ProfileActivity.this,"The game was inserted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(ProfileActivity.this,"All fields are necessary", Toast.LENGTH_LONG).show();
                }

            }
        });

        listViewGames.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                UserGame userGame = gameList.get(position);

                showUpdateDialog(userGame.getUserGameId(), userGame.getGame());

                return false;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (userProfile == null) {
            Intent intent = getIntent();
            userProfile = (UserProfile) intent.getSerializableExtra("UserProfile");
        }

        if (databaseUserProfile == null) {
            databaseUserProfile = FirebaseDatabase.getInstance().getReference("user_games").child(userProfile.getUserId());
        }

        databaseUserProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                gameList.clear();

                for(DataSnapshot userGamesSnapshot : dataSnapshot.getChildren()){
                    UserGame userGame = userGamesSnapshot.getValue(UserGame.class);

                    gameList.add(userGame);

                }

                GamesList adapter = new GamesList(ProfileActivity.this, gameList);
                listViewGames.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void showUpdateDialog(final String userGameId, final Game game){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editGameCode = (EditText) dialogView.findViewById(R.id.editGameCode);
        final Button   buttonUpdate = (Button)   dialogView.findViewById(R.id.buttonUpdate);
        final Button   buttonDelete = (Button)   dialogView.findViewById(R.id.buttonDelete);

        dialogBuilder.setTitle("Updating "+ game.getGameName() +" Game Code");

        editGameCode.setText(game.getGameCode());

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = editGameCode.getText().toString().trim();

                if (TextUtils.isEmpty(game.getGameName())) {
                    editGameCode.setError("A code is required");
                    return;
                } else {
                    updateGame(userGameId, game, code);

                    alertDialog.dismiss();
                }

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                deleteUserGame(userGameId);

                alertDialog.dismiss();
            }
        });

    }

    private void deleteUserGame(String userGameId) {
        DatabaseReference userGame = FirebaseDatabase.getInstance().getReference("user_games").child(userProfile.getUserId()).child(userGameId);

        userGame.removeValue();

        Toast.makeText(this, "Game code Deleted", Toast.LENGTH_SHORT).show();

    }

    private boolean updateGame(String userGameId, Game game, String code) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("user_games").child(userProfile.getUserId()).child(userGameId);

        game.setGameCode(code);

        UserGame userGame = new UserGame(userGameId, game);

        databaseReference.setValue(userGame);

        Toast.makeText(this, "Game Code Updated", Toast.LENGTH_SHORT).show();

        return true;
    }

}
