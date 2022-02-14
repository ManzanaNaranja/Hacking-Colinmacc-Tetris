package helpers;

public class ANumbers {
	public static String hextobinary(int num) {
		return String.format("%16s", Integer.toBinaryString(num)).replace(' ', '0');
	}
}
