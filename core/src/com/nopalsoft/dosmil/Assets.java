package com.nopalsoft.dosmil;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

public class Assets {

    public static I18NBundle languages;

    public static BitmapFont fontSmall;
    public static BitmapFont fontLarge;

    public static AtlasRegion background;
    public static AtlasRegion backgroundBoard;
    public static AtlasRegion puzzleSolved;
    public static AtlasRegion title;

    public static NinePatchDrawable pixelBlack;

    public static AtlasRegion scoresBackground;

    public static TextureRegionDrawable buttonBack;
    public static TextureRegionDrawable buttonFacebook;
    public static TextureRegionDrawable buttonTwitter;

    public static AtlasRegion emptyTile;
    public static AtlasRegion tile2;
    public static AtlasRegion tile4;
    public static AtlasRegion tile8;
    public static AtlasRegion tile16;
    public static AtlasRegion tile32;
    public static AtlasRegion tile64;
    public static AtlasRegion tile128;
    public static AtlasRegion tile256;
    public static AtlasRegion tile512;
    public static AtlasRegion tile1024;
    public static AtlasRegion tile2048;

    public static LabelStyle labelStyleSmall;
    public static LabelStyle labelStyleLarge;

    public static ButtonStyle buttonStyleMusic;
    public static ButtonStyle buttonStylePause;
    public static ButtonStyle buttonStyleSound;

    static TextureAtlas atlas;
    static Sound move1;
    static Sound move2;
    private static Music music2;

    public static void loadFont() {
        fontSmall = new BitmapFont(
                Gdx.files.internal("data/font25.fnt"),
                atlas.findRegion("font25")
        );

        fontLarge = new BitmapFont(
                Gdx.files.internal("data/font45.fnt"),
                atlas.findRegion("font45")
        );
    }

    private static void loadStyles() {
        labelStyleSmall = new LabelStyle(fontSmall, Color.WHITE);
        labelStyleLarge = new LabelStyle(fontLarge, Color.WHITE);

        // Button Music
        TextureRegionDrawable buttonMusicOn = new TextureRegionDrawable(
                atlas.findRegion("btMusica"));
        TextureRegionDrawable buttonMusicOff = new TextureRegionDrawable(
                atlas.findRegion("btSinMusica"));
        buttonStyleMusic = new ButtonStyle(buttonMusicOn, null, buttonMusicOff);


        // Sound Button
        TextureRegionDrawable buttonSoundOn = new TextureRegionDrawable(
                atlas.findRegion("btSonido"));
        TextureRegionDrawable buttonSoundOff = new TextureRegionDrawable(
                atlas.findRegion("btSinSonido"));
        buttonStyleSound = new ButtonStyle(buttonSoundOn, null, buttonSoundOff);

        // ImageButton Pause
        TextureRegionDrawable buttonPauseUp = new TextureRegionDrawable(
                atlas.findRegion("btPause"));
        TextureRegionDrawable buttonPauseDown = new TextureRegionDrawable(
                atlas.findRegion("btPauseDown"));
        buttonStylePause = new ButtonStyle(buttonPauseUp, buttonPauseDown, null);

    }

    public static void load() {
        atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        loadFont();
        loadStyles();

        if (MathUtils.randomBoolean())
            background = atlas.findRegion("fondo");
        else
            background = atlas.findRegion("fondo2");
        backgroundBoard = atlas.findRegion("fondoPuntuaciones");

        title = atlas.findRegion("titulo");

        pixelBlack = new NinePatchDrawable(new NinePatch(
                atlas.findRegion("pixelNegro"), 1, 1, 0, 0));
        scoresBackground = atlas.findRegion("fondoPuntuaciones");

        puzzleSolved = atlas.findRegion("puzzleSolved");

        emptyTile = atlas.findRegion("piezaVacia");

        tile2 = atlas.findRegion("pieza2");
        tile4 = atlas.findRegion("pieza4");
        tile8 = atlas.findRegion("pieza8");
        tile16 = atlas.findRegion("pieza16");
        tile32 = atlas.findRegion("pieza32");
        tile64 = atlas.findRegion("pieza64");
        tile128 = atlas.findRegion("pieza128");
        tile256 = atlas.findRegion("pieza256");
        tile512 = atlas.findRegion("pieza512");
        tile1024 = atlas.findRegion("pieza1024");
        tile2048 = atlas.findRegion("pieza2048");

        buttonBack = new TextureRegionDrawable(atlas.findRegion("btAtras2"));
        buttonFacebook = new TextureRegionDrawable(atlas.findRegion("btFacebook"));
        buttonTwitter = new TextureRegionDrawable(atlas.findRegion("btTwitter"));

        move1 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move1.mp3"));
        move2 = Gdx.audio.newSound(Gdx.files.internal("data/Sounds/move2.mp3"));

        music2 = Gdx.audio.newMusic(Gdx.files
                .internal("data/Sounds/music2.mp3"));

        Settings.load();

        music2.setVolume(.1f);

        playMusic();

        languages = I18NBundle.createBundle(Gdx.files.internal("strings/strings"));
    }

    public static void playMusic() {
        if (Settings.isMusicOn)

            music2.play();
    }

    public static void pauseMusic() {
        music2.stop();
    }

    public static void playSoundMove() {
        if (Settings.isSoundOn) {
            if (MathUtils.randomBoolean())
                move1.play(.3f);
            else
                move2.play(.3f);
        }
    }
}
