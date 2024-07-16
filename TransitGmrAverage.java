import java.time.LocalDate;

/**
 * This Statistic class represents the average transit GMR in the given date 
 * range.
 *
 * @author Muhammed Keeka & Maiwand Nikmal
 * @version 2024-03-25
 */
public class TransitGmrAverage extends Statistic
{ 
    public TransitGmrAverage(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        name = "Percent change in visits to public transport hubs";
    }
    
    @Override
    protected Integer calculateStatistic()
    {
        return CovidDataLoader.getTransitGMR(startDate, endDate);
    }
}