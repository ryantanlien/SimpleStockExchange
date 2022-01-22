import java.util.ArrayList;

public class MarketBuyOrder implements BuyOrder {
    private final String stockTicker;
    private double priceBought;
    private final int stockNumber;
    private int fulfilledNumber = 0;
    private int unfulfilledNumber;
    private final int orderId;
    private String status;

    private final ArrayList<Integer> fulfilledVolume;
    private final ArrayList<Double> fulfilledPrice;

    public MarketBuyOrder(String stockTicker, int stockNumber, int orderId) {
        this.stockTicker = stockTicker;
        this.stockNumber = stockNumber;
        this.unfulfilledNumber = stockNumber;
        this.orderId = orderId;
        this.setStatus("PENDING");
        fulfilledVolume = new ArrayList<Integer>();
        fulfilledPrice = new ArrayList<Double>();
    }

    @Override
    public void fillOrder(Integer stockFill, Double priceFill) {
        fulfilledVolume.add(stockFill);
        fulfilledPrice.add(priceFill);
        fulfilledNumber += stockFill;
        unfulfilledNumber = stockNumber - fulfilledNumber;
        if (unfulfilledNumber == 0) {
            this.setStatus("FILLED");
        } else if (unfulfilledNumber != stockNumber) {
            this.setStatus("PFILLED");
        }
    }

    @Override
    public double getAskingPrice() {
        return Double.MAX_VALUE;
    }

    @Override
    public String getStockTicker() {
        return this.stockTicker;
    }

    @Override
    //Calculates and returns the average price of the stocks bought.
    public double getPriceBought() {
        double totalPrice = 0;
        int totalVolume = 0;
        for (int i = 0; i < fulfilledVolume.size(); i++) {
            Integer volume = fulfilledVolume.get(i);
            Double price = fulfilledPrice.get(i);
            double pv = price * volume;
            totalPrice += pv;
            totalVolume += volume;
        }
        if (totalVolume != 0) {
            this.priceBought = totalPrice/totalVolume;
        } else {
            this.priceBought = 0;
        }
        return this.priceBought;
    }

    @Override
    public int getStockNumber() {
        return this.stockNumber;
    }

    @Override
    public int getFulfilledNumber() {
        return this.fulfilledNumber;
    }

    @Override
    public int getUnfulfilledNumber() {
        return this.unfulfilledNumber;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

    @Override
    public int getOrderId() {
        return this.orderId;
    }

    @Override
    public int compareTo(BuyOrder buyOrder) {
        int value = Double.compare(buyOrder.getAskingPrice(), this.getAskingPrice());
        if (value == 0) {
            return Integer.compare(this.getOrderId(), buyOrder.getOrderId());
        }
        return value;
    }

    @Override
    public String toString() {
        String string = this.getStockTicker() + " MKT BUY $" + getAskingPrice() + " " + this.getFulfilledNumber() + "/"
                + this.getStockNumber() + " " + this.getStatus();
        return string;
    }

    @Override
    public void setFulfilledNumber(int numberFulfilled) {
        this.fulfilledNumber = unfulfilledNumber;
    }

    private void setStatus(String status) {
        this.status = status;
    }
}
