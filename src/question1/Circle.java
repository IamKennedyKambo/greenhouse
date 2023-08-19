package question1;

public class Circle {
    private static final double MAX_RADIUS = 100.0; // You can adjust the maximum radius

    private double x;
    private double y;
    private double radius;

    // Default constructor
    public Circle() {
        this.x = 0.0;
        this.y = 0.0;
        this.radius = 1.0; // Default radius
        setRadius(this.radius);
    }

    // Constructor with parameters
    public Circle(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        setRadius(this.radius);
    }

    public double circumference() {
        return 2 * Math.PI * radius;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public void setRadius(double r) {
        this.radius = Math.min(r, MAX_RADIUS);
    }

    public void printAttributes() {
        System.out.println("Circle Attributes:");
        System.out.println("Coordinates (X, Y): " + x + ", " + y);
        System.out.println("Radius: " + radius);
        System.out.println("Circumference: " + circumference());
        System.out.println("Area: " + area());
    }

    public boolean isInside(double xPoint, double yPoint) {
        double distanceSquared = (xPoint - x) * (xPoint - x) + (yPoint - y) * (yPoint - y);
        return distanceSquared <= radius * radius;
    }

    public void move(int xOffset, int yOffset) {
        this.x += xOffset;
        this.y += yOffset;
    }
}
