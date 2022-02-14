import java.awt.AWTException;
import java.awt.Robot;
import java.util.ArrayList;

import tetris.Tetris;
import tetris.piece.Piece;;

public class Main {
	public Logic logic;
	public Robot robot;
	public ArrayList<Integer> keys;
	public int i = 0;
	public Main() {
		keys = new ArrayList<Integer>();
		try {
			robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logic = new Logic();
		loop(); 
	}
	
	public void loop() {
		double MS_PER_UPDATE = 80;
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
	
	void update() {
		if(keys.isEmpty()) {
			keys = logic.getKeyPresses();
			System.out.println(logic.t);
		}
		if(keys.size() >= 1) {
			if(i++%2==0) {
				robot.keyPress(keys.get(0));
			} else {
				robot.keyRelease(keys.get(0));
				keys.remove(0);
			}
		}
	}
	
	public static void main(String[] args) {
		new Main();
//		Logic l = new Logic();
//		ArrayList<Integer> e = l.getKeyPresses(Piece.L,Piece.L);
//		System.out.println(e);
//		System.out.println(l.t);
//		
//		ArrayList<Integer> e2 = l.getKeyPresses(Piece.L,Piece.L);
//		System.out.println(e2);
//		System.out.println(l.t);

	}

}
