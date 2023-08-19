package question4;

import question3.ControllerException;
import question3.GreenhouseControls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

public class Hello {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Hello::createAndShowGUI);
    }

    static JMenuItem newWindowMenuItem;
    static JMenuItem closeWindowMenuItem;
    static JMenuItem openEventsMenuItem;
    static JMenuItem restoreEventsMenuItem;
    static JMenuItem exitEventsMenuItem;

    static String selectedFilePath;

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

    private static JPanel setUpButtons(JTextArea jTextArea) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton start = new JButton("Start");
        JButton restart = new JButton("Restart");
        JButton terminate = new JButton("Terminate");
        JButton suspend = new JButton("Suspend");
        JButton resume = new JButton("Resume");

        GreenhouseControls gc = new GreenhouseControls();

        resume.addActionListener(e -> gc.resumeEvents());

        suspend.addActionListener(e -> gc.pauseEvents());

        start.addActionListener(e -> {
            try {
                gc.addEvent(gc.new Restart(0, selectedFilePath));

                gc.run();
            } catch (ControllerException ex) {
                throw new RuntimeException(ex);
            }
        });

        restart.addActionListener(e -> {
            try {
                gc.addEvent(gc.new Restart(0, selectedFilePath));
                gc.run();
            } catch (ControllerException ex) {
                throw new RuntimeException(ex);
            }
        });

        terminate.addActionListener(e -> gc.shutdown());

        start.setFocusPainted(false);
        restart.setFocusPainted(false);
        terminate.setFocusPainted(false);
        suspend.setFocusPainted(false);
        resume.setFocusPainted(false);

        buttonPanel.add(start);
        buttonPanel.add(restart);
        buttonPanel.add(terminate);
        buttonPanel.add(suspend);
        buttonPanel.add(resume);

        return buttonPanel;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Greenhouse controller");
        frame.setSize(500, 500);
        ImageIcon icon = new ImageIcon("logo.png");
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a menu bar
        frame.setJMenuBar(createMenu());

        // Create a scrollable text area
        JTextArea textArea = new JTextArea(10, 40);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create buttons horizontally
        String[] data = {"Start", "Restart", "Terminate", "Suspend", "Resume"};

        JPopupMenu popupMenu = new JPopupMenu();

        for (String entry : data) {
            popupMenu.add(entry);
        }

        // Add ActionListener to menu items
        newWindowMenuItem.addActionListener(e -> createNewWindow());
        closeWindowMenuItem.addActionListener(e -> frame.dispose());
        openEventsMenuItem.addActionListener(e -> textArea.append("Opened Events\n"));
        restoreEventsMenuItem.addActionListener(e -> {
        });
        exitEventsMenuItem.addActionListener(e -> frame.dispose());

        // Add ActionListener to popup menu items
        textArea.setComponentPopupMenu(popupMenu);

        JButton loadButton = getButton(frame, textArea);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(setUpButtons(textArea), BorderLayout.SOUTH);
        frame.add(loadButton, BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
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
}
