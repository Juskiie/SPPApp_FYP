package k2028885.fyp.smartprojectplanner;


import com.toedter.calendar.JCalendar;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class CalendarTaskPlanner extends JFrame implements GUIComponent {
    private JTable taskTable;
    private final String stringPattern = "dd-MM-yyyy";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stringPattern) ;

    /**
     * Default constructor for task planner.
     * Never has to change parameters (currently).
     */
    public CalendarTaskPlanner() {
        createAndShowGUI();
    }

    @Override
    public void initComponents() {
        JCalendar calendar = new JCalendar();
        calendar.addPropertyChangeListener("calendar", new CalendarChangeListener());

        taskTable = new JTable();
        taskTable.setModel(new DefaultTableModel(new Object[]{"Project", "Description", "Deadline","Tasks"}, 0)); // NOTE: TO-DO: Change "Duration" to start time and end time.

        add(calendar, BorderLayout.WEST);
        add(new JScrollPane(taskTable), BorderLayout.CENTER);
    }

    @Override
    public void createAndShowGUI() {
        setTitle("Task Planner");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        initComponents();
        setVisible(true);
    }

    @Override
    public void setDefaultWindow(DefaultWindow window) {
    }

    /**
     * Inner-class of CalendarTaskPlanner which enables calendar cell updates.
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

    /**
     * Scans currently loaded project list for projects matching the passed date, and returns them as a List
     * @param selectedDate The date to find projects for (based on deadline).
     * @return A list of project files
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
                System.out.println(formatDate(project.getDeadline()));
                System.out.println(formatDate(selectedDate));
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