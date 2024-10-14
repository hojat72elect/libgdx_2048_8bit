package com.nopalsoft.dosmil.handlers;

public interface RequestHandler {
    void showRater();

    void loadInterstitial();

    void showInterstitial();

    void showFacebook();

    void shareOnFacebook(final String mensaje);

    void shareOnTwitter(final String mensaje);

    void removeAds();

    void showAdBanner();

    void hideAdBanner();

}
