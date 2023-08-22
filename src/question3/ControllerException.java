package question3;

public class ControllerException extends Exception {
    public String error;

    public ControllerException(String error) {
        this.error = error;
    }
}
