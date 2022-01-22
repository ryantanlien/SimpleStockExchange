import java.util.Locale;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        //Register current user
        User user = new User();
        User testUser = new User();
        Exchange exchange = new Exchange();

        //Registering Mock Stocks
        exchange.registerStock("SNAP", 0);
        exchange.registerStock("FB", 0);
        exchange.registerStock("GOOGL", 0);
        exchange.registerStock("AAPL", 0);
        try {
            exchange.registerOrder("GOOGL", "SELL", "LMT", 100, 30, testUser);
        } catch (StockNotFoundException e){
            System.out.println(e.getMessage());
        }

        //Terminal Window Implementation
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the simple stock exchange!");

        while (true) {
            System.out.println("Please input your desired action.");
            String string = scanner.nextLine();
            if (string.equals("VIEW ORDERS")) {
                user.viewAllOrders();
            } else if (string.equals("QUIT")){
                System.exit(0);
                break;
            } else if (string.contains("QUOTE")) {
                String[] stringArray = string.split(" ");
                if (stringArray.length != 2 || !stringArray[0].equals("QUOTE")) {
                    System.out.println("Invalid Action!");
                    continue;
                }
                String stockTicker = stringArray[1];
                try {
                    System.out.println(exchange.getStockQuote(stockTicker));
                } catch (StockNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String[] stringArray = string.split(" ");
                if (!stringArray[0].equals("BUY") && !stringArray[0].equals("SELL")) {
                    System.out.println("Invalid Action!");
                    continue;
                }
                if (stringArray.length < 3) {
                    System.out.println("Invalid Action!");
                    continue;
                }
                if (stringArray.length > 5) {
                    System.out.println("Invalid Action!");
                    continue;
                }
                if (!stringArray[2].equals("LMT") && !stringArray[2].equals("MKT")) {
                    System.out.println("Invalid Action!");
                    continue;
                }
                String stockTicker = stringArray[1];
                String buySell = stringArray[0];
                try {
                    if (stringArray.length == 5 && stringArray[2].equals("LMT") && stringArray[3].charAt(0) == '$') {
                        String stockNumberString = stringArray[4];
                        int stockNumber = Integer.parseInt(stockNumberString);
                        String stockPriceString = stringArray[3].substring(1);
                        double stockPrice = Double.parseDouble(stockPriceString);
                        try {
                            exchange.registerOrder(stockTicker, buySell, "LMT", stockNumber, stockPrice, user);
                            System.out.println("You have placed a limit " + buySell.toLowerCase(Locale.ROOT) +
                                    " order for " + stockNumberString + " " + stockTicker + " shares at $"
                                    + stockPriceString + " each");
                        } catch (StockNotFoundException e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (stringArray.length == 4 && stringArray[2].equals("MKT")) {
                        String stockNumberString = stringArray[3];
                        int stockNumber = Integer.parseInt(stockNumberString);
                        try {
                            exchange.registerOrder(stockTicker, buySell, "MKT", stockNumber, 0, user);
                            System.out.println("You have placed a limit " + buySell.toLowerCase(Locale.ROOT) +
                                    " order for " + stockNumberString + " " + stockTicker + " shares");
                        } catch (StockNotFoundException e) { //Change to StockNotFoundException
                            System.out.println(e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid Action!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Action! Stock number or stock price invalid!");
                }
            }
        }
    }
}
