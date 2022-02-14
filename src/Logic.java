import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import tetris.Move;
import tetris.Tetris;
import tetris.piece.Piece;
import tetris.piece.PieceInstance;

public class Logic {
	Piece curr, next = null;
	Input in;
	public Tetris t;
	AI ai;
	ArrayList<Integer> keyPresses;
	
	public Logic() {
		in = new Input();
		t = new Tetris();
		ai = new AI();
		keyPresses = new ArrayList<Integer>();
	}
	ArrayList<Integer> getKeyPresses(Piece curr, Piece next) {
		keyPresses.clear();
		if(curr == null) return keyPresses; 
		t.setCurrentPiece(new PieceInstance(curr));
		t.setNextPiece(new PieceInstance(next));
		fixPieceSpawn();
		keyPresses.add(KeyEvent.VK_S);
		
		PieceInstance fallingP = t.current_piece();
		int cx = fallingP.position.x;
		int cr = fallingP.rotation;
		Move currMove  = ai.bestMove(t);
		
		
		while(cr != currMove.getRotation()) {
			keyPresses.add(KeyEvent.VK_W);
			t.rotate();
		}
		int fx = currMove.getX();
		
		if(fx < cx) {
			for(int i = cx; i > fx; i--) {
				keyPresses.add(KeyEvent.VK_A);
				t.left();
			}
		} else {
			for(int i = cx; i < fx; i++) {
				keyPresses.add(KeyEvent.VK_D);
				t.right();
			}
		}
		keyPresses.add(KeyEvent.VK_SPACE);
		t.drop();
		return keyPresses;
		
	}
	
	ArrayList<Integer> getKeyPresses() {
		ArrayList<Integer> keyPresses = new ArrayList<Integer>();
		if(curr == null) {
			curr = getCurrPiece();
			next = getNextPiece();			
		} else {
			curr = next;
			next = getNextPiece();
		}
		if(curr == null) {
			return keyPresses;
		}
		return getKeyPresses(curr, next);

	}
	
	Piece getCurrPiece() {
		Color c = in.pixelGetColor(Settings.CURR_PIECE_X, Settings.CURR_PIECE_Y);
		return getCorrespondingPiece(c);
	}
	
	Piece getNextPiece() {
		Color c = in.pixelGetColor(Settings.NEXT_PIECE_X, Settings.NEXT_PIECE_Y);
		return getCorrespondingPiece(c);
	}
	
	Piece getCorrespondingPiece(Color c) {
		if(c.equals(Settings.I)) {
			return Piece.I;
		} 
		if(c.equals(Settings.J)) {
			return Piece.J;
		} 
		if(c.equals(Settings.L)) {
			return Piece.L;
		} 
		if(c.equals(Settings.O)) {
			return Piece.O;
		} 
		if(c.equals(Settings.Z)) {
			return Piece.Z;
		} 
		if(c.equals(Settings.S)) {
			return Piece.S;
		} 
		if(c.equals(Settings.T)) {
			return Piece.T;
		} 
		return null;
	}
	
	public void fixPieceSpawn() {
		if(t.current_piece().getPiece() == Piece.J || t.current_piece().getPiece() == Piece.L) {
			t.rotate();
			t.rotate();
		}
		if(t.current_piece().getPiece() == Piece.I) {
			t.left();
		}
		if(t.current_piece().getPiece() == Piece.T) {
			t.rotate();
			t.rotate();
		} 
	}
	
	
}
