import java.util.HashMap;

public class Exchange {
    private final HashMap<String, Stock> stockHashMap;
    private final HashMap<String, StockManager> stockManagerHashMap;
    private static int orderId = 0;

    public Exchange() {
        stockHashMap = new HashMap<>();
        stockManagerHashMap = new HashMap<>();
    }

    public Stock registerStock(String ticker, double price) {
        Stock stock = new Stock(ticker);
        stock.setPrice(price);
        stockHashMap.put(ticker, stock);
        StockManager stockManager = createBasicStockManager(ticker);
        stockManagerHashMap.put(ticker, stockManager);
        return stock;
    }

    public void registerOrder(String ticker, String buySell, String orderType, int stockNumber, double stockPrice, User user)
            throws IllegalArgumentException, StockNotFoundException {
        if (stockHashMap.get(ticker) == null) {
            throw new StockNotFoundException("Stock with supplied stock ticker does not exist!");
        } else {
            if (buySell.equals("BUY") && orderType.equals("LMT")) {
                StockManager stockManager = stockManagerHashMap.get(ticker);
                BuyOrder limitBuyOrder = new LimitBuyOrder(ticker, stockPrice, stockNumber, orderId++);
                stockManager.addToBuyOrderQueue((LimitBuyOrder) limitBuyOrder);
                user.addToBuyOrderList(limitBuyOrder);
            } else if (buySell.equals("BUY") && orderType.equals("MKT")) {
                StockManager stockManager = stockManagerHashMap.get(ticker);
                BuyOrder marketBuyOrder = new MarketBuyOrder(ticker, stockNumber, orderId++);
                stockManager.addToBuyOrderQueue((MarketBuyOrder) marketBuyOrder);
                user.addToBuyOrderList(marketBuyOrder);
            } else if (buySell.equals("SELL") && orderType.equals("LMT")) {
                StockManager stockManager = stockManagerHashMap.get(ticker);
                SellOrder limitSellOrder = new LimitSellOrder(ticker, stockPrice, stockNumber, orderId++);
                stockManager.addToSellOrderQueue((LimitSellOrder) limitSellOrder);
                user.addToSellOrderList(limitSellOrder);
            } else if (buySell.equals("SELL") && orderType.equals("MKT")) {
                StockManager stockManager = stockManagerHashMap.get(ticker);
                SellOrder marketSellOrder = new MarketSellOrder(ticker, stockNumber, orderId++);
                stockManager.addToSellOrderQueue((MarketSellOrder) marketSellOrder);
                user.addToSellOrderList(marketSellOrder);
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    public String getStockQuote(String ticker) throws StockNotFoundException {
        if (stockHashMap.get(ticker) == null) {
            throw new StockNotFoundException("Stock with supplied stock ticker does not exist!");
        }
        StockManager stockManager = stockManagerHashMap.get(ticker);
        double stockAskingPrice = stockManager.getStockAskingPrice();
        double stockSellingPrice = stockManager.getStockSellingPrice();
        double stockLastTradedPrice = stockManager.getStockLastTradedPrice();
        return ticker + " BID: $" + stockAskingPrice + " ASK: $" + stockSellingPrice + " LAST: $" + stockLastTradedPrice;
    }

    public Stock getStock(String ticker) {
        return stockHashMap.get(ticker);
    }

    private StockManager createBasicStockManager(String ticker) {
        Stock stock = this.getStock(ticker);
        BasicStockManager basicStockManager = new BasicStockManager(stock);
        stockManagerHashMap.put(stock.getTicker(), basicStockManager);
        return basicStockManager;
    }
}
