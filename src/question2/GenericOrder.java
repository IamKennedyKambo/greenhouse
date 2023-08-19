package question2;

import java.util.ArrayList;
import java.util.List;

class GenericOrder<T extends Product> {
    private static int orderCounter = 0;
    private int orderNumber;
    private List<T> items;

    public GenericOrder() {
        orderCounter++;
        orderNumber = orderCounter;
        items = new ArrayList<>();
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void addItem(T item) {
        items.add(item);
    }

    public List<T> getItems() {
        return items;
    }
}

class ComputerOrder extends GenericOrder<Product> {
    // Additional methods specific to ComputerOrder can be implemented here
}

class PartyTrayOrder extends GenericOrder<Product> {
    // Additional methods specific to PartyTrayOrder can be implemented here
}



