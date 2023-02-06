package k2028885.fyp.smartprojectplanner;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;

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
        try
        {
            String projectName = JOptionPane.showInputDialog("Enter project name:");
            if(projectName == null)
            {
                throw new InvalidFilenameException("InvalidFilenameException: Project name cannot be null!");
            }
            String projectDescription = JOptionPane.showInputDialog("Enter a brief description of the project:");
            Date deadline = showDeadlineDialog();
            ArrayList<Task> tasks = showTaskInputDialog();
            Project project = new Project(projectName, projectDescription, deadline, tasks);
            saveProject(project);
        }
        catch (InvalidFilenameException e)
        {
            System.err.println("Filename entered must not be null! Setting to default value");
            e.printStackTrace();
        }
    }

    private Date showDeadlineDialog()
    {
        JDateChooser deadlineChooser = new JDateChooser();
        int option = JOptionPane.showConfirmDialog(null, deadlineChooser, "Select deadline", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            return deadlineChooser.getDate();
        }
        return null;
    }

    private ArrayList<Task> showTaskInputDialog()
    {
        JTextField taskInput = new JTextField();
        int option = JOptionPane.showConfirmDialog(null, new JScrollPane(taskInput), "Enter task (press cancel when finished adding tasks)", JOptionPane.OK_CANCEL_OPTION);
        while(option == JOptionPane.OK_OPTION)
        {
            parseTasks(taskInput.getText());
            taskInput = new JTextField();
            option = JOptionPane.showConfirmDialog(null, new JScrollPane(taskInput), "Enter task (press cancel when finished adding tasks)", JOptionPane.OK_CANCEL_OPTION);

            if(option == JOptionPane.CANCEL_OPTION)
            {
                return tasks;
            }
        }


        return null;
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
                List<Task> tasks = project.getTasks();
                @SuppressWarnings(value = "MismatchedReadAndWriteOfArray") // It is used, IDE can't see that though
                String[] columnNames = {"Project", "Description", "Deadline", "Tasks"};
                // It's all objects now
                @SuppressWarnings(value = "MismatchedReadAndWriteOfArray") // It is used, IDE can't see that though
                Object[][] data = new Object[1][columnNames.length];
                Object[][] task_data = new Object[tasks.size()][];

                for (int i = 0; i < tasks.size(); i++) {
                    Task task = tasks.get(i);
                    // Extract the relevant information from each task and add it to the data array
                    task_data[i] = new Object[]{"Task: " + task.getTaskName() + ", Task Duration: ", task.getTaskDuration() + "\n"};
                }

                for(int i=0;i < 1;i++)
                {
                    data[i][0] = project.getProjectTitle();
                    data[i][1] = project.getProjectDescription();
                    data[i][2] = project.getDeadline();
                    data[i][3] = task_data;
                }

                ProjectTableModel model = new ProjectTableModel(project_list);

                // Check if table already exists
                if(!isTableEmpty)
                {
                    updateTableData(model, project);
                }
                else
                {
                    // Create new table model with data from Project object


                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);

                    // window.getPanel().removeAll();
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
    }

    public void updateTableData(ProjectTableModel model, Project obj)
    {
        System.out.println("ATTEMPTING TO UPDATE TABLE");
        model.addProject(obj);
    }


    private void parseTasks(String taskInput) {
        tasks.add(new Task(taskInput));
    }

    public void saveProject(Project project) {
        File projectsDirectory = new File("Projects");
        if (!projectsDirectory.exists()) {
            //noinspection ResultOfMethodCallIgnored
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