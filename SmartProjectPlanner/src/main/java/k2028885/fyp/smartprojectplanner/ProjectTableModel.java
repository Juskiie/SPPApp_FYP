package k2028885.fyp.smartprojectplanner;

import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProjectTableModel extends AbstractTableModel {

    private final List<Project> projects;
    private final String[] columnNames = {"Title", "Description", "Deadline", "Tasks"};

    /**
     * Takes a project list to model the table for.
     * @param projects The projects to be loaded into the table, as a List.
     */
    public ProjectTableModel(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int getRowCount() {
        return projects.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Project project = projects.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> project.getProjectTitle();
            case 1 -> project.getProjectDescription();
            case 2 -> project.getDeadline();
            case 3 -> getTaskNames(project.getTasks());
            default -> "";
        };
    }

    /**
     * Takes the task list of a Project and returns them as a string.
     * @param tasks The tasks of a chosen Project, as a List.
     * @return Task list as a String
     */
    private String getTaskNames(@NotNull List<Task> tasks) {
        return tasks.toString();
    }
}
