import java.util.*;
import java.io.*;
import java.nio.charset.*;

/**
 * Computes a matrix with Pearson's product-moment correlation coefficients
 * for the ratings given to movies by pairs users.
 *
 * Correlations are given by the formula:
 *      cor(X, Y) = Sum[(xi - E(X))(yi - E(Y))] / [(n - 1)s(X)s(Y)]
 * where E(X) is the mean of X, E(Y) is the mean of the Y values and s(X),
 * s(Y) are standard deviations.
 *
 * The PearsonsCorrelation can be ran from the commandline to construct
 * the matrix and to save the result to a file afterwards.
 * Example command:
 *      java -cp .:bin/ PearsonsCorrelation -trainingFile data/r1.train -outputFile out/r1.matrix
 *
 * @author Pieter Robberechts
 *
 */
public class PearsonsCorrelation {

    private int leastCommon;
    private int adjThreshold;
    private double threshold;
    private int noUsers;

    private ArrayList<UserCorrelation>[] corrList;

//    private double userAverages[];

    /**
     * Create an empty PearsonsCorrelation instance with default parameters.
     */
//    public PearsonsCorrelation() {
//        super();
//         FILL IN HERE //
//    }

    /**
     * Create a PearsonsCorrelation instance with default parameters.
     * @param ratings the Input Ratings
     * @param leastCommon the number of the least Common items two users should have rated in order to be considered
     * @param threshold the Correlation threshold, we don't save Correlations smaller than this;
     * @param adjThreshold below this value we adjust the Correlation
     */
    public PearsonsCorrelation(MovieHandler ratings, int leastCommon, double threshold, int adjThreshold) {
        super();
        // FILL IN HERE //
        this.leastCommon = leastCommon;
        this.threshold = threshold;
        this.adjThreshold = adjThreshold;
        noUsers = ratings.getNumUsers();
//        noUsers = 100;



        // Pearson Correlation is symmetrical thus we only compute half of the "matrix"
        // The last row remains empty and thus we only need noUsers - 1 and not noUsers
        corrList = new ArrayList[noUsers];

//        long start = System.currentTimeMillis();
        for (int i = 0; i < noUsers ; i++) {
            corrList[i] = new ArrayList<UserCorrelation>();
            for (int j = i + 1; j < noUsers; j++) {
                int iUser = ratings.getUserIDs().get(i); // External Id of i
                int jUser = ratings.getUserIDs().get(j); // of j

                // Compute correlation of the two users
                double corr = correlation(ratings.getUsersToRatings().get(iUser),
                        ratings.getUsersToRatings().get(jUser),
                        ratings.getUserAverageRating(iUser), ratings.getUserAverageRating(jUser));
                if (!Double.isNaN(corr)) {
                    corrList[i].add(new UserCorrelation(j,((short) Math.round(10000*corr))));
                }
            }
//            if ((i % 1000) == 0) {
//                System.out.println(i);
//                System.out.println((System.currentTimeMillis() - start)/1000.0);
//                start = System.currentTimeMillis();
//            }
        }

    }



    /**
     * Load a previously computed PearsonsCorrelation instance.
     */
    public PearsonsCorrelation(MovieHandler ratings, String filename) {
        // FILL IN HERE //
//        long startTime = System.currentTimeMillis();
//        System.out.println("Reading Correlation Matrix.. ");

        readCorrelationMatrix(filename);

//        System.out.println("done, took " +  (System.currentTimeMillis() - startTime)/1000.0 + "seconds.");
//        System.out.println("--------------");
    }



