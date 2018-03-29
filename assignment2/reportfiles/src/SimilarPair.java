/**
 * SimilarPair contains the ids of two objects and their similarity.
 * 
 * @author Toon Van Craenendonck
 *
 */
public class SimilarPair implements Comparable<SimilarPair>{
	int id1;
	int id2;
	double sim;
	
	/**
	 * Construct a SimilarPair object
	 * @param id1 id of object 1
	 * @param id2 id of object 2
	 * @param sim their similarity
	 */
	public SimilarPair(int id1, int id2, double sim){
		this.id1 = id1;
		this.id2 = id2;
		this.sim = sim;
	}

	/**
	 * Comparing a SimilarPair object to another SimilarPair object.
	 */
	@Override
	public int compareTo(SimilarPair c) {
		if (sim < c.getSimilarity()){
			return -1; 
		}else if (sim == c.getSimilarity()){
			return 0;
		}else{
			return 1;
		}
	}
	
	/**
	 * Returns the id of object 1.
	 */
	public int getId1() {
		return id1;
	}

	/**
	 * Returns the id of object 2.
	 */
	public int getId2() {
		return id2;
	}

	/**
	 * Returns the similarity between the objects.
	 */
	public double getSimilarity(){
		return sim;
	}
	
	public boolean equals(Object sPair) {
		if (!(sPair instanceof SimilarPair)) return false;
		SimilarPair c = (SimilarPair) sPair;
		return (id1 == c.getId1()) && (id2 == c.getId2());
	}
	
	@Override
	public int hashCode() {
		return id1;
	}

}
