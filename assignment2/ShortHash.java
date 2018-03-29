public class ShortHash extends UniversalHash{
	short values[];

	public ShortHash(int N) {
        super(N); 

        this.values = new short[N];
        
        for (int i = 0; i<N; i++) {
        	values[i] = (short) this.getHash(i);
        }
	}

	public short getValue(int x) {
		return values[x];
	}
	
	
}
