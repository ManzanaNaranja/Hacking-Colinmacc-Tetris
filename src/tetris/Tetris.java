package tetris;

import java.util.ArrayList;

import helpers.AUtils;
import helpers.AVector;
import tetris.piece.Bag;
import tetris.piece.PieceInstance;
import tetris.structure.GameActions;
import tetris.structure.GameInfo;
import tetris.structure.PlayerActions;

public class Tetris implements GameActions, PlayerActions, GameInfo{
	
	public Bag bag;
	private PieceInstance currentPiece;
	private BoardHistory boardHistory;
	public Board board;
	private int linesCleared = 0;
	private boolean gameOver = false;
	private TetrisEventListener tetrisEventListener = null;
	private boolean debug = false;
	
	public Tetris() {
		this.reset();
	}

	@Override
	public boolean game_over() {
		return this.gameOver;
	}

	@Override
	public int[][] board() {
		return board.getMemory();
	}
	
	public int[][] clean_board() {
		return board.getBackupMemory();
	}


	@Override
	public void ascii() {
		System.out.println(this.toString());		
	}
	
	public PieceInstance current_piece() {
		return this.currentPiece;
	}
	
	public void setCurrentPiece(PieceInstance p) {
		this.currentPiece = p;
		this.bag.changeCurrent(p);
		board.undo();
		board.place(this.currentPiece, this.currentPiece.position.x, this.currentPiece.position.y);
	}
	
	public void setNextPiece(PieceInstance p) {
		this.bag.changeNext(p);
	}

	@Override
	public PieceInstance next_piece() { 
		return bag.view_next();
	}
	
	private PieceInstance pick_next_piece() {
		return bag.pick();
	}

	@Override
	public int get(int x, int y) {
		return board.getBlock(x, y);
	}

	@Override
	public Move[] moves() {
		board.undo();
		int xx = currentPiece.position.x;
		int yy = currentPiece.position.y;
		ArrayList<Move> moves = new ArrayList<Move>();
		int xBound = Board.COLS - currentPiece.getWidth()+1;
		int shift = currentPiece.getGap();
		for(int spin = 0; spin < 4; spin++) {
			xBound = Board.COLS - currentPiece.getWidth()+1;
			shift = currentPiece.getGap();
			for(int x = 0-shift; x < xBound-shift; x++) {
				int y = board.dropHeight(currentPiece, x);
				Move move = new Move(x, y, currentPiece.rotation, currentPiece.getPiece());
				AVector[] a = move.getPoints();
				boolean addMove = true;
					for(AVector point : a) {
						if(point.y <= 1) {
							addMove = false;
							break;
						}
					}
				if(addMove) moves.add(move);
				
			}
			currentPiece.rotate();
		}
		Move[] m = new Move[moves.size()];
		for(int i = 0; i < m.length; i++) {
			m[i] = moves.get(i);		
		}
		
		this.board.place(currentPiece,xx,yy);
		return m;
	}

	@Override
	public int lines_cleared() {
		return this.linesCleared;
	}

	@Override
	public void right() {
		if(gameOver == true) return;
		board.undo();
		boolean placement = board.place(currentPiece, ++currentPiece.position.x, currentPiece.position.y);
		if(placement == false) {
			board.place(currentPiece, --currentPiece.position.x, currentPiece.position.y);
		}	
	}

	@Override
	public void left() {
		if(gameOver == true) return;
		board.undo();
		boolean placement = board.place(currentPiece, --currentPiece.position.x, currentPiece.position.y);
		if(placement == false) {
			board.place(currentPiece, ++currentPiece.position.x, currentPiece.position.y);
		}
	}

	@Override
	public void down() {
		if(this.game_over() == true) return;
		board.undo();
		boolean result = board.place(currentPiece, currentPiece.position.x, ++currentPiece.position.y);
		
		if(result == false) {
			boolean upplacement = board.place(currentPiece, currentPiece.position.x, --currentPiece.position.y);
			if(upplacement == false) {
				this.setGameOver();
			} else {
				this.finalizePlacement();
			}
			
		}
	}

	@Override
	public void rotate() {
		if(gameOver == true) return;
		board.undo();
		currentPiece.rotate();
		boolean placement = board.place(currentPiece, currentPiece.position.x, currentPiece.position.y);
		if(placement == false) {
			currentPiece.unrotate();
			board.place(currentPiece, currentPiece.position.x, currentPiece.position.y);
		}		
	}

	@Override
	public void drop() {
		if(gameOver == true) return;
		boolean result = false;
		do {
			board.undo();
			result = board.place(currentPiece, currentPiece.position.x, ++currentPiece.position.y);
		} while(result);
		board.undo();
		board.place(currentPiece, currentPiece.position.x, --currentPiece.position.y);
		this.finalizePlacement();
	}

	
	public int move(Move m, String flag) {
		if(gameOver == true) return -1;
//		Move[] moves = this.moves();
//		for(int i = 0; i < moves.length; i++) {
//			if(m.equals(moves[i])) break;
//			else if(i == moves.length-1) return -1;
//		}
		board.undo();
		if(board.place(m) == false) {
			if(flag == "allowend") this.setGameOver();
			return -1;
		}
		if(flag == "noevent") {
			return this.finalizePlacement(false);
		}
		return this.finalizePlacement();
	}
	
	@Override
	public int move(Move m) {
		return this.move(m, "");
	}
	
	

	@Override
	public void undo() { 
		if(gameOver == true) return;
		if(boardHistory.undo() == null) return;
		this.bag.undo();
		this.currentPiece = bag.view_prev();
		this.board = new Board(AUtils.deepCopy(boardHistory.current_state().board));
		this.linesCleared = boardHistory.current_state().lines;

	}

	@Override
	public void reset() {
		this.initilizeGame();
		board.place(currentPiece, currentPiece.position.x, currentPiece.position.y);
	}
	
	public String toString() {
		return this.board.toString();
	}
	
	private void initilizeGame() {
		boardHistory = new BoardHistory();
		bag = new Bag();
		this.currentPiece = this.pick_next_piece();
		this.board = new Board();
		boardHistory.add(new BoardData(this.board.getMemory(), this.linesCleared));
	}
	
	public int finalizePlacement() {
		return this.finalizePlacement(true);
	}
	
	private int finalizePlacement(boolean shouldCallEvent) {
		if(this.game_over() == true) return -1;
		int lines = board.clearLines();
		this.linesCleared += lines;
		
		board.commit();
		this.currentPiece = this.pick_next_piece();	
		boardHistory.add(new BoardData(this.board.getMemory(), this.linesCleared));
		boolean newPlacement = board.place(currentPiece, currentPiece.position.x, currentPiece.position.y);
		if(newPlacement == false) {
			this.setGameOver();
		}
		if(shouldCallEvent && this.tetrisEventListener != null) {
			if(lines != 0) {
				tetrisEventListener.linesCleared(lines);
			}
			
		}
		return lines;
	}
	
	public void addEventListener(TetrisEventListener t) {
		this.tetrisEventListener = t;
	}
	
	public boolean isDebugOn() {
		return this.debug;
	}
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	private void setGameOver() {
		board.commit();
		this.gameOver = true;
		if(this.debug) {
			System.out.println("GAME_OVER");
		}
	}
}
