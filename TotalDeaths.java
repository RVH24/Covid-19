import java.time.LocalDate;

/**
 * This Statistic class represents the total deaths in the given date range.
 *
 * @author Maiwand Nikmal & Muhammed Keeka
 * @version 2024-03-25
 */
public class TotalDeaths extends Statistic
{ 
    public TotalDeaths(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        name = "Total Deaths across all London boroughs";
    }
    
    @Override
    protected Integer calculateStatistic()
    {
        return CovidDataLoader.getDeaths(startDate, endDate);
    }
}