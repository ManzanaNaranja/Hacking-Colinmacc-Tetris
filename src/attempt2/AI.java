package attempt2;
import tetris.Board;
import tetris.Move;
import tetris.Tetris;

public class AI {
	private double w1 = -0.22568649650722883;
	private double w2 = 0.08679520494876472;
	private double w3 = 0.6152727732730796;
	private double w4 = -0.15842464424735841;
	private double w5 = 0.15452215909537684;
	private double w6 = 0.021586109522043928;

	public Move bestMove(Tetris game) {
		double bestScore = 10000000;
		Move bestMove = null;
		for(Move move : game.moves()) {
			int result = game.move(move, "noevent");
			for(Move m2 : game.moves()) {
				int linesCleared = game.move(m2, "noevent");
				double eval = this.evaluateBoard(linesCleared, game.clean_board());
				if(eval < bestScore) {
					bestScore = eval;
					bestMove = move;
					bestMove.score = bestScore;
				}
				if(linesCleared != -1) {
					game.undo();
				}	
			}
			if(result != -1) {
				game.undo();
			}
		}
		return bestMove;
		
	}

	public double evaluateBoard(int lines, int[][] board) {
		 int holes = getHoles(board);
		 int maxHeight = getMaxHeight(board);
		 int relativeHeight = this.relativeHeight(board);
		 int cumulativeHeight = this.cumulativeHeights(board);
		 double avgHeight = getAverageHeight(board);
		 double roughness = this.roughness(board);
		  return w1 * lines + w2 * maxHeight + w3 * cumulativeHeight + w4 * relativeHeight + w5 * holes + w6 * roughness;
	}
	
	public int squaresInRightWell(int[][] board) {
		int sum = 0;
		for(int i = 0; i < Board.ROWS; i++) {
			if(board[i][Board.COLS-1] != 0) {
				sum++;
			}
		}
		return sum;
	}
	
	
	public int relativeHeight(int[][] board) {
		return getMaxHeight(board) - getMinHeight(board);
	}
	
	public int getMaxHeight(int[][] board) {
	    int maxHeight = -1;
	    int[] heights = heights(board);
	    for(int i = 0; i < heights.length; i++) {
	        if(heights[i] > maxHeight) {
	        	maxHeight = heights[i];
	        }
	    }
	    return maxHeight;
	}
	
	public int getMinHeight(int[][] board) {
	    int minHeight = 99999999;
	    int[] heights = heights(board);
	    for(int i = 0; i < heights.length; i++) {
	        if(heights[i] < minHeight) {
	        	minHeight = heights[i];
	        }
	    }
	    return minHeight;
	}
	
	public int cumulativeHeights(int[][] board) {
		int sum = 0;
		int[] heights = heights(board);
	    for(int i = 0; i < heights.length; i++) {
	        sum += heights[i];
	    }
	    return sum;
	}
	
	
	public double getAverageHeight(int[][] board) {
		    double sum = 0;
		    int[] heights = this.heights(board);
		    for(int i = 0; i < heights.length; i++) {
		      sum += heights[i];
		    } 
		    double avg = sum / heights.length;
		    return avg;
		    
	 }
	
	public int[] heights(int[][] board) {
		 return heights(board, Board.ROWS-1);
	}
	
	public int[] heights(int[][] board, int maxHeight) {
	    int[] heights = new int[Board.COLS];
	    for(int x = 0; x < Board.COLS; x++) {
	      heights[x] = getHeightOfColumn(board, x, maxHeight);
	    }
		return heights;
	}
	
	private int getHeightOfColumn(int[][] board, int x, int maxHeight) {
	    int height = Board.ROWS;
	    
	    for(int i = 0; i < Board.ROWS; i++) {
	    	if(board[i][x] == 0) height--;
	    	else break;
	    }
	    
	    return height;   
	}
	 
	public double roughness(int[][] board) {
		int sum = 0;
		int[] heights = heights(board);
		for(int i = 1; i < heights.length-1; i++) {
			int d1 = Math.abs(heights[i-1] - heights[i]);
			int d2 = Math.abs(heights[i+1] - heights[i]);
			sum += d1;
			sum += d2;
		}
		return Math.pow(sum, 1.5);
		
	}
	public int getHoles(int[][] board) {
		    int holes = 0;
		    int[] heights = heights(board);
		    
		    for(int x = 0; x < Board.COLS; x++) {
		      for(int y = Board.ROWS-1; y >= Board.ROWS - heights[x]; y--) {
		        if(board[y][x] == 0) {
		          holes++;
		        }
		      }
		    }
		    return holes;
	 }
}
