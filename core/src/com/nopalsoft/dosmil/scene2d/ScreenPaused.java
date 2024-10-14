package com.nopalsoft.dosmil.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.game.GameScreen;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class ScreenPaused extends Group {

    Screens screen;

    public ScreenPaused(final Screens screen) {
        this.screen = screen;
        setSize(420, 300);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260);
        setScale(.5f);

        Image background = new Image(Assets.scoresBackground);
        background.setSize(getWidth(), getHeight());
        addActor(background);

        Label lbPaused = new Label(Assets.languages.get("pause"), Assets.labelStyleLarge);
        lbPaused.setAlignment(Align.center);
        lbPaused.setFontScale(.85f);
        lbPaused.setPosition(getWidth() / 2f - lbPaused.getWidth() / 2f, 230);
        addActor(lbPaused);

        final Label lbResume = new Label(Assets.languages.get("resume"), Assets.labelStyleSmall);
        lbResume.setWrap(true);
        lbResume.setAlignment(Align.center);
        lbResume.setPosition(getWidth() / 2f - lbResume.getWidth() / 2f, 155);
        screen.addEfectoPress(lbResume);
        lbResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameScreen oGame = (GameScreen) screen;
                remove();
                oGame.setRunning();

            }
        });

        final Label lbMainMenu = new Label(Assets.languages.get("menu"), Assets.labelStyleSmall);
        lbMainMenu.setWrap(true);
        lbMainMenu.setAlignment(Align.center);
        lbMainMenu
                .setPosition(getWidth() / 2f - lbMainMenu.getWidth() / 2f, 65);
        screen.addEfectoPress(lbMainMenu);
        lbMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(MainMenuScreen.class,
                        screen.game);

            }
        });

        addAction(Actions.sequence(Actions.scaleTo(1, 1, .2f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        addActor(lbMainMenu);
                        addActor(lbResume);

                    }
                })));

    }
}