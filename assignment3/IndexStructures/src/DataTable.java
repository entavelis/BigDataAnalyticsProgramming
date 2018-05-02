import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

/**
 * This class represents a table in a database and allows naive operations on it
 *
 * @author Jessa Bekker
 */

public class DataTable {

    private String[][] data; // the data is stored per record, so data[i][j] is attribute j from record i

    /**
     * Constructor of the data table
     *
     * @param dataPath path were the data is stored
     * @param nbRecords The number of records in the data
     * @param separator the character used as separator between features.
     * @throws IOException
     */
    public DataTable(String dataPath, int nbRecords, String separator) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(dataPath));

        data = new String[nbRecords][];
        for (int i = 0; i<nbRecords; i++)
            data[i] = br.readLine().split(separator);

        br.close();
    }

    public DataTable(String[][] data) {
        this.data = data;
    }

    /**
     * This returns the raw data. Be careful, arrays are mutable!
     *
     * @return the data
     */
    public String[][] getData(){
        return data;
    }

    /**
     * This returns the number of records in the table
     * @return the number of records
     */
    public int size() {
        return data.length;
    }

    /**
     * This method returns the records that were kept by the selector.
     * The selected records are the indexes of the array that have "true" as their value.
     *
     * @param selector The selector to choose records.
     * @return selected records
     */
    public BitSet select(RecordSelector selector) {
        BitSet selection = new BitSet();
        for (int i=0; i<data.length; i++) {
            if (selector.keep(data[i]))
                selection.set(i);
        }
        return selection;
    }

    /**
     * This method returns the records that were kept by the selector.
     * The selected records are the indexes of the array that have "true" as their value.
     *
     * @param selector The selector to choose records.
     * @return selected records
     */
    public int count(RecordSelector selector) {
        int count = 0;
        for (String[] record: data) {
            if (selector.keep(record)) {
                count++;
            }
        }
        return count;
    }


    /**
     * This method returns the records that were kept by the selector.
     * The selected records are the indexes of the array that have "true" as their value.
     *
     * @param recordSelector The selector to choose records.
     * @param valueSelector The selector to get a double from an record
     * @return selected records
     */
    public double sum(RecordSelector recordSelector, ValueSelector valueSelector) {
        double sum = 0.0;
        for (String[] record: data) {
            if (recordSelector.keep(record))
                sum+=valueSelector.select(record);
        }
        return sum;
    }


    //\begin{notInStub}
    // added because some student added this
    public double avg(RecordSelector recordSelector, ValueSelector valueSelector) {
        double sum = 0.0;
        double count = 0.0;
        for (String[] record: data) {
            if (recordSelector.keep(record)){
                sum+=valueSelector.select(record);
                count+=1;
            }
        }
        return sum/count;
    }

    //\end{notInStub}
}
