public interface StockManager {
    public BuyOrder addToBuyOrderQueue(LimitBuyOrder buyOrder);
    public BuyOrder addToBuyOrderQueue(MarketBuyOrder buyOrder);
    public SellOrder addToSellOrderQueue(LimitSellOrder sellOrder);
    public SellOrder addToSellOrderQueue(MarketSellOrder sellOrder);
    public double getStockAskingPrice();
    public double getStockSellingPrice();
    public double getStockLastTradedPrice();
}
