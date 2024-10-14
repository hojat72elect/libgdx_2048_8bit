package com.nopalsoft.dosmil.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import com.nopalsoft.dosmil.Assets;
import com.nopalsoft.dosmil.screens.Screens;
import com.nopalsoft.dosmil.objects.Piece;
import java.util.Iterator;

public class Board extends Group {

    static public final int STATE_RUNNING = 1;
    static public final int STATE_NO_MORE_MOVES = 2;
    static public final int STATE_GAMEOVER = 3;

    public int state;
    public float time;
    public long score;
    public boolean moveUp, moveDown, moveLeft, moveRight;
    public boolean didWin;

    Array<com.nopalsoft.dosmil.objects.Piece> arrayPieces;

    public Board() {
        setSize(480, 480);
        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);
        addBackground();

        arrayPieces = new Array<>(16);

        didWin = false;

        // I initialize the board with pure zeros
        for (int i = 0; i < 16; i++) {
            addActor(new com.nopalsoft.dosmil.objects.Piece(i, 0));
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
        if (isTableroFull())
            return;
        boolean vacio = false;
        int num = 0;
        while (!vacio) {
            num = MathUtils.random(15);
            vacio = checkEmptySpace(num);
        }
        int worth = MathUtils.random(1) == 0 ? 2 : 4; // The initial value can be 2 or 4
        Piece obj = new Piece(num, worth);
        arrayPieces.add(obj);
        addActor(obj);

    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // If there are no pending actions now I will go to gameover
        if (state == STATE_NO_MORE_MOVES) {
            int numActions = 0;
            for (Piece piece : arrayPieces) {
                numActions += piece.getActions().size;
            }
            numActions += getActions().size;
            if (numActions == 0)
                state = STATE_GAMEOVER;
            return;
        }

        boolean didMovePieza = false;

        if (moveUp) {
            for (int con = 0; con < 4; con++) {
                Iterator<Piece> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Piece obj = i.next();
                    int nextPos = obj.position - 4;
                    // Primero reviso si se puede juntar
                    if (checarMergeUp(obj.position, nextPos)) {
                        Piece objNext = getPieceInPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePieza(obj);
                            objNext.setWorth(objNext.getWorth() * 2);
                            score += objNext.getWorth();
                            objNext.justChanged = true;
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
                Iterator<Piece> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Piece obj = i.next();
                    int nextPos = obj.position + 4;
                    // Primero reviso si se puede juntar
                    if (checarMergeUp(obj.position, nextPos)) {
                        Piece objNext = getPieceInPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePieza(obj);
                            objNext.setWorth(objNext.getWorth() * 2);
                            score += objNext.getWorth();
                            objNext.justChanged = true;
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
                Iterator<Piece> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Piece obj = i.next();
                    int nextPos = obj.position - 1;
                    // Primero reviso si se puede juntar
                    if (checkMergeSides(obj.position, nextPos)) {
                        Piece objNext = getPieceInPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePieza(obj);
                            objNext.setWorth(objNext.getWorth() * 2);
                            score += objNext.getWorth();
                            objNext.justChanged = true;
                            didMovePieza = true;
                            continue;
                        }
                    }
                    if (checkEmptySpaceLeft(nextPos)) {
                        obj.moveToPosition(nextPos);
                        didMovePieza = true;
                    }
                }
            }
        } else if (moveRight) {
            for (int con = 0; con < 4; con++) {
                Iterator<Piece> i = arrayPieces.iterator();
                while (i.hasNext()) {
                    Piece obj = i.next();
                    int nextPos = obj.position + 1;
                    // First I check if it can be put together
                    if (checkMergeSides(obj.position, nextPos)) {
                        Piece objNext = getPieceInPosition(nextPos);
                        if (!objNext.justChanged && !obj.justChanged) {
                            i.remove();
                            removePieza(obj);
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

        if (isTableroFull() && !isPossibleToMove()) {
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
        Piece obj1 = getPieceInPosition(currentPosition);
        Piece obj2 = getPieceInPosition(nextPosition);

        if (obj1 == null || obj2 == null)
            return false;
        else return obj1.getWorth() == obj2.getWorth();

    }

    private boolean checarMergeUp(int posActual, int nextPosition) {

        Piece obj1 = getPieceInPosition(posActual);
        Piece obj2 = getPieceInPosition(nextPosition);

        if (obj1 == null || obj2 == null)
            return false;
        else return obj1.getWorth() == obj2.getWorth();

    }

    private boolean checkEmptySpace(int pos) {
        ArrayIterator<Piece> ite = new ArrayIterator<>(arrayPieces);
        while (ite.hasNext()) {
            if (ite.next().position == pos)
                return false;
        }
        return true;
    }

    private boolean checkEmptySpaceUp(int pos) {
        if (pos < 0)
            return false;
        return checkEmptySpace(pos);
    }

    private boolean checkEmptySpaceDown(int pos) {
        if (pos > 15)
            return false;
        return checkEmptySpace(pos);
    }

    private boolean checkEmptySpaceRight(int pos) {
        if (pos == 4 || pos == 8 || pos == 12 || pos == 16)
            return false;
        return checkEmptySpace(pos);
    }

    private boolean checkEmptySpaceLeft(int pos) {
        if (pos == 11 || pos == 7 || pos == 3 || pos == -1)
            return false;
        return checkEmptySpace(pos);
    }

    private Piece getPieceInPosition(int pos) {
        ArrayIterator<Piece> ite = new ArrayIterator<>(arrayPieces);
        while (ite.hasNext()) {
            Piece obj = ite.next();
            if (obj.position == pos)
                return obj;
        }
        return null;
    }

    private boolean isTableroFull() {
        return arrayPieces.size == (16);
    }

    private boolean didWin() {
        ArrayIterator<Piece> ite = new ArrayIterator<>(arrayPieces);
        while (ite.hasNext()) {
            Piece obj = ite.next();
            if (obj.getWorth() >= 2000)// si hay una pieza que valga mas de 15 mil se gana
                return true;
        }
        return false;
    }

    private boolean isPossibleToMove() {

        boolean canMove = isPosibleToMoveRightLeft();

        if (isPosibleToMoveUpDown()) {
            canMove = true;
        }
        return canMove;

    }

    boolean isPosibleToMoveRightLeft() {
        for (int ren = 0; ren < 16; ren += 4) {
            for (int col = ren; col < ren + 3; col++) {
                if (checkMergeSides(col, col + 1))
                    return true;
            }
        }
        return false;
    }

    boolean isPosibleToMoveUpDown() {
        for (int col = 0; col < 4; col++) {
            for (int ren = col; ren < col + 16; ren += 4) {
                if (checarMergeUp(ren, ren + 4))
                    return true;
            }
        }
        return false;
    }

    private void removePieza(com.nopalsoft.dosmil.objects.Piece obj) {
        removeActor(obj);
        arrayPieces.removeValue(obj, true);
    }
}
