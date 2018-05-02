/**
 * CorrelationPair contains the ids of two objects and their corrilarity.
 * Based on the code of Toon Van Craenendonck
 * @author Evan Ntavelis
 *
 */
public class CorrelationPair{
	int id1;
	int id2;
	double corr;
	
	/**
	 * Construct a CorrelationPair object
	 * @param id1 id of Movie 1
	 * @param id2 id of Movie 2
	 */

	public CorrelationPair(int id1, int id2){
		this.id1 = id1;
		this.id2 = id2;
	}
//
//	/**
//	 * Comparing a CorrelationPair object to another CorrelationPair object.
//	 */
//	@Override
//	public int compareTo(CorrelationPair c) {
//		if (corr < c.getCorrelation()){
//			return -1;
//		}else if (corr == c.getCorrelation()){
//			return 0;
//		}else{
//			return 1;
//		}
//	}
//
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
	 * Returns the correlation between the objects.
	 */
//	public double getCorrelation(){
//		return corr;
//	}
	
	public boolean equals(Object sPair) {
		if (!(sPair instanceof CorrelationPair)) return false;
		CorrelationPair c = (CorrelationPair) sPair;
		return (id1 == c.getId1()) && (id2 == c.getId2());
	}
	
	@Override
	public int hashCode() {
		return id1* 70000 + id2;
	}

}
