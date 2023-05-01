package k2028885.fyp.smartprojectplanner;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project implements Serializable {
    @Serial
    private static final long serialVersionUID  = 202888520288851234L; // This value must be declared so projects saved conform to this ID
    private String projectTitle;
    private String description;
    private Date deadline;
    private List<Task> tasks;

    // Default constructor
    public Project()
    {
        this.projectTitle = "New Project";
        this.description = "This is a new project";
        this.deadline = new Date();
        this.tasks = new ArrayList<>();
    }

    /**
     *
     * @param projectTitle String value for the title of the project
     * @param description String value for the description of the project
     * @param deadline Date value for the deadline of the project
     * @param tasks List of tasks for the project
     */
    public Project(String projectTitle,String description, Date deadline, List<Task> tasks)
    {
        this();
        this.projectTitle = projectTitle;
        this.description = description;
        this.deadline=deadline;
        this.tasks=tasks;
    }

    /**
     * Gets the title field of the project.
     * @return projectTitle
     */
    public String getProjectTitle() {
        return projectTitle;
    }

    /**
     * Gets the description field of the project.
     * @return description
     */
    public String getProjectDescription() {
        return description;
    }

    /**
     * Returns the deadline (Date) of the current project.
     * @return deadline
     */
    public Date getDeadline() {
        return deadline;
    }

    /**
     * Gets all tasks from the project as a list.
     * @return List<Task>
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the tasks for the project.
     * @param tasks The list of tasks
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Sets the title of the project.
     * @param title The project title
     */
    public void setProjectTitle(String title) {
        this.projectTitle = title;
    }

    /**
     * Sorts the task list of a project by duration, from longest to shortest.
     * @return List<Task>
     */
    public List<Task> getFormattedTaskList()
    {
        List<Task> sortedTasks = new ArrayList<>(this.tasks);

        sortedTasks.sort((task1, task2) -> Integer.compare(task2.getDuration(), task1.getDuration()));

        return sortedTasks;
    }

    @Override
    public String toString()
    {
        return "Project title: " + projectTitle + ", Description: " + description + ", Deadline: " + deadline + ", Tasks: " + tasks.toString();
    }
}
