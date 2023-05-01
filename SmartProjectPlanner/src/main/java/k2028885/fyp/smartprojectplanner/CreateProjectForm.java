package k2028885.fyp.smartprojectplanner;

import com.toedter.calendar.JDateChooser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateProjectForm extends JFrame implements GUIComponent {
    private JTextField projectNameField;
    private JTextField projectDescriptionField;
    private JDateChooser deadlineChooser;
    private final List<Task> taskList = new ArrayList<>();
    private final ProjectHelper helper = ProjectHelper.getInstance();

    private List<Task> getTasksFromList(){
        return taskList;
    }

    /**
     * Takes a label and a type of component, then generates a panel for it.
     * @param lab The panel's label.
     * @param component The type of component for the panel to contain
     * @return The completed JPanel
     */
    private @NotNull JPanel createPanel(JLabel lab, JComponent component) {
        lab.setLabelFor(component);
        lab.setOpaque(true);
        JPanel newPanel = new JPanel();
        newPanel.setLayout(new BorderLayout());
        newPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
        newPanel.add(lab, BorderLayout.WEST);
        newPanel.add(component, BorderLayout.CENTER);
        return newPanel;
    }

    /**
     * Calls the initComponents method to initialize the components,
     * then, adds to the frame and sets visibility to true.
     */
    public CreateProjectForm()
    {
        // Set up the form components
        initComponents();
        createAndShowGUI();
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

    @Override
    public void initComponents() {
        projectNameField = new JTextField();
        projectNameField.setVisible(true);
        projectDescriptionField = new JTextField();
        deadlineChooser = new JDateChooser();

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        // Panel
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        // Set project name label and panel for components
        String[] labels = {"Project Name: ", "Description: ", "Deadline: ", "Tasks: "};
        p.add(createPanel(new JLabel(labels[0], JLabel.TRAILING), projectNameField));

        // Set project description label and panel for components
        p.add(createPanel(new JLabel(labels[1], JLabel.TRAILING), projectDescriptionField));

        // Set project deadline label and panel for components
        p.add(createPanel(new JLabel(labels[2], JLabel.TRAILING), deadlineChooser));

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

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        p.add(buttonPanel);
        p.setVisible(true);


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        add(new JLabel("TEST"));
        add(p);

        // Add action listeners for the buttons
        submitButton.addActionListener(e -> submit());
        cancelButton.addActionListener(e -> cancel());
    }

    @Override
    public void createAndShowGUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }

    @Override
    public void setDefaultWindow(DefaultWindow window) {
    }
}