package com.nopalsoft.dosmil.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Array
import com.nopalsoft.dosmil.Assets
import com.nopalsoft.dosmil.Assets.playSoundMove
import com.nopalsoft.dosmil.objects.Tile
import com.nopalsoft.dosmil.screens.Screens

class Board : Group() {
    @JvmField
    var state: Int
    @JvmField
    var time: Float = 0f
    @JvmField
    var score: Long = 0
    @JvmField
    var moveUp: Boolean = false
    @JvmField
    var moveDown: Boolean = false
    @JvmField
    var moveLeft: Boolean = false
    @JvmField
    var moveRight: Boolean = false
    @JvmField
    var didWin: Boolean

    var arrayPieces: Array<Tile>

    init {
        setSize(480f, 480f)
        setPosition(Screens.SCREEN_WIDTH / 2f - width / 2f, 200f)
        addBackground()

        arrayPieces = Array(16)

        didWin = false

        // I initialize the board with pure zeros
        for (i in 0..15) {
            addActor(Tile(i, 0))
        }

        addPiece()
        addPiece()
        state = STATE_RUNNING
    }

    private fun addBackground() {
        val background = Image(Assets.backgroundBoard)
        background.setSize(width, height)
        background.setPosition(0f, 0f)
        addActor(background)
    }

    fun addPiece() {
        if (isBoardFull) return
        var vacio = false
        var num = 0
        while (!vacio) {
            num = MathUtils.random(15)
            vacio = checkEmptySpace(num)
        }
        val worth = if (MathUtils.random(1) == 0) 2 else 4 // The initial value can be 2 or 4
        val obj = Tile(num, worth)
        arrayPieces.add(obj)
        addActor(obj)
    }

