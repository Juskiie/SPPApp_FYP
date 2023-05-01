package k2028885.fyp.smartprojectplanner;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to perform various functions required for this
 * application to work.
 * This class saves and loads project files, opens new JFrames, logs events
 * and controls the flow of operation for the application.
 */
public class ProjectHelper implements ActionListener, Serializable {
    @Serial
    private static final long serialVersionUID  = 202888520288851234L;                  // This value must be declared so projects saved conform to this ID
    public static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);    // Set up global logger
    private DefaultWindow window;                                                       // Store instance of main window
    private static final List<Project> project_list = new ArrayList<>();                // Used when constructing table of projects
    private boolean isTableEmpty = true;                                                // Boolean used to prevent table from being drawn more than once
    private static ProjectHelper instance;

    /**
     * Private constructor to enforce singleton pattern
     */
    private ProjectHelper()
    {
        // construct helper
    }

    /**
     * Generates a reference to the main window instance.
     * @param window The main window
     */
    public void setWindow(DefaultWindow window) {
        this.window = window;
    }

    /**
     * Gets the current ProjectHelper, required for singleton pattern.
     * Will create an instance if one is not present.
     * @return The ProjectHelper instance
     */
    public static ProjectHelper getInstance() {
        if (instance == null) {
            instance = new ProjectHelper();
        }
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == window) createProject();
    }       // Check main window called this method, otherwise do nothing


    /**
     * Creates a new project creation form from the CreateProjectForm.java class.
     */
    public void createProject() { new CreateProjectForm(); }

    /**
     * This method takes a File object as input and returns true
     * if that File object is a directory and false if not.
     * Redundant method.
     * @param file File
     * @return Boolean output of file being directory or not
     */
    public boolean isDirectory(File file) {
        return file.isDirectory();
    }

    /**
     * This method when called with a File object will load that file as a Project
     * object to display in the project list view.
     * This object is also added to the global project_list.
     * @param file File
     * @throws AssertionError if ProjectHelper instance without reference to DefaultWindow is used to call it
     * @throws NullPointerException If singleton pattern isn't enforced, and this method is loaded without a reference to the main window.
     */
    public void loadProject(File file) throws AssertionError, NullPointerException
    {
        // First check for directory or individual file
        try
        {
            // this.window = DefaultWindow.getInstance();
            if(window == null) {
                window = DefaultWindow.getInstance();
            }
            assert window != null;
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

                // Action listener for projects in table view, so they can be modified.
                table.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        int row = table.rowAtPoint(e.getPoint());
                        int col = table.columnAtPoint(e.getPoint());

                        if (row >=0 && col >=0) {
                            Project selectedProject = project_list.get(row);
                            TaskChecklistFrame taskChecklist = new TaskChecklistFrame(selectedProject);
                            taskChecklist.setDefaultWindow(DefaultWindow.getInstance());
                            taskChecklist.setVisible(true);
                        }
                    }
                });

                // Check if table already exists
                if(!isTableEmpty)
                {
                    window.getPanel().validate();
                    window.setVisible(true);
                    in.close();
                    // System.out.println("Table updated");
                }
                // Table doesn't already exist, so create a new one
                else
                {
                    // Create new table model with data from Project object
                    // System.out.println("Table created");
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
            LOGGER.log(Level.WARNING, "Either you attempted loading a non-existent file, or the wrong type of file, or you called this method without an instance of DefaultWindow");
            e.printStackTrace();
        }
        catch (IOException ice)
        {
            System.err.println("Error: Invalid class exception");
            System.err.println("The file you have tried to load is likely for a different version of this application, and therefore cannot be read.");
        }
        catch (Exception e)
        {
            System.out.println("File directory is empty!");
            e.printStackTrace();
        }
        finally
        {
            if(window != null) {
                window.getPanel().repaint();
                window.getPanel().revalidate();
            }
            else {
                LOGGER.log(Level.WARNING, "ProjectHelper instance without default window attempted to load a project");
            }
        }
    }

    /**
     * This method takes a Project object as input and (if one doesn't already exist) creates a new folder
     * to save the serialized project to.
     * @param project Project
     * @throws SecurityException When user does not have sufficient file system permission for working directory.
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

    /**
     * Calling this method returns all loaded project files in memory.
     * @return The current project list as a hash set to prevent duplicates.
     */
    public static Set<Project> getAllProjectsInTable()
    {
        // No project should exist twice, return as set
        return new HashSet<>(project_list);
    }

    /**
     * Similar to the getAllProjectsInTable() method, except returns as a List.
     * @return The project list (as a List).
     */
    public List<Project> getProject_list() {
        return project_list;
    }

    /**
     * Loads the helpme.txt file in a new window when called.
     */
    public static void showHelp(){
        JFrame helpFrame = new JFrame();
        helpFrame.setTitle("Help");
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        helpFrame.setSize(d.width/2, d.height/2); // Set window size to half screen res
        helpFrame.setLocationRelativeTo(null); // Set window location to screen centre
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Allow the application to be closed
        helpFrame.setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setEditable(true);
        JScrollPane scrollPane = new JScrollPane(textArea);

        try (Scanner reader = new Scanner(new File("helpme.txt")))
        {
            while(reader.hasNext())
            {
                String text = reader.nextLine();
                textArea.append(text+"\n");
            }
        }
        catch(FileNotFoundException e)
        {
            LOGGER.log(Level.WARNING, "Error: File 'helpme.txt' could not be found/accessed");
            e.printStackTrace();
        }
        finally
        {
            textArea.setEditable(false);
            panel.add(scrollPane);
            helpFrame.add(panel);
            helpFrame.pack();
            helpFrame.validate();
            helpFrame.setVisible(true);
        }
    }

    /**
     * Calculates time until deadline of a given project
     * @param project A Project object to check the deadline for
     * @return long value for the time difference in millis, converted to TimeUnit.DAYS
     */
    public static long checkDeadlines(@NotNull Project project) {
        Date currentDate = new Date();

        long diffInMillies = Math.abs(project.getDeadline().getTime() - currentDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diff <= 7) {
            System.out.println("Project: [" + project.getProjectTitle() + "] is due within 7 days.");
        }
        return diff;
    }
}
