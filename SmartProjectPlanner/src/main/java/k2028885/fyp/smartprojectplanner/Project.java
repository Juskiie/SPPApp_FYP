package k2028885.fyp.smartprojectplanner;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Project implements Serializable {
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

    // Fully Parametrised Constructor
    public Project(String projectTitle,String description, Date deadline, List<Task> tasks)
    {
        this();
        this.projectTitle = projectTitle;
        this.description = description;
        this.deadline=deadline;
        this.tasks=tasks;
    }



    public String getProjectTitle() {
        return projectTitle;
    }

    public String getProjectDescription() { return description; }

    public Date getDeadline() {
        return deadline;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
