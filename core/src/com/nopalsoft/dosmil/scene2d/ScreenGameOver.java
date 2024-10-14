package com.nopalsoft.dosmil.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.Settings;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class ScreenGameOver extends Group {

    Screens screen;

    public ScreenGameOver(final Screens screen, boolean didWin, int time, long score) {
        this.screen = screen;
        setSize(420, 450);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260);
        setScale(.5f);

        Image background = new Image(Assets.scoresBackground);
        background.setSize(getWidth(), getHeight());
        addActor(background);

        String textTitulo = Assets.languages.get("gameOver");
        if (didWin)
            textTitulo = Assets.languages.get("congratulations");

        Label lbCongratulations = new Label(textTitulo, Assets.labelStyleLarge);
        lbCongratulations.setAlignment(Align.center);
        lbCongratulations.setFontScale(.50f);
        lbCongratulations.setPosition(getWidth() / 2f - lbCongratulations.getWidth() / 2f, 365);
        addActor(lbCongratulations);

        final Table scoreTable = new Table();
        scoreTable.setSize(getWidth(), 180);
        scoreTable.setY(170);
        scoreTable.padLeft(15).padRight(15);

        // ACTUAL TIME
        Label lbTime = new Label(Assets.languages.get("time"), Assets.labelStyleSmall);
        lbTime.setAlignment(Align.left);

        Label lblNumTime = new Label(time + Assets.languages.get("secondAbbreviation"), Assets.labelStyleSmall);
        lblNumTime.setAlignment(Align.right);

        // ACTUAL SCORE
        Label lbScore = new Label(Assets.languages.get("score"), Assets.labelStyleSmall);
        lbScore.setAlignment(Align.left);

        Label lbNumScore = new Label(score + "", Assets.labelStyleSmall);
        lbNumScore.setAlignment(Align.right);
        // lbNumMoves.setFontScale(.75f);

        // BEST MOVES
        Label lbBestScore = new Label(Assets.languages.get("bestScore"), Assets.labelStyleSmall);
        lbBestScore.setAlignment(Align.left);

        Label lbBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyleSmall);
        lbBestNumScore.setAlignment(Align.right);

        scoreTable.add(lbTime).left();
        scoreTable.add(lblNumTime).right().expand();

        scoreTable.row();
        scoreTable.add(lbScore).left();
        scoreTable.add(lbNumScore).right().expand();

        scoreTable.row();
        scoreTable.add(lbBestScore).left();
        scoreTable.add(lbBestNumScore).right().expand();

        // Facebook Twitter
        final Button btShareFacebook;
        final Button btShareTwitter;

        btShareTwitter = new Button(Assets.buttonTwitter);
        btShareTwitter.setSize(50, 50);
        btShareTwitter.setPosition(155, 110);
        screen.addEfectoPress(btShareTwitter);
        btShareTwitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.game.reqHandler.shareOnTwitter("My best score playing 2048 8Bit is " + Settings.bestScore + " points, can you beat me?");
            }
        });

        btShareFacebook = new Button(Assets.buttonFacebook);
        btShareFacebook.setSize(50, 50);
        btShareFacebook.setPosition(225, 110);
        screen.addEfectoPress(btShareFacebook);
        btShareFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.game.reqHandler.shareOnFacebook("My best score playing 2048 8Bit is " + Settings.bestScore + " points, can you beat me?");
            }
        });

        final Label lbMainMenu = new Label(Assets.languages.get("menu"), Assets.labelStyleLarge);
        lbMainMenu.setWidth(getWidth() - 10);
        lbMainMenu.setFontScale(.75f);
        lbMainMenu.setPosition(getWidth() / 2f - lbMainMenu.getWidth() / 2f, 30);
        lbMainMenu.setWrap(true);
        lbMainMenu.setAlignment(Align.center);
        screen.addEfectoPress(lbMainMenu);
        lbMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        addAction(Actions.sequence(Actions.scaleTo(1, 1, .2f), Actions.run(new Runnable() {

            @Override
            public void run() {
                addActor(scoreTable);
                addActor(btShareTwitter);
                addActor(btShareFacebook);
                addActor(lbMainMenu);
            }
        })));

    }
}
