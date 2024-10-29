package com.nopalsoft.dosmil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.nopalsoft.dosmil.MainGame;
import com.nopalsoft.dosmil.handlers.GoogleGameServicesHandler;
import com.nopalsoft.dosmil.handlers.RequestHandler;

public class DesktopLauncher {

    static RequestHandler reqHandler = new RequestHandler() {

        @Override
        public void showRater() {

        }

        @Override
        public void loadInterstitial() {

        }

        @Override
        public void showInterstitial() {

        }

        @Override
        public void showFacebook() {

        }

        @Override
        public void showAdBanner() {

        }

        @Override
        public void shareOnTwitter(String message) {

        }

        @Override
        public void shareOnFacebook(String message) {

        }

        @Override
        public void removeAds() {

        }

        @Override
        public void hideAdBanner() {

        }

    };
    static GoogleGameServicesHandler gameHandler = new GoogleGameServicesHandler() {

        @Override
        public void signIn() {

        }

        @Override
        public boolean isSignedIn() {
            return false;
        }

        @Override
        public void getLeaderboard() {

        }

        @Override
        public void submitScore(long score) {

        }
    };

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "2048 8Bit";
        cfg.width = 480;
        cfg.height = 800;

        new LwjglApplication(new MainGame(reqHandler, gameHandler), cfg);
    }
}