    /**
     * Computes the Pearson's product-moment correlation coefficient between
     * the ratings of two users.
     *
     * Returns {@code NaN} if the correlation coefficient is not defined.
     *
     * @param xArray first data array
     * @param yArray second data array
     * @param xMean precomputed mean of first array
     * @param yMean precomputed mean of second array
     * @return Returns Pearson's correlation coefficient for the two arrays
     */
    public double correlation(List<MovieRating> xRatings, List<MovieRating> yRatings, double xMean, double yMean) {
    double correlation = 0;

        // Keep the ones we need if not enough return NaN

        Iterator<MovieRating> xIter = xRatings.iterator();
        Iterator<MovieRating> yIter = yRatings.iterator();


        // We don't have to check as we already know we have at least 20 movies for each user

        // To bypass the problem for y not initialised for equality check
        MovieRating y = new MovieRating(-1, 0) ;
        MovieRating x;

        // break the loop
        // ATTENTION
        int cnt = 0; // Number of common elements
        double xStd =0;
        double yStd =0;
        double upperSum =0; // The Sum on the upper part
        while (xIter.hasNext()){
            x = xIter.next();

            while (yIter.hasNext()){
                y = yIter.next();
                if (y.getMovieID() >= x.getMovieID()){
                    break;
                }
            }

            // if we find two same IDs we update our variables
            if (x.getMovieID() == y.getMovieID()) {
                cnt++;
                double xR = x.getRating();
                double yR = y.getRating();

                xStd += Math.pow(xR-xMean,2);
                yStd += Math.pow(yR-yMean,2);
                upperSum += (xR-xMean)*(yR-yMean);

            }
        }

        if (cnt < leastCommon) {
            return Double.NaN;
        }

        correlation = upperSum / Math.sqrt(xStd * yStd);
//        correlation = computeCorrelation(xLeft, yLeft, xMean, yMean);

        // Adjust correlation based on adj leastCommon
        if (cnt < adjThreshold){
            correlation = correlation * cnt/adjThreshold;
        }

        // The threshold is the last step
        if (correlation < threshold){
            return Double.NaN;
        }
        return correlation;
    }

    private double computeCorrelation(List<MovieRating> xLeft, List<MovieRating> yLeft){
        return computeCorrelation(xLeft, yLeft, computeUserRatingAverage(xLeft), computeUserRatingAverage(yLeft));
    }

    private double computeCorrelation(List<MovieRating> xLeft, List<MovieRating> yLeft, double xMean, double yMean){
        double correlation, yStd, xStd, upperSum;
        Iterator<MovieRating>  xIter, yIter;

        correlation =0.0;

        // Computes S(X)
        xStd =0;
        yStd =0;
        upperSum =0; // The Sum on the upper part
        xIter = xLeft.iterator();
        yIter = yLeft.iterator();

        // xLeft and yLeft have the same number of elements
        while (xIter.hasNext()){
            MovieRating xRating = xIter.next();
            MovieRating yRating = yIter.next();

            double xR = xRating.getRating();
            double yR = yRating.getRating();

            xStd += Math.pow(xR-xMean,2);
            yStd += Math.pow(yR-yMean,2);
            upperSum += (xR-xMean)*(yR-yMean);
        }

        correlation = upperSum / Math.sqrt(xStd * yStd);

        return correlation;

    }
    /**
     * Computers the average Movie Rating of a User
     * @param ratings
     * @return
     */
    private double computeUserRatingAverage(List <MovieRating> ratings){
        double average = 0.0;
        // FILL IN HERE
        for (MovieRating xR : ratings) average += xR.getRating();
        average /= ratings.size();

        return average;
    }

