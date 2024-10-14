package com.nopalsoft.dosmil.objetos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.dosmil.Assets;

import java.util.LinkedHashMap;

public class Pieza extends Actor {
    // //Las posiciones empiezan a contar de izq a derecha desde arriba hacia abajo
    final static LinkedHashMap<Integer, Vector2> mapPosiciones = new LinkedHashMap<>();

    static {
        mapPosiciones.put(0, new Vector2(20, 350));
        mapPosiciones.put(1, new Vector2(130, 350));
        mapPosiciones.put(2, new Vector2(240, 350));
        mapPosiciones.put(3, new Vector2(350, 350));
        mapPosiciones.put(4, new Vector2(20, 240));
        mapPosiciones.put(5, new Vector2(130, 240));
        mapPosiciones.put(6, new Vector2(240, 240));
        mapPosiciones.put(7, new Vector2(350, 240));
        mapPosiciones.put(8, new Vector2(20, 130));
        mapPosiciones.put(9, new Vector2(130, 130));
        mapPosiciones.put(10, new Vector2(240, 130));
        mapPosiciones.put(11, new Vector2(350, 130));
        mapPosiciones.put(12, new Vector2(20, 20));
        mapPosiciones.put(13, new Vector2(130, 20));
        mapPosiciones.put(14, new Vector2(240, 20));
        mapPosiciones.put(15, new Vector2(350, 20));
    }

    final float SIZE = 110;// Tamano final de la ficha
    public boolean justChanged = false;
    public int posicion;
    TextureRegion keyframe;
    private int valor;// esta pieza la hice privada porque cuando cambio su valor tambien tengo que cambiar la imagen de esta pieza

    public Pieza(int posicion, int valor) {
        this.posicion = posicion;
        setWidth(SIZE);
        setHeight(SIZE);
        setOrigin(SIZE / 2f, SIZE / 2f);

        setPosition(mapPosiciones.get(posicion).x, mapPosiciones.get(posicion).y);
        setValor(valor);

        if (valor != 0) {// Si la pieza vale 0 es un cuadro de los azules que no tienen nada
            setScale(.8f);
            addAction(Actions.scaleTo(1, 1, .25f));
            Gdx.app.log("Se creo pieza en ", posicion + "");
        }

    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
        switch (valor) {
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
        this.posicion = pos;
        Gdx.app.log("Move to ", pos + "");
        addAction(Actions.moveTo(mapPosiciones.get(posicion).x, mapPosiciones.get(posicion).y, .075f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(keyframe, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }

}
