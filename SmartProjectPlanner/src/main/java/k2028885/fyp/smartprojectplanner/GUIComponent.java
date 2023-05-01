package k2028885.fyp.smartprojectplanner;

/**
 * The GUIComponent interface represents a generic component that can be used to create and display a graphical user interface.
 * It provides methods to initialize components, create and show the GUI, and set the shared default window across all classes.
 */
public interface GUIComponent {
    /**
     * Initializes the components of the GUI.
     */
    void initComponents();      // Method to initialize components
    /**
     * Creates and displays the GUI.
     */
    void createAndShowGUI();    // Method to create and display the GUI
    /**
     * Sets the shared DefaultWindow object that will be used by all classes.
     * @param window The shared DefaultWindow object to be set.
     */
    void setDefaultWindow(DefaultWindow window);    // Set the shared DefaultWindow across all classes
}
