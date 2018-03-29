import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;


/**
 * @author Jessa Bekker
 *
 * This class is a stub for naive bayes with feature hashing
 *
 * (c) 2017
 */
public class NaiveBayesFeatureHashing extends OnlineTextClassifier{

    private int logNbOfBuckets;
    private int[][] counts; // counts[c][i]: The count of n-grams in e-mails of class c (spam: c=1) that hash to value i
    private int[] classCounts; //classCounts[c] the count of e-mails of class c (spam: c=1)

    /* FILL IN HERE */
    private int seed;
    //private byte[] hashbees;
    
    
    
    /**
     * Initialize the naive Bayes classifier
     *
     * THIS CONSTRUCTOR IS REQUIRED, DO NOT CHANGE THE HEADER
     * You can write additional constructors if you wish, but make sure this one works
     *
     * This classifier uses simple feature hashing: The features of this classifier are the hash values that n-grams
     * hash to.
     *
     * @param logNbOfBuckets The hash function hashes to the range [0,2^NbOfBuckets-1]
     * @param threshold The threshold for classifying something as positive (spam). Classify as spam if Pr(Spam|n-grams)>threshold)
     */
    public NaiveBayesFeatureHashing(int logNbOfBuckets, double threshold){
        this.logNbOfBuckets=logNbOfBuckets;
        this.threshold = threshold;

        /* FILL IN HERE */
        // Seed for the hashing function
        Random rand = new Random();
        seed = rand.nextInt();
        //seed = (int) Math.random() * Integer.MAX_VALUE - Integer.MIN_VALUE;
        //this.seed = 0x9747b28c;
        		
        //Initialize Byte Array
        //this.hashbees = new byte[2 ^ this.logNbOfBuckets];
       
        //Initialize Arrays
        counts = new int[2][1 << logNbOfBuckets];
        classCounts = new int[2];
      
        //Laplace Smoothing
        for (int i = 0; i<2; i++) {
        	Arrays.fill(counts[i], 1);
        	classCounts[i] = 1;
        }
        
    }

    /**
     * Calculate the hash value for string str
     *
     * THIS METHOD IS REQUIRED
     *
     * The hash function hashes to the range [0,2^NbOfBuckets-1]
     *
     * @param str The string to calculate the hash function for
     * @return the hash value of the h'th hash function for string str
     */
    private int hash(String str){
        int v=0;
        
        //
        /* FILL IN HERE */
        // Remark: we don't want negative number so I am changing the % --> Math.FloorMod
        // v = MurmurHash.hash32(str, seed) % (2 ^ logNbOfBuckets);
        v = Math.floorMod(MurmurHash.hash32(str, seed), 1 << logNbOfBuckets);

        //System.out.println(v);
        return v;
    }


    /**
     * Calculate the hash value for string str
     *
     * THIS METHOD IS REQUIRED
     *
     * The hash function hashes to the range [0,2^NbOfBuckets-1]
     *
     * @param str The string to calculate the hash function for
     * @return the hash value of the h'th hash function for string str
     */

    private long hashLong(String str){
        long v=0;
        
        //
        /* FILL IN HERE */
        // Remark: we don't want negative number so I am changing the % --> Math.FloorMod
        // v = MurmurHash.hash32(str, seed) % (2 ^ logNbOfBuckets);
        v = Math.floorMod(MurmurHash.hash64(str, seed), 1 << logNbOfBuckets);

        //System.out.println(v);
        return v;
    }

    /**
     * This method will update the parameters of your model using the incoming mail.
     *
     * THIS METHOD IS REQUIRED
     *
     * @param labeledText is an incoming e-mail with a spam/ham label
     */
    @Override
    public void update(LabeledText labeledText){
        super.update(labeledText);

        /* FILL IN HERE */
        classCounts[labeledText.label]++;
        for (String ng: labeledText.text.ngrams) { 
        	counts[labeledText.label][hash(ng)]++; 
        	//System.out.println("Hash: " + hash(ng) + " Label : " + labeledText.label + " Update: " + counts[labeledText.label][hash(ng)]);
        }
        
          
    }


    /**
     * Uses the current model to make a prediction about the incoming e-mail belonging to class "1" (spam)
     * The prediction is the probability for the e-mail to be spam.
     * If the probability is larger than the threshold, then the e-mail is classified as spam.
     *
     * THIS METHOD IS REQUIRED
     *
     * @param text is an parsed incoming e-mail
     * @return the prediction
     */
    @Override
   public double makePrediction(ParsedText text) {
        double pr = 0;

        /* FILL IN HERE */
        /* We want to compute the probability of the email being spam given all the n-grams
         * 
         */

        double productSpam = 0;
        double productNoSpam = 0;
        
        int i;
        for (String ng: text.ngrams) {
        	i = hash(ng);
        	productNoSpam += Math.log(counts[0][i]);
        	productSpam += Math.log(counts[1][i]);
        	//System.out.println(productNoSpam + " " + productSpam);
        }
       
        // size of set minus 1
        int lm1 = text.ngrams.size() - 1;
       
        //System.out.println((productNoSpam - productSpam ));
        //System.out.println((lm1*Math.log(this.classCounts[1]) - lm1*Math.log(this.classCounts[0])));

        //
        pr = 1 + Math.exp(productNoSpam - productSpam + lm1*(Math.log(classCounts[1]) - Math.log(classCounts[0])));
        //System.out.print(1.0/pr + "\n");
        
        return 1.0 / pr;

    }

     public double makePredictionTest(ParsedText text) {
        double pr = 0;

        /* FILL IN HERE */
        /* We want to compute the probability of the email being spam given all the n-grams
         * 
         */

        double productSpam = classCounts[1];
        double productNoSpam = classCounts[0];
        
        int i;
        for (String ng: text.ngrams) {
        	i = hash(ng);
        	productNoSpam *= counts[0][i]/classCounts[0];
        	productSpam *= counts[1][i]/classCounts[1];
        	//System.out.println(productNoSpam + " " + productSpam);
        }
       
        // size of set minus 1
        return productSpam/(productSpam + productNoSpam);
    }


    /**
     * This runs your code.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 7) {
            System.err.println("Usage: java NaiveBayesFeatureHashing <indexPath> <stopWordsPath> <logNbOfBuckets> <threshold> <outPath> <reportingPeriod> <maxN> [-writeOutAllPredictions]");
            throw new Error("Expected 7 or 8 arguments, got " + args.length + ".");
        }
        try {
            // parse input
            String indexPath = args[0];
            String stopWordsPath = args[1];
            int logNbOfBuckets = Integer.parseInt(args[2]);
            double threshold = Double.parseDouble(args[3]);
            String out = args[4];
            int reportingPeriod = Integer.parseInt(args[5]);
            int n = Integer.parseInt(args[6]);
            boolean writeOutAllPredictions = args.length>7 && args[7].equals("-writeOutAllPredictions");

            // initialize e-mail stream
            MailStream stream = new MailStream(indexPath, new EmlParser(stopWordsPath,n));

            // initialize learner
            NaiveBayesFeatureHashing nb = new NaiveBayesFeatureHashing(logNbOfBuckets, threshold);

            // generate output for the learning curve
            EvaluationMetric[] evaluationMetrics = new EvaluationMetric[]{new Accuracy(),new Precision(),new TruePositiveRate(), new TrueNegativeRate()}; //ADD AT LEAST TWO MORE EVALUATION METRICS
            nb.makeLearningCurve(stream, evaluationMetrics, out+".nbfh", reportingPeriod, writeOutAllPredictions);
          
        } catch (FileNotFoundException e) {
            System.err.println(e.toString());
        }
    }


}
