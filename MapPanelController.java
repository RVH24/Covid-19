import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.layout.Pane;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import java.io.IOException;
import java.util.List;
import java.time.LocalDate;
import javafx.scene.paint.Paint;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;

/**
 * This is the controller class for the map panel.
 *
 * @author Rishi Hundia & Muhammed Keeka
 * @version 2024-03-25
 */
public class MapPanelController {
    // Used for Google Maps integration.
    @FXML
    private WebView mapWebView;
    private WebEngine webEngine;
    private static final String API_KEY = "ENTER_API_KEY_HERE";
    private Pane root;
    private String boroughName;
    private Label currentBorough;
    private LocalDate startDate;
    private LocalDate endDate;

    public MapPanelController()
    {
        // Load the FXML file.
        URL url = getClass().getResource("MapChooser.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load root pane.");
        }

        // Verify that mapWebView is not null.
        if (mapWebView == null) {
            throw new RuntimeException("mapWebView is null.");
        }

        // Set up the map in the WebView.
        webEngine = mapWebView.getEngine();
        if (webEngine == null) {
            throw new RuntimeException("WebEngine is null.");
        }
        setupImageBackground();
        currentBorough = new Label("");
        currentBorough.setStyle("-fx-text-fill: #ffffff");
        Pane tmp = (Pane) root.getChildren().get(0);
        BorderPane bp = new BorderPane();
        bp.setCenter(currentBorough);
        bp.setStyle("-fx-background-color: #000000");
        tmp.getChildren().add(bp);
    }

    /**
     * @return the root pane of the map panel.
     */
    public Pane getPane() {
        return root;
    }

    /**
     * Sets up the background image of the map panel.
     */
     private void setupImageBackground()
     {
         Image image = new Image(getClass().getResourceAsStream("London.jpg"));
         // This image is free for use: https://pixabay.com/photos/london-sunrise-england-city-river-5354315/
         BackgroundImage backgroundImage = new BackgroundImage(
                 image,
                 BackgroundRepeat.NO_REPEAT,
                 BackgroundRepeat.NO_REPEAT,
                 BackgroundPosition.DEFAULT,
                 new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
        );
        Background background = new Background(backgroundImage);
        
        root.setBackground(background);
    }

    /**
     * Removes the temporary text from before a borough has been selected.
     */
    private void removeText(){
        Text tmp = (Text) root.lookup("#TmpText");
        if (tmp != null){
            Pane parent = (Pane) tmp.getParent();
            parent.getChildren().remove(tmp);
        }
    }

    /**
     * Updates the heat map based on the selected date range.
     * @param startDate the start date.
     * @param endDate the end date.
     */
    public void heatMapUpdate(LocalDate startDate, LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        List<Polygon> polygonList = getPolygonList();
        Integer maxDeaths = CovidDataLoader.getMaxDeaths(startDate, endDate);
        for(Polygon p: polygonList){
            String borough = capitalize(p.getParent().getId().replace("+", " "));
            Integer deaths = CovidDataLoader.getDeaths(startDate, endDate, borough);
            if(maxDeaths != null && deaths != null)
            {
                if (deaths.equals(maxDeaths)){
                    p.setFill(Color.RED);
                }else if(deaths>(maxDeaths*0.66)){
                    Paint darkerOrange = Color.rgb(255, 100, 0);
                    p.setFill(darkerOrange);
                }else if (deaths>(maxDeaths*0.33)){
                    Paint lighterOrange = Color.rgb(255, 200, 0);
                    p.setFill(lighterOrange);
                }else{
                    p.setFill(Color.GREEN);
                }
            }
            else
            {
                Paint backgroundBlue = Color.rgb(44, 62, 80);
                p.setFill(backgroundBlue);
            }
            setupHoverEffect(p);
        }
    }

    /**
     * @return a list of Polygons representing the boroughs.
     */
    private List<Polygon> getPolygonList(){
        return ((Pane) root.getChildren().get(0))
                .getChildren().stream()
                .filter(e -> e instanceof AnchorPane)
                .map(e -> (Pane) e).findFirst().get()
                .getChildren().stream()
                .filter(e -> e instanceof StackPane)
                .map(e -> (Polygon) ((StackPane) e)
                        .getChildren().stream()
                        .filter(f -> f instanceof Polygon).findFirst().get())
                .toList();
    }

    /**
     * Loads the Google Maps iframe.
     */
    private void loadMaps(){
        String iframeContent = "<iframe width='100%' height='100%' style='border:0' loading='eager' allowfullscreen='true' "
                + "src='https://www.google.com/maps/embed/v1/place?key=" + API_KEY
                + "&q="+ boroughName.replace(" ", "+")+"'></iframe>";
    
        String htmlContent = "<html><head><style>"
                + "html, body { height: 100%; margin: 0; padding: 0; }"
                + ".flag-icon { position: relative; left: 10px; }"
                + "iframe { width: 100%; height: 100%; border: 0; }"
                + "</style></head><body>" + iframeContent + "</body></html>";
     
        webEngine.loadContent(htmlContent);
    }

    /**
     * Opens the new window on a StackPane click.
     * Also loads the Google Maps iframe.
     * Also updates the current borough label.
     * @param event the MouseEvent.
     */
    @FXML
    public void handleStackPaneClick(MouseEvent event) {
        StackPane clickedPane = (StackPane) event.getSource();
        boroughName = clickedPane.getId().replace("+", " ");
        removeText();
        loadMaps();
        currentBorough.setText("Current Borough: " + boroughName);
        BoroughWindow bw = new BoroughWindow(startDate, endDate, boroughName);
    }

    /**
     * Makes it so that the StackPane containing a Polygon p change colour when hovered over.
     * @param p the Polygon.
     */
    @FXML
    public void setupHoverEffect(Polygon p){
        StackPane parent = (StackPane) p.getParent();
        Color originalColor = (Color) p.getFill();
        parent.setOnMouseEntered(e -> p.setFill(Color.WHITE)); // Change color on hover
        parent.setOnMouseExited(e -> p.setFill(originalColor));
    }

    /**
     * Capitalizes the first letter of each word in a string.
     * I'm aware that a method that does this already exists in the Apache Commons library.
     * I don't want to have to import it though, when the method is so simple.
     * @param s the string.
     * @return the capitalized string.
     */
    private String capitalize(String s)
    {
        for(int i = 0; i < s.length() - 1; i++)
        {
            if(s.charAt(i) == ' ')
                s = s.substring(0, i + 1) + s.substring(i + 1, i + 2).toUpperCase() + s.substring(i + 2);
        }
        return s;
    }
}
