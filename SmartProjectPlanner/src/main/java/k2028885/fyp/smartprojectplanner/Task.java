package k2028885.fyp.smartprojectplanner;

import java.io.Serializable;

public class Task implements Serializable {
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

    public String getTaskName()
    {
        return taskName;
    }

    public int getTaskDuration()
    {
        return taskDuration;
    }

    @Override
    public String toString()
    {
        return taskName + ", " + taskDuration + " hours";
    }
}