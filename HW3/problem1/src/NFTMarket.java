import java.util.*;

public class NFTMarket {

    private Map<Integer, Asset> idAsset = new HashMap<>();
    private Map<String, Buyer> nameBuyer = new HashMap<>();

    public Map<Integer, Asset> getIdAsset() {return idAsset;}
    public Map<String, Buyer> getNameBuyer() {return nameBuyer;}

    public Buyer master;

    public NFTMarket(Buyer master) {
        this.master = master;
        nameBuyer.put("Master", master);
    }


    public boolean addAsset(int id, String item, float price, String artist){
        if(idAsset.containsKey(id) || id < 0){
            return false;
        } else if(price < 0 || price > 100000) {
            return false;
        } else {
            Asset asset = new Asset(id, item, price, artist, master);
            idAsset.put(id, asset);
            return true;
        }
    }
    public boolean addBuyer(String buyername){
        if(nameBuyer.containsKey(buyername)){
            return false;
        } else if(buyername.equals(null) || buyername.isBlank()){
            return false;
        } else {
            Buyer buyer = new Buyer(buyername);
            nameBuyer.put(buyername, buyer);
            return true;
        }
    }
    public Asset findAsset(int id){
        return idAsset.get(id);
    }
    public Buyer findBuyer(String buyername){
        return nameBuyer.get(buyername);
    }
    public List<Asset> findAssetsWithConditions(int minprice, int maxprice, String item, String artist){
        List<Asset> assetsWithConditions = new ArrayList<>();

        if(minprice == -1 && maxprice == -1){
            assetsWithConditions = checkByString(item, artist);
        }
        else if(minprice != -1 && maxprice != -1){
            assetsWithConditions = checkByString(item, artist);
            for(int i=0; i<assetsWithConditions.size(); i++){
                if(assetsWithConditions.get(i).getPrice() < minprice || assetsWithConditions.get(i).getPrice() > maxprice){
                    assetsWithConditions.remove(i);
                    i = -1;
                }
            }
        }

        assetIdComparator comparator = new assetIdComparator();

        Collections.sort(assetsWithConditions, comparator);
        return assetsWithConditions;
    }
    public List<Asset> checkByString(String item, String artist){
        List<Asset> tAssets = new ArrayList<>();
        if(item.equals("All") && artist.equals("All")){
            for(Asset asset: idAsset.values()){
                tAssets.add(asset);
            }
        } else if(item.equals("All") && !artist.equals("All")){
            for(Asset asset: idAsset.values()){
                if(asset.getArtist().equals(artist)){
                    tAssets.add(asset);
                }
            }
        } else if(!item.equals("All") && artist.equals("All")){
            for(Asset asset: idAsset.values()){
                if(asset.getItem().equals(item)){
                    tAssets.add(asset);
                }
            }
        } else {
            for(Asset asset: idAsset.values()){
                if(asset.getItem().equals(item) && asset.getArtist().equals(artist)){
                    tAssets.add(asset);
                }
            }
        }
        return tAssets;
    }

    public boolean trade(Buyer seller, Buyer buyer, int id, float portion){
        Asset asset = findAsset(id);

        if(asset == null){
            return false;
        } else if(seller == null || buyer == null) {
            return false;
        } else if(!asset.getOwners().contains(seller)) {
            return false;
        } else if(buyer.getBalance() < asset.getPrice()) {
            return false;
        } else {
            float tradePortion = portion * seller.getAssetPortion(id);
            if(asset.getOwners().contains(buyer)){
                buyer.getPortfolio().replace(id, new Pair<>(asset, buyer.getAssetPortion(id) + tradePortion));
            }
            else{
                buyer.addAsset(asset, tradePortion);
                asset.getOwners().add(buyer);
            }

            seller.getPortfolio().replace(id, new Pair<>(asset, seller.getAssetPortion(id) - tradePortion));
            if(seller.getAssetPortion(id) == 0) {
                seller.getPortfolio().remove(id);
                asset.getOwners().remove(seller);
                }

            seller.setBalance(tradePortion * asset.getPrice());
            buyer.setBalance(- tradePortion * asset.getPrice());
            return true;
        }
    }


    public void reflectIssues(Asset asset, float effectFactor) {
        asset.setPrice(effectFactor);
    }

    public void reflectIssues(String artist, float effectFactor) {
        List<Asset> assetsByArtist = findAssetsWithConditions(-1, -1, "All", artist);
        for(Asset asset: assetsByArtist){
            asset.setPrice(effectFactor);
        }
    }
}
