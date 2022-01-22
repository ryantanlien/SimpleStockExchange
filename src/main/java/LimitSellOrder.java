import java.util.ArrayList;

public class LimitSellOrder implements SellOrder {
    private final String stockTicker;
    private final double sellingPrice;
    private final int stockNumber;
    private int fulfilledNumber = 0;
    private int unfulfilledNumber;
    private final int orderId;
    private String status;
    //write an enum for status

    private final ArrayList<Integer> fulfilledVolume;
    private final ArrayList<Double> fulfilledPrice;

    public LimitSellOrder(String stockTicker, double sellingPrice, int stockNumber, int orderId) {
        this.stockTicker = stockTicker;
        this.sellingPrice = sellingPrice;
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
        if (unfulfilledNumber == 0 ) {
            this.setStatus("FILLED");
        } else if (unfulfilledNumber != stockNumber) {
            this.setStatus("PFILLED");
        }
    }

    @Override
    public double getSellingPrice() {
        return this.sellingPrice;
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
        double priceSold;
        if (totalVolume != 0) {
            priceSold = totalPrice/totalVolume;
        } else {
            priceSold = 0;
        }
        return priceSold;
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
        String string = this.getStockTicker() + " LMT SELL $" + getSellingPrice() + " " + this.getFulfilledNumber() + "/"
                + this.getStockNumber() + " " + this.getStatus();
        return string;
    }

    private void setStatus(String status) {
        this.status = status;
    }
}
