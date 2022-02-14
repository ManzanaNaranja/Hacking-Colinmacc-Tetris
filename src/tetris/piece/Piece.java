package tetris.piece;

import java.awt.Color;
import java.util.ArrayList;

import helpers.AVector;

public enum Piece {

	
	I(new int[] {0x0F00, 0x2222, 0x0F00, 0x2222}, 1),
	J(new int[] {0x8E00, 0x6440, 0x0E20, 0x44C0}, 2),
	L(new int[] {0x2E00, 0x4460, 0x0E80, 0xC440}, 3),
	O(new int[] {0xCC00, 0xCC00, 0xCC00, 0xCC00}, 4),
	S(new int[] {0x6C00, 0x4620, 0x6C00, 0x4620}, 5),
	T(new int[] {0x4E00, 0x4640, 0x0E40, 0x4C40}, 6),
	Z(new int[] {0xC600, 0x2640, 0xC600, 0x2640}, 7);
	private final int[] blocks;
	private int value;
	
	Piece(int[] blocks, int value) {
		this.blocks = blocks;
		this.value = value;
	}
	
	public int[] getBlocks() {
		return blocks;
	}
	
	public AVector[] getPoints(int rotation) {
		ArrayList<AVector> list = new ArrayList<AVector>();
		int blocks = this.getBlocks()[rotation];
		int row = 0;
		int col = 0;
		for(int bit = 0x8000;  bit > 0; bit = bit >> 1) {
			if((blocks & bit) != 0) {
				list.add(new AVector(col, row));
			}
			if(++col == 4) {
				col = 0;
				row++;
			}
		}
		
		AVector[] a = new AVector[list.size()];
		for(int i = 0; i < a.length; i++) {
			a[i] = list.get(i);
		}
		return a;
	}
	
	public static Piece getPieceByValue(int val) {
		for(Piece p : Piece.values()) {
			if(p.value == val) return p;
		}
		return null;
		
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String toString(int index) {
		String res = String.format("%16s", Integer.toBinaryString(blocks[index])).replace(' ', '0');
		String[] arr = res.split("");
		for(int i = 3; i < res.length(); i+=4) {
			arr[i] = arr[i] + "\n";
		}
		return String.join("", arr);
	}
}
