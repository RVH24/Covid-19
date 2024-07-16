import java.time.LocalDate;

/**
 * This Statistic class represents the total cases per day in the given date
 * range. 
 *
 * @author Maiwand Nikmal & Muhammed Keeka
 * @version 2024-03-25
 */
public class TotalCasesPerDay extends Statistic
{
    public TotalCasesPerDay(LocalDate startDate, LocalDate endDate) {
        super(startDate, endDate);
        name = "Average cases per day";
    }
    
    @Override
    protected Integer calculateStatistic()
    {
        Integer cases = CovidDataLoader.getCases(startDate, endDate);
        if(cases!=null)
            cases = cases/(startDate.until(endDate).getDays()+1);
        return cases;
    }
}
