package testing;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.junit.Test;

import assignment.Piece;
import assignment.TetrisPiece;

public class PieceTest implements PieceConstants {
	
	/**
	 * I decided to put multiple assertions in one test in this scenario
	 * because it made sense to me. I didn't think it was a good idea to split
	 * up rotation testing because to get certain rotations, I would need to rotate
	 * through others
	 */
	@Test
	public void iShapeRotationTest() {
		Piece block = TetrisPiece.getPiece("0 0  1 0  2 0  3 0");

		//90 Degrees
		block = block.nextRotation();
		Point[] blockBody = block.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.iShape90);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//180 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.iShape180);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//270 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.iShape270);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//360 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.iShape);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//Make sure the rotation wraps
		//90 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.iShape90);
		assertTrue(this.pointEquals(blockBody, correctBody));
	}
	
	@Test
	public void rightLShapeRotationTest() {
		Piece rlPiece = TetrisPiece.getPiece(PieceConstants.rightLShape);

		//90 Degrees
		rlPiece = rlPiece.nextRotation();
		Point[] rlBody = rlPiece.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.rightLShape90);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//180 Degrees
		rlPiece = rlPiece.nextRotation();
		rlBody = rlPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.rightLShape180);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//270 Degrees
		rlPiece = rlPiece.nextRotation();
		rlBody = rlPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.rightLShape270);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//360 Degrees
		rlPiece = rlPiece.nextRotation();
		rlBody = rlPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.rightLShape);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//Make sure the rotation wraps
		//90 Degrees
		rlPiece = rlPiece.nextRotation();
		rlBody = rlPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.rightLShape90);
		assertTrue(this.pointEquals(rlBody, correctBody));
	}
	
	@Test
	public void leftLShapeRotationTest() {
		Piece llPiece = TetrisPiece.getPiece(PieceConstants.leftLShape);

		//90 Degrees
		llPiece = llPiece.nextRotation();
		Point[] rlBody = llPiece.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.leftLShape90);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//180 Degrees
		llPiece = llPiece.nextRotation();
		rlBody = llPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.leftLShape180);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//270 Degrees
		llPiece = llPiece.nextRotation();
		rlBody = llPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.leftLShape270);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//360 Degrees
		llPiece = llPiece.nextRotation();
		rlBody = llPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.leftLShape);
		
		assertTrue(this.pointEquals(rlBody, correctBody));
		
		//Make sure the rotation wraps
		//90 Degrees
		llPiece = llPiece.nextRotation();
		rlBody = llPiece.getBody();
		correctBody = this.parsePoints(PieceConstants.leftLShape90);
		assertTrue(this.pointEquals(rlBody, correctBody));
	}
	
	@Test
	public void rightZShapeRotationTest() {
		Piece block = TetrisPiece.getPiece(PieceConstants.rightZShape);

		//90 Degrees
		block = block.nextRotation();
		Point[] blockBody = block.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.rightZShape90);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//180 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.rightZShape180);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//270 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.rightZShape270);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//360 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.rightZShape);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//Make sure the rotation wraps
		//90 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.rightZShape90);
		assertTrue(this.pointEquals(blockBody, correctBody));
	}
	
	@Test
	public void leftZShapeRotationTest() {
		Piece block = TetrisPiece.getPiece(PieceConstants.leftZShape);

		//90 Degrees
		block = block.nextRotation();
		Point[] blockBody = block.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.leftZShape90);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//180 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.leftZShape180);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//270 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.leftZShape270);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//360 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.leftZShape);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//Make sure the rotation wraps
		//90 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.leftZShape90);
		assertTrue(this.pointEquals(blockBody, correctBody));
	}
	
	@Test
	public void squareShapeRotationTest() {
		Piece block = TetrisPiece.getPiece(PieceConstants.squareShape);

		//Should always be itself
		block = block.nextRotation();
		Point[] blockBody = block.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.squareShape);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
	}
	
	@Test
	public void tShapeRotationTest() {
		Piece block = TetrisPiece.getPiece(PieceConstants.tShape);

		//90 Degrees
		block = block.nextRotation();
		Point[] blockBody = block.getBody();
		Point[] correctBody = this.parsePoints(PieceConstants.tShape90);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//180 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.tShape180);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//270 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.tShape270);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//360 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.tShape);
		
		assertTrue(this.pointEquals(blockBody, correctBody));
		
		//Make sure the rotation wraps
		//90 Degrees
		block = block.nextRotation();
		blockBody = block.getBody();
		correctBody = this.parsePoints(PieceConstants.tShape90);
		assertTrue(this.pointEquals(blockBody, correctBody));
	}
}
