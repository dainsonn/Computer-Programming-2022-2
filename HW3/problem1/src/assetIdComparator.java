import java.util.Comparator;

public class assetIdComparator implements Comparator<Asset> {
    @Override
    public int compare(Asset a1, Asset a2) {
        if(a1.getId() > a2.getId()){
            return 1;
        } else if(a1.getId() < a2.getId()){
            return -1;
        }
        return 0;
    }
}
