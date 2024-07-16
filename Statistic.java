import java.time.LocalDate;

/**
 * This abstract class represents a single statistic in the statistics panel.
 *
 * @author Maiwand Nikmal & Muhammed Keeka
 * @version 2024-03-25
 */
public abstract class Statistic
{
    protected String name; //name of the statistic
    protected Integer data; //data to be displayed
    protected LocalDate startDate;
    protected LocalDate endDate;

    /**
     * Constructor for the Statistic class
     * @param startDate the start date of the statistic
     * @param endDate the end date of the statistic
     */
    public Statistic(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        name = "";
        data = calculateStatistic();
    }

    /**
     * Returns the name of the statistic
     * @return the name of the statistic as a String
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the data of the statistic
     * @return the data of the statistic as an Integer
     */
    public Integer getData() {
        return data;
    }
    
    /**
     * Calculates the statistic from the data in the database
     * @return the calculated statistic as an Integer
     */
    protected abstract Integer calculateStatistic();
}
