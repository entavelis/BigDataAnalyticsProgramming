import java.io.IOException;
import java.util.*;

/**
 * This class is a bitmap for querying the bitMap efficiently.
 *
 * @author Jessa Bekker.
 */
public class BitMap {

    private BitSet[] bitMap; // The bitmap, there is a bit set for every possible value of the attribute
    private Map<String, Integer> value2position; // This maps the possible values for the attribute to their position in the bitmap

    /**
     * Constructor of the bit map
     *
     * @param data           The original table thatkkjj contains the attribute
     * @param attributeIndex The column number of the attribute in the table
     * @throws IOException
     */
    public BitMap(DataTable data, int attributeIndex) throws IOException {

        // FILL IN HERE //
        // A bitmap index is simply a set of bit arrays
        // >> One array for each attribute value, INCLUDING NULL
        // >> #bits -> # records

        String[][] dat = data.getData();
        value2position = new HashMap();

        // Can we do it in one pass?

        // Computing the distinct number of attribute values
        int noValues = 0;
        for (int i = 0; i < dat.length; i++) {
            // Retrieve Attribute String Value
            String attr = dat[i][attributeIndex];

            // add it to position
            Integer position = value2position.putIfAbsent(attr, noValues);

            // update the distinct number of values
            // ATTENTION: count the no values afterwards?
            if (position == null) {
                noValues++;
                //position = noBits;
            }
        }


        // Filling up the bitmap
        bitMap = new BitSet[noValues];
        for (int i = 0; i < noValues; i++) bitMap[i] = new BitSet();

        for (int i = 0; i < dat.length; i++) {
            // Retrieve Attribute String Value
            String attr = dat[i][attributeIndex];

            //set bit value
            bitMap[value2position.get(attr)].set(i);
        }
    }





    /**
     * This returns the bit map. Be careful, bit sets are mutable!
     * @return the bit map
     */
    public BitSet[] getBitMap() {
        return bitMap;
    }

    /**
     * This returns the position of the given value of the attribute that is represented by this bitmap
     * @param value the value
     * @return the position of the given value
     */
    public int getPositionOf(String value) {
        return value2position.get(value);
    }


    /**
     * This method returns the records where the attribute of this bit map takes the ith value.
     *
     * @param bitmap the bitmap to select from
     * @param i the value to select on
     * @return the selected records
     */
    public static BitSet select(BitSet[] bitmap, int i) {
        BitSet selection;

//        selection = new BitSet();
        // FILL IN HERE //
//        try {
            selection = bitmap[i];
//        } catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Exception thrown:" + e);
//        }


        return selection;
    }

    /**
     * Returns BitSet given the input attribute
     * @param attribute the input attribute
     * @return the selected records
     */
    public BitSet selectAttribute(String attribute){
        return select(bitMap, value2position.get(attribute));
    }

    /**
     * Returns the number of records which have the input attribute
     * @param attribute the attribute String
     * @return the number of records that have the given attribute value
     */
    public int countAttribute(String attribute){
        return count(bitMap, value2position.get(attribute));
    }


    /**
     * This method returns the number of records in this bit map.
     * @param bitmap the BitSet of records
     * @return the number of records given a BitSet
     */
    public static int count(BitSet bitmap) {
        int count = 0;

        // FILL IN HERE //
//        byte[] byteArray;

//        byteArray = bitmap.toByteArray();


//        for (int j=0; j< byteArray.length; j++){
//            count += NumberOnes.getNumberOfOnes(byteArray[j]);
//        }
        count = bitmap.cardinality();
        return count;
    }
    /**
     * This method returns the number of records  where the attribute of this bit map takes the ith value.
     *
     * @param bitmap the bitmap to count in
     * @param i the value to select on
     * @return the number of records that have the given attribute value
     */
    public static int count(BitSet[] bitmap, int i) {
        int cnt = 0;

        // FILL IN HERE //
//        try {
//            cnt = count(bitmap[i]);
//        }
//        catch (ArrayIndexOutOfBoundsException e) {
//            System.out.println("Exception thrown:" + e);
//        }

        cnt = bitmap[i].cardinality();
        return cnt;
    }
}
