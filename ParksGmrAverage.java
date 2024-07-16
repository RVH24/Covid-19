import java.time.LocalDate;

/**
 * This Statistic class represents the average of the parks GMR in the given 
 * date range.
 *
 * @author Maiwand Nikmal and Muhammed Keeka
 * @version 2024-03-25
 */
public class ParksGmrAverage extends Statistic
{ 
    public ParksGmrAverage(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        name = "Percent change in visits to parks";
    }
    
    @Override
    protected Integer calculateStatistic()
    {
        return CovidDataLoader.getParksGMR(startDate, endDate);
    }
}