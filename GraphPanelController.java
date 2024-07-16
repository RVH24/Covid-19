import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

/**
 * Displays a graph of the Covid cases over time.
 * 
 * @author Rishi Hundia & Muhammed Keeka
 * @version 2024-03-25
 */
public class GraphPanelController {
    
    private Pane root;
    @FXML
    private BorderPane innerPane;
    private ArrayList<LineChart> graphs;
    private int currentGraphIndex;
    
    public GraphPanelController()
    {
        // Load the FXML file
        URL url = getClass().getResource("GraphPanel.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        
        //Verify that the innerPane is not null
        if (innerPane == null) {
            throw new RuntimeException("innerPane is null.");
        }
        
        root.setPrefWidth(Region.USE_COMPUTED_SIZE);
        root.setPrefHeight(Region.USE_COMPUTED_SIZE);
    }
    
    public Pane getPane(){
        return root;
    }
    
    /**
     * Creates all the graphs and displays the first one
     * @param startDate the start date for the data
     * @param endDate the end date for the data
     */
    public void createGraphs(LocalDate startDate, LocalDate endDate){
        graphs = new ArrayList<>();
        graphs.add(casesGraph(startDate, endDate));
        graphs.add(deathsGraph(startDate, endDate));
        currentGraphIndex = 0;
        innerPane.setCenter(graphs.get(currentGraphIndex));
    }
    
    /**
     * Creates a LineChart graph displaying the number of cases over time
     * @param startDate the start date for the data
     * @param endDate the end date for the data
     * @return the LineChart created
     */
    private LineChart<String, Number> casesGraph(LocalDate startDate, LocalDate endDate) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Covid Cases");
        LineChart<String,Number> lc = new LineChart<>(xAxis,yAxis);
        lc.setTitle("Covid Cases Over Time");
        XYChart.Series series = new XYChart.Series();
        series.setName("New Cases Per Day");
        Map<LocalDate, Integer> data = CovidDataLoader.getCasesPerDay(startDate, endDate);
        Set<LocalDate> keys = data.keySet();
        keys.stream().filter(e -> data.get(e) != null).forEach(e -> series.getData().add(new XYChart.Data(e.toString(), data.get(e))));
        lc.getData().add(series);
        return lc;
    }

     /**
     * Creates a LineChart graph displaying the number of deaths over time.
     * @param startDate the start date for the data
     * @param endDate the end date for the data
     * @return the LineChart created
     */
    private LineChart<String, Number> deathsGraph(LocalDate startDate, LocalDate endDate) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Covid Deaths");
        LineChart<String,Number> lc = new LineChart<>(xAxis,yAxis);
        lc.setTitle("Covid Deaths Over Time");
        XYChart.Series series = new XYChart.Series();
        series.setName("New Deaths Per Day");
        Map<LocalDate, Integer> data = CovidDataLoader.getDeathsPerDay(startDate, endDate);
        Set<LocalDate> keys = data.keySet();
        keys.stream().filter(e -> data.get(e) != null).forEach(e -> series.getData().add(new XYChart.Data(e.toString(), data.get(e))));
        lc.getData().add(series);
        return lc;
    }
    
    /**
     * Displays the next graph.
     */
    @FXML
    private void nextGraph() {
        if(currentGraphIndex != graphs.size()-1)
            currentGraphIndex++;
        else
            currentGraphIndex = 0;
        innerPane.setCenter(graphs.get(currentGraphIndex));
    }
    
    /**
     * Displays the previous graph.
     */
    @FXML
    private void previousGraph() {
        if(currentGraphIndex != 0)
            currentGraphIndex--;
        else
            currentGraphIndex = graphs.size()-1;
        innerPane.setCenter(graphs.get(currentGraphIndex));
    }
}
