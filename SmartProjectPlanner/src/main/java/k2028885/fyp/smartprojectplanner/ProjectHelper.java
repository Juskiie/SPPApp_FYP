package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectHelper implements ActionListener, Serializable {
    @Serial
    private static final long serialVersionUID  = 202888520288851234L; // This value must be declared so projects saved conform to this ID
    public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);    // Set up global logger
    private final DefaultWindow window;                                                 // Store instance of main window
    private static final List<Project> project_list = new ArrayList<>();                // Used when constructing table of projects
    private boolean isTableEmpty = true;                                                // Boolean used to prevent table from being drawn more than once


    // * Default
    public ProjectHelper(DefaultWindow window)
    {
        this.window = window;
    }

    public ProjectHelper()
    {
        this.window = new DefaultWindow();
    }



    /*
    * Rest of
    * Code
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == window) createProject();
    }       // Check main window called this method, otherwise do nothing

    public void createProject()
    {
        new CreateProjectForm();
    }

    // Simple method that checks if file is a directory
    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    /*
     * This method is a little bloated, should be reduced eventually.
     * What does this method do?
     * 1. Take as an argument any "File" type file
     * 2. Then, try checking the input file is a directory, or an individual file
     * 3. Encapsulate in try-catch block in case file invalid, and handle later
     * 4. If file IS a directory, perform recursion on this method with all valid containing files
     * 5. If not a directory, and file is valid serialized project file, read the file and add its data to global "project_list"
     * 6. Check if an instanced table already exists, if not, create a new table
     * 7. Load individual Projects into project table
     * 8. Draw/update table for the user
     */
    public void loadProject(File file)
    {
        // First check for directory or individual file
        try
        {
            if(isDirectory(file))
            {
                File[] files = file.listFiles();
                assert files != null;       // File has to exist or this method doesn't execute, so we can assert this to be so.
                for (File selFile : files)
                {
                    if(selFile.getName().endsWith(".ser"))
                    {
                        loadProject(selFile);
                    }
                }
            }

            // Then load individual files
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            Object obj = in.readObject();
            if(obj instanceof Project project)
            {
                project_list.add(project);
                ProjectTableModel model = new ProjectTableModel(project_list);
                JTable table = new JTable(model);
                JScrollPane scrollPane = new JScrollPane(table);

                // Check if table already exists
                if(!isTableEmpty)
                {
                    window.getPanel().validate();
                    window.setVisible(true);
                    in.close();
                    System.out.println("Table updated");
                }
                // Table doesn't already exist, so create a new one
                else
                {
                    // Create new table model with data from Project object
                    System.out.println("Table created");
                    window.getPanel().add(scrollPane, BorderLayout.CENTER);
                    window.getPanel().validate();
                    window.getPanel().repaint();
                    in.close();
                    isTableEmpty = false;
                }
            }
            else
            {
                LOGGER.log(Level.WARNING, "Invalid instance 'obj' of type ["+obj.getClass()+"], expected Project type");
                System.out.println("Expected object of type Project, but got: " + obj.getClass().getName());
            }
        }
        catch (ClassNotFoundException | AssertionError e)
        {
            // No file || no class
            e.printStackTrace();
        }
        catch (IOException ice)
        {
            System.err.println("Error: Invalid class exception");
            System.err.println("The file you have tried to load is likely for a different version of this application, and therefore cannot be read.");
        }
        catch (Exception e) {
            System.out.println("File directory is empty!");
            e.printStackTrace();
        }
        finally
        {
            window.getPanel().repaint();
            window.getPanel().revalidate();
        }
    }
    /*
     * 1. Takes a project object (from project creation form) and attempts to save it to a file.
     * 2. If "Projects" directory doesn't already exist, should create on to store projects. [Log]
     * 3. Can throw a security exception if user doesn't have permission to read/write to directory
     */
    public void saveProject(Project project) throws SecurityException
    {
        File projectsDirectory = new File("Projects");
        if (!projectsDirectory.exists()) {
            var mkdir = projectsDirectory.mkdir();
            LOGGER.log(Level.INFO, "New file/directory created: " + mkdir);
        }

        try(FileOutputStream fileOut = new FileOutputStream(new File(projectsDirectory, project.getProjectTitle() + ".ser")))
        {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(project);
            out.close();
            fileOut.close();
            JOptionPane.showMessageDialog(window, "Project saved successfully");
        } catch (IOException | SecurityException i) {
            LOGGER.log(Level.WARNING,"User does not have permission to save to current directory!");
            i.printStackTrace();
        }
        finally
        {
            // Then load project to table, regardless if it was saved or not.
            loadProject(new File(projectsDirectory, project.getProjectTitle() +".ser"));
        }
    }

    public static Set<Project> getAllProjectsInTable()
    {
        // No project should exist twice, return as set
        return new HashSet<>(project_list);
    }

    public void showHelp()
    {
        JFrame helpFrame = new JFrame();
        helpFrame.setTitle("Help");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        helpFrame.setSize(d.width/2, d.height/2); // Set window size to half screen res
        helpFrame.setLocationRelativeTo(null); // Set window location to screen centre
        helpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow the application to be closed
        helpFrame.setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        helpFrame.add(panel);

        helpFrame.pack();
        helpFrame.validate();
    }
}