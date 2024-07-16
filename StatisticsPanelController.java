import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.text.Text;
import java.net.URL;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This is the controller class for the statistic panel.
 * 
 * @author Maiwand Nikmal & Muhammed Keeka
 * @version 2024-03-25
 */
public class StatisticsPanelController {
    private Pane root;
    @FXML
    private Text statistic;
    @FXML
    private Text data;
    private ArrayList<Statistic> statistics;
    private int currentStatisticIndex;

    public StatisticsPanelController()
    {
        // Load the FXML file
        URL url = getClass().getResource("StatisticsPanel.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);

        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        statistics = new ArrayList<>();
    }

    public Pane getPane()
    {
        return root;
    }
    
    /**
     * Creates all the statistics and displays the first one.
     * @param startDate the start date for creating each statistic.
     * @param endDate the end date for creating each statistic.
     */
    public void createStatistics(LocalDate startDate, LocalDate endDate)
    {
        statistics.clear();
        statistics.add(new TotalDeaths(startDate, endDate));
        statistics.add(new ParksGmrAverage(startDate, endDate));
        statistics.add(new TransitGmrAverage(startDate, endDate));
        statistics.add(new TotalCasesPerBorough(startDate, endDate));
        statistics.add(new TotalCasesPerDay(startDate, endDate));
        currentStatisticIndex = 0;
        displayCurrentStatistic();
    }
    
    /**
     * Displays the next statistic
     */
    @FXML
    private void nextStatistic() {
        if(currentStatisticIndex != statistics.size()-1)
            currentStatisticIndex++;
        else
            currentStatisticIndex = 0;
        displayCurrentStatistic();
    }
    
    /**
     * Displays the previous statistic
     */
    @FXML
    private void previousStatistic() {
        if(currentStatisticIndex != 0)
            currentStatisticIndex--;
        else
            currentStatisticIndex = statistics.size()-1;
        displayCurrentStatistic();
    }
    
    /**
     * Displays the current statistic
     */
    private void displayCurrentStatistic()
    {
        statistic.setText(statistics.get(currentStatisticIndex).getName());
        if(statistics.get(currentStatisticIndex).getData() == null)
            data.setText("Sufficient data to calculate this statistic is unavailable over the selected period");
        else
            data.setText("" + statistics.get(currentStatisticIndex).getData());
    }
}