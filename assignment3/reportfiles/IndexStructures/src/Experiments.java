import java.io.IOException;
import java.sql.Time;
import java.util.BitSet;

/**
 * This class executes the experiments
 *
 * @author Jessa Bekker
 */
public class Experiments {

    private DataTable data;

    //indexes
    private BitSlice priceIndex;
    private BitMap styleIndex;
    private BitMap volumeIndex;


    // column numbers in the data
    int name = 0;
    int price = 1;
    int style = 2;
    int volume = 3;

    public Experiments(DataTable data, BitSlice priceIndex, BitMap styleIndex, BitMap volumeIndex){
        this.data = data;
        this.priceIndex = priceIndex;
        this.styleIndex = styleIndex;
        this.volumeIndex = volumeIndex;
    }

    /////////////
    // QUERY 1 //
    /////////////

    /**
     * Which seasonal beers are offered?
     * (Select all seasonal beers)
     */



    public BitSet query1Naive() {
        // FILL IN HERE //
        return data.select(new RecordSelector() {
            @Override
            public boolean keep(String[] record) {
                return record[style].equals("Seasonal");
            }
        });
    }

    public BitSet query1WithIndexes() {
        // FILL IN HERE //
        return styleIndex.selectAttribute("Seasonal");
    }


    /////////////
    // QUERY 2 //
    /////////////

    /**
     * How many beers have a volume of 33 cl?
     */

    public int query2Naive() {
        // FILL IN HERE //
        // ATTENTION: Is this okay?
        return data.count(new RecordSelector() {
            @Override
            public boolean keep(String[] record) {
                return record[volume].equals("33");
            }
        });
    }

    public int query2WithIndexes() {
        // FILL IN HERE //
        return volumeIndex.countAttribute("33");
    }


    /////////////
    // QUERY 3 //
    /////////////

    /**
     * How much would does it cost to order one of each beer?
     * (What is the sum of all the prices?)
     */

    public double query3Naive() {
        // FILL IN HERE //
        return data.sum(new RecordSelector() {
            @Override
            public boolean keep(String[] record) { return true;}
            }, new ValueSelector(){
            @Override
            public double select(String[] record) { return Double.parseDouble(record[price]); }
            });
    }

    public double query3WithIndexes() {
        // FILL IN HERE //
        return priceIndex.sumAll();
    }


    /////////////
    // QUERY 4 //
    /////////////

    /**
     * How many beers cost more than 10 euros?
     */

    public int query4Naive() {
        // FILL IN HERE //
        // ATTENTION: check if better way to compare
        return data.count(new RecordSelector() {
           @Override
           public boolean keep(String[] record) { return Double.parseDouble(record[price]) > 10;}
           });
   }

    public int query4WithIndexes() {
        // FILL IN HERE //
        // Counts the second value of range bitSet array corresponding to "Greater Than"
        BitSet filter =  priceIndex.rangeOf(10.0)[1];
        return BitMap.count(filter) ;
    }



    /////////////
    // QUERY 5 //
    /////////////

    /**
     * What is the average price of a 25 cl Pils?
     */

    public double query5Naive() {
         // FILL IN HERE //
        return data.avg(new RecordSelector() {
            @Override
            public boolean keep(String[] record) { return record[volume].equals("25") && record[style].equals("Pils");}
            }, new ValueSelector(){
            @Override
            public double select(String[] record) { return Double.parseDouble(record[price]); }
            });
    }       // FILL IN HERE //


    public double query5WithIndexes() {
        // FILL IN HERE //
        BitSet selection = styleIndex.selectAttribute("Pils");
        selection.and(volumeIndex.selectAttribute("25"));

        int cnt = BitMap.count(selection);

        double sum = priceIndex.sumSubset(selection);

        return sum/cnt;
    }



    /////////////
    // QUERY 6 //
    /////////////

    /**
     * How many Abbey beers cost 3.00 EUR or less?
     */

    public int query6Naive() {
        // FILL IN HERE //
       return data.count(new RecordSelector() {
           @Override
           public boolean keep(String[] record) {
               return record[style].equals("Abbey beers") && Double.parseDouble(record[price]) <= 3;
           }});
    }


    public int query6WithIndexes() {
        // FILL IN HERE //
        BitSet selection = styleIndex.selectAttribute("Abbey beers");

        //ATTENTION: change operations' order
        BitSet[] filter = priceIndex.rangeOf(3.00);

        filter[0].or(filter[2]);
        selection.and(filter[0]);

        return BitMap.count(selection);
    }


