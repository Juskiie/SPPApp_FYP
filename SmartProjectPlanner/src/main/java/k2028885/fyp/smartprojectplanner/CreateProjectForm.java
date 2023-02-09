package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import com.toedter.calendar.JDateChooser;

public class CreateProjectForm extends JFrame {
    private final JTextField projectNameField;
    private final JTextField projectDescriptionField;
    private final JDateChooser deadlineChooser;
    private final List<Task> taskList = new ArrayList<>();
    private final ProjectHelper helper = new ProjectHelper();


    private List<Task> getTasksFromList(){
        return taskList;
    }

    public CreateProjectForm()
    {
        // Set up the form components
        projectNameField = new JTextField();
        projectNameField.setVisible(true);
        projectDescriptionField = new JTextField();
        deadlineChooser = new JDateChooser();

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

        // NEW CODE HERE -------------------------------------------------------------------------------------------------------------------------------
        JTextField taskNameField = new JTextField(5);
        JSpinner taskDurationSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        JButton addTaskButton = new JButton("Add Task to list");
        DefaultListModel<String> taskListModel =  new DefaultListModel<>();
        JList<String> taskListStr = new JList<>(taskListModel);

        // Add action listener to update task list on adding task
        addTaskButton.addActionListener(e -> {
            Task task = new Task(taskNameField.getText(), (Integer) taskDurationSpinner.getValue());
            taskListModel.addElement(task.toString());
            taskList.add(task);
        });

        // Task Panel
        JPanel taskPanel = new JPanel();
        taskPanel.setLayout(new BoxLayout(taskPanel, BoxLayout.Y_AXIS));
        JLabel lTask = new JLabel("Task: ", JLabel.TRAILING);
        lTask.setLabelFor(taskNameField);
        lTask.setOpaque(true);
        JPanel nameTaskPanel = new JPanel();
        nameTaskPanel.setLayout(new BorderLayout());
        nameTaskPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        nameTaskPanel.add(lTask, BorderLayout.WEST);
        nameTaskPanel.add(taskNameField, BorderLayout.CENTER);
        taskPanel.add(nameTaskPanel);

        // Task duration
        JLabel lDuration = new JLabel("Duration (hours): ", JLabel.TRAILING);
        lDuration.setLabelFor(taskDurationSpinner);
        lDuration.setOpaque(true);
        JPanel durationPanel = new JPanel();
        durationPanel.setLayout(new BorderLayout());
        durationPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        durationPanel.add(lDuration, BorderLayout.WEST);
        durationPanel.add(taskDurationSpinner, BorderLayout.CENTER);
        taskPanel.add(durationPanel);

        // Task list panel
        JLabel lTasks = new JLabel("Project tasks: ", JLabel.TRAILING);
        lTasks.setLabelFor(taskListStr);
        lTasks.setOpaque(true);
        JPanel taskListPanel = new JPanel();
        taskListPanel.setLayout(new BorderLayout());
        taskListPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        taskListPanel.add(lTasks, BorderLayout.WEST);
        taskListPanel.add(taskListStr, BorderLayout.CENTER);
        taskListPanel.add(addTaskButton, BorderLayout.AFTER_LAST_LINE);
        taskPanel.add(taskListPanel);
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
        String projectName = projectNameField.getText();        // Project name
        String projectDesc = projectDescriptionField.getText(); // Project description
        Date deadline = deadlineChooser.getDate();              // Project deadline
        List<Task> tasksFromList = getTasksFromList();          // Task list
        System.out.println(tasksFromList);

        // Validate the values
        if(projectName.isEmpty())
        {
            projectName = "New Project";
        }
        if(projectDesc.isEmpty())
        {
            projectDesc = "This is a new project";
        }

        // Save the project to file
        Project proj = new Project(projectName,projectDesc,deadline,tasksFromList);
        helper.saveProject(proj);

        // Close the form
        setVisible(false);

    }

    private void cancel()
    {
        // Close the form
        setVisible(false);
    }
}