package tetris;

import java.util.LinkedList;

import helpers.AUtils;

public class BoardHistory {
	private LinkedList<BoardData> data;
	
	public BoardHistory() {
		data = new LinkedList<BoardData>();
	}
	
	public void add(BoardData item) {
		data.add(new BoardData(AUtils.deepCopy(item.board), item.lines));
	}
	
	public BoardData undo() {
		if(data.size() > 1) {
			return data.removeLast();
		}
		return null;
	}
	
	public BoardData current_state() {
		return data.getLast();
	}
}
