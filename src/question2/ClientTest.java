package question2;

class ClientTest {
    public static void main(String[] args) {
        OrderProcessor orderProcessor = new OrderProcessor();

        ComputerOrder computerOrder = new ComputerOrder();
        computerOrder.addItem(new Motherboard("Asus", 37.5f));
        computerOrder.addItem(new RAM("Kingston", 512, 25.0f));
        computerOrder.addItem(new Printer("Epson", 12.56f));
        orderProcessor.accept(computerOrder);

        PartyTrayOrder partyTrayOrder = new PartyTrayOrder();
        partyTrayOrder.addItem(new Cheddar(10.0f));
        partyTrayOrder.addItem(new Apple(5.0f));
        orderProcessor.accept(partyTrayOrder);

        orderProcessor.process();

        //Dispatching Computer Parts
        orderProcessor.dispatchComputerParts();

        //Dispatching Peripherals
        orderProcessor.dispatchPeripherals();

        //Dispatching Cheese and Fruit:
        orderProcessor.dispatchCheeseAndFruit();
    }
}
