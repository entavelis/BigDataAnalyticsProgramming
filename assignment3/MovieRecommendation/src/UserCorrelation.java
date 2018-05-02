/**
 * CorrelationPair contains the ids of two objects and their corrilarity.
 * Based on the code of Toon Van Craenendonck
 * @author Evan Ntavelis
 *
 */
public class UserCorrelation implements Comparable<UserCorrelation>{
	int userID;
	short corr;

	/**
	 * Construct a CorrelationPair object
	 * @param userID of Neighbor
	 * @param corr the correlation
	 */

	public UserCorrelation(int userID, short corr){
		this.userID = userID;
		this.corr = corr;
	}

	/**
	 * Comparing a CorrelationPair object to another CorrelationPair object.
	 */
	@Override
	public int compareTo(UserCorrelation c) {
		if (corr < c.getCorrelation()){
			return -1;
		}else if (corr == c.getCorrelation()){
			return 0;
		}else{
			return 1;
		}
	}

	/**
	 * Returns the id of object 1.
	 */
	public int getUserId() {
		return userID;
	}


	/**
	 * Returns the correlation between the objects.
	 */
	public short getCorrelation(){
		return corr;
	}

	public double getRealCorrelation(){
		return corr/10000.0;
	}

	@Override
	public int hashCode() {
		return userID;
	}

}
