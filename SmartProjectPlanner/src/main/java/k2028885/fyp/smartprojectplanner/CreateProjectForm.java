package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.awt.*;
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

    public CreateProjectForm(DefaultWindow window)
    {
        // Set up the form components
        projectNameField = new JTextField(10);
        projectNameField.setVisible(true);
        projectDescriptionField = new JTextField(20);
        deadlineChooser = new JDateChooser();
        // deadlineChooser.setDate(new Date());
        taskListArea = new JTextArea(10,30);
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        Toolkit tk = Toolkit.getDefaultToolkit();

        // Panel
        String[] labels = {"Project Name: ","Description: ","Deadline: ","Tasks: "};

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lName = new JLabel(labels[0], JLabel.LEFT);
        lName.setLabelFor(projectNameField);
        lName.setOpaque(true);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
        namePanel.add(lName);
        namePanel.add(projectNameField);
        p.add(namePanel);

        JLabel lDesc = new JLabel(labels[1], JLabel.TRAILING);
        lDesc.setLabelFor(projectDescriptionField);
        lDesc.setOpaque(true);
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new FlowLayout());
        descPanel.add(lDesc);
        descPanel.add(projectDescriptionField);
        p.add(descPanel);

        JLabel lDeadline = new JLabel(labels[2], JLabel.TRAILING);
        lDeadline.setLabelFor(deadlineChooser);
        lDeadline.setOpaque(true);
        JPanel deadlinePanel = new JPanel();
        deadlinePanel.setLayout(new FlowLayout());
        deadlinePanel.add(lDeadline);
        deadlinePanel.add(deadlineChooser);
        p.add(deadlinePanel);

        JLabel lTasks = new JLabel(labels[3], JLabel.TRAILING);
        lTasks.setLabelFor(taskListArea);
        lTasks.setOpaque(true);
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new FlowLayout());
        taskPanel.add(lTasks);
        taskPanel.add(taskListArea);
        p.add(taskPanel);

        p.add(submitButton);
        p.add(cancelButton);
        p.setVisible(true);

        // Add the components to the form and load gui
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new JLabel("TEST"));
        add(p);
        repaint();
        // validate();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);



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