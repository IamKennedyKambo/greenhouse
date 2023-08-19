package question2;

public class ComputerPartyOrderClient {
    public static void main(String[] args) {
        ComputerPartyOrder computerPartyOrder = new ComputerPartyOrder("Halloween");

        computerPartyOrder.addItem(new Motherboard("Asus", 37.5f));
        computerPartyOrder.addItem(new RAM("Kingston", 512, 25.0f));
        computerPartyOrder.addItem(new Printer("HP LaserJet", 150.0f));
        computerPartyOrder.addItem(new Cheddar(10.0f));
        computerPartyOrder.addItem(new Apple(5.0f));
        computerPartyOrder.addItem(new DeliveryService("Express", 20.0f));

        OrderProcessor orderProcessor = new OrderProcessor();
        orderProcessor.accept(computerPartyOrder);

        orderProcessor.process();

        System.out.println("\nDispatching Computer Parts:");
        orderProcessor.dispatchComputerParts();

        System.out.println("\nDispatching Peripherals:");
        orderProcessor.dispatchPeripherals();

        System.out.println("\nDispatching Cheese and Fruit:");
        orderProcessor.dispatchCheeseAndFruit();

        System.out.println("\nDispatching Services:");
        orderProcessor.dispatchServices();
    }
}