    override fun act(delta: Float) {
        super.act(delta)

        // If there are no pending actions now I will go to gameover
        if (state == STATE_NO_MORE_MOVES) {
            var numActions = 0
            for (tile in arrayPieces) {
                numActions += tile.actions.size
            }
            numActions += actions.size
            if (numActions == 0) state = STATE_GAME_OVER
            return
        }

        var didMovePieza = false

        if (moveUp) {
            for (con in 0..3) {
                val i: MutableIterator<Tile> = arrayPieces.iterator()
                while (i.hasNext()) {
                    val obj = i.next()
                    val nextPos = obj.position - 4
                    // First I check if it can be put together
                    if (checkMergeUp(obj.position, nextPos)) {
                        val nextTile = getPieceInPosition(nextPos)
                        if (!nextTile!!.justChanged && !obj.justChanged) {
                            i.remove()
                            removePiece(obj)
                            nextTile.worth = nextTile.worth * 2
                            score += nextTile.worth.toLong()
                            nextTile.justChanged = true
                            didMovePieza = true
                            continue
                        }
                    }
                    if (checkEmptySpaceUp(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didMovePieza = true
                    }
                }
            }
        } else if (moveDown) {
            for (con in 0..3) {
                val i: MutableIterator<Tile> = arrayPieces.iterator()
                while (i.hasNext()) {
                    val obj = i.next()
                    val nextPos = obj.position + 4
                    // First I check if it can be put together
                    if (checkMergeUp(obj.position, nextPos)) {
                        val nextTile = getPieceInPosition(nextPos)
                        if (!nextTile!!.justChanged && !obj.justChanged) {
                            i.remove()
                            removePiece(obj)
                            nextTile.worth = nextTile.worth * 2
                            score += nextTile.worth.toLong()
                            nextTile.justChanged = true
                            didMovePieza = true
                            continue
                        }
                    }
                    if (checkEmptySpaceDown(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didMovePieza = true
                    }
                }
            }
        } else if (moveLeft) {
            for (con in 0..3) {
                val i: MutableIterator<Tile> = arrayPieces.iterator()
                while (i.hasNext()) {
                    val tile = i.next()
                    val nextPosition = tile.position - 1
                    // First I check if it can be put together
                    if (checkMergeSides(tile.position, nextPosition)) {
                        val objNext = getPieceInPosition(nextPosition)
                        if (!objNext!!.justChanged && !tile.justChanged) {
                            i.remove()
                            removePiece(tile)
                            objNext.worth = objNext.worth * 2
                            score += objNext.worth.toLong()
                            objNext.justChanged = true
                            didMovePieza = true
                            continue
                        }
                    }
                    if (checkEmptySpaceLeft(nextPosition)) {
                        tile.moveToPosition(nextPosition)
                        didMovePieza = true
                    }
                }
            }
        } else if (moveRight) {
            for (con in 0..3) {
                val iterator: MutableIterator<Tile> = arrayPieces.iterator()
                while (iterator.hasNext()) {
                    val obj = iterator.next()
                    val nextPos = obj.position + 1
                    // First I check if it can be put together
                    if (checkMergeSides(obj.position, nextPos)) {
                        val objNext = getPieceInPosition(nextPos)
                        if (!objNext!!.justChanged && !obj.justChanged) {
                            iterator.remove()
                            removePiece(obj)
                            objNext.worth = objNext.worth * 2
                            score += objNext.worth.toLong()
                            objNext.justChanged = true
                            didMovePieza = true
                            continue
                        }
                    }
                    if (checkEmptySpaceRight(nextPos)) {
                        obj.moveToPosition(nextPos)
                        didMovePieza = true
                    }
                }
            }
        }

        if (didWin()) {
            state = STATE_NO_MORE_MOVES
            didWin = true
        }

        if ((moveUp || moveDown || moveRight || moveLeft) && didMovePieza) {
            addPiece()
            playSoundMove()
        }

        if (isBoardFull && !isPossibleToMove) {
            state = STATE_NO_MORE_MOVES
        }

        moveUp = false
        moveRight = moveUp
        moveLeft = moveRight
        moveDown = moveLeft

        time += Gdx.graphics.rawDeltaTime
    }

    private fun checkMergeSides(currentPosition: Int, nextPosition: Int): Boolean {
        if ((currentPosition == 3 || currentPosition == 7 || currentPosition == 11) && nextPosition > currentPosition)  // Only those of the same row can be joined together.
            return false
        if ((currentPosition == 12 || currentPosition == 8 || currentPosition == 4) && nextPosition < currentPosition) return false
        val obj1 = getPieceInPosition(currentPosition)
        val obj2 = getPieceInPosition(nextPosition)

        return if (obj1 == null || obj2 == null) false
        else obj1.worth == obj2.worth
    }

    private fun checkMergeUp(posActual: Int, nextPosition: Int): Boolean {
        val tile1 = getPieceInPosition(posActual)
        val tile2 = getPieceInPosition(nextPosition)

        return if (tile1 == null || tile2 == null) false
        else tile1.worth == tile2.worth
    }

    private fun checkEmptySpace(position: Int): Boolean {
        val ite = Array.ArrayIterator(arrayPieces)
        while (ite.hasNext()) {
            if (ite.next().position == position) return false
        }
        return true
    }

    private fun checkEmptySpaceUp(position: Int): Boolean {
        if (position < 0) return false
        return checkEmptySpace(position)
    }

    private fun checkEmptySpaceDown(position: Int): Boolean {
        if (position > 15) return false
        return checkEmptySpace(position)
    }

    private fun checkEmptySpaceRight(position: Int): Boolean {
        if (position == 4 || position == 8 || position == 12 || position == 16) return false
        return checkEmptySpace(position)
    }

    private fun checkEmptySpaceLeft(position: Int): Boolean {
        if (position == 11 || position == 7 || position == 3 || position == -1) return false
        return checkEmptySpace(position)
    }

    private fun getPieceInPosition(position: Int): Tile? {
        val ite = Array.ArrayIterator(arrayPieces)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.position == position) return obj
        }
        return null
    }

    private val isBoardFull: Boolean
        get() = arrayPieces.size == (16)

    private fun didWin(): Boolean {
        val ite = Array.ArrayIterator(arrayPieces)
        while (ite.hasNext()) {
            val obj = ite.next()
            if (obj.worth >= 2000)  // If there is a piece worth more than 15 thousand, you win.
                return true
        }
        return false
    }

    private val isPossibleToMove: Boolean
        get() {
            var canMove = isPossibleToMoveRightLeft

            if (isPossibleToMoveUpDown) {
                canMove = true
            }
            return canMove
        }

    val isPossibleToMoveRightLeft: Boolean
        get() {
            var ren = 0
            while (ren < 16) {
                for (col in ren until ren + 3) {
                    if (checkMergeSides(col, col + 1)) return true
                }
                ren += 4
            }
            return false
        }

    val isPossibleToMoveUpDown: Boolean
        get() {
            for (column in 0..3) {
                var ren = column
                while (ren < column + 16) {
                    if (checkMergeUp(ren, ren + 4)) return true
                    ren += 4
                }
            }
            return false
        }

    private fun removePiece(tile: Tile) {
        removeActor(tile)
        arrayPieces.removeValue(tile, true)
    }

    companion object {
        const val STATE_RUNNING: Int = 1
        const val STATE_NO_MORE_MOVES: Int = 2
        const val STATE_GAME_OVER: Int = 3
    }
}
