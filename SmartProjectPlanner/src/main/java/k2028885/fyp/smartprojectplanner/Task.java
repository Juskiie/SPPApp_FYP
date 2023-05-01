package k2028885.fyp.smartprojectplanner;

import java.io.Serial;
import java.io.Serializable;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID  = 202888520288851234L; // This value must be declared so projects saved conform to this ID
    private String taskName;
    private int taskDuration;

    public Task()
    {
        this.taskName = "New task";
        this.taskDuration = 0;
    }

    /**
     * Fully parameterized constructor for Task objects
     * @param taskName Name of the task
     * @param taskDuration Duration, in hours, of the task
     */
    public Task(String taskName, int taskDuration)
    {
        this();
        this.taskName = taskName;
        this.taskDuration = taskDuration;
    }

    /**
     * This retrieves the duration of the current task, in hours.
     * @return The duration of the task in hours, as an integer,
     */
    public int getDuration() {
        return taskDuration;
    }

    @Override
    public String toString()
    {
        return " Task: " + taskName + ", " + "Duration: " + taskDuration + " hours ";
    }
}