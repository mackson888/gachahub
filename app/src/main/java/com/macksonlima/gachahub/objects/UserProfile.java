package com.macksonlima.gachahub.objects;

import java.io.Serializable;

public class UserProfile implements Serializable {

    private String userId;
    private String googleId;
    private String nickname;
    private String birthday;

    public UserProfile(String userId, String googleId, String nickname, String birthday) {
        this.userId = userId;
        this.googleId = googleId;
        this.nickname = nickname;
        this.birthday = birthday;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public UserProfile(){

    }

}
