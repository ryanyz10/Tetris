package testing;

import static org.junit.Assert.*;

import org.junit.Test;

import assignment.Board;
import assignment.JTetris;
import assignment.Piece;
import assignment.TetrisBoard;
import assignment.TetrisPiece;
import testing.PieceConstants;

/**
 * Class to test board functionality.
 * Includes tests for every interface function with the goal of checking
 * both negative and positive test results
 *
 */
public class TetrisBoardTest implements PieceConstants{

	@Test
	public void invalidBoardWidthTest() {
		try {
			new TetrisBoard(-1, 10);
			fail("Didn't throw illegal width");
		} catch (IllegalArgumentException iae) {
			assertNotNull("Successfully caught argument error");
		} catch (Exception e) {
			fail("Didn't catch illegal width correctly");
		}
	}
	
	@Test
	public void invalidBoardHeightTest() {
		try {
			new TetrisBoard(10, -1);
			fail("Didn't throw illegal height");
		} catch (IllegalArgumentException iae) {
			assertNotNull("Successfully caught argument error");
		} catch (Exception e) {
			fail("Didn't catch illegal height correctly");
		}
	}
	
	@Test
	public void invalidBoardTotalTest() {
		try {
			new TetrisBoard(-1, -1);
			fail("Didn't throw illegal height/width");
		} catch (IllegalArgumentException iae) {
			assertNotNull("Successfully caught argument error");
		} catch (Exception e) {
			fail("Didn't catch illegal height/width correctly");
		}
	}
	
