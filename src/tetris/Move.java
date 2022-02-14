package tetris;

import helpers.AVector;
import tetris.piece.Piece;

public final class Move {
	private int x, y, rotation;
	private Piece piece;
	public double score;
	
	public Move(int x, int y, int rotation, Piece piece) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
		this.piece = piece;
	}
	
	public AVector[] getPoints() {
		AVector[] pts = this.getPiece().getPoints(this.getRotation());
		for(int i = 0; i < pts.length; i++) {
			pts[i].add(new AVector(this.getX(), this.getY()));
		}
		return pts;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getRotation() {
		return rotation;
	}

	public Piece getPiece() {
		return piece;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == this) return true;
		if(!(other instanceof Move)) return false;
		Move o = (Move) other;
		return o.rotation == this.rotation && o.x == this.x && o.y == this.y && o.piece == this.piece;
	}
}
