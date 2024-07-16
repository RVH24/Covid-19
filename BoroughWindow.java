import javafx.application.Application;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ComboBox;
import javafx.stage.Screen;
import javafx.scene.layout.HBox;

/**
 * The window in which a borough's data is displayed.
 *
 * @author Muhammed Keeka
 * @version 2024-03-25
 */
public class BoroughWindow extends Application
{
    private GridPane pane;
    private LocalDate start, end;
    private String boroughName;
    private final String[] FIELD_NAMES = {"Date", "Retail Recreational GMR", "Grocery Pharmacy GMR", "Parks GMR", "Transit GMR", "Workplaces GMR", "Residential GMR", "New Cases", "Total Cases", "New Deaths"};

    public BoroughWindow(LocalDate start, LocalDate end, String boroughName)
    {
        this.start = start;
        this.end = end;
        this.boroughName = boroughName;
        start(new Stage());
    }
    
    /**
     * The start method is the main entry point for every JavaFX application. 
     * It is called after the init() method has returned and after 
     * the system is ready for the application to begin running.
     *
     * @param  stage the primary stage for this application.
     */
    @Override
    public void start(Stage stage)
    {

        BorderPane root = new BorderPane();
        
        // Create a new grid pane
        pane = new GridPane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setVgap(10);
        pane.setHgap(10);
        setColumnNames();
        
        ScrollPane sp = new ScrollPane(pane);
        sp.setMinSize(927, 300);
        sp.setMaxSize(927, Screen.getPrimary().getVisualBounds().getHeight() - 100);
        
        ComboBox field = new ComboBox();
        field.getItems().addAll(FIELD_NAMES);
        field.valueProperty().addListener((observable, oldValue, newValue) -> {
            orderBy(newValue.toString());
        });
        field.setValue("Date");
        
        HBox hb = new HBox(new Label("Order by: "), field);
        
        root.setTop(hb);
        root.setCenter(sp);
        
        // JavaFX must have a Scene (window content) inside a Stage (window)
        Scene scene = new Scene(root);
        stage.setTitle(boroughName + " Statistics");
        stage.setScene(scene);
        
        // Show the Stage (window)
        stage.show();
    }
    
    private void setColumnNames()
    {
        for(int i = 0; i < 10; i++)
        {
            pane.add(new Label(FIELD_NAMES[i]), i, 1);
        }
    }
    
    private void orderBy(String field)
    {
        pane.getChildren().clear();
        setColumnNames();
        List<CovidData> data = CovidDataLoader.getBoroughData(start, end, boroughName, field.replace(" ", ""));
        int row = 2;
        for(CovidData d : data)
        {
            pane.add(new Label(d.getDate().toString()), 0, row);

            ArrayList<Label> labels = new ArrayList<>();
            for(int i = 1; i < 10; i++)
            {
                Label label = new Label("N/A");
                labels.add(label);
                pane.add(label, i, row);
            }
            
            if(d.getRetailRecreationalGMR() != null)
                labels.get(0).setText(d.getRetailRecreationalGMR().toString());
            if(d.getGroceryPharmacyGMR() != null)
                labels.get(1).setText(d.getGroceryPharmacyGMR().toString());
            if(d.getParksGMR() != null)
                labels.get(2).setText(d.getParksGMR().toString());
            if(d.getTransitGMR() != null)
                labels.get(3).setText(d.getTransitGMR().toString());
            if(d.getWorkplacesGMR() != null)
                labels.get(4).setText(d.getWorkplacesGMR().toString());
            if(d.getResidentialGMR() != null)
                labels.get(5).setText(d.getResidentialGMR().toString());
            if(d.getNewCases() != null)
                labels.get(6).setText(d.getNewCases().toString());
            if(d.getTotalCases() != null)
                labels.get(7).setText(d.getTotalCases().toString());
            if(d.getNewDeaths() != null)
                labels.get(8).setText(d.getNewDeaths().toString());

            row++;
        }
    }
}
