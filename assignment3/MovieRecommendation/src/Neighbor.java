/**
 * Simple class introduced for convenience, a neighbor has an ID and
 * a similarity to a certain user.
 *
 * @author Toon Van Craenendonck
 *
 */
public class Neighbor implements Comparable<Neighbor>{

    int id;
    double similarity;

    public Neighbor(int id, double similarity) {
        this.id = id;
        this.similarity = similarity;
    }

    public int compareTo(Neighbor nb) {
        if (similarity < nb.getSimilarity()) {
            return -1;
        } else if (similarity == nb.getSimilarity()){
            return 0;
        } else{
            return 1;
        }
    }

    public double getSimilarity() {
        return similarity;
    }

    public int getUserID() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Neighbor other = (Neighbor) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
