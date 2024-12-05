package hand.agent;

public class Seller extends Agent{
    public Seller(double priceLimit) {
        super(priceLimit);
    }

    @Override
    public boolean willTransact(double price) {
        if(price >= expectedPrice && !hadTransaction){
            return true;
        }
        else return false;
    }

    @Override
    public void reflect() {
        if(hadTransaction){
            expectedPrice += adjustment;
            adjustment -= 5;
            if(adjustment < 0){
                adjustment = 0;
            }
        }
        else{
            expectedPrice -= adjustment;
            if(expectedPrice < priceLimit){
                expectedPrice = priceLimit;
            }
            else{
                adjustment += 5;
                if(adjustment > adjustmentLimit){
                    adjustment = adjustmentLimit;
                }
            }
        }
        hadTransaction = false;
    }
}
