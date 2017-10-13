package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class JBrainTetrisTuner {
    /* Population size = 100
     * Rounds per candidate = 5
     * Max moves per round = 200
     * Theoretical fitness limit = 5 * 200 * 4 / 10 = 400
     */
    private final int POPULATION_SIZE = 1000;
    private final int NEW_POPULATION_PERCENT = 30;
    private final int MAX_MOVES = 500;
    private final int MAX_GAMES = 100;
    private WeightsVector[] originalPopulation = new WeightsVector[POPULATION_SIZE];
    private WeightsVector[] newPopulation = new WeightsVector[(int)(POPULATION_SIZE * NEW_POPULATION_PERCENT / 100.0)];


    public JBrainTetrisTuner() {
        for (int i = 0; i < originalPopulation.length; i++) {
            WeightsVector vector = new WeightsVector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
            vector.unitize();
            originalPopulation[i] = vector;
        }
    }

    public void tune() {
        String weightsFile = "weights.txt";
        System.out.println("Start Original Population");
        calculateFitnesses(originalPopulation);
        System.out.println("Finish Original Population");
        Arrays.sort(originalPopulation, Collections.reverseOrder());
        int generation = 0;
        while (true) {
            for (int i = 0; i < newPopulation.length; i++) {
                int[] parentIndexes = tournamentSelection(10);
                WeightsVector child = crossover(originalPopulation[parentIndexes[0]], originalPopulation[parentIndexes[1]]);
                int mutate = (int)(Math.random() * 100) + 1;
                if (mutate <= 5) {
                    mutate(child);
                }

                newPopulation[i] = child;
            }
            System.out.println("Start new population");
            calculateFitnesses(newPopulation);
            System.out.println("End new population");
            System.out.println("Darwin");
            survivalOfTheFittest();
            System.out.println("End darwin");
            Arrays.sort(originalPopulation, Collections.reverseOrder());

            try {
                PrintWriter out = new PrintWriter(new File(weightsFile));
                WeightsVector fittest = originalPopulation[0];
                out.print("Fittest in generation " + generation + ": " + fittest.aggHeightWeight + " " + fittest.rowsCompWeight + " " + fittest.numHolesWeight + " " + fittest.bumpinessWeight);
                out.close();
            } catch (FileNotFoundException e) {
                System.err.println("Could not create weights.txt");
            }

            generation++;
        }
    }

    private void calculateFitnesses(WeightsVector[] vectors) {
        for (WeightsVector vector : vectors) {
//            System.out.print("Starting: ");
//            printVector(vector);
            Brain brain = new GeneticBrain(vector.aggHeightWeight, vector.rowsCompWeight, vector.numHolesWeight, vector.bumpinessWeight);
            for (int game = 0; game < MAX_GAMES; game++) {
                Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
                int numPieces = 1;
                board.nextPiece(pickNextPiece());
                while (numPieces < MAX_MOVES && !gameOver(board)) {
                    Board.Action nextMove = brain.nextMove(board);
                    board.move(nextMove);
                    if (board.getLastResult() == Board.Result.PLACE) {
                        numPieces++;
                        board.nextPiece(pickNextPiece());
                    }
                    vector.fitness += board.getRowsCleared();
                }
            }
            printVector(vector);
        }
    }

    private Piece pickNextPiece() {
        int pieceNum = (int) (JTetris.pieceStrings.length * Math.random());
        Piece piece  = TetrisPiece.getPiece(JTetris.pieceStrings[pieceNum]);
        return(piece);
    }

    private WeightsVector crossover(WeightsVector one, WeightsVector two) {
        double newAggHeightWeight = one.aggHeightWeight * one.fitness + two.aggHeightWeight * two.fitness;
        double newRowsCompWeight = one.rowsCompWeight * one.fitness + two.rowsCompWeight * two.fitness;
        double newNumHolesWeight = one.numHolesWeight * one.fitness + two.numHolesWeight * two.fitness;
        double newBumpinessWeight = one.bumpinessWeight * one.fitness + two.bumpinessWeight * two.fitness;
        WeightsVector result = new WeightsVector(newAggHeightWeight, newRowsCompWeight, newNumHolesWeight, newBumpinessWeight);
        result.unitize();
        return result;
    }

    /**
     * Mutates a given vector. A randomly selected attribute is changed by +/- 0.2
     * @param vector the vector to be mutated
     */
    private void mutate(WeightsVector vector) {
        int attribute = (int) (Math.random() * 4) + 1;
        double mutation = Math.random() * 0.4 - 0.2;
        switch (attribute) {
            case 1: vector.aggHeightWeight += mutation; break;
            case 2: vector.rowsCompWeight += mutation; break;
            case 3: vector.numHolesWeight += mutation; break;
            case 4: vector.bumpinessWeight += mutation; break;
        }
    }

    /**
     * Randomly selects a subset of the population and selects the two fittest to be parents for a next generation
     * @param percent the percentage of the population to select
     */
    private int[] tournamentSelection(int percent) {
        // for ease of writing, parentOne fitness must always be greater than parentTwoIndex
        int parentOneIndex = -1;
        int parentTwoIndex = -1;
        HashSet<Integer> checkedIndices = new HashSet<>();

        for (int i = 0; i < (int)(percent * originalPopulation.length / 100.0);) {
            int index = (int)(Math.random() * originalPopulation.length);
            if (checkedIndices.contains(index)) {
                continue;
            }

            checkedIndices.add(index);
            i++;
            if (parentOneIndex == -1) {
                parentOneIndex = index;
            } else if (originalPopulation[index].fitness > originalPopulation[parentOneIndex].fitness) {
                parentTwoIndex = parentOneIndex;
                parentOneIndex = index;
            } else if (parentTwoIndex == -1) {
                parentTwoIndex = index;
            }
        }

        // in times like these, you really wish java had tuples like python
        int[] result = new int[2];
        result[0] = parentOneIndex;
        result[1] = parentTwoIndex;

        return result;
    }

    /**
     * replaces a subset of the old population with the new population
     */
    private void survivalOfTheFittest() {
        for (int i = originalPopulation.length - newPopulation.length; i < originalPopulation.length; i++) {
            originalPopulation[i] = newPopulation[i - (originalPopulation.length - newPopulation.length)];
        }
    }

    /**
     * since we bypass JTetris, this checks if the game is over i.e. a piece is sticking into the top 4 rows
     * @param board the game board
     * @return true if there is a piece in the top 4 rows, false otherwise
     */
    private boolean gameOver(Board board) {
        if (board.getMaxHeight() > JTetris.HEIGHT) {
            return true;
        }

        return false;
    }

    /**
     * prints all the info of a vector
     * @param vector the vector to print
     */
    private void printVector(WeightsVector vector) {
        System.out.println(vector.aggHeightWeight + " " + vector.rowsCompWeight + " " + vector.numHolesWeight + " " + vector.bumpinessWeight + " " + vector.fitness);
    }

    public static void main(String[] args) {
        JBrainTetrisTuner tuner = new JBrainTetrisTuner();
        tuner.tune();
    }

    /**
     * A helper class that holds a collection of weights
     * Also contains info about its "fitness" i.e. how many rows it is able to clear
     */
    private class WeightsVector implements Comparable<WeightsVector> {
        private double aggHeightWeight;
        private double rowsCompWeight;
        private double numHolesWeight;
        private double bumpinessWeight;
        // as described in the source above, the fitness of a vector is how many rows it clears
        private int fitness;

        private WeightsVector(double aggHeightWeight, double rowsCompWeight, double numHolesWeight, double bumpinessWeight) {
            this.aggHeightWeight = aggHeightWeight;
            this.rowsCompWeight = rowsCompWeight;
            this.numHolesWeight = numHolesWeight;
            this.bumpinessWeight = bumpinessWeight;
        }

        private void unitize() {
            double len = Math.sqrt(aggHeightWeight * aggHeightWeight + rowsCompWeight * rowsCompWeight + numHolesWeight * numHolesWeight + bumpinessWeight * bumpinessWeight);
            if (len != 0) {
                aggHeightWeight /= len;
                rowsCompWeight /= len;
                numHolesWeight /= len;
                bumpinessWeight /= len;
            }
        }

        @Override
        public int compareTo(WeightsVector o) {
            return o.fitness - fitness;
        }
    }
}
