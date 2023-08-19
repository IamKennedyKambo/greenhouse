package question3;//package question3;//: innerclasses/GreenhouseControls.java

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

abstract class Event implements Runnable {
    private boolean paused = false;
    private long eventTime;
    protected final long delayTime;

    public Event(long delayTime) {
        this.delayTime = delayTime;
        start();
    }

    public void start() {
        eventTime = System.currentTimeMillis() + delayTime;
    }

    public boolean ready() {
        return System.currentTimeMillis() >= eventTime;
    }

    public abstract void action() throws ControllerException;

    public static void main(String[] args) {

    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify();
    }

    @Override
    public void run() {
        synchronized (this) {
            while (paused) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class Controller {
    private final List<Event> eventList = new ArrayList<>();

    public static boolean eventsPaused = false;

    public void addEvent(Event c) {
        eventList.add(c);
        System.out.println("Event added: " + c);
    }

//    public void run() throws ControllerException {
//        while (!eventList.isEmpty()) {
//            for (Event e : new ArrayList<>(eventList)) {
//                if (e.ready()) {
//                    System.out.println(e);
//                    System.out.println("Ready to execute: " + e);
//                    e.action();
//                    eventList.remove(e);
//                }
//            }
//        }
//    }

    public void run() throws ControllerException {
        while (!eventList.isEmpty()) {
            for (Event e : new ArrayList<>(eventList)) {
                if (!eventsPaused && e.ready()) {
                    System.out.println(e);
                    System.out.println("Ready to execute: " + e);
                    e.action();
                    eventList.remove(e);
                }
            }
            synchronized (this) {
                if (eventsPaused) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

public class GreenhouseControls extends Controller {
    private boolean light = false;
    private boolean water = false;
    private String thermostat = "Day";
    private String eventsFile = "examples.txt";
    private boolean fans = false;
    private boolean windowok = false;
    private boolean poweron = false;
    private int errorcode;
    Map<String, Object> stateVariables = new HashMap<>();

    interface Fixable {
        void fix();

        void log();
    }

    public class LightOn extends Event {
        public LightOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            light = true;
        }

        public String toString() {
            return "Light is on";
        }
    }

    public class LightOff extends Event {
        public LightOff(long delayTime) {
            super(delayTime);
        }

        public void action() {

            // physically turn off the light.
            light = false;
        }

        public String toString() {
            return "Light is off";
        }
    }

    public class WaterOn extends Event {
        public WaterOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            // Put hardware control code here.
            water = true;
        }

        public String toString() {
            return "Greenhouse water is on";
        }
    }

    public class WaterOff extends Event {
        public WaterOff(long delayTime) {
            super(delayTime);
        }

        public void action() {
            // Put hardware control code here.
            water = false;
        }

        public String toString() {
            return "Greenhouse water is off";
        }
    }

    public class ThermostatNight extends Event {
        public ThermostatNight(long delayTime) {
            super(delayTime);
        }

        public void action() {
            // Put hardware control code here.
            thermostat = "Night";
        }

        public String toString() {
            return "Thermostat on night setting";
        }
    }

    public class ThermostatDay extends Event {
        public ThermostatDay(long delayTime) {
            super(delayTime);
        }

        public void action() {
            // Put hardware control code here.
            thermostat = "Day";
        }

        public String toString() {
            return "Thermostat on day setting";
        }
    }

    public class Bell extends Event {
        private final int rings;

        public Bell(long delayTime, int rings) {
            super(delayTime);
            this.rings = rings;
        }

        public void action() {
            if (rings > 0) {
                System.out.println("Bing!");
                addEvent(new Bell(delayTime + 2000, rings - 1));
            }
        }

        public String toString() {
            return "Bell rings: " + rings;
        }
    }

    public class Restart extends Event {
        public Restart(long delayTime, String filename) {
            super(delayTime);
            eventsFile = filename;
        }

        public void action() {
            try {
                BufferedReader br = new BufferedReader(new FileReader(eventsFile));
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    long time = Long.parseLong(parts[1].trim().split("=")[1]);
                    String event = parts[0].trim().split("=")[1];
                    switch (event) {
                        case "ThermostatNight":
                            addEvent(new ThermostatNight(time));
                            break;
                        case "LightOn":
                            addEvent(new LightOn(time));
                            break;
                        case "WaterOff":
                            addEvent(new WaterOff(8000));
                            break;
                        case "ThermostatDay":
                            addEvent(new ThermostatDay(10000));
                            break;
                        case "Bell":
                            addEvent(new Bell(3000, 3));
                            break;
                        case "WaterOn":
                            addEvent(new WaterOn(6000));
                            break;
                        case "LightOff":
                            addEvent(new LightOff(4000));
                            break;
                        case "Terminate":
                            addEvent(new Terminate(12000));
                            break;
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(null, "No events file chosen. Choose an events file to proceed");
            }
        }

        public String toString() {
            return "Restarting system";
        }
    }

    public static class Terminate extends Event {
        public Terminate(long delayTime) {
            super(delayTime);
        }

        public void action() {
            System.exit(0);
        }

        public String toString() {
            return "Terminating";
        }
    }

    public class FansOff extends Event {
        public FansOff(long delayTime) {
            super(delayTime);
        }

        public void action() {
            fans = false;
        }

        public String toString() {
            return "Fans off";
        }
    }

    public class FansOn extends Event {
        public FansOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            fans = true;
        }

        public String toString() {
            return "Fans on";
        }
    }

    public class WindowMalfunction extends Event {
        public WindowMalfunction(long delayTime) {
            super(delayTime);
        }

        public void action() {
            windowok = false;
            poweron = false;
            try {
                throw new ControllerException("Window Malfunction detected.");
            } catch (ControllerException e) {
                throw new RuntimeException(e);
            }
        }

        public String toString() {
            return "Window malfunction detected";
        }
    }

    public class PowerOut extends Event {
        public PowerOut(long delayTime) {
            super(delayTime);
        }

        public void action() throws ControllerException {
            windowok = false;
            poweron = false;
            throw new ControllerException("Power Outage detected.");
        }

        public String toString() {
            return "Power out";
        }
    }

    public int getError() {
        return errorcode;
    }

    private Fixable getFixable(int errorCode) {
        switch (errorcode) {
            case 1:
                return new PowerOn();
            case 2:
                return new FixWindow();
            default:
                return null;
        }
    }

    public void shutdown() {
        System.out.println("Emergency shutdown initiated.");

        // Log the error to error.log
        String errorMessage = "Emergency shutdown due to ";
        if (errorcode == 1) {
            errorMessage += "Window Malfunction";
        } else if (errorcode == 2) {
            errorMessage += "Power Outage";
        } else {
            errorMessage += "Unknown Error";
        }
        String errorLog = new Date() + ": " + errorMessage;

        try (PrintWriter writer = new PrintWriter(new FileWriter("error.log", true))) {
            writer.println(errorLog);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Serialize and save the GreenhouseControls object
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dump.out"))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(1); // Exit the program with a non-zero status code
    }

    public static void printUsage() {
        System.out.println("Correct format: ");
        System.out.println("  java GreenhouseControls -f <filename>, or");
        System.out.println("  java GreenhouseControls -d dump.out");
    }

    private class PowerOn implements Fixable {
        public void fix() {
            poweron = true;
            errorcode = 0;
        }

        public void log() {
            // Log the fix to fix.log
            String fixLog = new Date() + ": Power fixed.";
            try (PrintWriter writer = new PrintWriter(new FileWriter("fix.log", true))) {
                writer.println(fixLog);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            return "Power on";
        }
    }

    private class FixWindow implements Fixable {
        public void fix() {
            windowok = true;
            errorcode = 0;
        }

        public void log() {
            // Log the fix to fix.log
            String fixLog = new Date() + ": Window fixed.";
            try (PrintWriter writer = new PrintWriter(new FileWriter("fix.log", true))) {
                writer.println(fixLog);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static class Restore {
        private final String filename;

        public Restore(String filename) {
            this.filename = filename;
        }

        public void restoreAndRun(GreenhouseControls gc) {
            // Load the dumped object and restore the system state
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
                GreenhouseControls restoredControls = (GreenhouseControls) in.readObject();
                // Restore the necessary variables and fix issues
                gc.light = restoredControls.light;
                gc.water = restoredControls.water;
                gc.thermostat = restoredControls.thermostat;
                gc.eventsFile = restoredControls.eventsFile;
                gc.fans = restoredControls.fans;
                gc.windowok = restoredControls.windowok;
                gc.poweron = restoredControls.poweron;
                gc.errorcode = restoredControls.errorcode;

                // Start running the restored system
                gc.run();
            } catch (IOException | ClassNotFoundException | ControllerException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void setVariable(String key, Object value) {
        stateVariables.put(key, value);
    }

    public synchronized Object getVariable(String key) {
        return stateVariables.get(key);
    }

    synchronized Event createEvent(String eventName, long delayTime) {
        try {
            Class<?> eventClass = Class.forName(eventName);
            Constructor<?> constructor = eventClass.getConstructor(long.class);
            return (Event) constructor.newInstance(delayTime);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to pause event processing
    public void pauseEvents() {
        eventsPaused = true;
    }

    // Method to resume event processing
    public void resumeEvents() {
        eventsPaused = false;
        synchronized (this) {
            notifyAll();
        }
    }

    public static void main(String[] args) {
        GreenhouseControls gc = new GreenhouseControls();

        if (args.length < 2) {
            printUsage();
            return;
        }

        String option = args[0];
        String filename = args[1];

        try {
            System.out.println(option);
            if (option.equals("-f")) {
                gc.addEvent(gc.new Restart(0, filename));
                gc.run();
            } else if (option.equals("-d")) {
                Restore restore = new Restore(filename);
                restore.restoreAndRun(gc);
            } else {
                System.out.println("Invalid option");
                printUsage();
            }
        } catch (ControllerException ce) {
            System.out.println(ce.getMessage());
            gc.shutdown();
        }
    }
}

