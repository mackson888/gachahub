package com.macksonlima.gachahub.objects;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.macksonlima.gachahub.R;

import java.util.List;

public class GamesList extends ArrayAdapter<UserGame> {

    private Activity context;
    private List<UserGame> gameList;

    public GamesList(Activity context, List<UserGame> gameList){
        super(context, R.layout.games_list, gameList);
        this.context = context;
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.games_list, null, true);

        TextView textGameName = (TextView) listViewItem.findViewById(R.id.textGameName);
        TextView textGameCode = (TextView) listViewItem.findViewById(R.id.textGame0ode);

        UserGame userGame = gameList.get(position);

        textGameName.setText(userGame.getGame().getGameName());
        textGameCode.setText(userGame.getGame().getGameCode());

        return listViewItem;
    }
}
