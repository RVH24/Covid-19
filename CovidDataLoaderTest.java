import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;

/**
 * The test class for the CovidDataLoader class.
 *
 * @author  Muhammed Keeka
 * @version 2024-03-25
 */
public class CovidDataLoaderTest
{

    /**
     * Default constructor for test class CovidDataLoaderTest
     */
    public CovidDataLoaderTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }

    /**
     * Ensure that all records are present in the database.
     */
    @Test
    public void assertRecordCount()
    {
        assertEquals(36399, CovidDataLoader.getRecordCount());
    }

    /**
     * Ensure that the getCases() method for all boroughs functions correctly.
     */
    @Test
    public void assertCasesAllBoroughs()
    {
        assertEquals(8873, CovidDataLoader.getCases(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1)));
    }

    /**
     * Ensure that the getCases() method for a specific borough functions correctly.
     */
    @Test
    public void assertCasesSpecificBorough()
    {
        assertEquals(201, CovidDataLoader.getCases(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), "Sutton"));
    }

    /**
     * Ensure that the getMaxCases() method functions correctly.
     */
    @Test
    public void assertMaxCases()
    {
        assertEquals(10743, CovidDataLoader.getMaxCases(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31)));
        assertNull(CovidDataLoader.getMaxCases(LocalDate.of(2023, 3, 1), LocalDate.of(2023, 3, 31)));
        assertNull(CovidDataLoader.getMaxCases(LocalDate.of(2023, 2, 8), LocalDate.of(2023, 2, 9)));
        assertNull(CovidDataLoader.getMaxCases(LocalDate.of(2023, 2, 9), LocalDate.of(2023, 2, 9)));
        assertEquals(28, CovidDataLoader.getMaxCases(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1)));
        assertEquals(0, CovidDataLoader.getMaxCases(LocalDate.of(2023, 1, 2), LocalDate.of(2023, 1, 1)));
    }

    /**
     * Ensure that the getDeaths() method for all boroughs functions correctly.
     */
    @Test
    public void assertDeathsAllBoroughs()
    {
        assertEquals(325, CovidDataLoader.getDeaths(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1)));
    }

    /**
     * Ensure that the getDeaths() method for a specific borough functions correctly.
     */
    @Test
    public void assertDeathsSpecificBorough()
    {
        assertEquals(5, CovidDataLoader.getDeaths(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), "Sutton"));
    }

    /**
     * Ensure that the getMaxDeaths() method functions correctly.
     */
    @Test
    public void assertMaxDeaths()
    {
        assertEquals(23, CovidDataLoader.getMaxDeaths(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31)));
        assertNull(CovidDataLoader.getMaxDeaths(LocalDate.of(2023, 3, 1), LocalDate.of(2023, 3, 31)));
        assertEquals(3, CovidDataLoader.getMaxDeaths(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 1, 1)));
        assertEquals(0, CovidDataLoader.getMaxDeaths(LocalDate.of(2023, 1, 2), LocalDate.of(2023, 1, 1)));
    }

    /**
     * Ensure that the getParksGMR() method functions correctly.
     */
    @Test
    public void assertParksGMR()
    {
        assertEquals(12, CovidDataLoader.getParksGMR(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31)));
        assertNull(CovidDataLoader.getParksGMR(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1)));
    }

    /**
     * Ensure that the getTransitGMR() method functions correctly.
     */
    @Test
    public void assertTransitGMR()
    {
        assertEquals(-32, CovidDataLoader.getTransitGMR(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 3, 31)));
        assertNull(CovidDataLoader.getTransitGMR(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1)));
    }

    /**
     * Ensure that the earliest date is correct.
     */
    @Test
    public void assertEarliestDate()
    {
        assertEquals(LocalDate.of(2020, 2, 3), CovidDataLoader.getEarliestDate());
    }

    /**
     * Ensure that the latest date is correct.
     */
    @Test
    public void assertLatestDate()
    {
        assertEquals(LocalDate.of(2023, 2, 9), CovidDataLoader.getLatestDate());
    }

    /**
     * Ensure that the getCasesPerDay() method functions correctly.
     */
    @Test
    public void assertCasesPerDay()
    {
        Map<LocalDate, Integer> data = CovidDataLoader.getCasesPerDay(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1));
        assertEquals(32, data.size());
        assertEquals(328, data.get(LocalDate.of(2023, 1, 1)));
        assertEquals(199, data.get(LocalDate.of(2023, 1, 15)));
        assertEquals(379, data.get(LocalDate.of(2023, 2, 1)));
    }

    /**
     * Ensure that the getDeathsPerDay() method functions correctly.
     */
    @Test
    public void assertDeathsPerDay()
    {
        Map<LocalDate, Integer> data = CovidDataLoader.getDeathsPerDay(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1));
        assertEquals(32, data.size());
        assertEquals(15, data.get(LocalDate.of(2023, 1, 1)));
        assertEquals(13, data.get(LocalDate.of(2023, 1, 15)));
        assertEquals(4, data.get(LocalDate.of(2023, 2, 1)));
    }

    /**
     * Ensure that the borough data is being fetched correctly.
     */
    @Test
    public void assertBoroughData()
    {
        List<CovidData> data = CovidDataLoader.getBoroughData(LocalDate.of(2022, 5, 1), LocalDate.of(2022, 5, 31), "Sutton", "Date");
        assertEquals(31, data.size());
        for(CovidData d: data)
        {
            assertEquals(d.getBorough(), "Sutton");  
        }
        assertEquals(LocalDate.of(2022, 5, 1), data.get(0).getDate());
        assertEquals(-20, data.get(0).getRetailRecreationalGMR());
        assertEquals(-13, data.get(0).getGroceryPharmacyGMR());
        assertEquals(-4, data.get(0).getParksGMR());
        assertEquals(-28, data.get(0).getTransitGMR());
        assertEquals(-14, data.get(0).getWorkplacesGMR());
        assertEquals(-1, data.get(0).getResidentialGMR());
        assertEquals(24, data.get(0).getNewCases());
        assertEquals(72879, data.get(0).getTotalCases());
        assertEquals(2, data.get(0).getNewDeaths());
        assertEquals(565, data.get(0).getTotalDeaths());
        assertEquals(LocalDate.of(2022, 5, 16), data.get(15).getDate());
        assertEquals(-16, data.get(15).getRetailRecreationalGMR());
        assertEquals(-2, data.get(15).getGroceryPharmacyGMR());
        assertEquals(50, data.get(15).getParksGMR());
        assertEquals(-44, data.get(15).getTransitGMR());
        assertEquals(-39, data.get(15).getWorkplacesGMR());
        assertEquals(8, data.get(15).getResidentialGMR());
        assertEquals(33, data.get(15).getNewCases());
        assertEquals(73320, data.get(15).getTotalCases());
        assertEquals(1, data.get(15).getNewDeaths());
        assertEquals(567, data.get(15).getTotalDeaths());
        assertEquals(LocalDate.of(2022, 5, 31), data.get(30).getDate());
        assertEquals(-8, data.get(30).getRetailRecreationalGMR());
        assertEquals(-4, data.get(30).getGroceryPharmacyGMR());
        assertEquals(-1, data.get(30).getParksGMR());
        assertEquals(-44, data.get(30).getTransitGMR());
        assertEquals(-50, data.get(30).getWorkplacesGMR());
        assertEquals(8, data.get(30).getResidentialGMR());
        assertEquals(23, data.get(30).getNewCases());
        assertEquals(73608, data.get(30).getTotalCases());
        assertEquals(0, data.get(30).getNewDeaths());
        assertEquals(568, data.get(30).getTotalDeaths());

        data = CovidDataLoader.getBoroughData(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 2, 1), "Sutton", "Date");
        for(CovidData d: data)
        {
            assertEquals("Sutton", d.getBorough());
            assertNull(d.getRetailRecreationalGMR());
            assertNull(d.getGroceryPharmacyGMR());
            assertNull(d.getParksGMR());
            assertNull(d.getTransitGMR());
            assertNull(d.getWorkplacesGMR());
            assertNull(d.getResidentialGMR());
        }
    }
}
