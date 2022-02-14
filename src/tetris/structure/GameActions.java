package tetris.structure;

import tetris.Move;

public interface GameActions {
	public int move(Move m);
	public void undo();
	public void reset();
}
