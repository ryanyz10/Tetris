package assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import assignment.Board.Action;

/**
 * An extension of JTetris which runs an implementation of a Brain.
 */
public class JBrainTetris extends JTetris {
    /**
     * The brain being used.
     */
    private Brain brain;
    private WeightsVector[] weightVectors = new WeightsVector[1000];

    /**
     * Creates a new JTetris running the given Brain.
     * @param brain The brain to use.
     */
    public JBrainTetris(Brain brain) {
        super();
        for (int i = 0; i < 1000; i++) {
            WeightsVector vector = new WeightsVector(Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5, Math.random() - 0.5);
            vector.normalize();
            weightVectors[i] = vector;
        }
        this.brain = brain;
        this.timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Action act = brain.nextMove(JBrainTetris.this.board);
                tick(act);
            }
        });
    }

    public static void main(String[] args) {
        // Replace LameBrain with your own Brain implementation.
        createGUI(new JBrainTetris(new LameBrain()));
    }

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

        private void normalize() {
            double len = Math.sqrt(aggHeightWeight * aggHeightWeight + rowsCompWeight * rowsCompWeight + numHolesWeight * numHolesWeight + bumpinessWeight * bumpinessWeight);
            aggHeightWeight /= len;
            rowsCompWeight /= len;
            numHolesWeight /= len;
            bumpinessWeight /= len;
        }


        @Override
        public int compareTo(WeightsVector o) {
            return o.fitness - fitness;
        }
    }
}
