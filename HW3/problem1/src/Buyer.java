import java.util.*;

public class Buyer {
    private String buyername;
    private float balance;
    private Map<Integer, Pair<Asset, Float>> portfolio;

    public Buyer(String buyername) {
        this(buyername, 100000);
    }

    public Buyer(String buyername, float balance){
        this.balance = balance;
        this.buyername = buyername;
        portfolio = new HashMap<Integer, Pair<Asset, Float>>();
    }
    public float getBalance(){ return balance; }
    public void addAsset(Asset asset, float portion) {
        portfolio.put(asset.getId(), new Pair(asset, portion));
    }

    public Map<Integer, Pair<Asset, Float>> getPortfolio() {
        return portfolio;
    }

    public float getAssetPortion(int id) {
        float portion = 0;
        portion = portfolio.get(id).getValue();
        return portion;
    }

    public void setBalance(float price){
        balance += price;
    }

    public float getTotalValue() {
        float sum = balance;
        for(int idNum: portfolio.keySet()){
            sum += (portfolio.get(idNum).getKey().getPrice()) * (portfolio.get(idNum).getValue());
        }
        return sum;
    }

    @Override
    public String toString() {
        return buyername;
    }
}