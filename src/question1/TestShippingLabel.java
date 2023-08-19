package question1;

class FullName {
    private final String title;
    private final String firstName;
    private final String middleName;
    private final String lastName;

    public FullName(String title, String firstName, String middleName, String lastName) {
        this.title = title;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return title + " " + firstName + " " + (middleName.isEmpty() ? "" : middleName + " ") + lastName;
    }
}

class MailingAddress {
    private final FullName fullName;
    private final String streetAddress;
    private final String city;
    private final String province;
    private final String postalCode;

    public MailingAddress(FullName fullName, String streetAddress, String city, String province, String postalCode) {
        this.fullName = fullName;
        this.streetAddress = streetAddress;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return fullName + "\n" +
                streetAddress + "\n" +
                city + ", " + province + " " + postalCode;
    }
}

class ShippingLabel {
    private final MailingAddress shipFrom;
    private final MailingAddress shipTo;

    public ShippingLabel(MailingAddress shipFrom, MailingAddress shipTo) {
        this.shipFrom = shipFrom;
        this.shipTo = shipTo;
    }

    public void printLabel() {
        System.out.println("Ship From:\n" + shipFrom + "\n");
        System.out.println("Ship To:\n" + shipTo);
    }
}

public class TestShippingLabel {
    public static void main(String[] args) {
        FullName senderName = new FullName("Mr.", "John", "Doe", "");
        MailingAddress senderAddress = new MailingAddress(senderName, "123 Main St", "Cityville", "State", "12345");

        FullName recipientName = new FullName("Mrs.", "Jane", "", "Smith");
        MailingAddress recipientAddress = new MailingAddress(recipientName, "456 Elm St", "Townsville", "Province", "67890");

        ShippingLabel label = new ShippingLabel(senderAddress, recipientAddress);
        label.printLabel();
    }
}
