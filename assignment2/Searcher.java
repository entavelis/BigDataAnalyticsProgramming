import java.util.*;

public abstract class Searcher {

	TwitterReader reader;

	public Searcher(TwitterReader reader){
	
		System.out.println("Initialising " + methodName());


		this.reader = reader;
	}
	
	/**
	 * Jaccard similarity between two sets.
	 * @param set1
	 * @param set2
	 * @return the similarity
	 */
	public <T> double jaccardSimilarity(Set<T> set1, Set<T> set2) {
		Set<T> union = new HashSet<T>(set1);
		union.addAll(set2);

		Set<T> intersection = new HashSet<T>(set1);
		intersection.retainAll(set2);

		if (union.size() == 0){
			return 0;
		}
		return (double) intersection.size() / union.size();
	}
	
	
	abstract public Set<SimilarPair> getSimilarPairsAboveThreshold(double threshold);
	

	abstract String methodName();

}
