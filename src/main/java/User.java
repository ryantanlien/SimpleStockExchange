import java.util.ArrayList;

public class User {

    private final ArrayList<BuyOrder> buyOrders;
    private final ArrayList<SellOrder> sellOrders;

    public User() {
        buyOrders = new ArrayList<BuyOrder>();
        sellOrders = new ArrayList<SellOrder>();
    }

    public BuyOrder addToBuyOrderList(BuyOrder buyOrder) {
        buyOrders.add(buyOrder);
        return buyOrder;
    }

    public BuyOrder removeFromBuyOrderList(BuyOrder buyOrder) {
        buyOrders.remove(buyOrder);
        return buyOrder;
    }

    public SellOrder addToSellOrderList(SellOrder sellOrder) {
        sellOrders.add(sellOrder);
        return sellOrder;
    }

    public SellOrder removeFromSellOrderList(SellOrder sellOrder) {
        sellOrders.remove(sellOrder);
        return sellOrder;
    }

    public void viewAllOrders() {
        boolean buyOrdersEmpty = buyOrders.isEmpty();
        boolean sellOrdersEmpty = sellOrders.isEmpty();

        if (!buyOrdersEmpty) {
            buyOrders.forEach(buyOrder -> System.out.println(buyOrder.toString()));
        }
        if (!sellOrdersEmpty) {
            sellOrders.forEach(sellOrder -> System.out.println(sellOrder.toString()));
        }
        if (buyOrdersEmpty && sellOrdersEmpty) {
            System.out.println("No orders under this user!");
        }
    }
}
