import java.util.HashSet;
import java.util.Set;

/**
 * A Shingler constructs the shingle representations of documents.
 * It takes all substrings of length k of the document, and maps these substrings to an integer value that is inserted into the documents shingle set.
 * 
 * @author Toon Van Craenendonck
 *
 */
public class ShinglerTokens {
	
	int n;
	int nShingles;

	/**
	 * Construct a shingler.
	 * @param k number of n grams 
	 */
	public ShinglerTokens(int n, int nShingles){
		this.n = n;
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
        Set<String> ngrams = getNgrams(doc, n);
		for (String ngram: ngrams){
			shingled.add(hashShingle(ngram));
		}
		return shingled;
	}

    /**
     * Extract clean n-grams from a document.
     *
     * @param doc the 
     * @param n The maximum n for n-grams. E.g. if n=3 the parser extracts single words, pairs and triples.
     * @param stopWords Set of stopwords to be removed from the text. The stopwords are expected to be stemmed.
     * @return Set of clean N-grams
     */
    public static Set<String> getNgrams(String doc, int n){
    	Set<String> stopWords = new HashSet<>();
    	return getNgrams(doc,n,stopWords);
    }
	
    public static Set<String> getNgrams(String doc, int n, Set<String> stopWords){
        try{
            String[] words = doc.split("\\s+");

            // Stem all the word. Stemming reduces inflected (or sometimes derived) words to their word stem.
            // The goal of stemming is to map related words (e.g eat, eats, eating) to the same stem.
            // Meanwhile, remove the stop words
            PorterStemmer stemmer = new PorterStemmer();
            Set<String> ngrams = new HashSet<>();
            for (int i =0; i<words.length; i++) {
                String stemmed = stemmer.stem(words[i]);
                if (stopWords.contains(stemmed)) {
                    words[i]=null;
                }
                else{
                    words[i]=stemmed;
                }
            }

            //Extract n-grams from the stemmed sequence of words
            for (int i =0; i<words.length; i++){
                int length=0;
                String ngram = "";
                int j=0;
                while (length<n && i+j<words.length){
                    if (words[i+j]!=null){
                        ngram +=" "+words[i+j];
                        length++;
                        ngrams.add(ngram.trim());
                    }
                    j++;
                }
            }

            return ngrams;
        } catch (Exception e) {
            return new HashSet<>();
        }
    }


}
