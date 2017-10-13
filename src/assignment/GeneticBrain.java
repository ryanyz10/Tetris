package assignment;

import java.util.ArrayList;

public class GeneticBrain implements Brain {
    // help from here: https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/
    private double[] components;
    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMove;

    public GeneticBrain(double[] components) {
        this.components = components;
    }

    private int getFullCells(Board currentBoard) {
        int total = 0;
        int[] widths = ((TetrisBoard) currentBoard).getWidths();
        for (int i = 0; i < widths.length; i++) {
            total += widths[i];
        }

        return total;
    }

    private int maxSlope(Board currentBoard) {
        int[] heights = ((TetrisBoard) currentBoard).getHeights();
        int maxDiff = 0;
        for (int i = 0; i < heights.length - 1; i++) {
            maxDiff = Math.max(Math.abs(heights[i] - heights[i + 1]), maxDiff);
        }

        return maxDiff;
    }

    /**
     * Calculates the aggregate height of the board
     * aggregate height is the sum of the heights of all the columns
     * we want to minimize this value
     * @param currentBoard the board we are examining
     * @return an integer representing the
     */
    private int getAggregateHeight(Board currentBoard) {
        int[] heights = ((TetrisBoard) currentBoard).getHeights();
        int sum = 0;
        for (int i : heights) {
            sum += i;
        }

        return sum;
    }

    /**
     * Calculates the "bumpiness" of a board
     * Bumpiness is defined as the sum of the absolute values of the differences between column heights
     * @param currentBoard the board to examine
     * @return an integer representing the bumpiness of the board
     */
    private int getBumpiness(Board currentBoard) {

        int[] heights = ((TetrisBoard) currentBoard).getHeights();
        int sum = 0;
        for (int i = 0; i < heights.length - 1; i++) {
            sum += Math.abs(heights[i] - heights[i + 1]);
        }

        return sum;
    }

    /**
     * Calculates the number of holes in the current board positions
     * @param currentBoard the board we are examining
     * @return an integer representing the number of holes in the board
     */
    private int getHoles(Board currentBoard) {
        int numHoles = 0;
        for (int x = 0; x < currentBoard.getWidth(); x++) {
            // flag for whether the column has pieces in it
            boolean filled = false;
            int maxHeight = currentBoard.getMaxHeight();
            for (int y = maxHeight - 1; y >= 0; y--) {
                if (currentBoard.getGrid(x, y)) {
                    filled = true;
                } else if (filled && !currentBoard.getGrid(x, y)) {
                    // if this column has pieces above the current y, but the current y is not filled, then it's a hole
                    numHoles++;
                }
            }
        }
        return numHoles;
    }

    // calculate the score of a position based on the four things we care about
    private double calculateMoveScore(Board option) {
//        return option.getRowsCleared() * components[0] + getHoles(option) * components[1] + getBumpiness(option) * components[2] + getAggregateHeight(option) * components[3] + components[4] * option.getMaxHeight() + components[5] * getFullCells(option) + components[6] * maxSlope(option);
        return option.getRowsCleared() * components[0] + getHoles(option) * components[1] + getBumpiness(option) * components[2] + getAggregateHeight(option) * components[3];
    }

    @Override
    public Board.Action nextMove(Board currentBoard) {
        options = new ArrayList<>();
        firstMove = new ArrayList<>();

        genAllMoves(currentBoard);

        double best = 0;
        int bestIndex = 0;

        for (int i = 0; i < options.size(); i++) {
            Board curr = options.get(i);
            double score = calculateMoveScore(curr);
            if (best == 0 || score > best) {
                best = score;
                bestIndex = i;
            }
        }

        return firstMove.get(bestIndex);
    }

    private void genAllMoves(Board currentBoard) {
        Board testBoard = currentBoard.testMove(Board.Action.NOTHING);
        boolean counterclockwise = true;
        for (int rotation = 0; rotation < 4; rotation++) {
            if (testBoard.getLastResult() == Board.Result.SUCCESS) {
                enumerateOptions(testBoard, counterclockwise ? rotation : 3 - rotation);
            } else {
                if (counterclockwise) {
                    counterclockwise = false;
                    testBoard = currentBoard.testMove(Board.Action.NOTHING);
                    rotation = 0;
                } else {
                    break;
                }
            }

            testBoard.move(counterclockwise ? Board.Action.COUNTERCLOCKWISE : Board.Action.CLOCKWISE);
        }
    }

    private void enumerateOptions(Board currentBoard, int rotation) {
        options.add(currentBoard.testMove(Board.Action.DROP));
        firstMove.add(Board.Action.DROP);

        Board left = currentBoard.testMove(Board.Action.LEFT);
        while (left.getLastResult() == Board.Result.SUCCESS) {
            options.add(left.testMove(Board.Action.DROP));
            if (rotation == 0) {
                firstMove.add(Board.Action.LEFT);
            } else if (rotation == 3) {
                firstMove.add(Board.Action.CLOCKWISE);
            } else {
                firstMove.add(Board.Action.COUNTERCLOCKWISE);
            }

            left.move(Board.Action.LEFT);
        }

        Board right = currentBoard.testMove(Board.Action.RIGHT);
        while (right.getLastResult() == Board.Result.SUCCESS) {
            options.add(right.testMove(Board.Action.DROP));
            if (rotation == 0) {
                firstMove.add(Board.Action.RIGHT);
            } else if (rotation == 3) {
                firstMove.add(Board.Action.CLOCKWISE);
            } else {
                firstMove.add(Board.Action.COUNTERCLOCKWISE);
            }

            right.move(Board.Action.RIGHT);
        }
    }
}
