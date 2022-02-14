package tetris.piece;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

public class Bag {
	private LinkedList<PieceInstance> contents;
	private ListIterator<PieceInstance> iterator;
	
	public Bag() {
		contents = new LinkedList<PieceInstance>();
		this.add();
		
		iterator = contents.listIterator(0);
	}
	
	public void changeCurrent(PieceInstance p) {
		this.iterator.set(p);
	}
	
	public void changeNext(PieceInstance p) {
		if(iterator.hasNext() == false) this.add();
		this.iterator.next();
		this.changeCurrent(p);
		this.iterator.previous();
		
	}
	
	public PieceInstance pick() {
		if(iterator.hasNext() == false) {
			this.add();
		}
		return new PieceInstance(iterator.next().getPiece());
	}
	
	public void undo() {
		if(iterator.hasPrevious()) {
			iterator.previous();
		}
	}
	
	public PieceInstance view_prev() {
		if(iterator.hasPrevious()) {
			return new PieceInstance(contents.get(iterator.previousIndex()).getPiece());
		}
		return null;
	}
	
	public PieceInstance view_next() {
		if(iterator.hasNext() == false) {
			this.add();
		}
		return new PieceInstance(contents.get(iterator.nextIndex()).getPiece());
	}
	
	public PieceInstance view_next_next() {
		if(iterator.hasNext() == false) {
			this.add();
		} 
		int index = iterator.nextIndex() + 1;
		if(index >= contents.size()) this.add();
		return contents.get(index);
	}
	
	
	private void add() {
		LinkedList<PieceInstance> temp = new LinkedList<PieceInstance>();
		Piece[] pieces = Piece.values();
		for(int i = 0; i < pieces.length; i++) {
			for(int j = 0; j < 2; j++) {
				temp.add(new PieceInstance(pieces[i]));
			}
		}
		Collections.shuffle(temp);
		contents.addAll(temp);
		if(iterator != null) iterator = contents.listIterator(iterator.nextIndex());
	}
	
	public String toString() {
		String a = "";
		for(PieceInstance p : contents) {
			a += p.getPiece();
		}
		return a;
	}
	
	public static void main(String[] args) {
		Bag b = new Bag();
		System.out.println(b);
		b.pick();
		b.pick();
		b.undo();
		System.out.println(b.view_next());
	}
	
}
