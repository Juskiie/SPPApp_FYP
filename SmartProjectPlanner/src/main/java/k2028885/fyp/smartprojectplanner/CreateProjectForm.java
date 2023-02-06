package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class CreateProjectForm extends JFrame {
    private JTextField projectNameField;
    private JTextField projectDescriptionField;
    private JDateChooser deadlineChooser;
    private JTextArea taskListArea;
    private JButton submitButton;
    private JButton cancelButton;
    private ProjectHelper helper = new ProjectHelper();

    public CreateProjectForm(String createNewProject)
    {
        // Set up the form components
        projectNameField = new JTextField();
        projectDescriptionField = new JTextField();
        deadlineChooser = new JDateChooser();
        deadlineChooser.setDate(new Date());
        taskListArea = new JTextArea();
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        // Add the components to the form
        // ...

        // Add action listeners for the buttons
        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> cancel());
    }

    private void submit()
    {
        // Get the values from the form
        String projectName = projectNameField.getText();
        String projectDesc = projectDescriptionField.getText();
        Date deadline = deadlineChooser.getDate();
        List<Task> tasks = parseTasks();

        // Validate the values
        if(projectName.isEmpty())
        {
            projectName = "New Project";
        }
        if(projectDesc.isEmpty())
        {
            projectDesc = "This is a new project";
        }
        if(tasks.isEmpty())
        {
            tasks = new ArrayList<>();
        }
        Project proj = new Project(projectName,projectDesc,deadline,tasks);
        helper.saveProject(proj);

        // Close the form
        setVisible(false);

    }

    private void cancel()
    {
        // Close the form
        setVisible(false);
    }

    private List<Task> parseTasks()
    {
        // Parse the task list from the text area
        // ...
        List<Task> tasks = new ArrayList<>();
        String[] taskDescriptions = taskListArea.getText().split("\n");
        for (String taskName : taskDescriptions) {
            tasks.add(new Task(taskName));
        }
        return tasks;
    }
}