package question3;

import java.io.*;
import java.util.Date;
import java.util.Scanner;

import static question4.GreenhouseControlsGUI.gui;

public class GreenhouseControls extends Controller implements Serializable {
    // Fields that set variables to proper status for testing purposes
    private volatile boolean suspended = false; // Add this line
    private boolean light = false;
    private boolean water = false;
    private String thermostat = "Day";
    public static String eventsFile = "examples4.txt";
    private boolean fans = false;
    private boolean windowok = true;
    private boolean poweron = true;
    private int errorCode;
    private int checkerror = 0;
    private boolean isRunning = false;
    private boolean isSuspended = false;

    public class FansOn extends Event {
        public FansOn(long delayTime) {
            super(delayTime);
        }

        @Override
        public void action() {
            // Put hardware control code here to
            // physically turn on the light.
            fans = true;
            logEventState("Fans on");
        }

        public String toString() {
            return "Fan is on";
        }
    }

    public class FansOff extends Event {
        public FansOff(long delayTime) {
            super(delayTime);
        }

        @Override
        public void action() {
            // Put hardware control code here to
            // physically turn on the light.
            fans = false;
            logEventState("Fans off");
        }

        public String toString() {
            return "Fan is off";
        }
    }

    public class LightOn extends Event {
        public LightOn(long delayTime) {
            super(delayTime);
        }

        public void action() {
            // Put hardware control code here to
            // physically turn on the light.
            light = true;
            logEventState("Light on");
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
            // Put hardware control code here to
            // physically turn off the light.
            light = false;
            logEventState("Light off");
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
            logEventState("Greenhouse water is on");
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
            logEventState("Greenhouse water is off");
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
            logEventState("Thermostat on night setting");
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
            logEventState("Thermostat on day setting");
            thermostat = "Day";
        }

        public String toString() {
            return "Thermostat on day setting";
        }
    }

    public class Bell extends Event {
        public Bell(long delayTime, long rings) {
            super(delayTime);
            long ringTime = 0;
            // 2000ms after each ring more bell events are added
            if (rings > 0) {
                for (int i = 1; i < Math.toIntExact(rings); i++) {
                    int difference = (i * 2000);
                    ringTime = difference + delayTime;
                    addEvent(new Bell(delayTime, 0));
                }
                start();
            }
        }

        public void action() {
            logEventState("Bell ring");
        }

        public String toString() {
            return "Bing!";
        }
    }

    public class Restart extends Event {
        public Restart(long delayTime, String filename) {
            super(delayTime);
            eventsFile = filename;
        }

