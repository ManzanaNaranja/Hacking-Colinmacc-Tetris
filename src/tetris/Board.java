package tetris;

import helpers.AUtils;
import tetris.piece.PieceInstance;

public class Board {
	public static final int ROW_START = 2;
	public static final int ROWS = 22;
	public static final int COLS = 10;
	private int[][] memory;
	private int[][] backupMemory;
	public Board() {
		this.reset();
	}
	
	public Board(int[][] memory) {
		this.memory = memory;
		this.backupMemory = AUtils.deepCopy(memory);
	}
	
	public void reset() {
		this.memory = new int[Board.ROWS][Board.COLS];
		this.backupMemory = new int[Board.ROWS][Board.COLS];
	}
	
	public String toString() {
		String res = "";
		for(int[] row : memory) {
			for(int item : row) {
				res += item;
			}
			res += "\n";
		}
		return res;
	}
	
	public int clearLines() {
		int lines = 0;
		for(int i = 0; i < memory.length; i++) {
			for(int j = 0; j < memory[0].length; j++) {
				if(memory[i][j] == 0) break; // don't need to clear this line;
				else if(j == memory[0].length - 1) { // reached end 
					clearLine(i);
					slideBlocksDown(i);
					lines++;
				}
			}
		}
		
		return lines;
	}
	
	private void clearLine(int row) {
		for(int i = 0; i < memory[0].length; i++) {
			memory[row][i] = 0;
		}
	}
	
	private void slideBlocksDown(int row) { // row to shift to
		for(int i = row-1; i >= 0; i--) {
			for(int j = 0; j < memory[0].length; j++) {
				memory[i+1][j] = memory[i][j];
			}
		}
	}
	
	public int[][] getMemory() {
		return this.memory;
	}
	
	public int[][] getBackupMemory() {
		return this.backupMemory;
	}
	
	public int getBlock(int x, int y) {
		return this.memory[y][x];
	}	
	
	public boolean occupied(PieceInstance p, int x, int y) {
		int row = 0;
		int col = 0;
		var result = false;
		for(int bit = 0x8000;  bit > 0; bit = bit >> 1) {
			if((p.getBlocks() & bit) != 0) {
				int ypos = y+row;
				int xpos = x+col;
				if(xpos < 0 || xpos >= Board.COLS || ypos < 0 || ypos >= Board.ROWS || getBlock(xpos, ypos) != 0) {
					result = true;
				}
			}
			if(++col == 4) {
				col = 0;
				row++;
			}
		}
		return result;
	}
	
	public void commit() {
		this.backupMemory = AUtils.deepCopy(memory);
	}
	
	public void undo() {
		this.memory = AUtils.deepCopy(backupMemory);
	}

	public int dropHeight(PieceInstance p, int x) {
		int y = 0;
		boolean res = true;
		while(res == true) {
			res = this.place(p, x, ++y);
			this.undo();
		}
		return --y;
	}
	
	public boolean place(PieceInstance p, int x, int y) {
		if(this.occupied(p, x, y)) return false;
		int row = 0;
		int col = 0;
		for(int bit = 0x8000;  bit > 0; bit = bit >> 1) {
			if((p.getBlocks() & bit) != 0) {
				this.memory[y + row][x + col] = p.getPiece().getValue();
			}
			if(++col == 4) {
				col = 0;
				row++;
			}
		}
		return true;
		
	}
	public boolean place(Move m) {
		PieceInstance p = new PieceInstance(m.getPiece());
		p.rotation = m.getRotation();
		if(this.occupied(p, m.getX(), m.getY())) return false;
		int row = 0;
		int col = 0;
		for(int bit = 0x8000;  bit > 0; bit = bit >> 1) {
			if((p.getBlocks() & bit) != 0) {
				this.memory[m.getY() + row][m.getX() + col] = p.getPiece().getValue();
			}
			if(++col == 4) {
				col = 0;
				row++;
			}
		}
		return true;
		
	}
}
