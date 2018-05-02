/**
 * This interface is used to select records
 *
 * @author Jessa Bekker
 */
public interface RecordSelector{
    boolean keep(String[] record);
}
