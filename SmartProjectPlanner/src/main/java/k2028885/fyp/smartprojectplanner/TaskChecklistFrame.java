package k2028885.fyp.smartprojectplanner;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The TaskChecklistFrame class represents a frame used to display and manage a project's tasks using checkboxes.
 * It extends JFrame and implements the GUIComponent interface.
 */
public class TaskChecklistFrame extends JFrame implements GUIComponent {
    private final Project project;
    private final ArrayList<JCheckBox> checkboxes = new ArrayList<>();
    private final ProjectHelper helper = ProjectHelper.getInstance();


    public ArrayList<JCheckBox> getCheckboxes() {
        return checkboxes;
    }

    /**
     * Takes a Project object as a parameter, and uses that to build the checklist frame with its tasks.
     * @param project The project object from which to generate the task list
     */
    public TaskChecklistFrame(Project project) {
        this.project = project;
        createAndShowGUI();
    }

    @Override
    public void createAndShowGUI() {
        setTitle("Task checklist"); // Set window title

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();

        setSize(d.width/2, d.height/2); // Set window size to half screen res
        setLocationRelativeTo(null); // Set window location to screen centre
        setResizable(true);
        initComponents();

        setVisible(true);
    }

    @Override
    public void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        List<Task> sortedTasks = project.getFormattedTaskList();

        for (Task task : sortedTasks) {
            JCheckBox checkbox = new JCheckBox(task.toString());
            checkboxes.add(checkbox);
            panel.add(checkbox);
        }

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveTasks());
        panel.add(saveButton);

        add(panel);
        pack();
    }

    /**
     * This method creates a new task list for the currently selected project
     * that only contains incomplete tasks, and then updates the project with the new task list.
     * Is called upon clicking the "Save" button in the checklist.
     */
    void saveTasks() {
        List<Task> tasks = project.getTasks();
        List<Task> remainingTasks = new ArrayList<>();

        for (int i=0; i<checkboxes.size(); i++) {
            if(!checkboxes.get(i).isSelected()) {
                remainingTasks.add(tasks.get(i));
            }
        }

        project.setTasks(remainingTasks);
        helper.saveProject(project);
    }

    @Override
    public void setDefaultWindow(DefaultWindow window) {
    }
}