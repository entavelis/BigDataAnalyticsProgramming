/**
 * Simple class to store movies together with their rating.
 *
 * @author Toon Van Craenendonck
 *
 */
public class MovieRating implements Comparable<MovieRating>{

    private int movieID;
    private double rating;

    public MovieRating(int movieID, double rating) {
        this.movieID = movieID;
        this.rating = rating;
    }

    public int getMovieID() {
        return movieID;
    }

    public double getRating() {
        return rating;
    }

    @Override
    /**
     * Changed to sort on MovieID;
     */
    public int compareTo(MovieRating r) {
        if (movieID < r.getMovieID()) {
            return -1;
        } else if (movieID == r.getMovieID()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "[ID: "+getMovieID()+", Rating: " + getRating() + "]";
    }

    public boolean equals(MovieRating r) {
        return (movieID == r.movieID);
    }

    @Override
    public int hashCode(){
        return movieID;
    }

}