	@Test
	public void nullMoveTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		assertEquals(tetris.move(Board.Action.DOWN), Board.Result.NO_PIECE);
	}
	
	@Test
	public void moveLeftValidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.LEFT), Board.Result.SUCCESS);
	}
	
	@Test
	public void moveLeftInvalidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		//All 3 should be valid
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.LEFT);
		
		assertEquals(tetris.move(Board.Action.LEFT), Board.Result.OUT_BOUNDS);
	}
	
	@Test
	public void moveRightValidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.RIGHT), Board.Result.SUCCESS);
	}
	
	@Test
	public void moveRightInvalidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		//All 3 should be valid
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.RIGHT);
		
		assertEquals(tetris.move(Board.Action.RIGHT), Board.Result.OUT_BOUNDS);
	}

	@Test
	public void moveDownValidTest() { 
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.DOWN), Board.Result.SUCCESS);
	}
	
	//Check if movement to bottom results in placement
	@Test
	public void moveDownEnitreValidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.tShape);
		tetris.nextPiece(iP);
		for(int y = 0; y<JTetris.HEIGHT - 4; y++)
			tetris.move(Board.Action.DOWN);
		
		assertEquals(tetris.move(Board.Action.DOWN), Board.Result.PLACE);
	}
	
	@Test
	public void moveDownEntireInvalidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		for(int y = 0; y<=JTetris.HEIGHT; y++)
			tetris.move(Board.Action.DOWN);
		
		assertEquals(tetris.move(Board.Action.DOWN), Board.Result.NO_PIECE);
	}
	
	@Test
	public void dropDownTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);

		assertEquals(tetris.move(Board.Action.DROP), Board.Result.PLACE);
	}
	
	@Test
	public void dropOnPieceTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.leftZShape);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.DROP), Board.Result.PLACE);
	}
	
	@Test 
	public void clockWiseBasicTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.CLOCKWISE), Board.Result.SUCCESS);
	}
	
	@Test 
	public void clockWiseWallKickValidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		//The Setup
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		for(int y = 0; y<JTetris.HEIGHT-4; y++)
			tetris.move(Board.Action.DOWN);
		
		assertEquals(tetris.move(Board.Action.CLOCKWISE), Board.Result.SUCCESS);
	}
	
	@Test 
	public void clockWiseWallKickInvalidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		//The Setup
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		
		
		
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		for(int y = 0; y<JTetris.HEIGHT-4; y++)
			tetris.move(Board.Action.DOWN);
		
		assertEquals(tetris.move(Board.Action.CLOCKWISE), Board.Result.OUT_BOUNDS);
	}
	
	@Test 
	public void counterClockWiseBasicTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.COUNTERCLOCKWISE), Board.Result.SUCCESS);
	}
	
	@Test 
	public void counterClockWiseWallKickValidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		//The Setup
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		for(int y = 0; y<JTetris.HEIGHT-4; y++)
			tetris.move(Board.Action.DOWN);
		
		assertEquals(tetris.move(Board.Action.COUNTERCLOCKWISE), Board.Result.SUCCESS);
	}
	
	@Test 
	public void counterClockWiseWallKickInvalidTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		//The Setup
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		
		
		iP = TetrisPiece.getPiece(PieceConstants.iShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		for(int y = 0; y<JTetris.HEIGHT-4; y++)
			tetris.move(Board.Action.DOWN);
		
		assertEquals(tetris.move(Board.Action.COUNTERCLOCKWISE), Board.Result.OUT_BOUNDS);
	}
	
	@Test
	public void nothingTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		assertEquals(tetris.move(Board.Action.NOTHING), Board.Result.SUCCESS);
	}
	
	@Test
	public void testMoveBasicTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		Board secondaryTetris = tetris.testMove(Board.Action.DROP);
		tetris.move(Board.Action.DOWN);
		//Triple-Check
		assertNotEquals(secondaryTetris.getLastAction(), tetris.getLastAction());
		assertEquals(secondaryTetris.getLastAction(), Board.Action.DROP);
		assertEquals(secondaryTetris.getLastResult(), Board.Result.PLACE);
	}
	
	@Test
	public void testMoveAdvancedTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.leftZShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.RIGHT);
		//tetris.move(Board.Action.DROP);
		Board testBoard = tetris.testMove(Board.Action.DROP);

		//Double-Check
		assertEquals(testBoard.getRowsCleared(), 1);
		assertEquals(tetris.getRowsCleared(), 0);
	}
	
	@Test
	public void getRowWidthTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.tShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.COUNTERCLOCKWISE);
		tetris.move(Board.Action.DROP);
		
		assertEquals(tetris.getRowWidth(0), 2);
		assertEquals(tetris.getRowWidth(1), 3);
		assertEquals(tetris.getRowWidth(3), 1);
	}
	
	@Test
	public void getColumnHeightTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.DROP);
		
		assertEquals(tetris.getColumnHeight(4), 1);
	}
	
	@Test
	public void getColumnHeightEmptyTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		
		assertEquals(tetris.getColumnHeight(4), 0);
	}
	
	@Test
	public void getWidthBoardTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		assertEquals(tetris.getWidth(), JTetris.WIDTH);
	}
	
	@Test
	public void getHeightBoardTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		assertEquals(tetris.getHeight(), JTetris.HEIGHT);
	}

	@Test
	public void getMaxHeightTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.CLOCKWISE);
		tetris.move(Board.Action.LEFT);
		tetris.move(Board.Action.DROP);
		
		iP = TetrisPiece.getPiece(PieceConstants.leftZShape);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.RIGHT);
		tetris.move(Board.Action.DROP);
		
		assertEquals(tetris.getMaxHeight(), 4);
	}

	@Test
	public void getMaxHeightEmptyTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		assertEquals(tetris.getMaxHeight(), 0);
	}

	@Test
	public void dropHeightFloorTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		assertEquals(tetris.dropHeight(iP, 0), 0);
	}
	
	@Test
	public void dropHeightBlockTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.DROP);
		iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		assertEquals(tetris.dropHeight(iP, JTetris.WIDTH/2), 1);
	}
	
	@Test
	public void getGridEmptyTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		assertFalse(tetris.getGrid(0, 0));
	}
	
	@Test
	public void getGridFilledTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.DROP);
		assertTrue(tetris.getGrid(4, 0));
	}
	
	@Test
	public void getGridOutBoundsTest() {
		Board tetris = new TetrisBoard(JTetris.WIDTH, JTetris.HEIGHT);
		Piece iP = TetrisPiece.getPiece(PieceConstants.iShapeDeclaration);
		tetris.nextPiece(iP);
		tetris.move(Board.Action.DROP);
		assertTrue(tetris.getGrid(-1, -1));
	}
}