package assignment;

import java.awt.*;
import java.util.Arrays;

/**
 * Represents a Tetris board -- essentially a 2-d grid of booleans. Supports
 * tetris pieces and row clearing.  Does not do any drawing or have any idea of
 * pixels. Instead, just represents the abstract 2-d board.
 */
public final class TetrisBoard implements Board {
    private boolean[][] board;
    private Action lastAction;
    private Result lastResult;
    private int rowsCleared;
    private Piece nextPiece;
    private int height, width;
    private int[] heights;
    private int[] widths;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
        this.height = height;
        this.width = width;
        board = new boolean[height][width];
        heights = new int[width];
        widths = new int[height];
    }

    @Override
    public Result move(Action act) {
        lastAction = act;
        if (nextPiece == null) {
            lastResult = Result.NO_PIECE;
        } else {
            Point[] body = nextPiece.getBody();
            switch (act) {
                case NOTHING:
                    lastResult = Result.SUCCESS;
                    break;
                case LEFT:
                    break;
                case RIGHT:
                    break;
                case DOWN:
                    break;
                case DROP:
                    break;
                case CLOCKWISE:
                    break;
                case COUNTERCLOCKWISE:
                    break;
                case HOLD:
                    break;
            }
        }

        return lastResult;
    }

    @Override
    public Board testMove(Action act) {
        return null;
    }

    @Override
    public void nextPiece(Piece p) {
        nextPiece = p;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TetrisBoard) {
            boolean[][] otherBoard = ((TetrisBoard) other).board;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != otherBoard[i][j]) {
                        return false;
                    }
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public Result getLastResult() {
        return lastResult;
    }

    @Override
    public Action getLastAction() {
        return lastAction;
    }

    @Override
    public int getRowsCleared() {
        return rowsCleared;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getMaxHeight() {
        int maxHeight = 0;
        for (int height : heights) {
            maxHeight = Math.max(height, maxHeight);
        }

        return maxHeight;
    }

    @Override
    public int dropHeight(Piece piece, int x) {
        return -1;
    }

    @Override
    public int getColumnHeight(int x) {
        return heights[x];
    }

    @Override
    public int getRowWidth(int y) {
        return widths[height - y - 1];
    }

    @Override
    public boolean getGrid(int x, int y) {
        if (x < 0 || x > width || y < 0 || y > height) {
            return true;
        }

        return board[height - y - 1][x];
    }

}
