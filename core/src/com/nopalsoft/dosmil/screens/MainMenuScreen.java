package com.nopalsoft.dosmil.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.MainGame;
import com.nopalsoft.dosmil.Settings;
import com.nopalsoft.dosmil.game.GameScreen;

public class MainMenuScreen extends Screens {

    Image imgTitulo;

    Label lbPlay;
    Label lbHelp;
    Label lbLeaderboard;
    Label lbRate;


    Button btMusica;
    Button btSonido;
    Button btFacebook;

    public MainMenuScreen(final MainGame game) {
        super(game);
        imgTitulo = new Image(Assets.title);
        imgTitulo.setPosition(SCREEN_WIDTH / 2f - imgTitulo.getWidth() / 2f, 580);

        lbPlay = new Label(Assets.languages.get("play"), Assets.labelStyleLarge);
        lbPlay.setPosition(SCREEN_WIDTH / 2f - lbPlay.getWidth() / 2f, 450);
        addPressEffect(lbPlay);
        lbPlay.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }

        });

        // Help
        lbHelp = new Label(Assets.languages.get("help"), Assets.labelStyleLarge);
        lbHelp.setPosition(SCREEN_WIDTH / 2f - lbHelp.getWidth() / 2f, 350);
        addPressEffect(lbHelp);
        lbHelp.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(HelpScreen.class, game);
            }

        });

        // Rate
        lbRate = new Label(Assets.languages.get("rate"), Assets.labelStyleLarge);
        lbRate.setPosition(SCREEN_WIDTH / 2f - lbRate.getWidth() / 2f, 250);
        addPressEffect(lbRate);
        lbRate.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showRater();
            }

        });

        // Leaderboard
        lbLeaderboard = new Label(Assets.languages.get("leaderboard"), Assets.labelStyleLarge);
        lbLeaderboard.setFontScale(.85f);
        lbLeaderboard.setWidth(SCREEN_WIDTH);
        lbLeaderboard.setPosition(SCREEN_WIDTH / 2f - lbLeaderboard.getWidth() / 2f, 150);
        lbLeaderboard.setAlignment(Align.center);
        lbLeaderboard.setWrap(true);

        addPressEffect(lbLeaderboard);
        lbLeaderboard.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getLeaderboard();
                } else
                    game.gameServiceHandler.signIn();

            }

        });

        btMusica = new Button(Assets.buttonStyleMusic);
        btMusica.setPosition(5, 5);
        btMusica.setChecked(!Settings.isMusicOn);
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                btMusica.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.playMusic();
                else
                    Assets.pauseMusic();

            }
        });

        btSonido = new Button(Assets.buttonStyleSound);
        btSonido.setPosition(75, 5);
        btSonido.setChecked(!Settings.isSoundOn);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                btSonido.setChecked(!Settings.isSoundOn);
            }
        });

        btFacebook = new Button(Assets.buttonFacebook);
        btFacebook.setSize(50, 50);
        btFacebook.setPosition(SCREEN_WIDTH - btFacebook.getWidth() - 5, 10);
        addPressEffect(btFacebook);
        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showFacebook();
            }
        });

        stage.addActor(imgTitulo);
        stage.addActor(lbPlay);
        stage.addActor(lbHelp);
        stage.addActor(lbLeaderboard);
        stage.addActor(lbRate);
        stage.addActor(btMusica);
        stage.addActor(btSonido);
        stage.addActor(btFacebook);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            Gdx.app.exit();
        return true;

    }
}
