package com.nopalsoft.dosmil.handlers;

public interface RequestHandler {
    void showRater();

    void loadInterstitial();

    void showInterstitial();

    void showFacebook();

    void shareOnFacebook(final String message);

    void shareOnTwitter(final String message);

    void removeAds();

    void showAdBanner();

    void hideAdBanner();

}
