package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectHelper implements ActionListener {
    private final DefaultWindow window;
    private final List<Project> project_list = new ArrayList<>();
    private final ArrayList<Task> tasks = new ArrayList<>();
    private boolean isTableEmpty = true;


    // * Default
    public ProjectHelper(DefaultWindow window) {
        this.window = window;
    }

    public ProjectHelper() { this.window = new DefaultWindow(); }

    /*
    * Rest of
    * Code
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == window) createProject();
    }

    public void createProject()
    {
        new CreateProjectForm(window);
    }

    public void loadProject(File file)
    {
        try
        {
            if(file.isDirectory())
            {
                File[] files = file.listFiles();
                assert files != null;
                if(files.length < 1)
                {
                    throw(new Exception());
                }
                for (File selFile : files)
                {
                    if(selFile.getName().endsWith(".ser"))
                    {
                        loadProject(selFile);
                    }
                }
            }
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            Object obj = in.readObject();
            if(obj instanceof Project)
            {
                Project project = (Project) obj;
                project_list.add(project);
                ProjectTableModel model = new ProjectTableModel(project_list);

                // Check if table already exists
                if(!isTableEmpty)
                {
                    model.addProject(project);
                    window.getPanel().revalidate();
                    window.getPanel().repaint();
                }
                else
                {
                    // Create new table model with data from Project object
                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);

                    window.getPanel().add(scrollPane, BorderLayout.CENTER);
                    window.getPanel().revalidate();
                    window.getPanel().repaint();
                    in.close();

                    isTableEmpty = false;

                }
            }
            else
            {
                System.out.println("Expected object of type Project, but got: " + obj.getClass().getName());
            }
        }
        catch (IOException | ClassNotFoundException e)
        {
            // No file || no class
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("File directory is empty!");
            e.printStackTrace();
        }
        finally
        {
            window.getPanel().repaint();
            window.getPanel().revalidate();
        }
    }

    public void saveProject(Project project) {
        File projectsDirectory = new File("Projects");
        if (!projectsDirectory.exists()) {
            projectsDirectory.mkdir();
        }

        try(FileOutputStream fileOut = new FileOutputStream(new File(projectsDirectory, project.getProjectTitle() + ".ser")))
        {
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(project);
            out.close();
            fileOut.close();
            JOptionPane.showMessageDialog(window, "Project saved successfully");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}