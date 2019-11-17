package com.macksonlima.gachahub.objects;

import java.io.Serializable;

public class UserGame implements Serializable {

    private String userGameId;
    private Game game;

    public UserGame() {
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getUserGameId() {
        return userGameId;
    }

    public void setUserGameId(String userGameId) {
        this.userGameId = userGameId;
    }

    public UserGame(String userGameId, Game game) {
        this.userGameId = userGameId;
        this.game = game;
    }
}
