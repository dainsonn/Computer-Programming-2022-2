package hand.agent;

public abstract class Agent {
    protected double priceLimit;
    protected double expectedPrice;
    protected double adjustment;
    protected double adjustmentLimit;
    protected boolean hadTransaction;

    Agent(double priceLimit) {
        this.priceLimit = priceLimit;
        expectedPrice = priceLimit;
        adjustment = 10;
        adjustmentLimit = 100;
        hadTransaction = false;
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }
    public double getAdjustment() {
        return adjustment;
    }

    public void makeTransaction() {
        hadTransaction = true;
    }

    abstract public boolean willTransact(double price);
    abstract public void reflect();

    public boolean getHadTransaction() { return hadTransaction; }
}
