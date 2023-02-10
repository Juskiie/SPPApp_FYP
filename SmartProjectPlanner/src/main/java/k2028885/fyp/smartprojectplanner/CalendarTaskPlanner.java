package k2028885.fyp.smartprojectplanner;


import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JCalendar;
import org.jetbrains.annotations.NotNull;

public class CalendarTaskPlanner extends JFrame {
    private final JTable taskTable;
    private final String stringPattern = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stringPattern) ;

    /*
     * Default constructor for task planner.
     * Never has to change parameters (currently)
     */

    public CalendarTaskPlanner() {
        setTitle("Task Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JCalendar calendar = new JCalendar();
        calendar.addPropertyChangeListener("calendar", new CalendarChangeListener());

        taskTable = new JTable();
        taskTable.setModel(new DefaultTableModel(new Object[]{"Project", "Task", "Deadline","Tasks"}, 0)); // NOTE: TO-DO: Change "Duration" to start time and end time.

        add(calendar, BorderLayout.WEST);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);

        setVisible(true);
    }

    /*
     * 1. Override implement PropertyChangeListener interface
     * 2. Override propertyChange method to support Calendar cell updates
     * 3. Get new changed value of calendar cell for selected date
     * 4. Assign a new var with all project tasks
     * 5. Generate a new table for the chosen date
     */
    private class CalendarChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Date selectedDate = ((Calendar) evt.getNewValue()).getTime();   // Get selected Date from cast to calendar object
            List<Project> projects = retrieveProjectsForDate(selectedDate);
            ProjectHelper.LOGGER.log(Level.INFO,"selectedDate value:" + selectedDate);
            DefaultTableModel model = (DefaultTableModel) taskTable.getModel();
            model.setRowCount(0);
            for (Project proj : projects) {
                model.addRow(new Object[]
                        {
                                proj.getProjectTitle(),
                                proj.getProjectDescription(),
                                proj.getDeadline(),
                                proj.getTasks()
                        });
            }
        }
    }

    /*
     * 1. Get all projects loaded in project table (Default window)
     * 2. Get all tasks associated with projects from window
     * 3. Parse data to local vars for use in calendar view
     * 4. Return this list to caller
     */
    private @NotNull List<Project> retrieveProjectsForDate(Date selectedDate) {
        // Code to retrieve the tasks for the selected date from the database or file.
        List<Project> projectsIn = new ArrayList<>(ProjectHelper.getAllProjectsInTable());   // Add all projects loaded/stored in the global project table set
        for(Project p : projectsIn)
        {
            System.out.println(p);
        }
        System.out.println();
        List<Project> projectsOut = new ArrayList<>();
        ProjectHelper.LOGGER.log(Level.INFO, "Method 'retrieveProjectsForDate' called with parameter ["+selectedDate.toString()+"] of type 'Date'");

        for(Project project : projectsIn)
        {
            if (formatDate(project.getDeadline()).equals(formatDate(selectedDate)))
            {
                System.out.println(true);
                projectsOut.add(project);
            }
        }
        return projectsOut;
    }

    private String formatDate(Date date)
    {
        return simpleDateFormat.format(date);
    }
}