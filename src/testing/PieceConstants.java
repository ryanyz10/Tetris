package testing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Interface for use in testing.
 * Includes definitions of pieces and their rotations for easy checking/creation
 * during testing.
 * Also includes two default methods taken from two different class to allow changes to be
 * made for testing sake, and to provide easy access for the testing methods.
 *
 */
public interface PieceConstants {
	
	//How 'I' is passed
	String iShapeDeclaration= "0 0  1 0  2 0  3 0";
	//How we need 'I'
	//This correction occurs in first constructor
	String iShape 			= "0 1  1 1  2 1  3 1";
	String iShape90 		= "2 0  2 1  2 2  2 3";
	String iShape180 		= "0 2  1 2  2 2  3 2";
	String iShape270 		= "1 0  1 1  1 2  1 3";
	
	
	//Right L Rotating
	String rightLShape 		= "0 1  1 1  2 1  2 0";
	String rightLShape90 	= "1 0  1 1  1 2  2 2";
	String rightLShape180 	= "0 2  0 1  1 1  2 1";
	String rightLShape270 	= "0 0  1 0  1 1  1 2";
	
	//Left L Rotating
	String leftLShape 		= "0 0  0 1  1 1  2 1";
	String leftLShape90 	= "1 0  2 0  1 1  1 2";
	String leftLShape180 	= "0 1  1 1  2 1  2 2";
	String leftLShape270 	= "0 2  1 2  1 1  1 0";
	
	//Right Z Rotating
	String rightZShape 		= "0 0  1 0  1 1  2 1";
	String rightZShape90 	= "2 0  1 1  1 2  2 1";
	String rightZShape180 	= "0 1  1 1  1 2  2 2";
	String rightZShape270 	= "0 1  0 2  1 1  1 0";
	
	//Left Z Rotating
	String leftZShape 		= "0 1  1 1  1 0  2 0";
	String leftZShape90 	= "1 0  1 1  2 1  2 2";
	String leftZShape180 	= "0 2  1 2  1 1  2 1";
	String leftZShape270 	= "0 0  0 1  1 1  1 2";
	
	//Square Rotating
	String squareShape 		= "0 0  0 1  1 0  1 1";
	
	//T Rotating
	String tShape 			= "0 1  1 0  1 1  2 1";
	String tShape90 		= "1 0  1 1  1 2  2 1";
	String tShape180 		= "0 1  1 1  2 1  1 2";
	String tShape270 		= "0 1  1 2  1 1  1 0";
	
	/**
	 * Implementation taken from TetrisPiece.java
	 */
	default boolean pointEquals(Point[] shapeOut, Point[] shapeExpected) {
		Set<Point> tmp = new HashSet<Point>();
		// since each block is made of 4 and only 4 points, we can check both here
		for (int i = 0; i < shapeOut.length; i++) {
			tmp.add(shapeOut[i]);
			tmp.add(shapeExpected[i]);
		}

		return tmp.size() == 4;
	}
	
	/**
	 * Implementation taken from Piece.java
	 * @param string
	 * @return
	 */
	default Point[] parsePoints(String string) {
        ArrayList<Point> points = new ArrayList<>();
        StringTokenizer tok = new StringTokenizer(string);
        try {
            while(tok.hasMoreTokens()) {
                int x = Integer.parseInt(tok.nextToken());
                int y = Integer.parseInt(tok.nextToken());

                points.add(new Point(x, y));
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Could not parse x,y string:" + string);
        }

        Point[] array = new Point[0];
        array = points.toArray(array);
        return array;
    }
}
