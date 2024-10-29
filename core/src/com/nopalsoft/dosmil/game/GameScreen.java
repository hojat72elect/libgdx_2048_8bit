package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.MainGame;
import com.nopalsoft.dosmil.Settings;
import com.nopalsoft.dosmil.scene2d.ScreenPaused;
import com.nopalsoft.dosmil.screens.MainMenuScreen;
import com.nopalsoft.dosmil.screens.Screens;

public class GameScreen extends Screens {

    static final int STATE_RUNNING = 1;
    static final int STATE_PAUSED = 2;
    static final int STATE_GAME_OVER = 3;
    public int state;

    Board board;
    Table tableMarkers;
    Label labelTime, labelScore, labelBestScore;
    Button buttonPause;
    ScreenPaused screenPaused;
    private final Stage stageGame;

    public GameScreen(MainGame game) {
        super(game);
        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        board = new Board();
        stageGame.addActor(board);

        initUI();

        Settings.numberTimesPlayed++;
        game.reqHandler.loadInterstitial();

    }

    private void initUI() {
        tableMarkers = new Table();
        tableMarkers.setSize(SCREEN_WIDTH, 100);
        tableMarkers.setPosition(0, 680);


        labelTime = new Label(Assets.languages.get("score") + "\n0", Assets.labelStyleSmall);
        labelTime.setAlignment(Align.center);
        labelTime.setFontScale(1.15f);

        labelScore = new Label(Assets.languages.get("score") + "\n0", Assets.labelStyleSmall);
        labelScore.setFontScale(1.15f);
        labelScore.setAlignment(Align.center);

        labelBestScore = new Label(Assets.languages.get("best") + "\n" + Settings.bestScore, Assets.labelStyleSmall);
        labelBestScore.setAlignment(Align.center);
        labelBestScore.setFontScale(1.15f);

        tableMarkers.row().expand().uniform().center();
        tableMarkers.add(labelTime);
        tableMarkers.add(labelScore);
        tableMarkers.add(labelBestScore);

        screenPaused = new com.nopalsoft.dosmil.scene2d.ScreenPaused(this);

        buttonPause = new Button(Assets.buttonStylePause);
        buttonPause.setPosition((float) SCREEN_WIDTH / 2 - buttonPause.getWidth() / 2, 110);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }

        });

        stage.addActor(tableMarkers);
        stage.addActor(buttonPause);

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageGame.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {

        if (state == STATE_RUNNING) {
            updateRunning(delta);
        }

        labelTime.setText(Assets.languages.get("time") + "\n" + ((int) board.time));
        labelScore.setText(Assets.languages.get("score") + "\n" + (board.score));

    }

    private void updateRunning(float delta) {
        stageGame.act(delta);

        if (board.state == Board.STATE_GAME_OVER) {
            setGameOver();
        }
    }

    @Override
    public void draw(float delta) {

        batcher.begin();
        batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.end();

        stageGame.draw();

    }

    @Override
    public void pause() {
        setPaused();
        super.pause();
    }

    @Override
    public void hide() {
        super.hide();
        stageGame.dispose();
        game.reqHandler.hideAdBanner();
        if (Settings.numberTimesPlayed % 3 == 0)
            game.reqHandler.showInterstitial();
    }

    private void setPaused() {
        if (state == STATE_GAME_OVER || state == STATE_PAUSED)
            return;
        state = STATE_PAUSED;
        stage.addActor(screenPaused);
        game.reqHandler.showAdBanner();

    }

    public void setRunning() {
        if (state == STATE_GAME_OVER)
            return;
        state = STATE_RUNNING;
        game.reqHandler.hideAdBanner();
    }

    private void setGameOver() {
        state = STATE_GAME_OVER;
        Settings.setBestScores(board.score);
        game.gameServiceHandler.submitScore(board.score);
        com.nopalsoft.dosmil.scene2d.ScreenGameOver oGameOver = new com.nopalsoft.dosmil.scene2d.ScreenGameOver(this, board.didWin, (int) board.time, board.score);
        stage.addActor(oGameOver);
        game.reqHandler.showAdBanner();

    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        if (state != STATE_PAUSED)
            setRunning();
        return super.fling(velocityX, velocityY, button);
    }

    /**
     * It's important to remember that it is the white piece that moves, so if I say moveUp the white piece will move up, but the user thinks
     * that if he does a swipe down the piece above the white piece is the one that goes down. When we know that the one that goes up is the white piece.
     */
    @Override
    public void up() {
        board.moveUp = true;
        super.up();
    }

    @Override
    public void down() {
        board.moveDown = true;
        super.down();
    }

    @Override
    public void right() {
        board.moveRight = true;
        super.right();
    }

    @Override
    public void left() {
        board.moveLeft = true;
        super.left();
    }

    /**
     * It's important to remember that it is the white piece that moves, so if I say moveUp the white piece will move up, but the user thinks that if he does
     * a swipe down the piece above the white piece is the one that goes down. When we know that the one that goes up is the white piece.
     */

    @Override
    public boolean keyDown(int keycode) {
        if (state != STATE_PAUSED) {
            if (keycode == Keys.LEFT) {
                board.moveLeft = true;
                setRunning();
            } else if (keycode == Keys.RIGHT) {
                board.moveRight = true;
                setRunning();
            } else if (keycode == Keys.UP) {
                board.moveUp = true;
                setRunning();
            } else if (keycode == Keys.DOWN) {
                board.moveDown = true;

                setRunning();
            }
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {

            changeScreenWithFadeOut(MainMenuScreen.class, game);
        }

        return true;
    }

}
