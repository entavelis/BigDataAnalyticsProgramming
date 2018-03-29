import java.util.*;

public class LSHshort extends Searcher{

	private int bands;
	private int rowsPerBand;
	
	int nbOfMinHashes;
	int nbOfBuckets;
	
	int nbOfDocs;
	
	int nbOfShingles;

	public LSHshort(TwitterReader reader, int nbOfDocs, int nbOfShingles, int bands, int rows, int nbOfBuckets){
		super(reader);
		
		this.nbOfMinHashes = rows * bands;
		
		this.nbOfBuckets = nbOfBuckets;

		this.bands = bands;
		this.rowsPerBand = rows;
		

		this.nbOfDocs = nbOfDocs;
		
		this.nbOfShingles = nbOfShingles;
		System.out.println("bands: " + this.bands + " rowsPerBand: " + rowsPerBand);
			
		
	}

	private short[][] setSignatureMatrix(){
		System.out.println("Setting Signature Matrix");
		
		ShortHash[] hashes = new ShortHash[nbOfMinHashes];

		
		for (int i = 0; i < hashes.length; i++) {
			hashes[i] = new ShortHash(nbOfShingles);
		}

		short[][] sig = new short[nbOfMinHashes][nbOfDocs];

		for (int i=0; i< nbOfMinHashes; i++){
			Arrays.fill(sig[i], Short.MAX_VALUE);
		}

		int id = 0;

		while (reader.hasNext()){
			Set<Integer> shingles = reader.next();
			
			for (int row: shingles) {
			
				for (int h = 0; h < nbOfMinHashes; h++) {
					if (hashes[h].getValue(row) < sig[h][id]) {
						sig[h][id] = hashes[h].getValue(row);
					}
				}		
			}
			
			id++;
		}
		
		return sig;

	
	}

		
	private int hashBand(short[] band) {
		int lent = rowsPerBand * 2; // Change Cause of short
        
		byte[] byteBuffer = new byte[lent];
		

		for (int i = 0; i < rowsPerBand; i++) {
			int bi = i*2;
			byteBuffer[bi] = (byte)(band[i] & 0xff);
			byteBuffer[bi+1] = (byte)((band[i] >> 8) & 0xff);
		}

        // check seed
		int hash = MurmurHash.hash32(byteBuffer, lent,4321);
		return Math.abs(hash) % nbOfBuckets;
	}

	/**
	 * Get pairs of objects with similarity above threshold.
	 * @param threshold the similarity threshold
	 * @return the pairs
	 */
	public Set<SimilarPair> getSimilarPairsAboveThreshold(double threshold) {
	
		short[][] sig = setSignatureMatrix();
		
		return computePairs(sig,threshold);

	}

	private Set<SimilarPair> computePairs(short[][] sig, double threshold) {
		System.out.println("Computing Similarity Pairs:");
		
		Set<SimilarPair> cands = new HashSet<SimilarPair>();
		
		

		// LSH Signature Mapping Manipulation
		int left = nbOfMinHashes;

		for (int b = 0; b < bands; b++){
			System.out.println("\t At band " + (1 + b) + "/" + bands);

			Map<Integer, Set<Integer>> buckets = new HashMap<Integer, Set<Integer>>();
			
			

			for (int c=0; c < nbOfDocs; c++) {
				short[] toHash = new short[rowsPerBand];
				
				// Better overflow handling

				int end = Math.min(rowsPerBand, left);

				for (int r=0; r < end; r++) {
					toHash[r] = sig[b*rowsPerBand+r][c];
				}
				

				Integer keyh = hashBand(toHash);


				Set<Integer> similar = new HashSet<Integer>();

				if (buckets.containsKey(keyh)) {
					similar =  buckets.get(keyh);
					for (int simc: similar) {
						SimilarPair newPair = new SimilarPair(simc,c,0);
						if (!cands.contains(newPair)){
						int countSimilar = 0;
						for (int i = 0; i < nbOfMinHashes; i++) if (sig[i][simc] == sig[i][c]) countSimilar++;
						
						newPair.sim = (double)  countSimilar / nbOfMinHashes;

						if (newPair.sim > threshold) cands.add(newPair);
						}
					}

					similar.add(c);
					buckets.replace(keyh, similar);
				}
				else {
					similar.add(c);
					buckets.put(keyh, similar);
				}

			}

			left -= rowsPerBand;
			System.out.println(buckets.size());
		}

		
		//System.out.println(cands.);
		return cands;
		
}
	

	@Override
	String methodName() {
		return "LSH - Short Version";
	}


}
