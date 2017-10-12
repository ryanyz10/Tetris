package assignment;

public class GeneticBrain implements Brain {
    // help from here: https://codemyroad.wordpress.com/2013/04/14/tetris-ai-the-near-perfect-player/
    private double aggHeightWeight;
    private double rowsCompWeight;
    private double numHolesWeight;
    private double bumpinessWeight;


    public GeneticBrain() {

    }

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

    @Override
    public Board.Action nextMove(Board currentBoard) {
        return null;
    }


}
