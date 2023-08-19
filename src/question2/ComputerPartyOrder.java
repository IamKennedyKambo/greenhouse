package question2;

import java.util.List;

class ComputerPartyOrder extends GenericOrder<Product> {

    private String partyTheme; // Additional attribute for party theme

    public ComputerPartyOrder(String partyTheme) {
        this.partyTheme = partyTheme;
    }

    public String getPartyTheme() {
        return partyTheme;
    }

    public void setPartyTheme(String theme) {
        this.partyTheme = theme;
    }

    public void addPeripherals(List<Peripheral> peripherals) {
        getItems().addAll(peripherals);
    }

    public void addCheeseAndFruit(List<Cheese> cheeses, List<Fruit> fruits) {
        getItems().addAll(cheeses);
        getItems().addAll(fruits);
    }

    public void addServices(List<Service> services) {
        getItems().addAll(services);
    }
}
