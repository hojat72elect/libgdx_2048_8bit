package com.nopalsoft.dosmil.handlers;

public interface GameServicesHandler {

    void submitScore(long score);

    void getLeaderboard();

    boolean isSignedIn();

    void signIn();

}