    /**
     *
     * We assume that the arraylist containing the  corr matrix is sorted
     * on UserIDs
     *
     * Writes the correlation matrix into a file as comma-separated values.
     *
     * The resulting file contains the full nb_users x nb_users correlation
     * matrix, such that the value on position (row_i, col_j) corresponds to
     * the correlation between the user with internal id i and the user with
     * internal id j. The values are separated by commas and rounded to four
     * decimal digits. The actual matrix starts on line 3. The first line
     * contains a single integer which defines the size of the matrix. The
     * second line is reserved for additional parameter values which where
     * used during the construction of the correlation matrix. You are free to
     * use any format for this line. E.g.:
     *  3
     *  param1=value,param2=value
     *  1.0000,-.3650,NaN
     *  -.3650,1.0000,.0012
     *  NaN,.0012,1.0000
     *
     * @param filename Path to the output file.
     */
    public void writeCorrelationMatrixOld(String filename) throws IOException{
        // FILL IN HERE //

        // Initialize new file
        File fout = new File(filename);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos), 16 * (int) Math.pow(1024, 2));


        try{
            // number of users
            bw.write(Integer.toString(noUsers));
            bw.newLine();

            // parameters
            bw.write("LeastCommon " + Integer.toString(leastCommon)
                                    + ", Threshold " + Double.toString(threshold)
                                    + ", AdjThreshold " + Integer.toString(adjThreshold));
            bw.newLine();

        // Initialises the Indices to 0
            int[] userIndices = new int[noUsers - 1];

        // We will be looping over the corrList twice, once for the rows and once for the columns
            for (int i = 0 ; i < noUsers; i++){

            // Write correlations based on previous ids
                for (int j = 0; j<i; j++){

                // Write None when the current movieId is smaller than the next in the arraylist or
                // When we have already found all the movieIds in the array list
                    if (userIndices[j] == corrList[j].size() || corrList[j].get(userIndices[j]).getUserId() > i){
//                        toWrite[j] = Double.NaN;
                        bw.write("NaN,");
                    }
                    else {
//                    toWrite[j] = corrList[j].get(userIndices[j]).getCorrelation();
                        bw.write(Double.toString(corrList[j].get(userIndices[j]).getRealCorrelation()));
                        bw.write(",");
                        userIndices[j]++;
                    }

            }


            //Write self-correlation, 1.0000 to File
//            toWrite[i] = 1.0;
                bw.write("1.0000");

                //We need No,users commas per line, before self - corr we write them after
                // and after it we write the comma before the value
            //Write next correlations
            int position = i + 1;
            if (position < noUsers) {
                Iterator<UserCorrelation> tempIter = corrList[i].iterator();
                while (tempIter.hasNext()) {
                    UserCorrelation temp = tempIter.next();

                    // Fills NaNs for the correlations we don't have
                    for (; position < temp.getUserId(); position++) {
                        // Write Nan to file
//                    toWrite[position] = Double.NaN;
                        bw.write(",NaN");
                    }

                    // Write temp.getCorrelation() to file;
//                toWrite[position] = temp.getCorrelation();
                    bw.write(",");
                    bw.write(Double.toString(temp.getRealCorrelation()));
                }

                for (; position < noUsers; position++) {
                    // Write NaN to file
//                toWrite[position] = Double.NaN;
                    bw.write(",NaN");
                }

                // Write the buffer Array to file
                bw.newLine();
            }

        }
        bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }


    /**
     * Reads the correlation matrix from a file.
     *
     * @param filename Path to the input file.
     * @see writeCorrelationMatrix
     */
    public void readCorrelationMatrix(String filename) {
        // FILL IN HERE //
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(filename),16 * (int) Math.pow(1024, 2));
            String line;

            // Read Number of Users
            line = br.readLine();
            noUsers = Integer.parseInt(line);
            corrList = new ArrayList[noUsers];

            // Read Parameters
            line = br.readLine();

            int index = 0;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                corrList[index] = new ArrayList<UserCorrelation>();
                for (int i = index+1; i<noUsers; i++){
                    if (!tokens[i].equals("NaN")){
                        corrList[index].add(new UserCorrelation(i,((short) Math.round(10000*Double.parseDouble(tokens[i])))));
                    }
                }
                index++;
            }

//            System.out.println("Read " + index + "data lines!");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public ArrayList<UserCorrelation>[] getCorrelationMatrix(){
        return corrList;
    }


    public ArrayList<UserCorrelation> getUserCorrelations(int userID){
        return corrList[userID];
    }
    public static void main(String[] args) {
        String trainingFile = "";
        String outputFile = "";
        int leastCommon=4;
        int adjThreshold=10;
        double threshold=0;

        int i = 0;
        while (i < args.length && args[i].startsWith("-")) {
            String arg = args[i];
            if(arg.equals("-trainingFile")) {
                trainingFile = args[i+1];
            } else if(arg.equals("-outputFile")) {
                outputFile = args[i+1];
            } else if(arg.equals("-adjThreshold")) {
                adjThreshold = Integer.parseInt(args[i+1]);
            } else if(arg.equals("-leastCommon")) {
                leastCommon = Integer.parseInt(args[i+1]);
            } else if(arg.equals("-threshold")) {
                threshold = Double.parseDouble(args[i+1]);
            }
            // ADD ADDITIONAL PARAMETERS //
            i += 2;
        }

        long start,stop, movieTime, pearsonTime, writeTime;

//        System.out.println("Least Common: " + leastCommon);
//        System.out.println("Threshold: " + threshold);
//        start = System.currentTimeMillis();

        MovieHandler ratings = new MovieHandler(trainingFile);

//        stop = System.currentTimeMillis();
//        movieTime = stop-start;
//
//        System.out.println("Movie Handling: " + movieTime/1000.0);

//        start = System.currentTimeMillis();
//        System.out.println("Computing Movies Pearson Correlations...");
        PearsonsCorrelation matrix = new PearsonsCorrelation(ratings, leastCommon, threshold, adjThreshold);

//        stop = System.currentTimeMillis();
//        pearsonTime = stop-start;

//        System.out.println("done, took " + pearsonTime/1000.0);

//        System.out.println("Saving Correlations to the disk...");
//        start = System.currentTimeMillis();

        try {
            matrix.writeCorrelationMatrixOld(outputFile);
        }
        catch(IOException e){
            e.printStackTrace();
        }

//        stop = System.currentTimeMillis();

//        writeTime = stop-start;

//        System.out.println("done, took " + writeTime/1000.0);
    }

}
