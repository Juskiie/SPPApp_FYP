package k2028885.fyp.smartprojectplanner;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskChecklistFrameTest {
    private Project project;
    private TaskChecklistFrame frame;
    private Task task2;

    @BeforeEach
    void setUp() {
        Task task1 = new Task("Task 1", 2);
        task2 = new Task("Task 2", 3);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        project = new Project();
        project.setTasks(tasks);
        frame = new TaskChecklistFrame(project);
    }

    @Test
    void saveTasks_oneTaskOutOfTwo() {
        // Check the first task
        frame.getCheckboxes().get(0).setSelected(true);

        // Save the tasks
        frame.saveTasks();

        // Assert that the first task is removed
        assertEquals(1, project.getTasks().size());
        assertEquals(task2, project.getTasks().get(0));
    }

    @Test
    void saveTasks_bothTasksRemoved() {
        frame.getCheckboxes().get(0).setSelected(true);
        frame.getCheckboxes().get(1).setSelected(true);

        frame.saveTasks();

        assertEquals(0, project.getTasks().size());
    }
}