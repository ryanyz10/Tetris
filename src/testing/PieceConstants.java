package testing;

public interface PieceConstants {
	
	/*
	 * Not perfect, implementation in progress
	 */
	String iShape 			= "0 0  1 0  2 0  3 0";
	String iShape90 		= "0 0  1 0  2 0  3 0";
	String iShape180 		= "0 0  1 0  2 0  3 0";
	String iShape270 		= "0 0  1 0  2 0  3 0";
	
	
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
}
