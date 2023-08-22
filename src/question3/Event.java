package question3;

public abstract class Event {
    private long eventTime;
    protected final long delayTime;
    private boolean running = false;

    private boolean suspended = true;

    public void suspend() {
        suspended = true;
    }

    public void resume() {
        suspended = false;
    }

    public Event(long delayTime) {
        this.delayTime = delayTime;
        start();
    }

    public void start() {
        eventTime = System.currentTimeMillis() + delayTime;
    }

    public void process() throws ControllerException {
        running = true;
        action();
    }

    public boolean ready() {
        return System.currentTimeMillis() >= eventTime;
    }

    public abstract void action() throws ControllerException;
}
