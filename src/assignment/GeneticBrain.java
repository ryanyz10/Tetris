package assignment;

import java.util.ArrayList;

public class GeneticBrain implements Brain {
    // help from here: https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/
    private double aggHeightWeight;
    private double rowsCompWeight;
    private double numHolesWeight;
    private double bumpinessWeight;
    private ArrayList<Board> options;
    private ArrayList<Board.Action> firstMove;

    public GeneticBrain(double aggHeightWeight, double rowsCompWeight, double numHolesWeight, double bumpinessWeight) {
        this.aggHeightWeight = aggHeightWeight;
        this.rowsCompWeight = rowsCompWeight;
        this.numHolesWeight = numHolesWeight;
        this.bumpinessWeight = bumpinessWeight;
    }

    /**
     * Calculates the aggregate height of the board
     * aggregate height is the sum of the heights of all the columns
     * we want to minimize this value
     * @param currentBoard the board we are examining
     * @return an integer representing the
     */
    private int getAggregateHeight(Board currentBoard) {
        if (currentBoard instanceof TetrisBoard) {
            int[] heights = ((TetrisBoard) currentBoard).getHeights();
            int sum = 0;
            for (int i : heights) {
                sum += i;
            }

            return sum;
        }

        return -1;
    }

    /**
     * Calculates the "bumpiness" of a board
     * Bumpiness is defined as the sum of the absolute values of the differences between column heights
     * @param currentBoard the board to examine
     * @return an integer representing the bumpiness of the board
     */
    private int getBumpiness(Board currentBoard) {
        if (currentBoard instanceof TetrisBoard) {
            int[] heights = ((TetrisBoard) currentBoard).getHeights();
            int sum = 0;
            for (int i = 0; i < heights.length - 1; i++) {
                sum += Math.abs(heights[i] - heights[i + 1]);
            }

            return sum;
        }

        return -1;
    }

    /**
     * Calculates the number of holes in the current board positions
     * @param currentBoard the board we are examining
     * @return an integer representing the number of holes in the board
     */
    private int getHoles(Board currentBoard) {
        if (currentBoard instanceof TetrisBoard) {
            // we only need to examine to maxHeight since that's the highest filled point
            int numHoles = 0;
            for (int x = 0; x < currentBoard.getWidth(); x++) {
                // flag for whether the column has pieces in it
                boolean filled = false;
                for (int y = currentBoard.getHeight() - 1; y >= 0; y--) {
                    if (currentBoard.getGrid(x, y)) {
                        filled = true;
                    } else if (!currentBoard.getGrid(x, y) && filled) {
                        // if this column has pieces above the current y, but the current y is not filled, then it's a hole
                        numHoles++;
                    }

                }
            }

            return numHoles;
        }

        return -1;
    }

    // calculate the score of a position based on the four things we care about
    private double calculateMoveScore(Board option) {
        return option.getRowsCleared() * rowsCompWeight + getHoles(option) * numHolesWeight + getBumpiness(option) * bumpinessWeight + getAggregateHeight(option) * aggHeightWeight;
    }

    @Override
    public Board.Action nextMove(Board currentBoard) {
        options = new ArrayList<>();
        firstMove = new ArrayList<>();

        genAllMoves(currentBoard);

        double best = -Double.MAX_VALUE;
        int bestIndex = 0;

        for (int i = 0; i < options.size(); i++) {
            Board curr = options.get(i);
            double score = calculateMoveScore(curr);
            if (score > best) {
                best = score;
                bestIndex = i;
            }
        }

        // System.out.println("Best move is " + firstMove.get(bestIndex) + " with score " + best);

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
