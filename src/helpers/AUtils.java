package helpers;

public class AUtils {
	public static int[][] deepCopy(int[][] arr) {
		int[][] copied = new int[arr.length][];
		for(int i = 0; i < arr.length; i++) {
			copied[i] = arr[i].clone();
		}
		return copied;
	}
}
