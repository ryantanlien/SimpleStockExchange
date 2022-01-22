import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UnitTests {
    @Test
    public void testLimitBuyOrderCreation() {
        BuyOrder buyOrder = new LimitBuyOrder("SNAP", 100.0, 20, 1);
        assertEquals("SNAP" ,buyOrder.getStockTicker());
        assertEquals(100.0, buyOrder.getAskingPrice(), 0);
        assertEquals(buyOrder.getStockNumber(), 20);
        assertEquals(0, buyOrder.getFulfilledNumber());
        assertEquals(20, buyOrder.getUnfulfilledNumber());
        assertEquals(buyOrder.getOrderId(), 1);
        assertEquals(buyOrder.getStatus(), "PENDING");
    }

    @Test
    public void testLimitBuyOrderFill() {
        BuyOrder buyOrder = new LimitBuyOrder("SNAP", 100.0 , 20, 1);
        buyOrder.fillOrder(20, 100.0);
        assertEquals(buyOrder.getUnfulfilledNumber(), 0);
        assertEquals(buyOrder.getFulfilledNumber(), 20);
        assertEquals(buyOrder.getStatus(), "FILLED");
        assertEquals(buyOrder.getPriceBought(), 100.0, 0);
        BuyOrder buyOrder1 = new LimitBuyOrder("SNAP", 100.0, 20, 2);
        buyOrder1.fillOrder(10, 80.0);
        assertEquals(buyOrder1.getUnfulfilledNumber(), 10);
        assertEquals(buyOrder1.getFulfilledNumber(), 10);
        assertEquals(buyOrder1.getStatus(), "PFILLED");
        assertEquals(buyOrder1.getPriceBought(), 80.0, 0);
    }

    @Test
    public void testMarketBuyOrderCreation() {
        BuyOrder buyOrder = new MarketBuyOrder("SNAP", 20, 1);
        assertEquals("SNAP", buyOrder.getStockTicker());
        assertEquals(Double.MAX_VALUE, buyOrder.getAskingPrice(), 0);
        assertEquals(20, buyOrder.getStockNumber());
        assertEquals(20, buyOrder.getUnfulfilledNumber());
        assertEquals(0, buyOrder.getFulfilledNumber());
        assertEquals(1, buyOrder.getOrderId());
        assertEquals("PENDING", buyOrder.getStatus());
    }

    @Test
    public void testMarketBuyOrderFill() {
        BuyOrder buyOrder = new MarketBuyOrder("SNAP", 20 ,1);
        buyOrder.fillOrder(10, 10.00);
        assertEquals(10, buyOrder.getUnfulfilledNumber(), 0);
        assertEquals(10, buyOrder.getFulfilledNumber(), 0);
        assertEquals("PFILLED", buyOrder.getStatus());
        assertEquals(10.00, buyOrder.getPriceBought(), 0);
        buyOrder.fillOrder(10, 8.00);
        assertEquals(0, buyOrder.getUnfulfilledNumber(), 0);
        assertEquals(20, buyOrder.getFulfilledNumber(), 0);
        assertEquals("FILLED", buyOrder.getStatus());
        assertEquals(9.00, buyOrder.getPriceBought(), 0);
    }

    @Test
    public void testLimitSellOrderCreation() {
        SellOrder sellOrder = new LimitSellOrder("SNAP", 20.0, 10, 1);
        assertEquals("SNAP", sellOrder.getStockTicker());
        assertEquals(20.0, sellOrder.getSellingPrice(), 0);
        assertEquals(10, sellOrder.getStockNumber());
        assertEquals(1, sellOrder.getOrderId());
        assertEquals("PENDING", sellOrder.getStatus());
    }

    @Test
    public void testLimitSellOrderFill() {
        SellOrder sellOrder = new LimitSellOrder("SNAP", 100.0, 20, 1);
        sellOrder.fillOrder(10, 100.0);
        assertEquals(10, sellOrder.getUnfulfilledNumber());
        assertEquals(10, sellOrder.getFulfilledNumber());
        assertEquals(100.0, sellOrder.getPriceSold(), 0);
        assertEquals("PFILLED", sellOrder.getStatus());
        sellOrder.fillOrder(10, 120.0);
        assertEquals(0, sellOrder.getUnfulfilledNumber());
        assertEquals(20, sellOrder.getFulfilledNumber());
        assertEquals(110.0, sellOrder.getPriceSold(), 0);
        assertEquals("FILLED", sellOrder.getStatus());
    }

    @Test
    public void testMarketSellOrderCreation() {
        SellOrder sellOrder = new MarketSellOrder("SNAP", 20, 1);
        assertEquals("SNAP", sellOrder.getStockTicker());
        assertEquals(0, sellOrder.getSellingPrice(), 0);
        assertEquals(20, sellOrder.getStockNumber());
        assertEquals(1, sellOrder.getOrderId());
        assertEquals("PENDING", sellOrder.getStatus());
    }

    @Test
    public void testMarketSellOrderFill() {
        SellOrder sellOrder = new MarketSellOrder("SNAP", 20 ,1);
        sellOrder.fillOrder(10, 10.00);
        assertEquals(10, sellOrder.getUnfulfilledNumber(), 0);
        assertEquals(10, sellOrder.getFulfilledNumber(), 0);
        assertEquals("PFILLED", sellOrder.getStatus());
        assertEquals(10.00, sellOrder.getPriceSold(), 0);
        sellOrder.fillOrder(10, 8.00);
        assertEquals(0, sellOrder.getUnfulfilledNumber(), 0);
        assertEquals(20, sellOrder.getFulfilledNumber(), 0);
        assertEquals("FILLED", sellOrder.getStatus());
        assertEquals(9.00, sellOrder.getPriceSold(), 0);
    }

    @Test
    public void testStockCreation() {
        Stock stock = new Stock("SNAP");
        assertEquals("SNAP", stock.getTicker());
        assertEquals(0, stock.getPrice(), 0);
    }

    @Test
    public void testStockManagerLimitSellOrderLimitBuyOrder() {
        Stock stock = new Stock("SNAP");
        BuyOrder buyOrder = new LimitBuyOrder(stock.getTicker(), 50.0, 120, 1);
        SellOrder sellOrder = new LimitSellOrder(stock.getTicker(), 49.00, 100, 2);
        SellOrder sellOrder1 = new LimitSellOrder(stock.getTicker(), 49.00, 100, 3);
        BuyOrder buyOrder1 = new LimitBuyOrder(stock.getTicker(), 48.00, 120, 4);
        BasicStockManager stockManager = new BasicStockManager(stock);
        stockManager.addToSellOrderQueue((LimitSellOrder) sellOrder);
        stockManager.addToSellOrderQueue((LimitSellOrder) sellOrder1);
        assertEquals(49.00, stockManager.getStockSellingPrice(), 0);
        stockManager.addToBuyOrderQueue((LimitBuyOrder) buyOrder);
        assertEquals(49.00, stockManager.getStockLastTradedPrice(), 0);
        assertEquals(49.00, buyOrder.getPriceBought(), 0);
        assertEquals(0, buyOrder.getUnfulfilledNumber());
        assertEquals(120, buyOrder.getFulfilledNumber());
        assertEquals("FILLED", buyOrder.getStatus());
        assertEquals(49.00, sellOrder.getSellingPrice(), 0);
        assertEquals(0, sellOrder.getUnfulfilledNumber());
        assertEquals(100, sellOrder.getFulfilledNumber());
        assertEquals("FILLED", sellOrder.getStatus());
        assertEquals(49.00, sellOrder1.getSellingPrice(), 0);
        assertEquals(80, sellOrder1.getUnfulfilledNumber());
        assertEquals(20, sellOrder1.getFulfilledNumber());
        assertEquals("PFILLED", sellOrder1.getStatus());
        stockManager.addToBuyOrderQueue((LimitBuyOrder) buyOrder1);
        assertEquals(49.0, stockManager.getStockLastTradedPrice(), 0);
        assertEquals(48.0, stockManager.getStockAskingPrice(), 0);
        assertEquals(0, buyOrder1.getPriceBought(), 0);
        assertEquals(120, buyOrder1.getUnfulfilledNumber());
        assertEquals(0, buyOrder1.getFulfilledNumber());
        assertEquals("PENDING",buyOrder1.getStatus());
    }

    @Test
    public void testStockManagerMarketBuyOrderMarketSellOrder() {
        Stock stock = new Stock("SNAP");
        BuyOrder buyOrder = new MarketBuyOrder(stock.getTicker(), 300, 0);
        SellOrder sellOrder = new LimitSellOrder(stock.getTicker(), 120.0 , 100,  2);
        SellOrder sellOrder1 = new MarketSellOrder(stock.getTicker(), 120, 3);
        BasicStockManager stockManager = new BasicStockManager(stock);
        stockManager.addToSellOrderQueue((LimitSellOrder) sellOrder);
        stockManager.addToBuyOrderQueue((MarketBuyOrder) buyOrder);
        assertEquals(120.0, sellOrder.getPriceSold(), 0);
        assertEquals(0, sellOrder.getUnfulfilledNumber(), 0);
        assertEquals(100, sellOrder.getFulfilledNumber(), 0);
        assertEquals("FILLED", sellOrder.getStatus());
        assertEquals(120.0, buyOrder.getPriceBought(), 0);
        assertEquals(200, buyOrder.getUnfulfilledNumber(), 0);
        assertEquals(100, buyOrder.getFulfilledNumber(), 0);
        assertEquals("PFILLED", buyOrder.getStatus());
        stockManager.addToSellOrderQueue((MarketSellOrder) sellOrder1);
        assertEquals(120, sellOrder1.getPriceSold(), 0);
        assertEquals(0, sellOrder1.getUnfulfilledNumber(), 0);
        assertEquals(120, sellOrder1.getFulfilledNumber(), 0);
        assertEquals("FILLED", sellOrder1.getStatus());
        assertEquals(0, stockManager.getStockAskingPrice(), 0);
        assertEquals(0, stockManager.getStockSellingPrice(), 0);
    }

    @Test
    public void testExchangeRegisterStock() {
        Exchange exchange = new Exchange();
        exchange.registerStock("SNAP", 0);
        Stock SNAP = exchange.getStock("SNAP");
        assertNotNull(SNAP);
    }

    @Test
    public void testExchangeRegisterBuyOrders(){
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.registerStock("SNAP", 0);
        try {
            exchange.registerOrder("SNAP", "BUY", "LMT", 100, 30, user);
            exchange.registerOrder("SNAP", "BUY", "MKT", 100, 30, user);
        } catch (StockNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    public void testExchangeRegisterSellOrders(){
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.registerStock("SNAP", 0);
        try {
            exchange.registerOrder("SNAP", "SELL", "LMT", 100, 30, user);
            exchange.registerOrder("SNAP", "SELL", "MKT", 100, 30, user);
        } catch (StockNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    @Test(expected = StockNotFoundException.class)
    public void testExchangeRegisterLimitBuyOrderThrowsStockNotFoundException() throws StockNotFoundException {
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.registerOrder("SNAP", "BUY", "LMT", 100, 30, user);
    }

    @Test(expected = StockNotFoundException.class)
    public void testExchangeRegisterMarketBuyOrderThrowsStockNotFoundException() throws StockNotFoundException {
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.registerOrder("SNAP", "BUY", "MARKET", 100, 30, user);
    }

    @Test(expected = StockNotFoundException.class)
    public void testExchangeRegisterLimitSellOrderThrowsStockNotFoundException() throws StockNotFoundException {
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.registerOrder("SNAP", "SELL", "LMT", 100, 30, user);
    }

    @Test(expected = StockNotFoundException.class)
    public void testExchangeRegisterMarketSellOrderThrowsStockNotFoundException() throws StockNotFoundException {
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.registerOrder("SNAP", "SELL", "MKT", 100, 30, user);
    }

    @Test(expected = StockNotFoundException.class)
    public void testExchangeGetStockQuoteThrowsStockNotFoundException() throws StockNotFoundException {
        Exchange exchange = new Exchange();
        User user = new User();
        exchange.getStockQuote("SNAP");
    }
}
