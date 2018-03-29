public class IntegerHash extends UniversalHash{
	int values[];

	public IntegerHash(int N) {
        super(N); 

//        this.values = new int[N];
        
//        for (int i = 0; i<N; i++) {
//        	values[i] = this.getHash(i);
//        }
	}

	public int getValue(int x) {
//		return values[x];
		return getHash(x);
	}
	
	
}
