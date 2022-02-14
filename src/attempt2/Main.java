package attempt2;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import tetris.Move;
import tetris.Tetris;
import tetris.piece.Piece;
import tetris.piece.PieceInstance;

public class Main {
	
	static Tetris t = new Tetris();
	static Robot r;
	static AI ai = new AI();
	static Piece curr = null;
	static Piece next = null;
	static int i = 0;
	static boolean JUST_PRESSED_P = false;
	static boolean JUST_PRESSED_O = false;
	static ArrayList<Integer> keypresses = new ArrayList<Integer>();
	static double MS_PER_UPDATE = 65;

	public static void main(String[] args) {
		try {
			Main.r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		loop();
	}
	
	public static Piece currPiece() {
		ARobot ar = new ARobot();
		Color c = ar.pixelGetColor(Settings.CURR_PIECE_X, Settings.CURR_PIECE_Y);
		return getCorrespondingPiece(c);
	}
	
	public static Piece nextPiece() {
		ARobot ar = new ARobot();
		Color c = ar.pixelGetColor(Settings.NEXT_PIECE_X, Settings.NEXT_PIECE_Y);
		return getCorrespondingPiece(c);
	}
	
	public static void update() {
//		if(i++%2 == 0) {
//			r.keyPress(KeyEvent.VK_P);
//		} else {
//			r.keyRelease(KeyEvent.VK_P);
//		}
		if(keypresses.size() == 0) {
			refillKeypresses();
			if(curr != null) {
				System.out.println(curr + " " + next);
				System.out.println(t);
			}
		} else {
			if(i++%2==0) {
				// press key
				r.keyPress(keypresses.get(0));
			} else {
				// release and remove key
				r.keyRelease(keypresses.get(0));
				keypresses.remove(0);
			}
		}
		
	}
	
	public static void refillKeypresses() {
		if(curr == null) {
			curr = Main.currPiece();
			next = Main.nextPiece();
		} else {
			curr = next;
			next = Main.nextPiece();
		}
		if(curr == null) return;
		t.setCurrentPiece(new PieceInstance(curr));
		t.setNextPiece(new PieceInstance(next));
		fixPieceSpawn(t);
		Move m = ai.bestMove(t);
		Main.fixAfterAIGetsMove(t);
		keypresses = Main.getKeyPresses(t, m);
		t.move(m);
	}
	
	public static ArrayList<Integer> getKeyPresses(Tetris t, Move bestMove) {
		ArrayList<Integer> keypresses = new ArrayList<Integer>();
		keypresses.add(KeyEvent.VK_S);
		int a = bestMove.getRotation();
		int b = t.current_piece().rotation;
		while(a != b) {
			keypresses.add(KeyEvent.VK_W);
			b++;
			if(b == 4) b = 0;
		}
		
		int dist = bestMove.getX() - t.current_piece().position.x;
		if(t.current_piece().position.x < bestMove.getX()) {
			for(int i = 0; i < dist; i++) {
				keypresses.add(KeyEvent.VK_D);
			}
		} else if(t.current_piece().position.x > bestMove.getX()) {
			for(int i = 0; i < -1*dist; i++) {
				keypresses.add(KeyEvent.VK_A);
			}
		}
		keypresses.add(KeyEvent.VK_SPACE);
		return keypresses;
	}
	
	public static void fixAfterAIGetsMove(Tetris t) {
		t.board.place(t.current_piece(), t.current_piece().position.x, t.current_piece().position.y);
		fixPieceSpawn(t);
	}
	
	public static void fixPieceSpawn(Tetris t) {
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
	
	public static void loop() {
		double prev = System.currentTimeMillis();	
		double lag = 0.0;
		while(true) {
			double curr = System.currentTimeMillis();
			double elapsed = curr - prev;
			prev = curr;
			lag += elapsed;  	
			while(lag >= MS_PER_UPDATE) {
				update();
				lag -= MS_PER_UPDATE;
			}
		}
	}
	
	static Piece getCorrespondingPiece(Color c) {
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

}
