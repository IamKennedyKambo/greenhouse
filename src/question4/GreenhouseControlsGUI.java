package question4;

import question3.ControllerException;
import question3.GreenhouseControls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GreenhouseControlsGUI extends JFrame {
    private final JTextArea textArea;
    private static JButton startButton;
    private static JButton restartButton;
    private static JButton resumeButton;
    private static JButton terminateButton;
    private static JButton suspendButton;
    private final GreenhouseControls greenhouseControls;
    static String selectedFilePath;
    static JMenuItem newWindowMenuItem;
    static JMenuItem closeWindowMenuItem;
    static JMenuItem openEventsMenuItem;
    static JMenuItem restoreEventsMenuItem;
    static JMenuItem exitEventsMenuItem;

    public static GreenhouseControlsGUI gui; // Add this field

    public GreenhouseControlsGUI() {
        setTitle("Greenhouse Controls GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("logo.png");
        setIconImage(icon.getImage());
        setSize(800, 600);

        setJMenuBar(createMenu());

        greenhouseControls = new GreenhouseControls();  // Instantiate GreenhouseControls

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton loadButton = getButton(this, textArea);
        add(loadButton, BorderLayout.NORTH);

        startButton = new JButton("Start");
        restartButton = new JButton("Restart");
        resumeButton = new JButton("Resume");
        terminateButton = new JButton("Terminate");
        suspendButton = new JButton("Suspend");

        updateButtonStates(false, false, false, false, false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(restartButton);
        buttonPanel.add(resumeButton);
        buttonPanel.add(terminateButton);
        buttonPanel.add(suspendButton);
        add(buttonPanel, BorderLayout.SOUTH);

        JPopupMenu popupMenu = getMenu();

        newWindowMenuItem.addActionListener(e -> createNewWindow());
        closeWindowMenuItem.addActionListener(e -> dispose());
        openEventsMenuItem.addActionListener(e -> textArea.append("Opened Events\n"));
        restoreEventsMenuItem.addActionListener(e -> {
        });
        exitEventsMenuItem.addActionListener(e -> dispose());

        textArea.setComponentPopupMenu(popupMenu);

        startButton.addActionListener(e -> {
            updateButtonStates(false, false, true, true, false); // Enable terminate and suspend
            RunControlsWorker worker = new RunControlsWorker();
            worker.execute();
        });

        restartButton.addActionListener(e -> {
            if (greenhouseControls.isRunning()) {
                greenhouseControls.addEvent(greenhouseControls.new Restart(0, GreenhouseControls.eventsFile));
                textArea.append("Restart event added\n");
            } else {
                textArea.append("Cannot add restart event while GreenhouseControls is running\n");
            }
        });

        terminateButton.addActionListener(e -> {
            if (greenhouseControls.isRunning()) {
                // Stop background event and enable restart button
                String result = JOptionPane.showInputDialog(null, "Enter termination delay", 0);

                if (result != null && !result.isEmpty()) {
                    greenhouseControls.addEvent(new GreenhouseControls.Terminate(Long.parseLong(result)));
                    try {
                        greenhouseControls.run();
                    } catch (ControllerException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                textArea.append("GreenhouseControls terminated\n");
                updateButtonStates(false, true, false, false, false); // Enable restart
            }
        });

        suspendButton.addActionListener(e -> {
            System.out.println(greenhouseControls.isSuspended());
            if (greenhouseControls.isRunning()) {
                textArea.append("Suspending operations\n");
                greenhouseControls.suspendEvents();
                greenhouseControls.suspendAllEvents();
                updateButtonStates(false, false, true, false, true);
            } else {
                textArea.append("Cannot suspend as nothing is running\n");
                textArea.append("GreenhouseControls events suspended\n");
            }
        });

        resumeButton.addActionListener(e -> {
            greenhouseControls.resumeEvents();
            updateButtonStates(false, false, true, true, false);
            textArea.append("GreenhouseControls events resuming\n");
        });

        setVisible(true);
    }

    private static JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        newWindowMenuItem = new JMenuItem("New Window");
        closeWindowMenuItem = new JMenuItem("Close Window");
        openEventsMenuItem = new JMenuItem("Open Events");
        restoreEventsMenuItem = new JMenuItem("Restore");
        exitEventsMenuItem = new JMenuItem("Exit");

        newWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        closeWindowMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        openEventsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        restoreEventsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        exitEventsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        fileMenu.add(newWindowMenuItem);
        fileMenu.add(closeWindowMenuItem);
        fileMenu.add(openEventsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(restoreEventsMenuItem);
        fileMenu.add(exitEventsMenuItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    private static void updateButtonStates(boolean startEnabled, boolean restartEnabled,
                                           boolean terminateEnabled, boolean suspendEnabled, boolean resumeEnabled) {
        startButton.setEnabled(startEnabled);
        restartButton.setEnabled(restartEnabled);
        terminateButton.setEnabled(terminateEnabled);
        suspendButton.setEnabled(suspendEnabled);
        resumeButton.setEnabled(resumeEnabled);
    }

    private static JButton getButton(JFrame frame, JTextArea textArea) {
        JFileChooser fileChooser = new JFileChooser();

        JButton loadButton = new JButton("Load Events File");
        loadButton.setFocusPainted(false);

        // Load dump file button
        loadButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                // Load the selected dump file into the text area
                try {
                    selectedFilePath = fileChooser.getSelectedFile().getPath();
                    String fileContent = readFileContent(selectedFilePath);
                    textArea.setText(fileContent);
                    loadButton.setText(selectedFilePath);
                    updateButtonStates(true, false, false, false, false); // Enable Start button
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error loading file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return loadButton;
    }

    private static void createNewWindow() {
        JFrame newFrame = new JFrame("New Window");
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newFrame.setSize(400, 300);
        newFrame.setLocationRelativeTo(null); // Center the new frame
        newFrame.setVisible(true);
    }

    public void appendToTextArea(String text) {
        textArea.append(text);
    }

    private class RunControlsWorker extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() {
            if (!greenhouseControls.isRunning()) {
                greenhouseControls.addEvent(greenhouseControls.new Restart(0, selectedFilePath));
                try {
                    greenhouseControls.run();
                } catch (ControllerException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }

        @Override
        protected void done() {
            updateButtonStates(false, false, true, true, false); // Enable terminate and suspend
            textArea.append("GreenhouseControls started\n");
        }
    }

    private JPopupMenu getMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem start = new JMenuItem("Start");
        JMenuItem restart = new JMenuItem("Restart");
        JMenuItem terminate = new JMenuItem("Terminate");
        JMenuItem suspend = new JMenuItem("Suspend");
        JMenuItem resume = new JMenuItem("Resume");

        popupMenu.add(start);
        popupMenu.add(restart);
        popupMenu.add(terminate);
        popupMenu.add(suspend);
        popupMenu.add(resume);

        start.addActionListener(e -> {
            updateButtonStates(false, false, true, true, false); // Enable terminate and suspend
            RunControlsWorker worker = new RunControlsWorker();
            worker.execute();
        });

        restart.addActionListener(e -> {
            if (greenhouseControls.isRunning()) {
                greenhouseControls.addEvent(greenhouseControls.new Restart(0, GreenhouseControls.eventsFile));
                textArea.append("Restart event added\n");
            } else {
                textArea.append("Cannot add restart event while GreenhouseControls is running\n");
            }
        });

        terminate.addActionListener(e -> {
            if (greenhouseControls.isRunning()) {
                // Stop background event and enable restart button
                String result = JOptionPane.showInputDialog(null, "Enter termination delay", 0);

                if (result != null && !result.isEmpty()) {
                    greenhouseControls.addEvent(new GreenhouseControls.Terminate(Long.parseLong(result)));
                    try {
                        greenhouseControls.run();
                    } catch (ControllerException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                textArea.append("GreenhouseControls terminated\n");
                updateButtonStates(false, true, false, false, false); // Enable restart
            }
        });

        suspend.addActionListener(e -> {
            if (greenhouseControls.isRunning()) {
                textArea.append("Suspending operations\n");
                greenhouseControls.suspendEvents();
                updateButtonStates(false, false, true, false, true);
            } else {
                textArea.append("Cannot suspend as nothing is running\n");
                textArea.append("GreenhouseControls events suspended\n");
            }
        });

        resume.addActionListener(e -> {
            greenhouseControls.resumeEvents();
            updateButtonStates(false, false, true, true, false);
            textArea.append("GreenhouseControls events resuming\n");
        });

        return popupMenu;
    }

    private static String readFileContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();
        return content.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            gui = new GreenhouseControlsGUI(); // Initialize the GUI
        });
    }
}
