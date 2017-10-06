package assignment;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

/**
 * An immutable representation of a tetris piece in a particular rotation.
 * Each piece is defined by the blocks that make up its body.
 *
 * You need to implement this.
 */
public final class TetrisPiece extends Piece {
    private Point[] body;

    //TODO: calculate stuff
    private TetrisPiece(Point[] body) {
        this.body = body;
        // line, square or other
        // there will always be 4 rotations
        this.next = generateRotation(body);
        TetrisPiece curr = (TetrisPiece) this.next;
        while (!curr.equals(this)) {
            curr.next = generateRotation(curr.next.getBody());
            curr = (TetrisPiece) curr.next;
        }
    }

    private TetrisPiece generateRotation(Point[] points) {
        // used for determining the piece shape
        Set<Integer> xSet = new HashSet<>();
        Set<Integer> ySet = new HashSet<>();
        for (Point point : points) {
            xSet.add(point.x);
            ySet.add(point.y);
        }

        if (xSet.size() == 2 && ySet.size() == 2) {
            // shape is a square
            return new TetrisPiece(points);
        }

        int size = (xSet.size() == 1 || ySet.size() == 1) ? 4 : 3;
        // we use a 3x3 matrix to do rotation
        Point[] rotated = generatePointArray(rotateMatrix(generatePieceMatrix(body, size), size), size);
        return new TetrisPiece(rotated);
    }

    /**
     *
     * @param matrix 3x3 boolean matrix representation of a TetrisPiece
     * @return Point array representation of a TetrisPiece
     */
    private Point[] generatePointArray(boolean[][] matrix, int size) {
        Point[] points = new Point[4];
        int index = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j]) {
                    points[index++] = new Point(j, size - 1 - i);
                }
            }
        }

        return points;
    }

    /**
     *
     * @param points Point array representation of a TetrisPiece
     * @return 3x3 boolean matrix representation of points
     */
    private boolean[][] generatePieceMatrix(Point[] points, int size) {
            boolean[][] matrix = new boolean[size][size];

            for (Point point: points) {
                matrix[size - 1 - point.y][point.x] = true;
            }

            return matrix;
    }

    private boolean[][] rotateMatrix(boolean[][] matrix, int size) {
        boolean[][] rotated = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotated[i][j] = matrix[j][size - 1 - i];
            }
        }

        return rotated;
    }

    /**
     * Parse a "piece string" of the form "x1 y1 x2 y2 ..." into a TetrisPiece
     * where the corresponding (x1, y1), (x2, y2) positions have been filled in.
     */
    public static Piece getPiece(String pieceString) {
        return new TetrisPiece(parsePoints(pieceString));
    }

    @Override
    public int getWidth() { return -1; }

    @Override
    public int getHeight() { return -1; }

    @Override
    public Point[] getBody() { return body; }

    @Override
    public int[] getSkirt() { return null; }

    @Override
    public boolean equals(Object other) {
        if (other instanceof TetrisPiece) {
            Set<Point> tmp = new HashSet<>();
            // since each block is made of 4 and only 4 points, we can check both here
            for (int i = 0; i < body.length; i++) {
                tmp.add(body[i]);
                tmp.add(((TetrisPiece) other).body[i]);
            }

            return tmp.size() == 4;
        }

       return false;
    }
}
