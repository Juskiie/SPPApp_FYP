/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package k2028885.fyp.smartprojectplanner;


import com.formdev.flatlaf.FlatDarkLaf;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.*;
import java.util.ArrayList;

/**
 *
 * @author L.Casey Bull
 */
public class DefaultWindow extends JFrame implements informativeOutput{
    private static ArrayList<Object> instances;
    private final JPanel panel = new JPanel();
    private String title;
    private Toolkit tk;
    private Dimension d;
    boolean resizeable;

    public JPanel getPanel() {
        return panel;
    }

    public DefaultWindow()
    {
        setTitle("Procrastination Smart Project Planner"); // Set window title
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        
        setSize(d.width/2, d.height/2); // Set window size to half screen res
        setLocationRelativeTo(null); // Set window location to screen centre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow the application to be closed
        setResizable(true);

        panel.setLayout(new BorderLayout());
        add(panel);
        setJMenuBar(createMenuBar());
        validate();
    }

    public DefaultWindow(String title, Toolkit tk, Dimension d, boolean resizeable) throws HeadlessException
    {
        this();
        this.title = title;
        this.tk = tk;
        this.d = d;
        this.resizeable = resizeable;
    }

    private JMenuBar createMenuBar() {
        ProjectHelper helper = new ProjectHelper(this);
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

        // SHOW HELP INFORMATION TO USER
        JMenuItem showHelp = new JMenuItem("Help");
        showHelp.addActionListener((ActionEvent e) -> ProjectHelper.showHelp());
        fileMenu.add(showHelp);

        menuBar.add(fileMenu);
        return menuBar;
    }
    
    @Override
    public ArrayList<Object> getAllChildObjects()
    {
        return instances;
    }

    // Start execution here: pass args..
    /*
     * Argument list:
     * "-v" -> Display debugging info
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
        DefaultWindow frame = new DefaultWindow();
        instances.add(frame);
        CalendarTaskPlanner taskPlanner = new CalendarTaskPlanner();
        taskPlanner.setVisible(true);
        frame.setVisible(true);
        System.out.println(frame.getSize());
    }
}