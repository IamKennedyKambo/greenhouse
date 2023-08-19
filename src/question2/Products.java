package question2;

abstract class Product {
    protected float price;

    // return the price of a particular product
    abstract float price();
}

//------------------------------------------------------------

class ComputerPart extends Product {

    protected String manufacturer;
    public ComputerPart(float p, String manufacturer) {
        price = p;
        this.manufacturer = manufacturer;
    }

    public float price() {
        return price;
    }

    public String getManufacturer(){
        return manufacturer;
    }
}

class Motherboard extends ComputerPart {
    protected String manufacturer;

    public Motherboard(String mfg, float p) {
        super(p, mfg);
        manufacturer = mfg;
    }
}

class RAM extends ComputerPart {
    protected int size;
    protected String manufacturer;

    public RAM(String mfg, int size, float p) {
        super(p, mfg);
        this.manufacturer = mfg;
        this.size = size;
    }

    public String getManufacturer() {
        return manufacturer;
    }
}

class Drive extends ComputerPart {
    protected String type;
    protected int speed;

    public Drive(String type, int speed, float p, String mfg) {
        super(p, mfg);
        this.type = type;
        this.speed = speed;
    }

    public String getType() {
        return type;
    }

    public int getSpeed() {
        return speed;
    }
}


class Peripheral extends Product {

    protected String model;
    public Peripheral(float p, String model) {
        price = p;
        this.model = model;
    }

    public float price() {
        return price;
    }

    public String getModel(){
        return model;
    }
}

class Printer extends Peripheral {
    protected String model;

    public Printer(String model, float p) {
        super(p, model);
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}

class Monitor extends Peripheral {
    protected String model;

    public Monitor(String model, float p, String n) {
        super(p, n);
        this.model = model;
    }

    public String getModel() {
        return model;
    }
}

class Service extends Product {
    public Service(float p) {
        price = p;
    }

    public float price() {
        return price;
    }


}

class AssemblyService extends Service {
    String provider;

    public AssemblyService(String pv, float p) {
        super(p);
        provider = pv;
    }

    public String getProvider() {
        return provider;
    }
}

class DeliveryService extends Service {
    String courier;

    public DeliveryService(String c, float p) {
        super(p);
        courier = c;
    }

    public String getCourier() {
        return courier;
    }
}

//-------------------------------------------------------
class Cheese extends Product {
    public Cheese(float p) {
        price = p;

    }

    public float price() {
        return price;
    }
}

class Cheddar extends Cheese {
    public Cheddar(float p) {
        super(p);
    }
}

class Mozzarella extends Cheese {
    public Mozzarella(float p) {
        super(p);
    }
}

class Fruit extends Product {
    public Fruit(float p) {
        price = p;
    }

    public float price() {
        return price;
    }
}

class Apple extends Fruit {
    public Apple(float p) {
        super(p);
    }
}

class Orange extends Fruit {
    public Orange(float p, String n) {
        super(p);
    }
}