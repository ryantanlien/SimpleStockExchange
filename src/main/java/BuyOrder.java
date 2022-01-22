public interface BuyOrder extends Comparable<BuyOrder> {
    public double getAskingPrice();
    public String getStockTicker();
    public double getPriceBought();
    public int getStockNumber();
    public void fillOrder(Integer stockFill, Double priceFill);
    public int getFulfilledNumber();
    public int getUnfulfilledNumber();
    public String getStatus();
    public int getOrderId();
    public void setFulfilledNumber(int numberFulfilled);
}
