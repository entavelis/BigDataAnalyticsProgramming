import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * The MovieRunner can be ran from the commandline to predict user ratings.
 * Example command to run:
 *      java -cp .:bin/ MovieRunner -trainingFile data/ra.train -matrixFile data/ra.matrix -testFile data/ra.test
 *
 * @author Toon Van Craenendonck
 * @author Pieter Robberechts
 */

public class MovieRunner {

    static MovieHandler ratings;
    static PearsonsCorrelation similarities;
    static boolean onlinePearson = false;
    static String testFile;


    /**
     * The test set has all the userIDs the train set has so we are safe to just refill the symmetrical matrix on the go
     *
     * Returns the predictions for all the movies related to the certain user;
     * At the same time it moves the
     * @param externUserID the external ID of the user we want to make the predictions for
     * @param movies the movies whose ratings we want to predict;
     * @return
     */
    private static double[] predictRating(int externUserID, ArrayList<Integer> movies){
        double rating = ratings.getUserAverageRating(externUserID);

        // We  get the internal Id for our user
        int userID = ratings.getInternalUserId(externUserID);

        // Initialization of the predictions array
        double sums[] = new double[movies.size()];
        double dividers[] = new double[movies.size()];

        for (UserCorrelation uCorr : similarities.getUserCorrelations(userID)){


            // We save the the external Neighbors Id
            int externalNeighborID = ratings.getUserIDs().get(uCorr.getUserId());

            // We check for which of the movies we have ratings of the Neighbor
            Iterator<MovieRating> mrIter = ratings.getUsersToRatings().get(externalNeighborID).iterator();


            int movieIndex = 0;
            while (movieIndex < movies.size() && mrIter.hasNext()){
                MovieRating mR = mrIter.next();

                if (mR.getMovieID() == movies.get(movieIndex)){
                    double corr = uCorr.getRealCorrelation();
                    sums[movieIndex] += corr*(mR.getRating() - ratings.getUserAverageRating(externalNeighborID));
                    dividers[movieIndex] += corr;
                }

                while (movieIndex < movies.size() && mR.getMovieID() > movies.get(movieIndex)){
                    movieIndex++;
                }
            }

            // If we don't have computed the predictions for the neighbor yet, update the correlation list of that
            // neighbor
            if (externalNeighborID > externUserID) {
                // adds the short version of the Corr so we don't have to convert again
                similarities.getUserCorrelations(uCorr.getUserId())
                        .add(new UserCorrelation(userID, uCorr.getCorrelation()));
            }

        }

        // remove Correlation List from the memory for the UserID,
        // we pray in Turing's soul that the Garbage Collector will do its job
        similarities.getUserCorrelations(userID).clear();

        for (int i =0; i<sums.length; i++){
            if (dividers[i] == 0){
                sums[i] = rating;
//                sums[i] = rating + ratings.getMovieAverageRating(movies.get(i))  - ratings.getGlobalAverage();
            }
            else {
                sums[i] = rating + sums[i]/ dividers[i];
//                sums[i] = rating + ratings.getMovieAverageRating(movies.get(i)) - ratings.getGlobalAverage() + sums[i] / dividers[i];
            }
//            sums[i] = rating +  sums[i] / dividers[i] ;
        }
        // use Iterators to check
//            sum += uCorr.getRealCorrelation() * ();
        return sums;
    }



