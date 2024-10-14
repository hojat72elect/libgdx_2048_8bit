package com.nopalsoft.dosmil.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.dosmil.Assets;

import java.util.LinkedHashMap;

public class Piece extends Actor {
    // Positions start counting from left to right from top to bottom.
    final static LinkedHashMap<Integer, Vector2> mapPositions = new LinkedHashMap<>();

    static {
        mapPositions.put(0, new Vector2(20, 350));
        mapPositions.put(1, new Vector2(130, 350));
        mapPositions.put(2, new Vector2(240, 350));
        mapPositions.put(3, new Vector2(350, 350));
        mapPositions.put(4, new Vector2(20, 240));
        mapPositions.put(5, new Vector2(130, 240));
        mapPositions.put(6, new Vector2(240, 240));
        mapPositions.put(7, new Vector2(350, 240));
        mapPositions.put(8, new Vector2(20, 130));
        mapPositions.put(9, new Vector2(130, 130));
        mapPositions.put(10, new Vector2(240, 130));
        mapPositions.put(11, new Vector2(350, 130));
        mapPositions.put(12, new Vector2(20, 20));
        mapPositions.put(13, new Vector2(130, 20));
        mapPositions.put(14, new Vector2(240, 20));
        mapPositions.put(15, new Vector2(350, 20));
    }

    final float SIZE = 110; // Final size of the card
    public boolean justChanged = false;
    public int position;
    TextureRegion keyframe;
    private int worth;// I made this piece private because when I change its value I also have to change the image of this piece.

    public Piece(int position, int worth) {
        this.position = position;
        setWidth(SIZE);
        setHeight(SIZE);
        setOrigin(SIZE / 2f, SIZE / 2f);

        setPosition(mapPositions.get(position).x, mapPositions.get(position).y);
        setWorth(worth);

        if (worth != 0) {// If the piece is worth 0, it is a blue square that has nothing
            setScale(.8f);
            addAction(Actions.scaleTo(1, 1, .25f));
            Gdx.app.log("Se creo pieza en ", position + "");
        }

    }

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
        switch (worth) {
            default:
            case 0:
                keyframe = Assets.emptyTile;
                break;
            case 2:
                keyframe = Assets.tile2;
                break;
            case 4:
                keyframe = Assets.tile4;
                break;
            case 8:
                keyframe = Assets.tile8;
                break;
            case 16:
                keyframe = Assets.tile16;
                break;
            case 32:
                keyframe = Assets.tile32;
                break;
            case 64:
                keyframe = Assets.tile64;
                break;
            case 128:
                keyframe = Assets.tile128;
                break;
            case 256:
                keyframe = Assets.tile256;
                break;
            case 512:
                keyframe = Assets.tile512;
                break;
            case 1024:
                keyframe = Assets.tile1024;
                break;
            case 2048:
                keyframe = Assets.tile2048;
                break;
        }

    }

    @Override
    public void act(float delta) {
        justChanged = false;
        super.act(delta);
    }

    public void moveToPosition(int pos) {
        this.position = pos;
        Gdx.app.log("Move to ", pos + "");
        addAction(Actions.moveTo(mapPositions.get(position).x, mapPositions.get(position).y, .075f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyframe, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
