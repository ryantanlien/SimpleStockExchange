import java.util.ArrayList;

public class MarketSellOrder implements SellOrder{
    private String stockTicker;
    private double priceSold;
    private int stockNumber;
    private int fulfilledNumber = 0;
    private int unfulfilledNumber;
    private int orderId;
    private String status;

    private final ArrayList<Integer> fulfilledVolume;
    private final ArrayList<Double> fulfilledPrice;

    public MarketSellOrder(String stockTicker, int stockNumber, int orderId) {
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
    public double getSellingPrice() {
        return 0;
    }

    @Override
    public String getStockTicker() {
        return this.stockTicker;
    }

    @Override
    public double getPriceSold() {
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
            this.priceSold = totalPrice/totalVolume;
        } else {
            this.priceSold = 0;
        }
        return this.priceSold;
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
    public void setFulfilledNumber(int numberFulfilled) {
        this.fulfilledNumber = numberFulfilled;
    }

    @Override
    public int compareTo(SellOrder sellOrder) {
        int value = Double.compare(this.getSellingPrice(), sellOrder.getSellingPrice());
        if (value == 0) {
            return Integer.compare(this.getOrderId(), sellOrder.getOrderId());
        }
        return value;
    }

    @Override
    public String toString() {
        String string = this.getStockTicker() + " MKT SELL $" + getSellingPrice() + " " + this.getFulfilledNumber() + "/"
                + this.getStockNumber() + " " + this.getStatus();
        return string;
    }

    private void setStatus(String status) {
        this.status = status;
    }
}
