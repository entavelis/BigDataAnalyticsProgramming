import java.io.IOException;
import java.util.BitSet;

/**
 * This class is a bit slice for querying numerical attributes efficiently.
 *
 * @author Jessa Bekker.
 */
public class BitSlice {

    private BitSet[] slice; // The bitslice, there is a slice for every bit of the attribute
    private int amplification; // store 2.70 as 270 => the amplification is 100
    private int numberOfRecords; // stores the number of records we have in the bitSlice
    // needs to be updated if we implement an add record method

    /**
     * Constructor of the bit slice.
     *
     * @param data The original table that contains the attribute
     * @param attributeIndex The column number of the attribute in the table
     * @param encodingLength The number of bits to be used to encode the numbers
     * @param amplification The amplification of the values (used to encode doubles as integers)
     */
    public BitSlice(DataTable data, int attributeIndex, int encodingLength, int amplification) {
        this.amplification = amplification;

        // FILL IN HERE //
        String[][] dat = data.getData();

        slice = new BitSet[encodingLength];
        for (int i = 0 ; i<encodingLength; i++) slice[i] = new BitSet(dat.length);

        for (int i = 0; i < dat.length; i++){
//            int attr = (int) (Double.parseDouble(dat[i][attributeIndex]) * amplification);
            int attr = Integer.parseInt(dat[i][attributeIndex].replace(".",""));

            //ATTENTION: check for better implementation
            // The LSB is at position 0
            for (int j=0; j < encodingLength; j ++){
                slice[j].set(i, (attr & 1)>0);
                attr = attr>>>1;
            }

        }

        this.numberOfRecords = dat.length;

    }


    /**
     * This returns the bit slice. Be careful, bit sets are mutable!
     * @return the bit slice
     */
    public BitSet[] getSlice() {
        return slice;
    }

    /**
     *This returns a subset of the bit slice. This is equivalent to setting the other values to zero.
     *
     * @param bitSlice the bit slice to return a subset from.
     * @param filter the bitset to use as filter. If a record is not in the filter, set the value to zero.
     * @return the subset
     */
    public static BitSet[] getSubSlice(BitSet[] bitSlice, BitSet filter) {
        BitSet[] subset = new BitSet[bitSlice.length];

        // FILL IN HERE //
        // ATTENTION: check implementation
        for (int i = 0; i< subset.length; i++){
            subset[i] = (BitSet) filter.clone();
            subset[i].and(bitSlice[i]);
        }
        return  subset;
    }

    /**
     * This returns the amplification
     * @return amplification
     */
    public int getAmplification() {
        return amplification;
    }

    /**
     * This method answers the range query.
     * It returns the bitsets that respectively tell in which records the value is less than (LT), greater than (GT) and equal (EQ) to the number c.
     * @param c the number to compare with
     * @return the selected records for [LT,GT,EQ]
     */
    public BitSet[] rangeOf(double c) {
        return range(c,slice,amplification,numberOfRecords);
    }


    /**
     * This method answers the range query.
     * It returns the bitsets that respectively tell in which records the value is less than (LT), greater than (GT) and equal (EQ) to the number c.
     *
     * @param c the number to compare with
     * @param bitSlice the bit slice used to calculate the ranges in
     * @param amplification the amplification used on the numbers in the bit slice
     * @param nbRecords The number of records in the bitslice.
     * @return the selected records for [LT,GT,EQ]
     */
    public static BitSet[] range(double c, BitSet[] bitSlice, int amplification, int nbRecords) {

        BitSet lt = new BitSet(nbRecords);
        BitSet gt = new BitSet(nbRecords);
        BitSet eq = new BitSet();


        // FILL IN HERE //
        // Set eq ATTENTION: flip or set?
        eq.set(0,nbRecords);
//        eq.flip(0,nbRecords);

        // Amplify
        int cInt = (int) (c * amplification);


        for (int i = bitSlice.length -1 ; i > -1; i--){
            // a mask to check if i-th bit is 1
            int mask = 1 << i;

            BitSet bi = bitSlice[i];

            if ((cInt & mask) != 0) {
                // Check Mutability
                BitSet temp = (BitSet) eq.clone();
                temp.andNot(bi);
                lt.or(temp);
                eq.and(bi);
            }
            else {
                BitSet temp = (BitSet) eq.clone();
                temp.and(bi);
                gt.or(temp);
                eq.andNot(bi);
            }



        }

        return new BitSet[]{lt,gt,eq};
    }


    /**
     * Returns sum of all values in the slice
     * @return the sum of all values
     */
    public double sumAll(){
        return sum(slice,amplification);
    }

    /**
     * Returns sum of values given the input filter
     * @param filter the filter BitSet
     * @return the sum of values that satisfy the input filter
     */
    public double sumSubset(BitSet filter){
        return sum(getSubSlice(slice,filter),amplification);
    }

    /**
     * This method calculates the sum of the numbers in the bit slice.
     *
     * @param bitSlice the bitslice
     * @param amplification: The number with which the values in the bitslice are amplified.
     * @return the sum of the numbers in the bit slice. The sum should not be amplified!
     */
    public static double sum(BitSet[] bitSlice, int amplification) {
        double sum = 0.0;
        int encodingLength = bitSlice.length;
        // FILL IN HERE //

        int sumi = 0;
        // Uses BitMap's static count to compute the count and then applying the weights by shifting
//        for (int i = 0; i < encodingLength; i++) sum += BitMap.count(bitSlice, i) << i;
        for (int i = 0; i < encodingLength; i++) sumi += bitSlice[i].cardinality() << i;

        sum = 1.0*sumi/amplification;

        return sum;
    }





}
