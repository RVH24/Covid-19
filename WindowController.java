import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.DateCell;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import javafx.scene.layout.Pane;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * This is the controller class for the main window.
 *
 * @author Maiwand Nikmal & Muhammed Keeka
 * @version 2024-03-25
 */
public class WindowController extends Application
{
    // This stores our four panels.
    private ArrayList<Pane> panels;
    // This stores the index of the current panel in the ArrayList.
    private int currentPanel;
    private BorderPane root;
    // In addition to the panels we keep reference to the controllers of each panel.
    private WelcomePanelController wpc;
    private MapPanelController mpc;
    private StatisticsPanelController spc;
    private GraphPanelController gp;
    // Below are elements from SceneBuilder.
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    // The first and last date in the dataset, used for restricting the date range.
    private final LocalDate FIRST_DATE = LocalDate.of(2020, 2, 3);
    private final LocalDate LAST_DATE = LocalDate.of(2023, 2, 9);
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {                
        panels = new ArrayList<>();
        
        URL url = getClass().getResource("Window.fxml");
        
        // Load the root.
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        // Restrict the dates that can be selected, and disable the buttons.
        restrictDates();
        backButton.setDisable(true);
        nextButton.setDisable(true);

        // Load the panels.
        wpc = new WelcomePanelController();
        Pane welcomePanel = wpc.getPane();
        panels.add(welcomePanel);
        
        mpc = new MapPanelController();
        Pane mapPanel = mpc.getPane();
        panels.add(mapPanel);
        
        spc = new StatisticsPanelController();
        Pane statsPanel = spc.getPane();
        panels.add(statsPanel);
        
        gp = new GraphPanelController();
        Pane graphPane = gp.getPane();
        panels.add(graphPane);
        
        // Set the current panel to the welcome panel.
        currentPanel = 0;
        root.setCenter(panels.get(currentPanel));

        // JavaFX must have a Scene (window content) inside a Stage (window).
        Scene scene = new Scene(root);
        stage.setTitle("London COVID-19 Statistics");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Switches to the next panel.
     * Valid date range must be selected first.
     * This is ensured through keeping the buttons disabled until a valid date range is selected.
     */
    @FXML
    private void nextPanel()
    { 
        if(currentPanel != panels.size()-1)
            currentPanel++;
        else
            currentPanel = 0;
        root.setCenter(panels.get(currentPanel));
    }
    
    /**
     * Switches to the previous panel.
     * Valid date range must be selected first.
     * This is ensured through keeping the buttons disabled until a valid date range is selected.
     */
    @FXML
    private void previousPanel()
    {
        if(currentPanel != 0)
            currentPanel--;
        else
            currentPanel = panels.size()-1;
        root.setCenter(panels.get(currentPanel));
    }
    
    /**
     * Validates the selected date range.
     */
    @FXML
    private void checkDateRange()
    {
        LocalDate startDateSelected = startDate.getValue();
        LocalDate endDateSelected = endDate.getValue();
        if(startDate != null && endDateSelected != null)
        {
            if(startDateSelected.isAfter(endDateSelected)) {
                // Notify the user is the date range is invalid.
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Date Range Error");
                alert.setContentText("Invalid date range selected!");
                alert.show();
                wpc.setDateStatus("Invalid date range selected!");
                // Lock into the welcome panel and disable buttons until a valid date range is selected.
                root.setCenter(panels.get(0));
                backButton.setDisable(true);
                nextButton.setDisable(true);
            }
            else {
                displayDateRange();
                backButton.setDisable(false);
                nextButton.setDisable(false);
                mpc.heatMapUpdate(startDateSelected, endDateSelected);
                spc.createStatistics(startDateSelected, endDateSelected);
                gp.createGraphs(startDateSelected, endDateSelected);
            }
        }
    }
    
    /**
     * Displays the selected date range.
     */
    private void displayDateRange()
    {
        String formattedStartDate = startDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String formattedEndDate = endDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        wpc.setDateStatus("Selected: " + formattedStartDate + " to " + formattedEndDate);
    }
    
    /**
     * Restricts the dates that can be selected.
     * I'm convinced there was a way to do this with a lambda but sadly could not make it work.
     */
    private void restrictDates()
    {
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item.isBefore(FIRST_DATE) || item.isAfter(LAST_DATE)){
                            setDisable(true);
                        }
                    }
                };
            }
        };
        startDate.setDayCellFactory(dayCellFactory);
        endDate.setDayCellFactory(dayCellFactory);
    }
}
