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

    /**
     * Creates a new JTetris running the given Brain.
     * @param brain The brain to use.
     */
    public JBrainTetris(Brain brain) {
        super();

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
        double[] components = new double[]{-0.052961,-0.816545,-0.504955,-0.274716};
        createGUI(new JBrainTetris(new GeneticBrain(components)));
        //createGUI(new JBrainTetris(new LameBrain()));
    }
}
