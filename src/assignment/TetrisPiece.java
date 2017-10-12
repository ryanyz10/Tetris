package assignment;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
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
    private int width;
    private int height;
    private int[] skirt;
    private int pieceType;

    private TetrisPiece(Point[] body) {
        this.body = body;
        // This stuff should ideally be segregated into another method
        HashMap<Integer, Integer> minValues = new HashMap<>();
        int minWidth = Integer.MAX_VALUE, maxWidth = Integer.MIN_VALUE;
        int minHeight = Integer.MAX_VALUE, maxHeight = Integer.MIN_VALUE;
        for (Point point : body) {
            if (point.y > maxHeight) {
                maxHeight = point.y;
            }

            if (minValues.containsKey(point.x)) {
                if (minValues.get(point.x) > point.y) {
                    minValues.put(point.x, point.y);
                }
            } else {
                minValues.put(point.x, point.y);
            }
        }

        skirt = new int[minValues.size()];
        int index = 0;
        // same thing here
        for (Integer x : minValues.keySet()) {
            int y = minValues.get(x);

            skirt[index++] = y;

            if (x < minWidth) {
                minWidth = x;
            }

            if (x > maxWidth) {
                maxWidth = x;
            }

            if (y < minHeight) {
                minHeight = y;
            }
        }

        width = maxWidth - minWidth + 1;
        height = maxHeight - minHeight + 1;

        
        //Determine piece type
        //Only needs to be done once per piece
        Set<Integer> xSet = new HashSet<>();
        Set<Integer> ySet = new HashSet<>();
        for (Point point : body) {
            xSet.add(point.x);
            ySet.add(point.y);
        }
        if (xSet.size() == 2 && ySet.size() == 2) {
        	pieceType = 2; //Piece is SQUARE
        } else if((xSet.size() == 1 || ySet.size() == 1)) {
        	pieceType = 1; // Piece is I
        } else {
        	pieceType = 0; //Piece is OTHER
        }
       
        //Correct "I" Input
        if(pieceType == 1) {
        	Point[] newBody = new Point[4];
        	for(int pointI = 0; pointI<4; pointI++) {
        		newBody[pointI] = new Point();
        		newBody[pointI].setLocation(body[pointI].getX(), body[pointI].getY() + 1);
        	}
        	this.body = newBody;
        }
        // line, square or other
        // there will always be 4 rotations
        Point[] rotated = generateRotation(body);
        this.next = new TetrisPiece(rotated, this); 
    }

    // separate constructor for creating rotations
    // using the original constructor caused an infinite recursion
    private TetrisPiece(Point[] body, TetrisPiece original) {
    	this.body = body;
    	this.pieceType = original.getPieceType();
        HashMap<Integer, Integer> minValues = new HashMap<>();
        int minWidth = Integer.MAX_VALUE, maxWidth = Integer.MIN_VALUE;
        int minHeight = Integer.MAX_VALUE, maxHeight = Integer.MIN_VALUE;
        for (Point point : body) {
            if (point.y > maxHeight) {
                maxHeight = point.y;
            }

            if (minValues.containsKey(point.x)) {
                if (minValues.get(point.x) > point.y) {
                    minValues.put(point.x, point.y);
                }
            } else {
                minValues.put(point.x, point.y);
            }
        }

        skirt = new int[minValues.size()];
        int index = 0;
        // same thing here
        for (Integer x : minValues.keySet()) {
            int y = minValues.get(x);

            skirt[index++] = y;

            if (x < minWidth) {
                minWidth = x;
            }

            if (x > maxWidth) {
                maxWidth = x;
            }

            if (y < minWidth) {
                minHeight = y;
            }
        }

        width = maxWidth - minWidth;
        height = maxHeight - minHeight;
    	
    	Point[] rotated = generateRotation(this.body);
    	if(!original.equals(rotated))
    		this.next = new TetrisPiece(rotated, original);
    	else {
    		this.next = original;
    	}
    }

    /**
     * creates the next rotation of a given TetrisPiece
     * @param points a point array representing a TetrisPiece
     * @return a Point array representing the rotated TetrisPiece
     */
    private Point[] generateRotation(Point[] points) {
        // Determine piece characteristics
    	int size = -1;
    	switch(this.pieceType) {
    		case 2: return points;
    		case 1: size = 4; break;
    		case 0: size = 3; break;
    	}

        Point[] rotated = generatePointArray(rotateMatrix(generatePieceMatrix(body, size), size), size);
        return rotated;
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
    public int getWidth() { return width; }

    @Override
    public int getHeight() { return height; }

    @Override
    public Point[] getBody() { return body; }

    @Override
    public int[] getSkirt() { return skirt; }
    
    public int getPieceType() { return pieceType; }

    @Override
    public boolean equals(Object other) {
        // use a set because we can't be sure of the order points are given to us
        if (other instanceof TetrisPiece) {
            Set<Point> tmp = new HashSet<>();
            // since each block is made of 4 and only 4 points, we can check both here
            for (int i = 0; i < body.length; i++) {
                tmp.add(body[i]);
                tmp.add(((TetrisPiece) other).body[i]);
            }

            return tmp.size() == 4;
        } else if(other instanceof Point[]) {
        	Set<Point> tmp = new HashSet<>();
    		// since each block is made of 4 and only 4 points, we can check both here
    		for (int i = 0; i < this.body.length; i++) {
    			tmp.add(this.body[i]);
    			tmp.add(((Point[])other)[i]);
    		}

    		return tmp.size() == 4;
        }

       return false;
    }
}
