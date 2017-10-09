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
    private int[] heights; // height of pieces in each column
    private int[] widths; // number of filled columns in each row

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
                    togglePiece(nextPiece);
                    if (isValidMove(nextPiece, -1, 0)) {
                        nextPiece.setX(nextPiece.getX() - 1);
                        togglePiece(nextPiece);
                        lastResult = Result.SUCCESS;
                    } else {
                        togglePiece(nextPiece);
                        lastResult = Result.OUT_BOUNDS;
                    }
                    break;
                case RIGHT:
                    togglePiece(nextPiece);
                    if (isValidMove(nextPiece, 1, 0)) {
                        nextPiece.setX(nextPiece.getX() + 1);
                        togglePiece(nextPiece);
                        lastResult = Result.SUCCESS;
                    } else {
                        togglePiece(nextPiece);
                        lastResult = Result.OUT_BOUNDS;
                    }
                    break;
                case DOWN:
                    togglePiece(nextPiece);
                    if (isValidMove(nextPiece, 0, -1)) {
                        nextPiece.setY(nextPiece.getY() - 1);
                        togglePiece(nextPiece);
                        lastResult = Result.SUCCESS;
                    } else {
                        togglePiece(nextPiece);
                        nextPiece = null;
                        lastResult = Result.OUT_BOUNDS;
                        updateHeights(heights);
                        updateWidths(widths);
                        clearRows();
                    }
                    break;
                case DROP: {
                        togglePiece(nextPiece);
                        int[] skirt = nextPiece.getSkirt();
                        int x = nextPiece.getX();
                        int y = nextPiece.getY();

                        int minHeight = JTetris.HEIGHT + JTetris.TOP_SPACE;
                        for (int i = 0; i < skirt.length; i++) {
                            int currHeight = y + skirt[i] - heights[x + i];
                            minHeight = Math.min(currHeight, minHeight);
                        }

                        nextPiece.setY(y - minHeight);
                        togglePiece(nextPiece);
                        updateWidths(widths);
                        updateHeights(heights);
                        lastResult = Result.SUCCESS;
                        nextPiece = null;
                        clearRows();
                        break;
                    }
                case CLOCKWISE: {
                        int x = nextPiece.getX();
                        int y = nextPiece.getY();
                        togglePiece(nextPiece);
                        nextPiece = clockwise(nextPiece);
                        nextPiece.setPosition(x, y);
                        togglePiece(nextPiece);
                        break;
                    }
                case COUNTERCLOCKWISE: {
                        int x = nextPiece.getX();
                        int y = nextPiece.getY();
                        togglePiece(nextPiece);
                        nextPiece = (TetrisPiece) nextPiece.nextRotation();
                        nextPiece.setPosition(x, y);
                        togglePiece(nextPiece);
                        break;
                    }
                case HOLD:
                    break;
            }
        }

        return lastResult;
    }

    private void clearRows() {
        int rowsCleared = 0;
        for (int i = 0; i < widths.length;) {
            if (widths[i] == width) {
                // this row is full
                rowsCleared++;
                shiftBoard(board, i);
                updateWidths(widths);
            } else {
                i++;
            }
        }

        updateHeights(heights);
        this.rowsCleared = rowsCleared;
    }

    // row is the one being cleared
    private void shiftBoard(boolean[][] board, int row) {
        for (int i = row - 1; i >= 0; i--) {
            board[i] = board[i + 1];
        }

        board[height - 1] = new boolean[width];
    }

    private void updateHeights(int[] heights) {
        for (int c = 0; c < width; c++) {
            int row = 0;
            while (row < height && !board[row][c]) {
                row++;
            }

            heights[c] = height - row;
        }
    }

    private void updateWidths(int[] widths) {
        for (int r = 0; r < height; r++) {
            int count = 0;
            for (int c = 0; c < width; c++) {
                if (board[r][c]) {
                    count++;
                }
            }

            widths[r] = count;
        }
    }

    private TetrisPiece clockwise(TetrisPiece p) {
        return (TetrisPiece) p.nextRotation().nextRotation().nextRotation();
    }

    private boolean isValidMove(TetrisPiece p, int dx, int dy) {
        for (Point point : p.getBody()) {
            int r = height - (p.getY() + dy + point.y) - 1;
            int c = point.x + p.getX() + dx;

            if (r < 0 || r >= height || c < 0 || c >= width) {
                // System.out.println("Out of bounds");
                return false;
            }

            if (board[r][c]) {
                // System.out.println("Occupied");
                return false;
            }
        }

        // System.out.println("Space is free");
        return true;
    }

    // add the current piece to the board
    private void togglePiece(TetrisPiece p) {
        for (Point point : p.getBody()) {
            int r = height - (p.getY() + point.y) - 1;
            int c = point.x + p.getX();
            board[r][c] = !board[r][c];
        }
    }

    @Override
    public Board testMove(Action act) {
        return null;
    }

    @Override
    public void nextPiece(Piece p) {
        nextPiece = (TetrisPiece) p;
        nextPiece.setPosition((width - nextPiece.getWidth()) / 2, height - JTetris.TOP_SPACE);
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
