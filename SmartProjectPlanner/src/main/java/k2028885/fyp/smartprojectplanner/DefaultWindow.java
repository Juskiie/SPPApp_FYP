package k2028885.fyp.smartprojectplanner;


import com.formdev.flatlaf.FlatDarkLaf;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This is the DefaultWindow class.
 * It extends the JFrame class and implements the GUIComponent interface.
 * It is responsible for creating and displaying the main window of the application.
 */
public class DefaultWindow extends JFrame implements GUIComponent {
    private final JPanel panel = new JPanel();
    private static DefaultWindow instance;
    private ProjectHelper helper = ProjectHelper.getInstance();

    public JPanel getPanel() {
        return panel;
    }

    private DefaultWindow()
    {
        createAndShowGUI();
    }

    public static DefaultWindow getInstance() {
        if (instance == null) {
            instance = new DefaultWindow();
            instance.helper = ProjectHelper.getInstance();
        }
        return instance;
    }

    /**
     * Generates the JMenu items for project creation, loading, the help menu, and the calendar view.
     * @return JMenuBar with the required components
     */
    private @NotNull JMenuBar createMenuBar() {
        // ProjectHelper helper = ProjectHelper.getInstance();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        // CREATE NEW PROJECT
        JMenuItem CreateProjectMenuItem = new JMenuItem("Create a new project");
        CreateProjectMenuItem.addActionListener((ActionEvent e) -> helper.createProject());
        fileMenu.add(CreateProjectMenuItem);

        // LOAD EXISTING PROJECT
        JMenuItem loadProjectItem = new JMenuItem("Load existing project");
        loadProjectItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                helper.loadProject(selectedFile);
                validate();
            }
        });
        fileMenu.add(loadProjectItem);

        // LOAD CALENDAR TASK PLANNER/SCHEDULER
        JMenuItem showProjectScheduler = new JMenuItem("Open project planner view");
        showProjectScheduler.addActionListener((ActionEvent e) -> new CalendarTaskPlanner().setVisible(true));
        fileMenu.add(showProjectScheduler);


        // SHOW HELP INFORMATION TO USER
        JMenuItem showHelp = new JMenuItem("Help");
        showHelp.addActionListener((ActionEvent e) -> ProjectHelper.showHelp());
        fileMenu.add(showHelp);

        menuBar.add(fileMenu);
        return menuBar;
    }

    /**
     * Calling this method causes the program to search the current working directory for a directory named "Projects",
     * if found it loads all files ending with "*.ser" into the main project list.
     */
    public void loadAllProjects() {
        // ProjectHelper helper = ProjectHelper.getInstance();
        Path projectsDir = Paths.get("Projects");

        if (Files.exists(projectsDir)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(projectsDir, "*.ser")) {
                for (Path entry : stream) {
                    helper.loadProject(entry.toFile());
                }
            } catch (IOException | DirectoryIteratorException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Projects directory does not exist");
        }
    }

    // Start execution here

    /**
     * Main method; code execution begins here.
     * Sets up logging, enables "look and feel" theme for GUI, and instantiates required classes
     * @param args - Command line arguments, currently none exist.
     * @throws UnsupportedLookAndFeelException - Is thrown when the look and feel resource requested no longer exists, or was changed.
     * @throws ClassNotFoundException - Is thrown when the JVM cannot find the class referenced, and is triggered by the UIManager.
     * @throws InstantiationException - Is thrown by the UIManager when it fails to load a resource.
     * @throws IllegalAccessException - Is thrown by the UIManager if it tries accessing a class, method, or field it shouldn't have access to.
     */
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        // Set up logging
        LogManager logMng = LogManager.getLogManager();
        Logger log = logMng.getLogger(Logger.GLOBAL_LOGGER_NAME);
        log.log(Level.INFO, "Logging set up! Running application..");

        // Set up UI and LAF
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        log.info("Running smart project planner app");

        // Create new window on load
        DefaultWindow frame = DefaultWindow.getInstance();
        ProjectHelper.getInstance().setWindow(frame);
        frame.setVisible(true);
        frame.loadAllProjects();

        // Clear empty tasks
        // removes the current Project from the list without causing a ConcurrentModificationException -- empty projects are completed.
        ProjectHelper.getInstance().getProject_list().removeIf(project -> project.getTasks().size() < 1);

        for (Project project : ProjectHelper.getInstance().getProject_list()) {
            var proj_deadline = ProjectHelper.checkDeadlines(project);
            String message = ("Project deadline due in: " + proj_deadline + " days\n" + "For project: " + project.getProjectTitle());
            System.out.println(message);
            System.out.println();
            if(proj_deadline <= 7) {
                JOptionPane.showMessageDialog(null, message, "Deadline reminder", JOptionPane.WARNING_MESSAGE);
            }
        }

        Runtime runtime = Runtime.getRuntime();
        long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used Memory at runtime:" + usedMemoryBefore);
    }


    @Override
    public void initComponents() {
        panel.setLayout(new BorderLayout());
        add(panel);
        setJMenuBar(createMenuBar());
        validate();
        // pack();
    }

    @Override
    public void createAndShowGUI() {
        setTitle("Procrastination Smart Project Planner"); // Set window title

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(d.width/2, d.height/2); // Set window size to half screen res
        setLocationRelativeTo(null); // Set window location to screen centre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow the application to be closed
        setResizable(true);

        initComponents();
    }

    @Override
    public void setDefaultWindow(DefaultWindow window) {
        instance = window;
    }
}