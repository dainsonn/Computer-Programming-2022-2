package hand.agent;

public class Buyer extends Agent{
    public Buyer(double priceLimit) {
        super(priceLimit);
    }

    @Override
    public boolean willTransact(double price) {
        if(price <= expectedPrice && !hadTransaction){
            return true;
        }
        else return false;
    }

    @Override
    public void reflect() {
        if(hadTransaction){
            expectedPrice -= adjustment;
            adjustment += 5;
            if(adjustment > adjustmentLimit){
                adjustment = adjustmentLimit;
            }
        }
        else{
            expectedPrice += adjustment;
            if(expectedPrice > priceLimit){
                expectedPrice = priceLimit;
            }
            else{
                adjustment -= 5;
                if(adjustment < 0){
                    adjustment = 0;
                }
            }
        }
        hadTransaction = false;
    }
}
