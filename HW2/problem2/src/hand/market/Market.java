package hand.market;

import hand.agent.Buyer;
import hand.agent.Seller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

class Pair<K, V>{
    public K key;
    public V value;
    Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getSeller() {
        return key;
    }

    public V getBuyer(){
        return value;
    }

}

public class Market {
    public ArrayList<Buyer> buyers;
    public ArrayList<Seller> sellers;

    public Market(int nb, ArrayList<Double> fb, int ns, ArrayList<Double> fs) {
        buyers = createBuyers(nb, fb);
        sellers = createSellers(ns, fs);
    }

    private ArrayList<Pair<Seller, Buyer>> matchedPairs(int day, int round) {
        if(buyers==null || sellers==null) return null;
        ArrayList<Seller> shuffledSellers = new ArrayList<>(sellers);
        ArrayList<Buyer> shuffledBuyers = new ArrayList<>(buyers);
        Collections.shuffle(shuffledSellers, new Random(71 * day + 43 * round + 7));
        Collections.shuffle(shuffledBuyers, new Random(67 * day + 29 * round + 11));
        ArrayList<Pair<Seller, Buyer>> pairs = new ArrayList<>();
        for (int i = 0; i < shuffledBuyers.size(); i++) {
            if (i < shuffledSellers.size()) {
                pairs.add(new Pair<>(shuffledSellers.get(i), shuffledBuyers.get(i)));
            }
        }
        return pairs;
    }

    public double simulate() {
        for (int day = 1; day <= 2000; day++) {// do not change this line
            for (int round = 1; round <= 10; round++) { // do not change this line
                ArrayList<Pair<Seller, Buyer>> pairs = matchedPairs(day, round); // do not change this line
                if (pairs != null) {
                    for (int i = 0; i < pairs.size(); i++) {
                        if(day == 2000 && pairs.get(i).getBuyer() == buyers.get(500)){
                            //System.out.println(pairs.get(i).getSeller().getExpectedPrice());
                            //System.out.println(pairs.get(i).getBuyer().getExpectedPrice());
                        }
                        double price = pairs.get(i).getSeller().getExpectedPrice();
                        boolean boolSellerWillTransact = pairs.get(i).getSeller().willTransact(price);
                        boolean boolBuyerWillTransact = pairs.get(i).getBuyer().willTransact(price);
                        if (boolSellerWillTransact && boolBuyerWillTransact) {
                            pairs.get(i).getSeller().makeTransaction();
                            pairs.get(i).getBuyer().makeTransaction();
                        }
                        if(day == 2000 && pairs.get(i).getBuyer() == buyers.get(500)){
                            //System.out.println(pairs.get(i).getSeller().getHadTransaction());
                            //System.out.println(pairs.get(i).getBuyer().getHadTransaction());
                        }
                    }
                }
            }
            if (buyers != null) {
                for (int i = 0; i < buyers.size(); i++) {
                    buyers.get(i).reflect();
                    sellers.get(i).reflect();
                }
            }
        }
        return 0.0;
    }

    private ArrayList<Buyer> createBuyers(int n, ArrayList<Double> f) {
        //TODO: Problem 2.3
        return null;
    }


    private ArrayList<Seller> createSellers(int n, ArrayList<Double> f) {
        //TODO: Problem 2.3
        return null;
    }
}
