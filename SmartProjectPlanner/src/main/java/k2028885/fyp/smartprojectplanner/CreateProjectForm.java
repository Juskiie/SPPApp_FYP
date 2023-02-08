package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

public class CreateProjectForm extends JFrame {
    private final JTextField projectNameField;
    private final JTextField projectDescriptionField;
    private final JDateChooser deadlineChooser;
    private final JTextArea taskListArea;
    private final ProjectHelper helper = new ProjectHelper();

    public CreateProjectForm()
    {
        // Set up the form components
        projectNameField = new JTextField();
        projectNameField.setVisible(true);
        projectDescriptionField = new JTextField();
        deadlineChooser = new JDateChooser();
        // deadlineChooser.setDate(new Date());
        taskListArea = new JTextArea();
        taskListArea.setLineWrap(true);
        taskListArea.setWrapStyleWord(true);
        taskListArea.setRows(5);
        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        // Panel
        String[] labels = {"Project Name: ","Description: ","Deadline: ","Tasks: "};

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // Set project name label and panel for components
        JLabel lName = new JLabel(labels[0], JLabel.TRAILING);
        lName.setLabelFor(projectNameField);
        lName.setOpaque(true);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BorderLayout());
        namePanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        namePanel.add(lName, BorderLayout.WEST);
        namePanel.add(projectNameField, BorderLayout.CENTER);
        p.add(namePanel);

        // Set project description label and panel for components
        JLabel lDesc = new JLabel(labels[1], JLabel.TRAILING);
        lDesc.setLabelFor(projectDescriptionField);
        lDesc.setOpaque(true);
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BorderLayout());
        descPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        descPanel.add(lDesc, BorderLayout.WEST);
        descPanel.add(projectDescriptionField, BorderLayout.CENTER);
        p.add(descPanel);

        // Set project deadline label and panel for components
        JLabel lDeadline = new JLabel(labels[2], JLabel.TRAILING);
        lDeadline.setLabelFor(deadlineChooser);
        lDeadline.setOpaque(true);
        JPanel deadlinePanel = new JPanel();
        deadlinePanel.setLayout(new BorderLayout());
        deadlinePanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        deadlinePanel.add(lDeadline, BorderLayout.WEST);
        deadlinePanel.add(deadlineChooser, BorderLayout.CENTER);
        p.add(deadlinePanel);

        // Set project tasks label and panel for components
        JLabel lTasks = new JLabel(labels[3], JLabel.TRAILING);
        lTasks.setLabelFor(taskListArea);
        lTasks.setOpaque(true);
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BorderLayout());
        taskPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        taskPanel.add(lTasks, BorderLayout.WEST);
        taskPanel.add(taskListArea, BorderLayout.CENTER);
        p.add(taskPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        descPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        p.add(buttonPanel);
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