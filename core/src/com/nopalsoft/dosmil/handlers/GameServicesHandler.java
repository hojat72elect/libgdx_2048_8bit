package com.nopalsoft.dosmil.handlers;

public interface GameServicesHandler {

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void submitScore(long score);

    /**
     * Este metodo abstrae a GPGS o a AGC
     */
    void getLeaderboard();

    boolean isSignedIn();

    void signIn();

}
