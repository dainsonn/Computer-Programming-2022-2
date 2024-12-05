import hand.agent.Buyer;
import hand.agent.Seller;
import hand.market.Market;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        System.out.println("2.1 Test Cases -----------------------------");
        subproblem1();
        System.out.println("2.2 Test Cases -----------------------------");
        subproblem2();
        System.out.println("2.3 Test Cases -----------------------------");
        subproblem3();
    }

    static void subproblem1() {
        Buyer buyer = new Buyer(200);
        Seller seller = new Seller(100);

        printOX("2.1.1. willTransact in Buyer and Seller",
                !buyer.willTransact(250) && buyer.willTransact(200)
                        && seller.willTransact(100) && !seller.willTransact(50));

        buyer.makeTransaction(); buyer.reflect();
        double e1 = buyer.getExpectedPrice();
        seller.makeTransaction(); seller.reflect();
        double e2 = seller.getExpectedPrice();

        printOX("2.1.2. reflect for one day", e1 == 190.0 && e2 == 110.0);
    }

    static void subproblem2() {
        Market market = new Market(0, null, 0, null);
        market.buyers = new ArrayList<>();
        for(int i=0;i<1000;i++) market.buyers.add(new Buyer(i * 5));
        market.sellers = new ArrayList<>();
        for(int i=0;i<1000;i++) market.sellers.add(new Seller(i * 5));
        //System.out.println(market.buyers.get(500).getExpectedPrice());
        //System.out.println(market.sellers.get(500).getExpectedPrice());
        //System.out.println(market.buyers.get(600).getExpectedPrice());
        //System.out.println(market.sellers.get(600).getExpectedPrice());
        market.simulate();

        //System.out.println(market.buyers.get(500).getExpectedPrice());
        //System.out.println(market.sellers.get(500).getExpectedPrice());
        //System.out.println(market.buyers.get(600).getExpectedPrice());
        //System.out.println(market.sellers.get(600).getExpectedPrice());

        double buyer2ExpectedPrice = market.buyers.get(500).getExpectedPrice();
        double buyer3ExpectedPrice = market.buyers.get(600).getExpectedPrice();
        double buyer4ExpectedPrice = market.buyers.get(market.buyers.size()-1).getExpectedPrice();
        double seller2ExpectedPrice = market.sellers.get(500).getExpectedPrice();
        double seller3ExpectedPrice = market.sellers.get(600).getExpectedPrice();
        double seller4ExpectedPrice = market.sellers.get(market.sellers.size()-1).getExpectedPrice();

        printOX("2.2.1. simulate market - 500th and 600th buyer/seller",
                buyer2ExpectedPrice == 2400.0 && seller2ExpectedPrice == 2500.0
                        && buyer3ExpectedPrice == 2495.0 && seller3ExpectedPrice == 3000.0);
        printOX("2.2.2. simulate market - final buyer/seller", buyer4ExpectedPrice == 2445.0 && seller4ExpectedPrice == 4995.0);
    }

    static void subproblem3() {
        ArrayList<Double> fb = new ArrayList<>();
        fb.add(1000.0); fb.add(3000.0);
        fb.add(2000.0); fb.add(1000.0);
        ArrayList<Double> fs = new ArrayList<>();
        fs.add(2000.0); fs.add(1000.0);
        fs.add(1000.0); fs.add(3000.0);
        Market market = new Market(1000, fb, 1000, fs);
        double price = market.simulate();
        printOX("2.3.1. equilibrium", String.format("%.5f", price).equals("3093.43499"));
    }

    static void printOX(String prompt,boolean condition){
        if(condition){
            System.out.println("" + prompt + " | O");
        }
        else{
            System.out.println("" + prompt + " | X");
        }
    }
}