    public static void main(String[] args) throws IOException {

        if (args.length < 3) {
            throw new IllegalArgumentException("Not enough arguments. Expected 3 but got "+args.length+"\nExperiments <datapath> <nbRecords> <separator>");
        }
        String dataPath = args[0];
        int nbRecords = Integer.parseInt(args[1]);
        String separator = args[2];

        DataTable data = new DataTable(dataPath, nbRecords, separator);
        BitSlice priceIndex = new BitSlice(data, 1, 13, 100);
        BitMap styleIndex =  new BitMap(data, 2);
        BitMap volumeIndex = new BitMap(data,3);

        Experiments experiments = new Experiments(data,priceIndex,styleIndex,volumeIndex);

        long start,stop, naiveTime, indexTime;
        BitSet solutionNaiveBS, solutionWithIndexesBS;
        int solutionNaiveInt, solutionWithIndexesInt;
        double solutionNaiveDouble, solutionWithIndexesDouble;

        int noExperiments = 10;
        // Query 1

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionNaiveBS = experiments.query1Naive();
        stop = System.nanoTime();
        naiveTime = stop-start;

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionWithIndexesBS = experiments.query1WithIndexes();
        stop = System.nanoTime();
        indexTime = stop-start;

        System.out.println("query 1\tnaive: "+naiveTime+"\twith indexes: "+indexTime);
        System.out.println("Improvement "+ 1.0*indexTime/naiveTime);
//        System.out.println(solutionNaiveBS.equals(solutionWithIndexesBS));
//        System.out.println(solutionNaiveBS);
//        System.out.println(solutionWithIndexesBS);



        // Query 2

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionNaiveInt = experiments.query2Naive();
        stop = System.nanoTime();
        naiveTime = stop-start;

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionWithIndexesInt = experiments.query2WithIndexes();
        stop = System.nanoTime();
        indexTime = stop-start;

        System.out.println("query 2\tnaive: "+naiveTime+"\twith indexes: "+indexTime);
        System.out.println("Improvement "+ 1.0*indexTime/naiveTime);
//        System.out.println(solutionNaiveInt == (solutionWithIndexesInt));
//        System.out.println(solutionNaiveInt);
//        System.out.println(solutionWithIndexesInt);




        // Query 3

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionNaiveDouble = experiments.query3Naive();
        stop = System.nanoTime();
        naiveTime = stop-start;

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionWithIndexesDouble = experiments.query3WithIndexes();
        stop = System.nanoTime();
        indexTime = stop-start;

        System.out.println("query 3\tnaive: "+naiveTime+"\twith indexes: "+indexTime);
        System.out.println("Improvement "+ 1.0*indexTime/naiveTime);
//        System.out.println(solutionNaiveDouble == (solutionWithIndexesDouble));
//        System.out.println(solutionNaiveDouble);
//        System.out.println(solutionWithIndexesDouble);




        // Query 4

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionNaiveInt = experiments.query4Naive();
        stop = System.nanoTime();
        naiveTime = stop-start;

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionWithIndexesInt= experiments.query4WithIndexes();
        stop = System.nanoTime();
        indexTime = stop-start;

        System.out.println("query 4\tnaive: "+naiveTime+"\twith indexes: "+indexTime);
        System.out.println("Improvement "+ 1.0*indexTime/naiveTime);
//        System.out.println(solutionNaiveInt == (solutionWithIndexesInt));
//        System.out.println(solutionNaiveInt);
//        System.out.println(solutionWithIndexesInt);







        // Query 5

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionNaiveDouble = experiments.query5Naive();
        stop = System.nanoTime();
        naiveTime = stop-start;

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionWithIndexesDouble= experiments.query5WithIndexes();
        stop = System.nanoTime();
        indexTime = stop-start;

        System.out.println("query 5\tnaive: "+naiveTime+"\twith indexes: "+indexTime);
        System.out.println("Improvement "+ 1.0*indexTime/naiveTime);
//        System.out.println(solutionNaiveDouble == (solutionWithIndexesDouble));
//        System.out.println(solutionNaiveDouble);
//        System.out.println(solutionWithIndexesDouble);




        // Query 6

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionNaiveInt = experiments.query6Naive();
        stop = System.nanoTime();
        naiveTime = stop-start;

        start = System.nanoTime();
        for (int i = 0; i < noExperiments ; i++)
            solutionWithIndexesInt = experiments.query6WithIndexes();
        stop = System.nanoTime();
        indexTime = stop-start;

        System.out.println("query 6\tnaive: "+naiveTime+"\twith indexes: "+indexTime);
        System.out.println("Improvement "+ 1.0*indexTime/naiveTime);
//        System.out.println(solutionNaiveInt == (solutionWithIndexesInt));
//        System.out.println(solutionNaiveInt);
//        System.out.println(solutionWithIndexesInt);



    }

}