    /**
     * For each user/movie combination in the test set, predict the users'
     * rating for the movie and compare to the true rating.
     * Prints the current mean absolute error (MAE) after every 50 users.
     *
     * @param testFile path to file containing test set
     */
    public static void evaluate(String testFile) {

        double summedErrorRecommenderSq = 0;
        double summedErrorAvgSq = 0;

        int avg_used = 0;
        int est_used = 0;
        int ctr = 0;

        BufferedReader br;
        int startTime = (int) (System.currentTimeMillis()/1000);
        int elapsedTime = 0;
        try {
            br = new BufferedReader(new FileReader(testFile));
            String line;

            int userID = ratings.getUserIDs().get(0);

            ArrayList<Integer> movies = new ArrayList();
            ArrayList<Double> rtngs = new ArrayList();

            // We changed the code to do the prediction in a batched mode
            // We predict all the movies associated with a User in a batch
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("::|\t");

                if (userID == Integer.parseInt(tokens[0])){
                    movies.add(Integer.parseInt(tokens[1]));
                    rtngs.add(Double.parseDouble(tokens[2]));
                }
                else {

                    // We do it here in order to have first value of prevID to be 0;
                    double[] predictions = predictRating(userID, movies);

                    for (int i = 0; i < movies.size(); i++) {

                        int movieID = movies.get(i);
                        double rating = rtngs.get(i);

                        double avgRating = ratings.getMovieAverageRating(movieID);
                        double estimate = predictions[i];

                        summedErrorRecommenderSq += Math.pow(rating - estimate, 2);
                        summedErrorAvgSq += Math.pow(rating - avgRating, 2);
                        ctr++;

                        if (avgRating == estimate) {
                            avg_used++;
                        } else {
                            est_used++;
                        }
                        if ((ctr % 50) == 0) {
                            elapsedTime = (int) (System.currentTimeMillis() / 1000) - startTime;
                            int remainingTime = (int) (elapsedTime * 698780f / ctr) - elapsedTime;
                            System.out.println("RMSE (default): " + Math.sqrt(summedErrorAvgSq / ctr)
                                    + " RMSE (recommender): " + Math.sqrt(summedErrorRecommenderSq / ctr)
                                    + " Time remaining: " + (int) ((remainingTime / (60 * 60)) % 24) + "h" + (int) ((remainingTime / 60) % 60)
                            );
                        }

                    }

                    userID = Integer.parseInt(tokens[0]);
                    movies.clear();
                    movies.add(Integer.parseInt(tokens[1]));
                    rtngs.clear();
                    rtngs.add(Double.parseDouble(tokens[2]));
                }
            }

            // The last batch is not handled in the loop so...
            double[] predictions = predictRating(userID,movies);
            for (int i = 0; i<movies.size(); i++){

                int movieID = movies.get(i);
                double rating = rtngs.get(i);

                double avgRating = ratings.getMovieAverageRating(movieID);

                double estimate = predictions[i];

                summedErrorRecommenderSq += Math.pow(rating - estimate,2);
                summedErrorAvgSq += Math.pow(rating - avgRating, 2);
                ctr++;

                if (avgRating == estimate) {
                    avg_used++;
                } else {
                    est_used++;
                }
                if ((ctr % 50) == 0) {
                    elapsedTime = (int)(System.currentTimeMillis()/1000) - startTime;
                    int remainingTime = (int) (elapsedTime * 698780f / ctr) - elapsedTime;
                    System.out.println("RMSE (default): " + Math.sqrt(summedErrorAvgSq/ctr)
                            + " RMSE (recommender): " + Math.sqrt(summedErrorRecommenderSq/ctr)
                            + " Time remaining: " + (int) ((remainingTime / (60*60)) % 24) + "h" + (int) ((remainingTime / 60) % 60)
                    );
                }
            }


//            System.out.println("RMSE (default): " + Math.sqrt(summedErrorAvgSq / ctr)
//                    + " RMSE (recommender): " + Math.sqrt(summedErrorRecommenderSq / ctr)
//            );

//            System.out.println(" Counter: " + ctr);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {

        String trainingFile = "";
        String testFile = "";
        String matrixFile = null;

        int leastCommon = 5;
        double threshold = 0.2;
        int adjThreshold = 15;



        int i = 0;
        while (i < args.length && args[i].startsWith("-")) {
            String arg = args[i];
            if(arg.equals("-trainingFile")) {
                trainingFile = args[i+1];
            } else if(arg.equals("-testFile")) {
                testFile = args[i+1];
            } else if(arg.equals("-matrixFile")) {
                matrixFile = args[i+1];
            } else if(arg.equals("-onlinePearson")) {
                onlinePearson = true;

                // ADD ADDITIONAL PARAMETERS HERE //
                // IN CASE OF INLINE
            } else if(arg.equals("-leastCommon")) {
                leastCommon = Integer.parseInt(args[i+1]);
            } else if(arg.equals("-threshold")) {
                threshold = Double.parseDouble(args[i+1]);
             } else if(arg.equals("-adjThreshold")) {
                adjThreshold = Integer.parseInt(args[i+1]);
            }

            i += 2;
        }

        ratings = new MovieHandler(trainingFile);
        if (!onlinePearson)
            // Load a precomputed Pearson correlation matrix
            similarities = new PearsonsCorrelation(ratings, matrixFile);
        else
            // Compute Pearson correlations on the fly.
            // Beware that this will be very slow!
            similarities = new PearsonsCorrelation(ratings, leastCommon, threshold, adjThreshold);

        evaluate(testFile);

    }

}
