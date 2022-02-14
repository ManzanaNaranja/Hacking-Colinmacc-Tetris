package tetris.structure;

import tetris.Move;
import tetris.piece.PieceInstance;

public interface GameInfo {
	public boolean game_over();
	public int[][] board();
	public void ascii();
	public PieceInstance next_piece();
	public PieceInstance current_piece();
	public int get(int x, int y);
	public Move[] moves();
	public int lines_cleared();
	
}
