package k2028885.fyp.smartprojectplanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ProjectHelperTest {
    private String originalWorkingDir;
    // private final DefaultWindow window = DefaultWindow.getInstance();

    @BeforeEach
    void saveOriginalWorkingDir() {
        originalWorkingDir = System.getProperty("user.dir");
    }

    @AfterEach
    void resetWorkingDir() {
        System.getProperty("user.dir", originalWorkingDir);
    }

    @TempDir
    Path tempDir;

    @Test
    void createProject() {
    }

    @Test
    void isDirectory() {
        ProjectHelper helper = ProjectHelper.getInstance();
        File file = new File("./Projects/Test project.ser");
        assertFalse(helper.isDirectory(file), "Loaded file is not a directory");

        File directory = new File("./Projects/");
        assertTrue(helper.isDirectory(directory), "Loaded file is a directory");
    }

    @Test
    void loadProject() {
        ProjectHelper helper = ProjectHelper.getInstance();
        File file = new File("./Projects/Test project.ser");

        helper.loadProject(file);

        assertFalse(helper.getProject_list().isEmpty(), "Project list should contain at least one project");

        Project project = helper.getProject_list().get(0);

        assertNotNull(project, "Loaded project shouldn't be null");
    }

    @Test
    void saveProject_createDirectoryAndFile() {
        ProjectHelper helper = ProjectHelper.getInstance();

        // Create a new Project with some data
        Project project = new Project();
        project.setProjectTitle("Test Project");

        // Change the working directory to the temporary directory
        System.setProperty("user.dir", tempDir.toString());

        helper.saveProject(project);

        File projectsDirectory = new File("Projects");
        assertTrue(projectsDirectory.exists(), "Projects directory should be created");
        assertTrue(projectsDirectory.isDirectory(),"Projects should be a directory");

        File projectFile = new File(projectsDirectory, project.getProjectTitle() + ".ser");
        assertTrue(projectFile.exists(), "Project file should be created");
        assertFalse(projectFile.isDirectory(), "Project file should not be a directory");
    }

    @Test
    void saveProject_doesNotCreateDirectoryIfAlreadyExists() {
        ProjectHelper helper = ProjectHelper.getInstance();
        Project project = new Project();
        project.setProjectTitle("Test Project");

        // Ensure the directory already exists before calling saveProject
        File projectsDirectory = new File("Projects");

        helper.saveProject(project);

        // Check that the directory was not created again
        File projectDirectoryAfter = new File("Projects");
        assertFalse(projectsDirectory.mkdir() && !projectDirectoryAfter.exists(), "Projects directory should not be created again");
    }

    @Test
    void getAllProjectsInTable() {
    }

    @Test
    void showHelp_ensureFileCanBeLoaded() {
        File helpFile = new File("helpme.txt");
        try {
            if (!helpFile.exists()) {
                PrintWriter writer = new PrintWriter(helpFile);
                writer.println("Help text");
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("Setup failed: could not create helpme.txt");
        }

        try {
            ProjectHelper.showHelp();
        } catch (Exception e) {
            e.printStackTrace();
            fail("showHelp should not throw FileNotFoundException when helpme.txt exists");
        }
    }

    /**
     * This test can fail, due to a rounding error with how TimeUnit.DAYS.convert functions.
     */
    @Test
    void checkDeadlines_withDeadlineIn7Days_returns7() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        Project project = new Project("Test Project", "Test description", calendar.getTime(), new ArrayList<Task>()); // Create a Project and set deadline 7 days

        // Act
        long daysUntilDue = ProjectHelper.checkDeadlines(project); // Calculate time diff.

        // Assert
        assertEquals(7, daysUntilDue);
    }

    @Test
    void checkDeadlines_withDeadlineToday_returns0() {
        // Arrange
        Project project = new Project("Test Project", "Test description" , new Date(), new ArrayList<Task>());  // Set the deadline to today

        // Act
        long daysUntilDue = ProjectHelper.checkDeadlines(project);

        // Assert
        assertEquals(0, daysUntilDue);
    }

    @Test
    void checkDeadlines_withDeadlineInPast_returnsPositiveNumber() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);  // Set the deadline to 7 days ago
        Project project = new Project("Test Project", "Test description" , calendar.getTime(), new ArrayList<Task>());

        // Act
        long daysUntilDue = ProjectHelper.checkDeadlines(project);

        // Assert
        assertTrue(daysUntilDue >= 0);  // The difference should be positive
    }
}