        // action() method that starts the system by reading from a text file
        public void action() {
            // Field that sets functionality variables and creates a file for scanning
            File file = new File(eventsFile);
            String line = "";
            String event = "";
            long time = 0;
            // try statement to scan given text file
            try {
                Scanner scan = new Scanner(file);
                boolean limit = scan.hasNextLine();
                // if statements to check if WindowMalfunction or PowerOut has occured
                if (checkerror == 1) {
                    while (!(event.equals("WindowMalfunction")) && limit) {
                        line = scan.nextLine();
                        event = getEventName(line);
                    }
                    time = getEventTime(line) + 1;
                } else if (checkerror == 2) {
                    while (!(event.equals("PowerOut")) && limit) {
                        line = scan.nextLine();
                        event = getEventName(line);
                    }
                    time = getEventTime(line) + 1;
                }
                scan.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // try statement to scan given text file
            try {
                Scanner scanner = new Scanner(file);
                // while loop that scans through the text file
                while (scanner.hasNextLine()) {
                    line = scanner.nextLine();
                    event = getEventName(line);
                    long rings = 0;
                    long measure = 0;
                    // if statement to check if event is bell and the number of rings is given
                    if (event.equals("Bell")) {
                        if (line.contains("rings")) {
                            rings = getBellRings(line);
                            measure = getBellTime(line);
                        } else {
                            measure = getEventTime(line);
                        }
                    } else {
                        measure = getEventTime(line);
                    }
                    // execution variable that is used when new events are added
                    long execution = measure - time;
                    long x = 15000;
                    // enhanced switch statements to add the correct event for each line
                    if (execution > 0) {
                        switch (event) {
                            case "ThermostatDay" -> addEvent(new ThermostatDay(execution));
                            case "ThermostatNight" -> addEvent(new ThermostatNight(execution));
                            case "FansOff" -> addEvent(new FansOff(execution));
                            case "FansOn" -> addEvent(new FansOn(execution));
                            case "LightOff" -> addEvent(new LightOff(execution));
                            case "LightOn" -> addEvent(new LightOn(execution));
                            case "WaterOff" -> addEvent(new WaterOff(execution));
                            case "WaterOn" -> addEvent(new WaterOn(execution));
                            case "Terminate" -> addEvent(new Terminate(execution));
                            case "WindowMalfunction" -> addEvent(new WindowMalfunction(execution));
                            case "PowerOut" -> addEvent(new PowerOut(execution));
                            case "Bell" -> addEvent(new Bell(execution, rings));
                        }
                    }
                }
                logEventState("Restarting system");
                scanner.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        public String toString() {
            return "Restarting system";
        }
    }

    public String getEventName(String line) {
        String name;
        Scanner scanner = new Scanner(line);
        String[] part1 = scanner.nextLine().split("=");
        String[] part2 = part1[1].split(",");
        name = part2[0];
        return name;
    }

    public long getEventTime(String line) {
        long time;
        Scanner scanner = new Scanner(line);
        String[] part1 = scanner.nextLine().split("=");
        time = Long.parseLong(part1[2]);
        return time;
    }

    public long getBellTime(String line) {
        long time;
        Scanner scanner = new Scanner(line);
        String[] part = scanner.nextLine().split("=");
        String[] part2 = part[2].split(",");
        time = Long.parseLong(part2[0]);
        return time;
    }

    public long getBellRings(String line) {
        long rings;
        Scanner scanner = new Scanner(line);
        String[] part = scanner.nextLine().split("=");
        rings = Long.parseLong(part[3]);
        return rings;
    }

    public class WindowMalfunction extends Event {
        public WindowMalfunction(long delayTime) {
            super(delayTime);
        }

        @Override
        public void action() {
            errorCode = 1;
            windowok = false;
            String error = "Window Malfunction";
            logEventState("Window malfunctioned");
        }

        public String toString() {
            return "Window Malfunction";
        }
    }

    public class PowerOut extends Event {
        public PowerOut(long delayTime) {
            super(delayTime);
        }

        @Override
        public void action() {
            errorCode = 2;
            poweron = false;
            String error = "Power Outage";
            logEventState("Power outage experienced");
        }

        public String toString() {
            return "Power Outage";
        }
    }

    public static class Terminate extends Event {
        public Terminate(long delayTime) {
            super(delayTime);
        }

        public void action() {
            logEventState("Terminating operation");
//            System.exit(0);
        }

        public String toString() {
            return "Terminating";
        }
    }

    public static void printUsage() {
        System.out.println("Correct format: ");
        System.out.println("  java GreenhouseControls -f <filename>, or");
        System.out.println("  java GreenhouseControls -d dump.out");
    }

    @Override
    public void shutdown(ControllerException controllerException) {
        System.out.println(controllerException.error);
        dumpOut();
        errorLog();
        System.exit(0);
    }

    public void errorLog() {
        File file = new File("error.txt");
        Date date = new Date(System.currentTimeMillis());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedWriter bufferedWriter = getBufferedWriter(file, date);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedWriter getBufferedWriter(File file, Date date) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        if (errorCode == 1) {
            bufferedWriter.write("Warning! Window Malfunction");
            bufferedWriter.newLine();
            bufferedWriter.write("Time stamp: ");
            bufferedWriter.write(String.valueOf(date));
        } else if (errorCode == 2) {
            bufferedWriter.write("Warning! Power Outage");
            bufferedWriter.newLine();
            bufferedWriter.write("Time stamp: ");
            bufferedWriter.write(String.valueOf(date));
        }
        return bufferedWriter;
    }

    public void dumpOut() {
        String dump = "dump.out";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dump);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class FIxWindow implements Fixable {
        public void fix() {
            windowok = true;
            errorCode = 0;
        }

        public void log() {
            File file = new File("fix.log");
            Date date = new Date(System.currentTimeMillis());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write("Window Malfunction | Fixed");
                bufferedWriter.newLine();
                bufferedWriter.write("Time Stamp: ");
                bufferedWriter.write(String.valueOf(date));
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class PowerOn implements Fixable {
        public void fix() {
            poweron = true;
            errorCode = 0;
        }

        public void log() {
            File file = new File("fix.log");
            Date date = new Date(System.currentTimeMillis());
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                bufferedWriter.write("Power Outage | Fixed");
                bufferedWriter.newLine();
                bufferedWriter.write("Time Stamp: ");
                bufferedWriter.write(String.valueOf(date));
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int getError() {
        return errorCode;
    }

    public Fixable getFixable(int errorCode) {
        Fixable fixable = null;
        switch (errorCode) {
            case 1 -> fixable = new FIxWindow();
            case 2 -> fixable = new PowerOn();
            default -> System.out.println("Errors: 0");
        }
        return fixable;
    }

    public static class Restore {
        GreenhouseControls greenhouseControls = new GreenhouseControls();
        String path = "";

        public void fix(String filenname) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path + filenname));
                greenhouseControls = (GreenhouseControls) objectInputStream.readObject();
                // if statements to print state of system to console
                if (!greenhouseControls.windowok) {
                    System.out.println("Warning! Window Malfunction");
                }
                if (!greenhouseControls.poweron) {
                    System.out.println("Warning! Power Outage");
                }
                if (greenhouseControls.light) {
                    System.out.println("Light | on");
                } else {
                    System.out.println("Light | off");
                }
                if (greenhouseControls.water) {
                    System.out.println("Water | on");
                } else {
                    System.out.println("Water | off");
                }
                if (greenhouseControls.fans) {
                    System.out.println("Fans | on");
                } else {
                    System.out.println("Fans | off");
                }
                if (greenhouseControls.thermostat.equals("Day")) {
                    System.out.println("Thermostat | Day");
                } else if (greenhouseControls.thermostat.equals("Night")) {
                    System.out.println("Thermostat | Night");
                }
                // gets errorCode value and sets it for tracking
                int errorCode = greenhouseControls.getError();
                greenhouseControls.checkerror = errorCode;
                // creates Fixable with errorCode and logs  error
                Fixable fixable = greenhouseControls.getFixable(errorCode);
                if (fixable != null) {
                    fixable.log();
                    fixable.fix();
                }
                // restarts system and runs GreenhouseControls object
                greenhouseControls.addEvent(greenhouseControls.new Restart(0, eventsFile));
                greenhouseControls.run();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException | ControllerException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void run() throws ControllerException {
        isRunning = true;
        while (isRunning) {
            if (!isSuspended) {
                super.run();
            } else {
                suspendEvents();
            }
        }
    }

    // Method to suspend events
    public void suspendEvents() {
        for (Event event : Controller.eventList) {
            event.suspend();
            isSuspended = true;
        }

    }

    public void resumeEvents() {
        for (Event event : Controller.eventList) {
//            if (event) {
//                event.resume();
//            }
        }
    }

    private static void logEventState(String eventState) {
        String logMessage = String.format("%s\n", eventState);
        if (gui != null) {
            gui.appendToTextArea(logMessage);
        }
    }

    //---------------------------------------------------------
    public static void main(String[] args) {
        try {
            String option = args[0];
            String filename = args[1];
            if (!(option.equals("-f")) && !(option.equals("-d"))) {
                System.out.println("Invalid option");
                printUsage();
            }
            GreenhouseControls gc = new GreenhouseControls();
            if (option.equals("-f")) {
                gc.addEvent(gc.new Restart(0, filename));
            } else if ((option.equals("-d")) && (filename.equals("dump.out"))) {
                Restore restore = new Restore();
                restore.fix(filename);
            }
            gc.run();

            if (gc.isRunning()) {
                System.out.println("GreenhouseControls is running.");
            } else {
                System.out.println("GreenhouseControls is not running.");
            }
        } catch (ArrayIndexOutOfBoundsException | ControllerException e) {
            System.out.println("Invalid number of parameters");
            printUsage();
        }
    }
}

