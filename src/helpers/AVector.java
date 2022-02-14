package helpers;

public class AVector {
	public double x, y;
	
	public AVector() {
		this(0,0);
	}
	
	public AVector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static AVector add(AVector v1, AVector v2) {
		return new AVector(v1.x + v2.x, v1.y + v2.y);
	}
	
	public void add(AVector other) {
		this.x += other.x;
		this.y += other.y;
	}
	
	public void add(double x, double y) {
		this.x += x;
		this.x += y;
	}
	
	public AVector copy() {
		return new AVector(this.x, this.y);
	}
}
