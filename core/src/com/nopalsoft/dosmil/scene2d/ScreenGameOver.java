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

        String gameOverTitle = Assets.languages.get("gameOver");
        if (didWin)
            gameOverTitle = Assets.languages.get("congratulations");

        Label labelCongratulations = new Label(gameOverTitle, Assets.labelStyleLarge);
        labelCongratulations.setAlignment(Align.center);
        labelCongratulations.setFontScale(.50f);
        labelCongratulations.setPosition(getWidth() / 2f - labelCongratulations.getWidth() / 2f, 365);
        addActor(labelCongratulations);

        final Table scoreTable = new Table();
        scoreTable.setSize(getWidth(), 180);
        scoreTable.setY(170);
        scoreTable.padLeft(15).padRight(15);

        // ACTUAL TIME
        Label labelTime = new Label(Assets.languages.get("time"), Assets.labelStyleSmall);
        labelTime.setAlignment(Align.left);

        Label labelNumTime = new Label(time + Assets.languages.get("secondAbbreviation"), Assets.labelStyleSmall);
        labelNumTime.setAlignment(Align.right);

        // ACTUAL SCORE
        Label labelScore = new Label(Assets.languages.get("score"), Assets.labelStyleSmall);
        labelScore.setAlignment(Align.left);

        Label labelNumScore = new Label(score + "", Assets.labelStyleSmall);
        labelNumScore.setAlignment(Align.right);


        // BEST MOVES
        Label labelBestScore = new Label(Assets.languages.get("bestScore"), Assets.labelStyleSmall);
        labelBestScore.setAlignment(Align.left);

        Label labelBestNumScore = new Label(Settings.bestScore + "", Assets.labelStyleSmall);
        labelBestNumScore.setAlignment(Align.right);

        scoreTable.add(labelTime).left();
        scoreTable.add(labelNumTime).right().expand();

        scoreTable.row();
        scoreTable.add(labelScore).left();
        scoreTable.add(labelNumScore).right().expand();

        scoreTable.row();
        scoreTable.add(labelBestScore).left();
        scoreTable.add(labelBestNumScore).right().expand();

        // Facebook Twitter
        final Button buttonShareFacebook;
        final Button buttonShareTwitter;

        buttonShareTwitter = new Button(Assets.buttonTwitter);
        buttonShareTwitter.setSize(50, 50);
        buttonShareTwitter.setPosition(155, 110);
        screen.addPressEffect(buttonShareTwitter);
        buttonShareTwitter.addListener(new ClickListener() {
        });

        buttonShareFacebook = new Button(Assets.buttonFacebook);
        buttonShareFacebook.setSize(50, 50);
        buttonShareFacebook.setPosition(225, 110);
        screen.addPressEffect(buttonShareFacebook);
        buttonShareFacebook.addListener(new ClickListener() {
        });

        final Label labelMainMenu = new Label(Assets.languages.get("menu"), Assets.labelStyleLarge);
        labelMainMenu.setWidth(getWidth() - 10);
        labelMainMenu.setFontScale(.75f);
        labelMainMenu.setPosition(getWidth() / 2f - labelMainMenu.getWidth() / 2f, 30);
        labelMainMenu.setWrap(true);
        labelMainMenu.setAlignment(Align.center);
        screen.addPressEffect(labelMainMenu);
        labelMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
            }
        });

        addAction(Actions.sequence(Actions.scaleTo(1, 1, .2f), Actions.run(() -> {
            addActor(scoreTable);
            addActor(buttonShareTwitter);
            addActor(buttonShareFacebook);
            addActor(labelMainMenu);
        })));

    }
}
