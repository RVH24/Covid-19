import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import java.net.URL;
import java.io.IOException;

/**
 * This is the controller class for the welcome panel.
 * 
 * @author Maiwand Nikmal & Muhammed Keeka
 * @version 2024-03-25
 */
public class WelcomePanelController {
    @FXML
    private Text dateStatus;
    private Pane root;
    
    public WelcomePanelController()
    {
        // Load the FXML file.
        URL url = getClass().getResource("WelcomePanel.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        loader.setController(this);
        
        try
        {
            root = loader.load();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return the root pane of the welcome panel.
     */
    public Pane getPane()
    {
        return root;
    }

    /**
     * Sets the status of the date range.
     *
     * @param status the status.
     */
    public void setDateStatus(String status)
    {
        dateStatus.setText(status);
    }
}

