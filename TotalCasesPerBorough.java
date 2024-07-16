import java.time.LocalDate;

/**
 * This Statistic class represents the total cases per borough in the given 
 * date range.
 *
 * @author Muhammed Keeka & Maiwand Nikmal
 * @version 2024-03-25
 */
public class TotalCasesPerBorough extends Statistic
{ 
    public TotalCasesPerBorough(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        name = "Average cases per borough";
    }
    
    @Override
    protected Integer calculateStatistic()
    {
        Integer cases = CovidDataLoader.getCases(startDate, endDate);
        if(cases!=null)
            cases = cases/33;
        return cases;
    }
}
