package k2028885.fyp.smartprojectplanner;

import java.io.Serializable;

public class Task implements Serializable {
    private final String taskName;
    private final int taskDuration;

    public Task(String taskName, int taskDuration)
    {
        this.taskName = taskName;
        this.taskDuration = taskDuration;
    }

    public Task(String taskName)
    {
        this.taskName = taskName;
        this.taskDuration = 0;
    }

    public Task()
    {
        this.taskName = "New task";
        this.taskDuration = 0;
    }

    public String getTaskName()
    {
        return taskName;
    }

    public int getTaskDuration()
    {
        return taskDuration;
    }
}