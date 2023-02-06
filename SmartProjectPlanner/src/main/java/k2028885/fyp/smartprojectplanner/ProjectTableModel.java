package k2028885.fyp.smartprojectplanner;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProjectTableModel extends AbstractTableModel {

    private final List<Project> projects;
    private final String[] columnNames = {"Title", "Description", "Deadline", "Tasks"};

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
        switch (columnIndex) {
            case 0:
                return project.getProjectTitle();
            case 1:
                return project.getProjectDescription();
            case 2:
                return project.getDeadline();
            case 3:
                return getTaskNames(project.getTasks());
            default:
                return "";
        }
    }

    private String getTaskNames(List<Task> tasks) {
        StringBuilder sb = new StringBuilder();
        for (Task task : tasks) {
            sb.append(task.getTaskName());
            sb.append(", ");
        }
        if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

    public void addProject(Project project)
    {
        projects.add(project);
        fireTableDataChanged();
    }
}
