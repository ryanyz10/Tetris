package assignment;

import java.awt.*;

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
    private TetrisPiece nextPiece;
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
            switch (act) {
                case NOTHING:
                    lastResult = Result.SUCCESS;
                    break;
                case LEFT:
                    lastResult = Result.SUCCESS;
                    togglePiece(nextPiece);
                    nextPiece.setX(nextPiece.getX() - 1);
                    togglePiece(nextPiece);
                    break;
                case RIGHT:
                    lastResult = Result.SUCCESS;
                    togglePiece(nextPiece);
                    nextPiece.setX(nextPiece.getX() + 1);
                    togglePiece(nextPiece);
                    break;
                case DOWN:
                    lastResult = Result.SUCCESS;
                    togglePiece(nextPiece);
                    nextPiece.setY(nextPiece.getY() - 1);
                    togglePiece(nextPiece);
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

    // add the current piece to the board
    private void togglePiece(TetrisPiece p) {
        for (Point point : p.getBody()) {
            board[point.x + p.getX()][height - p.getY() - point.y + 1] = !board[point.x + p.getX()][height - p.getY() - point.y + 1];
        }
    }

    @Override
    public Board testMove(Action act) {
        return null;
    }

    @Override
    public void nextPiece(Piece p) {
        nextPiece = (TetrisPiece) p;
        nextPiece.setPosition(width / 2, height);
        togglePiece(nextPiece);
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
