import java.util.Random;

abstract public class UniversalHash{
	int a;
	int b;
	int N;
	int P;

	public UniversalHash(int N) {
		this.N = N;
		this.P = Primes.findLeastPrimeNumber(N);
		
        Random rand = new Random();
        a = Math.floorMod(rand.nextInt(), N);
        b = Math.floorMod(rand.nextInt(), N);
       
	}

	
	protected int getHash(int X) {
		return  ((a*X+b) % P) % N;
	}
	
	
}
