

public class tester {

	public static void main(String[] args) {
		final String text = "test";
		System.out.print(MurmurHash.hash32(text.getBytes(), text.length()));
	}
}