package question2;

import java.util.ArrayList;
import java.util.List;

class OrderProcessor {
    private final List<GenericOrder<?>> orders;

    public OrderProcessor() {
        orders = new ArrayList<>();
    }

    public void accept(GenericOrder<?> order) {
        orders.add(order);
    }

    public void process() {
        // Logic to sort and dispatch orders goes here
        // For demonstration purposes, let's just print the order details
        for (GenericOrder<?> order : orders) {
            System.out.println("Order number=" + order.getOrderNumber());
            List<?> items = order.getItems();
            for (Object item : items) {
                System.out.println(item);
            }
            System.out.println();
        }
    }

    // Define dispatchXXX methods here

    public void dispatchComputerParts() {
        System.out.println("Dispatching Computer Parts:");
        for (GenericOrder<?> order : orders) {
            List<?> items = order.getItems();
            for (Object item : items) {
                if (item instanceof ComputerPart) {
                    ComputerPart computerPart = (ComputerPart) item;
                    System.out.println(computerPart.getClass().getSimpleName() +
                            " name=" + computerPart.getManufacturer() +
                            ", price=$" + computerPart.price() +
                            ", order number=" + order.getOrderNumber() + "\n");
                }
            }
        }
    }

    public void dispatchPeripherals() {
        System.out.println("Dispatching Peripherals:");
        for (GenericOrder<?> order : orders) {
            List<?> items = order.getItems();
            for (Object item : items) {
                if (item instanceof Peripheral) {
                    Peripheral peripheral = (Peripheral) item;
                    System.out.println(peripheral.getClass().getSimpleName() +
                            " model=" + peripheral.getModel() +
                            ", price=$" + peripheral.price() +
                            ", order number=" + order.getOrderNumber() + "\n");
                }
            }
        }
    }

    public void dispatchCheeseAndFruit() {
        System.out.println("Dispatching Cheese and Fruit:");
        for (GenericOrder<?> order : orders) {
            List<?> items = order.getItems();
            for (Object item : items) {
                if (item instanceof Cheese || item instanceof Fruit) {
                    Product product = (Product) item;
                    System.out.println(product.getClass().getSimpleName() +
                            ", price=$" + product.price() +
                            ", order number=" + order.getOrderNumber() + "\n");
                }
            }
        }
    }

    public void dispatchServices() {
        System.out.println("Dispatching Services:");
        for (GenericOrder<?> order : orders) {
            List<?> items = order.getItems();
            for (Object item : items) {
                if (item instanceof Service) {
                    Service service = (Service) item;
                    System.out.println(service.getClass().getSimpleName() +
                            " name=" + service.getClass().getSimpleName() +
                            ", price=$" + service.price() +
                            ", order number=" + order.getOrderNumber());
                }
            }
        }
    }
}