import java.util.PriorityQueue;

public class BasicStockManager implements StockManager {
    private final Stock stock;
    private final PriorityQueue<BuyOrder> buyOrderQueue;
    private final PriorityQueue<SellOrder> sellOrderQueue;

    private double lastTradedPrice;

    public BasicStockManager(Stock stock) {
        this.stock = stock;
        this.buyOrderQueue = new PriorityQueue<BuyOrder>();
        this.sellOrderQueue = new PriorityQueue<SellOrder>();
        this.lastTradedPrice = 0;
    }

    @Override
    public BuyOrder addToBuyOrderQueue(LimitBuyOrder buyOrder) {
        SellOrder sellOrder = sellOrderQueue.peek();
        while (!sellOrderQueue.isEmpty() && sellOrder.getSellingPrice() <= buyOrder.getAskingPrice()
                && buyOrder.getUnfulfilledNumber() != 0) {
            int sellOrderVolume = sellOrder.getUnfulfilledNumber();
            int buyOrderVolume = buyOrder.getUnfulfilledNumber();
            if (buyOrderVolume >= sellOrderVolume) {
                if (sellOrder.getSellingPrice() == 0) {
                    //In the case that the order is a market sell order
                    buyOrder.fillOrder(sellOrderVolume, buyOrder.getAskingPrice());
                    sellOrder.fillOrder(sellOrderVolume, buyOrder.getAskingPrice());
                    lastTradedPrice = buyOrder.getAskingPrice();
                } else {
                    buyOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = sellOrder.getSellingPrice();
                }
                updateStockPrice(lastTradedPrice);
                sellOrderQueue.remove();
            } else {
                if (sellOrder.getSellingPrice() == 0 ) {
                    //In the case that the order is a market sell order
                    buyOrder.fillOrder(buyOrderVolume, buyOrder.getAskingPrice());
                    sellOrder.fillOrder(buyOrderVolume, buyOrder.getAskingPrice());
                    lastTradedPrice = buyOrder.getAskingPrice();
                } else {
                    buyOrder.fillOrder(buyOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(buyOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = sellOrder.getSellingPrice();
                }
                updateStockPrice(lastTradedPrice);
            }
            sellOrder = sellOrderQueue.peek();
        }
        if (buyOrder.getUnfulfilledNumber() != 0) {
            buyOrderQueue.add(buyOrder);
        }
        return buyOrder;
    }

    @Override
    public BuyOrder addToBuyOrderQueue(MarketBuyOrder buyOrder) {
        SellOrder sellOrder = sellOrderQueue.peek();
        while(!sellOrderQueue.isEmpty() && buyOrder.getUnfulfilledNumber() != 0) {
            int sellOrderVolume = sellOrder.getUnfulfilledNumber();
            int buyOrderVolume = buyOrder.getUnfulfilledNumber();
            if (buyOrderVolume >= sellOrderVolume) {
                //If the sellOrder is a marketSellOrder, then the trade will be executed at current price of the stock
                if (sellOrder.getSellingPrice() == 0) {
                    buyOrder.fillOrder(sellOrderVolume, stock.getPrice());
                    sellOrder.fillOrder(sellOrderVolume, stock.getPrice());
                } else {
                    buyOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = sellOrder.getSellingPrice();
                    updateStockPrice(lastTradedPrice);
                }
                sellOrderQueue.remove();
            } else {
                //If the sellOrder is a marketSellOrder, then the trade will be executed at current price of the stock
                if (sellOrder.getSellingPrice() == 0) {
                    buyOrder.fillOrder(buyOrderVolume, stock.getPrice());
                    sellOrder.fillOrder(buyOrderVolume, stock.getPrice());
                } else {
                    buyOrder.fillOrder(buyOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(buyOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = sellOrder.getSellingPrice();
                    updateStockPrice(lastTradedPrice);
                }
            }
            sellOrder = sellOrderQueue.peek();
        }
        if (buyOrder.getUnfulfilledNumber() != 0) {
            buyOrderQueue.add(buyOrder);
        }
        return buyOrder;
    }

    @Override
    public SellOrder addToSellOrderQueue(LimitSellOrder sellOrder) {
        BuyOrder buyOrder = buyOrderQueue.peek();
        while (!buyOrderQueue.isEmpty() && sellOrder.getSellingPrice() <= buyOrder.getAskingPrice()
                && sellOrder.getUnfulfilledNumber() != 0) {
            int sellOrderVolume = sellOrder.getUnfulfilledNumber();
            int buyOrderVolume = buyOrder.getUnfulfilledNumber();
            if (sellOrderVolume >= buyOrderVolume) {
                if (buyOrder.getAskingPrice() == Double.MAX_VALUE) {
                    buyOrder.fillOrder(buyOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(buyOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = sellOrder.getSellingPrice();
                } else {
                    buyOrder.fillOrder(buyOrderVolume, buyOrder.getAskingPrice());
                    sellOrder.fillOrder(buyOrderVolume, buyOrder.getAskingPrice());
                    lastTradedPrice = buyOrder.getAskingPrice();
                }
                updateStockPrice(lastTradedPrice);
                buyOrderQueue.remove();
            } else {
                if (buyOrder.getAskingPrice() == Double.MAX_VALUE) {
                    buyOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = sellOrder.getSellingPrice();
                } else {
                    buyOrder.fillOrder(sellOrderVolume, buyOrder.getAskingPrice());
                    sellOrder.fillOrder(sellOrderVolume, buyOrder.getAskingPrice());
                    lastTradedPrice = buyOrder.getAskingPrice();
                }
                updateStockPrice(lastTradedPrice);
            }
            buyOrder = buyOrderQueue.peek();
        }
        if (sellOrder.getUnfulfilledNumber() != 0) {
            sellOrderQueue.add(sellOrder);
        }
        return sellOrder;
    }

    @Override
    public SellOrder addToSellOrderQueue(MarketSellOrder sellOrder) {
        BuyOrder buyOrder = buyOrderQueue.peek();
        while(!buyOrderQueue.isEmpty() && sellOrder.getUnfulfilledNumber() != 0) {
            int sellOrderVolume = sellOrder.getUnfulfilledNumber();
            int buyOrderVolume = buyOrder.getUnfulfilledNumber();
            if (sellOrderVolume >= buyOrderVolume) {
                //If the buyOrder is a marketBuyOrder, then the trade will be executed at current price of the stock
                if (buyOrder.getAskingPrice() == Double.MAX_VALUE) {
                    buyOrder.fillOrder(buyOrderVolume, stock.getPrice());
                    sellOrder.fillOrder(buyOrderVolume, stock.getPrice());
                } else {
                    buyOrder.fillOrder(buyOrderVolume, buyOrder.getAskingPrice());
                    sellOrder.fillOrder(buyOrderVolume, buyOrder.getAskingPrice());
                    lastTradedPrice = buyOrder.getAskingPrice();
                    updateStockPrice(lastTradedPrice);
                }
                buyOrderQueue.remove();
            } else {
                //If the sellOrder is a marketSellOrder, then the trade will be executed at current price of the stock
                if (buyOrder.getAskingPrice() == Double.MAX_VALUE) {
                    buyOrder.fillOrder(sellOrderVolume, stock.getPrice());
                    sellOrder.fillOrder(sellOrderVolume, stock.getPrice());
                } else {
                    buyOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    sellOrder.fillOrder(sellOrderVolume, sellOrder.getSellingPrice());
                    lastTradedPrice = buyOrder.getAskingPrice();
                    updateStockPrice(lastTradedPrice);
                }
            }
            buyOrder = buyOrderQueue.peek();
        }
        if (sellOrder.getUnfulfilledNumber() != 0) {
            sellOrderQueue.add(sellOrder);
        }
        return sellOrder;
    }

    @Override
    public double getStockAskingPrice() {
        double askingPrice = 0;
        PriorityQueue<BuyOrder> temporaryQueue = new PriorityQueue<BuyOrder>();
        while (!buyOrderQueue.isEmpty()) {
            BuyOrder buyOrder = buyOrderQueue.poll();
            temporaryQueue.add(buyOrder);
            if (buyOrder instanceof LimitBuyOrder) {
                askingPrice = buyOrder.getAskingPrice();
                break;
            }
        }
        buyOrderQueue.addAll(temporaryQueue);
        return askingPrice;
    }

    @Override
    public double getStockSellingPrice() {
        double sellingPrice = 0;
        PriorityQueue<SellOrder> temporaryQueue = new PriorityQueue<SellOrder>();
        while(!sellOrderQueue.isEmpty()) {
            SellOrder sellOrder = sellOrderQueue.poll();
            temporaryQueue.add(sellOrder);
            if (sellOrder instanceof LimitSellOrder) {
                sellingPrice = sellOrder.getSellingPrice();
                break;
            }
        }
        sellOrderQueue.addAll(temporaryQueue);
        return sellingPrice;
    }

    @Override
    public double getStockLastTradedPrice() {
        return lastTradedPrice;
    }

    public void viewBuyOrderQueue() {
        System.out.println("All Orders in Buy Order Queue:");
        if(!buyOrderQueue.isEmpty()) {
            buyOrderQueue.forEach(buyOrder -> System.out.println(buyOrder.toString()));
        } else {
            System.out.println("Empty!\n");
        }
    }

    public void viewSellOrderQueue() {
        System.out.println("All Orders in Sell Order Queue:");
        if (!sellOrderQueue.isEmpty()) {
            sellOrderQueue.forEach(sellOrder -> System.out.println(sellOrder.toString()));
        } else {
            System.out.println("Empty!\n");
        }
    }

    private void updateStockPrice(double price) {
        this.stock.setPrice(price);
    }
}
