import java.util.HashSet;
import java.util.Set;

/**
 * A Shingler constructs the shingle representations of documents.
 * It takes all substrings of length k of the document, and maps these substrings to an integer value that is inserted into the documents shingle set.
 * 
 * @author Toon Van Craenendonck
 *
 */
public class Shingler {
	
	int k;
	int nShingles;

	/**
	 * Construct a shingler.
	 * @param k number of characters in one shingle
	 */
	public Shingler(int k, int nShingles){
		this.k = k;
		this.nShingles = nShingles;
	}

	/**
	 * Hash a k-shingle to an integer.
	 * @param shingle shingle to hash
	 * @return integer that the shingle maps to
	 */
	private int hashShingle(String shingle){
       int hash = MurmurHash.hash32(shingle,1234);
		return Math.abs(hash) % nShingles;
	}

	/**
	 * Get the shingle set representation of a document.
	 * @param doc document that should be shingled, given as a string
	 * @return the shingle set representation of the document
	 */
	public Set<Integer> shingle(String doc){
        Set<Integer> shingled = new HashSet<Integer>();
		for (int i = 0; i < doc.length() - k +1; i+=1){
			String toHash = Character.toString(doc.charAt(i));
			for (int j = 0; j < k - 1; j++){
				toHash += Character.toString(doc.charAt(i+j+1));
			}
			shingled.add(hashShingle(toHash));
		}
		return shingled;
	}

}
