package com.macksonlima.gachahub.objects;

public class Game {

    private String gameId;
    private String gameName;
    private String gameCode;

    public Game(String gameId, String gameName, String gameCode) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.gameCode = gameCode;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public Game() {
    }
}
