package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.screens.Screens;
import com.nopalsoft.dosmil.objects.Tile;
import java.util.Iterator;

public class Board extends Group {

    static public final int STATE_RUNNING = 1;
    static public final int STATE_NO_MORE_MOVES = 2;
    static public final int STATE_GAME_OVER = 3;

    public int state;
    public float time;
    public long score;
    public boolean moveUp, moveDown, moveLeft, moveRight;
    public boolean didWin;

    Array<com.nopalsoft.dosmil.objects.Tile> arrayPieces;

    public Board() {
        setSize(480, 480);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);
        addBackground();

        arrayPieces = new Array<>(16);

        didWin = false;

        // I initialize the board with pure zeros
        for (int i = 0; i < 16; i++) {
            addActor(new com.nopalsoft.dosmil.objects.Tile(i, 0));
        }

        addPiece();
        addPiece();
        state = STATE_RUNNING;
    }

    private void addBackground() {
        Image background = new Image(Assets.backgroundBoard);
        background.setSize(getWidth(), getHeight());
        background.setPosition(0, 0);
        addActor(background);

    }

    public void addPiece() {
        if (isBoardFull())
            return;
        boolean vacio = false;
        int num = 0;
        while (!vacio) {
            num = MathUtils.random(15);
            vacio = checkEmptySpace(num);
        }
        int worth = MathUtils.random(1) == 0 ? 2 : 4; // The initial value can be 2 or 4
        Tile obj = new Tile(num, worth);
        arrayPieces.add(obj);
        addActor(obj);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // If there are no pending actions now I will go to gameover
        if (state == STATE_NO_MORE_MOVES) {
            int numActions = 0;
            for (Tile tile : arrayPieces) {
                numActions += tile.getActions().size;
            }
            numActions += getActions().size;
            if (numActions == 0)
                state = STATE_GAME_OVER;
            return;
        }

        boolean didMovePieza = false;

        if (moveUp) {
            for (int con = 0; con < 4; con++) {
                Iterator<Tile> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Tile obj = i.next();
                    int nextPos = obj.position - 4;
                    // First I check if it can be put together
                    if (checkMergeUp(obj.position, nextPos)) {
                        Tile nextTile = getPieceInPosition(nextPos);
                        if (!nextTile.justChanged && !obj.justChanged) {
                            i.remove();
                            removePiece(obj);
                            nextTile.setWorth(nextTile.getWorth() * 2);
                            score += nextTile.getWorth();
                            nextTile.justChanged = true;
                            didMovePieza = true;
                            continue;
                        }
                    }
                    if (checkEmptySpaceUp(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didMovePieza = true;
                    }
                }
            }
        } else if (moveDown) {
            for (int con = 0; con < 4; con++) {
                Iterator<Tile> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Tile obj = i.next();
                    int nextPos = obj.position + 4;
                    // First I check if it can be put together
                    if (checkMergeUp(obj.position, nextPos)) {
                        Tile nextTile = getPieceInPosition(nextPos);
                        if (!nextTile.justChanged && !obj.justChanged) {
                            i.remove();
                            removePiece(obj);
                            nextTile.setWorth(nextTile.getWorth() * 2);
                            score += nextTile.getWorth();
                            nextTile.justChanged = true;
                            didMovePieza = true;
                            continue;
                        }
                    }
                    if (checkEmptySpaceDown(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didMovePieza = true;
                    }
                }
            }
        } else if (moveLeft) {
            for (int con = 0; con < 4; con++) {
                Iterator<Tile> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Tile tile = i.next();
                    int nextPosition = tile.position - 1;
                    // First I check if it can be put together
                    if (checkMergeSides(tile.position, nextPosition)) {
                        Tile objNext = getPieceInPosition(nextPosition);
                        if (!objNext.justChanged && !tile.justChanged) {
                            i.remove();
                            removePiece(tile);
                            objNext.setWorth(objNext.getWorth() * 2);
                            score += objNext.getWorth();
                            objNext.justChanged = true;
                            didMovePieza = true;
                            continue;
                        }
                    }
                    if (checkEmptySpaceLeft(nextPosition)) {
                        tile.moveToPosition(nextPosition);
                        didMovePieza = true;
                    }
                }
            }
        } else if (moveRight) {
            for (int con = 0; con < 4; con++) {
                Iterator<Tile> iterator = arrayPieces.iterator();
                while (iterator.hasNext()) {
                    Tile obj = iterator.next();
                    int nextPos = obj.position + 1;
                    // First I check if it can be put together
                    if (checkMergeSides(obj.position, nextPos)) {
                        Tile objNext = getPieceInPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            iterator.remove();
                            removePiece(obj);
                            objNext.setWorth(objNext.getWorth() * 2);
                            score += objNext.getWorth();
                            objNext.justChanged = true;
                            didMovePieza = true;
                            continue;
                        }
                    }
                    if (checkEmptySpaceRight(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didMovePieza = true;
                    }
                }
            }
        }

        if (didWin()) {
            state = STATE_NO_MORE_MOVES;
            didWin = true;
        }

        if ((moveUp || moveDown || moveRight || moveLeft) && didMovePieza) {
            addPiece();
            Assets.playSoundMove();
        }

        if (isBoardFull() && !isPossibleToMove()) {
            state = STATE_NO_MORE_MOVES;
        }

        moveDown = moveLeft = moveRight = moveUp = false;

        time += Gdx.graphics.getRawDeltaTime();

    }

    private boolean checkMergeSides(int currentPosition, int nextPosition) {
        if ((currentPosition == 3 || currentPosition == 7 || currentPosition == 11) && nextPosition > currentPosition)// Only those of the same row can be joined together.
            return false;
        if ((currentPosition == 12 || currentPosition == 8 || currentPosition == 4) && nextPosition < currentPosition)
            return false;
        Tile obj1 = getPieceInPosition(currentPosition);
        Tile obj2 = getPieceInPosition(nextPosition);

        if (obj1 == null || obj2 == null)
            return false;
        else return obj1.getWorth() == obj2.getWorth();

    }

    private boolean checkMergeUp(int posActual, int nextPosition) {

        Tile tile1 = getPieceInPosition(posActual);
        Tile tile2 = getPieceInPosition(nextPosition);

        if (tile1 == null || tile2 == null)
            return false;
        else return tile1.getWorth() == tile2.getWorth();

    }

    private boolean checkEmptySpace(int position) {
        ArrayIterator<Tile> ite = new ArrayIterator<>(arrayPieces);
        while (ite.hasNext()) {
            if (ite.next().position == position)
                return false;
        }
        return true;
    }

    private boolean checkEmptySpaceUp(int position) {
        if (position < 0)
            return false;
        return checkEmptySpace(position);
    }

    private boolean checkEmptySpaceDown(int position) {
        if (position > 15)
            return false;
        return checkEmptySpace(position);
    }

    private boolean checkEmptySpaceRight(int position) {
        if (position == 4 || position == 8 || position == 12 || position == 16)
            return false;
        return checkEmptySpace(position);
    }

    private boolean checkEmptySpaceLeft(int position) {
        if (position == 11 || position == 7 || position == 3 || position == -1)
            return false;
        return checkEmptySpace(position);
    }

    private Tile getPieceInPosition(int position) {
        ArrayIterator<Tile> ite = new ArrayIterator<>(arrayPieces);
        while (ite.hasNext()) {
            Tile obj = ite.next();
            if (obj.position == position)
                return obj;
        }
        return null;
    }

    private boolean isBoardFull() {
        return arrayPieces.size == (16);
    }

    private boolean didWin() {
        ArrayIterator<Tile> ite = new ArrayIterator<>(arrayPieces);
        while (ite.hasNext()) {
            Tile obj = ite.next();
            if (obj.getWorth() >= 2000)// If there is a piece worth more than 15 thousand, you win.
                return true;
        }
        return false;
    }

    private boolean isPossibleToMove() {

        boolean canMove = isPossibleToMoveRightLeft();

        if (isPossibleToMoveUpDown()) {
            canMove = true;
        }
        return canMove;

    }

    boolean isPossibleToMoveRightLeft() {
        for (int ren = 0; ren < 16; ren += 4) {
            for (int col = ren; col < ren + 3; col++) {
                if (checkMergeSides(col, col + 1))
                    return true;
            }
        }
        return false;
    }

    boolean isPossibleToMoveUpDown() {
        for (int column = 0; column < 4; column++) {
            for (int ren = column; ren < column + 16; ren += 4) {
                if (checkMergeUp(ren, ren + 4))
                    return true;
            }
        }
        return false;
    }

    private void removePiece(Tile tile) {
        removeActor(tile);
        arrayPieces.removeValue(tile, true);
    }
}
