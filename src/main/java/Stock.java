public class Stock {
    private String ticker = "";
    private double price = 0;

    public Stock(String ticker) {
        this.ticker = ticker;
    }

    public String getTicker() {
        return this.ticker;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double targetPrice) {
        price = targetPrice;
    }
}
