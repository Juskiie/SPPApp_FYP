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

/**
 *
 * @author L.Casey Bull
 */
public class DefaultWindow extends JFrame{
    private final JPanel panel = new JPanel();

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



    private JMenuBar createMenuBar()
    {
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
            if(returnValue == JFileChooser.APPROVE_OPTION)
            {
                File selectedFile = fileChooser.getSelectedFile();
                helper.loadProject(selectedFile);
            }
        });

        fileMenu.add(loadProjectItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

    // Start execution here: pass args..
    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf());
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }
        DefaultWindow frame = new DefaultWindow(); // Create new window on load
        frame.setVisible(true);
    }
}
