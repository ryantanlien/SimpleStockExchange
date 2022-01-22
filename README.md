# SimpleStockExchange
A SimpleStockExchange that handles trades on stocks using a PriorityQueue implementation

# Design Overview
## Data Classes
These classes are an encapsulation of the data in stock exchanges such as:
- Buy Orders 
- Sell Orders
- Stocks

The classes following this design are:
- Stock class
- LimitBuyOrder class
- MarketSellOrder class
- LimitSellOrder class
- MarketSellOrder class
  
## Manager Classes 
These classes handle business logic and interaction amongst classes, such as resolving trades between BuyOrders and SellOrders.
- StockManager class
  - Handles interactions between LimitBuyOrders, MarketBuyOrders, LimitSellOrders, and MarketSellOrders 
- Exchange class
  - Handles interactions between the App which takes in string inputs, the User, and the StockManager Classes.
- App class
  - Receives string inputs from the terminal and makes calls to the Exchange class.
 
## Exception Classes
StockNotFoundException is a custom exception class that extends the Exceptions class, and is thrown when users attempt to place a buy order, sell order or to get a quote on a stock that is not listed in the Exchange.

## Implementation Choices
LimitBuyOrder and MarketBuyOrder both implement the BuyOrder interfaces.
LimitSellOrder and MarketSellOrder both implement the SellOrder interfaces.
BasicStockManager implements the StockManager interface.

Implementing interfaces instead of inheritance is chosen for SellOrder and BuyOrder as inheritance causes a tighter coupling to the base class.
New types of buy orders and sell orders can be implemented by further implementing their respective interfaces or by using implementing from multiple new interfaces.
The same can be said for Stock Managers.

## Resolving Trades
Each Stock is placed into the Exchange and stored in a HashMap. StockManagers are also initialized for each Stock when a Stock is registered on the exchange. 
Each StockManager contains a PriorityQueue of BuyOrders and a PriorityQueue of SellOrders, sorted by lowest asking price (bid price) and highest selling price respectively. Any subtype implementing BuyOrder can be placed in the BuyOrder PriorityQueue and any subtype implementing SellOrder can be placed in the SellOrder PriorityQueue

Limit Order - Limit Order interactions as well as LimitOrder - MarketOrder interactions are not special cases, and are resolved how you would expect. 
Market Order - Market Order transactions are an issue as there no asking prices and selling prices for the orders. They are resolved using the last traded price for that stock on the exchange.
I recognize that this method is naive (actual exchanges probably place volumes and prices into buckets, regardless of order type; market orders resolve from these buckets), though it should more than suffcient for this simple exchange.

Once a trade is executed, its last traded price is recorded on the StockManager, volume and price per stock of the trade is stored in the BuyOrder and SellOrder implementing classes, allowing the actual aggregate price sold or bought to be obtained. Fulfilled volume and Order status are then changed for the sell order and buy order involved in the transaction. 

# Running the Program
## Requirements
In order to run this program, you are required to have a command line and Java 11 installed.

## Instructions
Open the project directory in the command line and navigate to this path location: SimpleStockManager/out/artifacts/SimpleStockExchange_jar
Execute this command in the terminal: java -jar SimpleStockExchange.jar

Alternatively, open the project in IntelliJ IDEA or Eclipse and run the App main class after building the Maven project using 

```mvn install``` 
then 
```mvn build```


