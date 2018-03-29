import java.util.*;

/**
 * Brute force implementation of the similarity searcher. The Jaccard similarity is computed for all pairs and the most similar ones are selected.
 * 
 * @author Toon Van Craenendonck
 *
 */
public class BruteForceSearch extends Searcher{

	public BruteForceSearch(TwitterReader reader) {
		super(reader);
	}

	/**
	 * Get pairs of objects with similarity above threshold.
	 * @param threshold the similarity threshold
	 * @return the pairs
	 */
	@Override
	public Set<SimilarPair> getSimilarPairsAboveThreshold(double threshold) {

		Map<Integer, Set<Integer>> docToShingle = new HashMap<Integer, Set<Integer>>();
		int id = 0;
		while (reader.hasNext()){
			docToShingle.put(id,reader.next());
			id++;
		}

		Set<SimilarPair> cands = new HashSet<SimilarPair>();
		for (Integer obj1 : docToShingle.keySet()){
		    if (obj1 % 10000 == 0){
		        System.out.println("at " + obj1);
            }
			for (Integer obj2 : docToShingle.keySet()){
				if (obj1 < obj2){
					double sim = jaccardSimilarity(docToShingle.get(obj1),docToShingle.get(obj2));
					if (sim > threshold){
						cands.add(new SimilarPair(obj1, obj2, sim));
					}
	   			}
			}
		}
		return cands;
	}

	@Override
	String methodName() {
		return "Brute Force";
	}

}
