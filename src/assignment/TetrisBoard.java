package assignment;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    private int pieceX, pieceY;
    private int height, width;
    private int[] heights; // height of pieces in each column
    private int[] widths; // number of filled columns in each row
    private int pieceState;

    // JTetris will use this constructor
    public TetrisBoard(int width, int height) {
    	if(width < 4 || height < 4) {
    		throw new IllegalArgumentException("Invalid Board Size! Both width and height must be greater than 3.");
    	}
        this.height = height;
        this.width = width;
        board = new boolean[height][width];
        heights = new int[width];
        widths = new int[height];
        pieceState = -1;
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
                    if (canMoveLeft()) {
                        pieceX -= 1;
                        togglePiece(nextPiece);
                        lastResult = Result.SUCCESS;
                    } else {
                        togglePiece(nextPiece);
                        lastResult = Result.OUT_BOUNDS;
                    }
                    break;
                case RIGHT:
                    togglePiece(nextPiece);
                    if (canMoveRight()) {
                        pieceX += 1;
                        togglePiece(nextPiece);
                        lastResult = Result.SUCCESS;
                    } else {
                        togglePiece(nextPiece);
                        lastResult = Result.OUT_BOUNDS;
                    }
                    break;
                case DOWN:
                    togglePiece(nextPiece);
                    if (canMoveDown(1)) {
                        pieceY -= 1;
                        togglePiece(nextPiece);
                        lastResult = Result.SUCCESS;
                    } else {
                        togglePiece(nextPiece);
                        lastResult = Result.PLACE;
                    }
                    break;
                case DROP: {
                        togglePiece(nextPiece);
                        int maxDropHeight = getMaxDropHeight();
                        if (canMoveDown(maxDropHeight)) {
                            pieceY -= maxDropHeight;
                        }

                        togglePiece(nextPiece);
                        lastResult = Result.PLACE;
                        break;
                    }
                case CLOCKWISE: {
                        togglePiece(nextPiece);
                        int nextState = (pieceState+1) % 4;
                        TetrisPiece tempPiece = wallKick(clockwise(nextPiece), nextState);
                        if(tempPiece != null) {
                        	nextPiece = tempPiece;
                        	pieceState = nextState;
                        	lastResult = Result.SUCCESS;
                        } else {
                        	lastResult = Result.OUT_BOUNDS;
                        }
                        togglePiece(nextPiece);
                        break;
                    }
                case COUNTERCLOCKWISE: {
                        togglePiece(nextPiece);
                        int nextState = (pieceState-1) < 0 ? 3:pieceState-1;
                        TetrisPiece tempPiece = wallKick((TetrisPiece) nextPiece.nextRotation(), nextState);
                        if(tempPiece != null) {
                        	nextPiece = tempPiece;
                        	pieceState = nextState;
                        	lastResult = Result.SUCCESS;
                        } else {
                        	lastResult = Result.OUT_BOUNDS;
                        }
                        
                        togglePiece(nextPiece);
                        break;
                    }
                case HOLD:
                    break;
            }
            if (lastResult == Result.PLACE) {
                nextPiece = null;
                updateHeights();
                updateWidths();
                clearRows();
            }
        }
        return lastResult;
    }

    @Override
    public Board testMove(Action act) {
        Board testBoard = copyBoard();
        testBoard.move(act);
        return testBoard;
    }

    /**
     * creates a copy of the current board for testMove
     * @return a Board object that is a copy of the current Board
     */
    private Board copyBoard() {
        Board clone = new TetrisBoard(width, height);
        ((TetrisBoard) clone).board = copyBooleanMatrix();
        ((TetrisBoard) clone).heights = copyIntArray(this.heights);
        ((TetrisBoard) clone).widths = copyIntArray(this.widths);
        ((TetrisBoard) clone).nextPiece = nextPiece;
        ((TetrisBoard) clone).pieceX = pieceX;
        ((TetrisBoard) clone).pieceY = pieceY;
        return clone;
    }

    /**
     * creates a copy of the current board matrix
     * needed because otherwise it will be passed by reference
     * @return copy of the board matrix in the current Board
     */
    private boolean[][] copyBooleanMatrix() {
        boolean[][] clone = new boolean[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                clone[i][j] = board[i][j];
            }
        }

        return clone;
    }

    private int[] copyIntArray(int[] array) {
        int[] result = new int[array.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = array[i];
        }

        return result;
    }

    /**
     * checks if board has any complete rows and if so, removes those rows
     */
    private void clearRows() {
        int rowsCleared = 0;
        for (int i = 0; i < widths.length;) {
            if (widths[i] == width) {
                // this row is full
                rowsCleared++;
                shiftBoard(i);
                updateWidths();
            } else {
                i++;
            }
        }

        updateHeights();
        this.rowsCleared = rowsCleared;
    }

    /**
     * clears a row and shifts the other rows to fill in the missing row
     * @param row row to be cleared
     */
    private void shiftBoard(int row) {
        for (int i = row - 1; i >= 0; i--) {
            board[i + 1] = board[i];
        }

        board[0] = new boolean[width];
    }

    /**
     * updates the heights array after placing piece
     */
    private void updateHeights() {
        for (int c = 0; c < width; c++) {
            int row = 0;
            while (row < height && !board[row][c]) {
                row++;
            }

            heights[c] = height - row;
        }
    }

    /**
     * updates the widths array after placing a piece
     */
    private void updateWidths() {
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

    /**
     * helper method to get a clockwise rotation without ugly code
     * @param p TetrisPiece being rotated
     * @return TetrisPiece p after clockwise rotation
     */
    private TetrisPiece clockwise(TetrisPiece p) {
        return (TetrisPiece) p.nextRotation().nextRotation().nextRotation();
    }
    
    /**
     * Wall Kick main function
     * @param p The rotated piece
     * @param newState The state of the rotated piece
     * @return If there is a possible position, x and y will be updated and the rotated piece will be returned.
     * Otherwise a null piece signifying no rotation is returned
     */
    private TetrisPiece wallKick(TetrisPiece p, int newState) {
    	int[] tests = new int[10];
    	
    	//Determine if negative multiplier needs to be applied
    	boolean changeSign = (pieceState > newState && !(pieceState==3 && newState==0))
    													|| (pieceState == 0 && newState == 3);
    	
    	//Get the correct set of possible locations
    	if(p.getPieceType() == 2) {
    		return p;
    	} else if(p.getPieceType() == 1) {
    		switch(pieceState+"&"+newState) {
    		case "0&1":
    		case "1&0": 
    			tests = new int[] {0,0,-2,0,1,0,-2,-1,1,2};
    			break;
    		case "1&2":
    		case "2&1": 
    			tests = new int[] {0,0,-1,0,2,0,-1,2,2,-1};
    			break;  		
    		case "2&3":
    		case "3&2": 
    			tests = new int[] {0,0,2,0,-1,0,2,1,-1,-2};
    			break;
    		case "3&0":
    		case "0&3": 
    			tests = new int[] {0,0,1,0,-2,0,1,-2,-2,1};
    			break;
    		}
    	} else {
    		switch(pieceState+"&"+newState) {
    		case "0&1":
    		case "1&0": 
    			tests = new int[] {0,0,-1,0,-1,1,0,-2,-1,-2};
    			break;
    		case "1&2":
    		case "2&1": 
    			tests = new int[] {0,0,1,0,1,-1,0,2,1,2};
    			break;  		
    		case "2&3":
    		case "3&2": 
    			tests = new int[] {0,0,1,0,1,1,0,-2,1,-2};
    			break;
    		case "3&0":
    		case "0&3": 
    			tests = new int[] {0,0,-1,0,-1,-1,0,2,-1,2};
    			break;
    		}
    	}
    	
    	//Check all cases
    	tests_label:
    	for(int testCase = 0; testCase+1 < tests.length; testCase+=2) {
    		for(Point po: p.getGameBody()) {
    			int newX = (int) (po.getX() + this.pieceX + (changeSign?-1:1) * tests[testCase]);
        		int newY = (int) (po.getY() + this.pieceY + (changeSign?-1:1) * tests[testCase+1]);
    			if((newX < 0 || newX >= width) || (yToRow(newY)<0 || yToRow(newY) >= height) || board[yToRow(newY)][newX])
    				continue tests_label;
    		}
    		
    		this.pieceX = this.pieceX + (changeSign?-1:1) * tests[testCase];
    		this.pieceY = this.pieceY + (changeSign?-1:1) * tests[testCase+1];
    		return p;
    	}
    	
    	return null;
    }

    /**
     * checks if piece can move left 1 unit
     * @return true if nextPiece can move left, false otherwise
     */
    private boolean canMoveLeft() {
        for (Point point : nextPiece.getGameBody()) {
            int newX = point.x + pieceX - 1;
            if (newX < 0 || board[yToRow(pieceY)][newX]) {
                return false;
            }
        }

        return true;
    }

    /**
     * checks if a piece can move right 1 unit
     * @return true if p can move right, false otherwise
     */
    private boolean canMoveRight() {
        for (Point point : nextPiece.getGameBody()) {
            int newX = point.x + pieceX + 1;
            if (newX >= width || board[yToRow(pieceY)][newX]) {
                return false;
            }
        }

        return true;
    }

    /**
     * checks if nextPiece can be moved down in the board
     * @param distance the distance to move down
     * @return true if nextPiece can be moved down length of distance, false otherwise
     */
    private boolean canMoveDown(int distance) {
        for (Point point : nextPiece.getGameBody()) {
            int newY = pieceY + point.y - distance;
            int row = yToRow(newY);
            if (row < 0 || row >= height || board[row][pieceX + point.x]) {
                return false;
            }
        }

        return true;
    }

    /**
     * gets the maximum distance the piece can fall in the board
     * this method used to use the skirt, however, because of how we do rotations, that led to IndexOutOfBounds
     * when the piece was rotated and moved against the left wall
     * @return value representing max distance the piece can fall
     */
    private int getMaxDropHeight() {
        int minDrop = Integer.MAX_VALUE;
        for (Point point : nextPiece.getGameBody()) {
            int currDrop = pieceY + point.y - heights[pieceX + point.x];
            minDrop = Math.min(currDrop, minDrop);
        }

        return minDrop;
    }

    // the following two methods convert from y position to row number and vice versa
    // the math is the same, but for the sake of readability I created 2 methods
    private int yToRow(int y) {
        return height - 1 - y;
    }

    private int rowToY(int r) {
        return height - 1 - r;
    }

    /**
     * toggle a piece in the board for moving
     * @param p the piece being moved
     */
    private void togglePiece(TetrisPiece p) {
        for (Point point : p.getGameBody()) {
            int r = yToRow(pieceY + point.y);
            int c = point.x + pieceX;
            board[r][c] = !board[r][c];
        }
    }

    protected int[] getHeights() {
        return heights;
    }

    protected int[] getWidths() {
        return widths;
    }

    public int getPieceX() {
        return pieceX;
    }

    public int getPieceY() {
        return pieceY;
    }

    @Override
    public void nextPiece(Piece p) {
        nextPiece = (TetrisPiece) p;
        pieceX = (width - nextPiece.getWidth()) / 2;
        pieceY = height - JTetris.TOP_SPACE;
        pieceState = 2;
        togglePiece(nextPiece);
    }

    //For Absolute Equality
    @Override
    public boolean equals(Object other) {
        if (other instanceof TetrisBoard) {
        	TetrisBoard otherTet = ((TetrisBoard) other);
            boolean[][] otherBoard = otherTet.board;
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (board[i][j] != otherBoard[i][j]) {
                        return false;
                    }
                }
            }
            
            if(otherTet.rowsCleared != this.rowsCleared)
            	return false;
            
            if(this.nextPiece != null && !(otherTet.nextPiece.equals(this.nextPiece)))
            	return false;
            
            if(otherTet.getLastAction() != this.getLastAction())
            	return false;
            
            if(otherTet.getLastResult() != this.getLastResult())
            	return false;
            
            if(otherTet.getPieceX() != this.getPieceX())
            	return false;
            
            if(otherTet.getPieceY() != this.getPieceY())
            	return false;
            
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
    	
        int minDrop = JTetris.HEIGHT + JTetris.TOP_SPACE;
        for (Point point : ((TetrisPiece) piece).getGameBody()) {
            int currDrop = (JTetris.HEIGHT - 1) + point.y - heights[x + point.x];
            minDrop = Math.min(currDrop, minDrop);
        }
        

        return JTetris.HEIGHT - minDrop;
    }

    @Override
    public int getColumnHeight(int x) {
        return heights[x];
    }

    @Override
    public int getRowWidth(int y) {
        return widths[yToRow(y)];
    }

    @Override
    public boolean getGrid(int x, int y) {
        // if position is out of bounds
        if (x < 0 || x > width || y < 0 || y > height) {
            return true;
        }

        return board[yToRow(y)][x];
    }

}
