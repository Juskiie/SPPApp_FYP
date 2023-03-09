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

    public Task(String taskName, int taskDuration)
    {
        this();
        this.taskName = taskName;
        this.taskDuration = taskDuration;
    }

    @Override
    public String toString()
    {
        return "[ Task: "+taskName + ", " + "Duration: " +taskDuration + " hours ]";
    }
}