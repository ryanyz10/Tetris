package assignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class JBrainTetrisTuner {
    private final int POPULATION_SIZE = 100;
    private final int NEW_POPULATION_PERCENT = 30;
    private final int MAX_MOVES = 200;
    private final int MAX_GAMES = 5;
    private Vector[] originalPopulation = new Vector[POPULATION_SIZE];
    private Vector[] newPopulation = new Vector[(int)(POPULATION_SIZE * NEW_POPULATION_PERCENT / 100.0)];


    public JBrainTetrisTuner() {
        for (int i = 0; i < originalPopulation.length; i++) {
            Vector vector = new Vector(4);
            for (int j = 0; j < vector.numComponents; j++) {
                vector.components[j] = Math.random() - 0.5;
            }

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
                Vector child = tournamentSelection(10);
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
            Arrays.sort(originalPopulation);
            Vector fittest = originalPopulation[0];
            int totalFitness = 0;
            for (int i = 0; i < originalPopulation.length; i++) {
                totalFitness += originalPopulation[i].fitness;
            }

            System.out.printf("Average: %f\n", (double)(totalFitness)/POPULATION_SIZE);
            System.out.printf("Fittest in generation %d: <%f,%f,%f,%f> with fitness %d\n", generation, fittest.components[0], fittest.components[1], fittest.components[2], fittest.components[3], fittest.fitness);
            try {
                PrintWriter out = new PrintWriter(new File(weightsFile));
                //out.printf("Fittest in generation %d: <%f,%f,%f,%f,%f,%f,%f> with fitness %d\n", generation, fittest.components[0], fittest.components[1], fittest.components[2], fittest.components[3], fittest.components[4], fittest.components[5], fittest.components[6], fittest.fitness);
                out.printf("Fittest in generation %d: <%f,%f,%f,%f> with fitness %d\n", generation, fittest.components[0], fittest.components[1], fittest.components[2], fittest.components[3], fittest.fitness);
                out.close();
            } catch (FileNotFoundException e) {
                System.err.println("Could not create weights.txt");
            }

            generation++;
        }
    }

    private void calculateFitnesses(Vector[] vectors) {
        for (Vector vector : vectors) {
//            System.out.print("Starting: ");
//            printVector(vector);
            Brain brain = new GeneticBrain(vector.components);
            for (int game = 0; game < MAX_GAMES; game++) {
                Board board = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT + JTetris.TOP_SPACE);
                int numMoves = 0;
                board.nextPiece(pickNextPiece());
                while (numMoves < MAX_MOVES && !gameOver(board)) {
                    Board.Action nextMove = brain.nextMove(board);
                    board.move(nextMove);
                    if (board.getLastResult() == Board.Result.PLACE) {
                        board.nextPiece(pickNextPiece());
                        vector.fitness += board.getRowsCleared();
                    }

                    numMoves++;
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

    private Vector crossover(Vector one, Vector two) {
        Vector child = new Vector(one.numComponents);
        for (int i = 0; i < one.numComponents; i++) {
            child.components[i] = one.components[i] * one.fitness + two.components[i] * two.fitness;
        }

        child.unitize();
        return child;
    }

    /**
     * Mutates a given vector. A randomly selected attribute is changed by +/- 0.2
     * @param vector the vector to be mutated
     */
    private void mutate(Vector vector) {
        int attribute = (int) (Math.random() * vector.numComponents);
        double mutation = Math.random() * 0.4 - 0.2;
        vector.components[attribute] += mutation;
        vector.unitize();
    }

    /**
     * Randomly selects a subset of the population and selects the two fittest to be parents for a next generation
     * @param percent the percentage of the population to select
     */
    private Vector tournamentSelection(int percent) {
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
            } else if (originalPopulation[parentTwoIndex].fitness < originalPopulation[index].fitness) {
                parentTwoIndex = index;
            }
        }

        return crossover(originalPopulation[parentOneIndex], originalPopulation[parentTwoIndex]);
    }

    /**
     * replaces a subset of the old population with the new population
     * Darwin would be proud
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
    private void printVector(Vector vector) {
//        System.out.printf("<%f,%f,%f,%f,%f,%f,%f> %d\n", vector.components[0], vector.components[1], vector.components[2], vector.components[3], vector.components[4], vector.components[5], vector.components[6], vector.fitness);
        System.out.printf("<%f,%f,%f,%f> %d\n", vector.components[0], vector.components[1], vector.components[2], vector.components[3], vector.fitness);
    }

    public static void main(String[] args) {
        JBrainTetrisTuner tuner = new JBrainTetrisTuner();
        tuner.tune();
    }

    /**
     * A helper class that holds a collection of weights
     * Also contains info about its "fitness" i.e. how many rows it is able to clear
     */
    private class Vector implements Comparable<Vector> {
        private int numComponents;
        private double[] components;
        // as described in the source above, the fitness of a vector is how many rows it clears
        private int fitness;

        private Vector(int numComponents) {
            this.numComponents = numComponents;
            components = new double[numComponents];
        }

        private void unitize() {
            double len = 0;
            for (double i : components) {
                len += i * i;
            }
            len = Math.sqrt(len);
            if (len == 0) {
                return;
            }

            for (int i = 0; i < numComponents; i++) {
                components[i] /= len;
            }
        }

        @Override
        public int compareTo(Vector o) {
            return o.fitness - fitness;
        }
    }
}
