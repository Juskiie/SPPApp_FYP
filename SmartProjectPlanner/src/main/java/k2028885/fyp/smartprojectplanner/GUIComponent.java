package k2028885.fyp.smartprojectplanner;

public interface GUIComponent {
    void initComponents();      // Method to initialize components
    void createAndShowGUI();    // Method to create and display the GUI
    void setDefaultWindow(DefaultWindow window);    // Set the shared DefaultWindow across all classes
}
