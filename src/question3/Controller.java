package question3;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    public static final List<Event> eventList = new ArrayList<>();

    public void addEvent(Event c) {
        eventList.add(c);
    }

    public void shutdown(ControllerException controllerException) {
    }

    public void run() throws ControllerException {
        while (!eventList.isEmpty())
            for (Event e : new ArrayList<>(eventList))
                if (e.ready()) {
                    System.out.println(e);
                    {
                        e.process();
                        eventList.remove(e);
                    }

                }
    }

    public void suspendAllEvents() {
        for (Event e : new ArrayList<>(eventList)) {
            e.suspend();
        }
    }
}
