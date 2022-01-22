public interface SellOrder extends Comparable<SellOrder>{
    public String getStockTicker();
    public double getSellingPrice();
    public double getPriceSold();
    public int getStockNumber();
    public void fillOrder(Integer stockFill, Double priceFill);
    public int getFulfilledNumber();
    public int getUnfulfilledNumber();
    public String getStatus();
    public int getOrderId();
    public void setFulfilledNumber(int numberFulfilled);
}
