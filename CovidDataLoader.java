import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Loads the covid data from the database.
 * I have attempted to use the Singleton design pattern.
 *
 * @author Muhammed Keeka
 * @version 2024-03-25
 */
public class CovidDataLoader {
    private static Connection conn = null;
    
    /**
     * Establishes a connection to the database.
     */
    private static void connect()
    {
        try
        {
            Class.forName("org.sqlite.JDBC"); //Specify the SQLite Java driver
            conn = DriverManager.getConnection("jdbc:sqlite:CovidData.db"); //Specify the database, since relative in the main project folder
            conn.setAutoCommit(false); // Important as I want control of when data is written
        }
        catch(Exception e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    /**
     * Closes the connection to the database.
     */
    public static void close()
    {
        if(!(conn == null))
        {
            try
            {
                conn.close();
            }
            catch (SQLException ex)
            {
                Logger.getLogger(CovidDataLoader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Used to execute modification queries.
     */
    private static void execute(String query)
    {
        if(conn == null)
            connect();
        Statement stmt;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            stmt.close();
            conn.commit();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Executes the statement below and returns a single Integer, of the field specified.
     * This seems like an odd method, but it's quite helpful.
     */
    private static Integer getInt(String query, String field)
    {
        if(conn == null)
            connect();
        Statement stmt;
        ResultSet rs;
        Integer result = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if(! rs.isClosed())
            {
                rs.next();
                result = rs.getInt(field);
                if (rs.wasNull())
                    result = null;
                rs.close();
            }
            stmt.close();   
        } 
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return result;
    }

    public static Integer getRecordCount()
    {
        String query = "SELECT COUNT(*) FROM CovidData;";
        return getInt(query, "COUNT(*)");
    }

    public static Integer getCases(LocalDate start, LocalDate end)
    {
        String query = "SELECT SUM(TotalCases) FROM CovidData WHERE `Date` = '";
        Integer endTotalDeaths = getInt(query + end + "';", "SUM(TotalCases)");
        Integer startTotalDeaths = getInt(query + start.minusDays(1) + "';", "SUM(TotalCases)");
        if(endTotalDeaths != null && startTotalDeaths != null)
            return endTotalDeaths - startTotalDeaths;
        else
            return null;
    }

    public static Integer getCases(LocalDate start, LocalDate end, String borough)
    {
        String query = "SELECT TotalCases FROM CovidData WHERE Borough = '" + borough + "' AND `Date` = '";
        Integer endTotalCases = getInt(query + end + "';", "TotalCases");
        Integer startTotalCases = getInt(query + start.minusDays(1) + "';", "TotalCases");
        if(endTotalCases != null && startTotalCases != null)
            return endTotalCases - startTotalCases;
        else
            return null;
    }
    
    public static Integer getMaxCases(LocalDate start, LocalDate end)
    {
        String query = "SELECT MAX(t2.TotalCases - t1.TotalCases) AS MaxCases FROM CovidData t1 JOIN CovidData t2 on t1.Borough = t2.Borough WHERE t1.Date = '" + start.minusDays(1) + "' AND t2.Date = '" + end +"';";
        return getInt(query, "MaxCases");
    }

    public static Integer getDeaths(LocalDate start, LocalDate end)
    {
        String query = "SELECT SUM(TotalDeaths) FROM CovidData WHERE `Date` = '";
        Integer endTotalDeaths = getInt(query + end + "';", "SUM(TotalDeaths)");
        Integer startTotalDeaths = getInt(query + start.minusDays(1) + "';", "SUM(TotalDeaths)");
        if(endTotalDeaths != null && startTotalDeaths != null)
            return endTotalDeaths - startTotalDeaths;
        else
            return null;
    }

    public static Integer getDeaths(LocalDate start, LocalDate end, String borough)
    {
        String query = "SELECT TotalDeaths FROM CovidData WHERE Borough = '" + borough + "' AND `Date` = '";
        Integer endTotalDeaths = getInt(query + end + "';", "TotalDeaths");
        Integer startTotalDeaths = getInt(query + start.minusDays(1) + "';", "TotalDeaths");
        if(endTotalDeaths != null && startTotalDeaths != null)
            return endTotalDeaths - startTotalDeaths;
        else
            return null;
    }

    public static Integer getMaxDeaths(LocalDate start, LocalDate end) {
        String query = "SELECT MAX(t2.TotalDeaths - t1.TotalDeaths) AS MaxDeaths FROM CovidData t1 JOIN CovidData t2 on t1.Borough = t2.Borough WHERE t1.Date = '" + start.minusDays(1) + "' AND t2.Date = '" + end + "';";
        return getInt(query, "MaxDeaths");
    }

    public static Integer getParksGMR(LocalDate start, LocalDate end)
    {
        String query = "SELECT AVG(ParksGMR) FROM CovidData WHERE `Date` BETWEEN '" + start + "' AND '" + end + "';";
        return getInt(query, "AVG(ParksGMR)");
    }

    public static Integer getTransitGMR(LocalDate start, LocalDate end)
    {
        String query = "SELECT AVG(TransitGMR) FROM CovidData WHERE `Date` BETWEEN '" + start + "' AND '" + end + "';";
        return getInt(query, "AVG(TransitGMR)");
    }
    
    public static LocalDate getEarliestDate()
    {
        if(conn == null)
            connect();
        Statement stmt;
        ResultSet rs;
        LocalDate date = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT MIN(`Date`) FROM CovidData;");
            if(!rs.isClosed())
            {
                rs.next();
                date = LocalDate.parse(rs.getString("MIN(`Date`)"));
                rs.close();
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return date;
    }

    public static LocalDate getLatestDate()
    {
        if(conn == null)
            connect();
        Statement stmt;
        ResultSet rs;
        LocalDate date = null;
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT MAX(`Date`) FROM CovidData;");
            if(!rs.isClosed())
            {
                rs.next();
                date = LocalDate.parse(rs.getString("MAX(`Date`)"));
                rs.close();
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return date;
    }
    
    public static Map<LocalDate, Integer> getCasesPerDay(LocalDate start, LocalDate end)
    {
        if(conn == null)
            connect();
        Statement stmt;
        ResultSet rs;
        Map<LocalDate, Integer> data = new HashMap<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT `Date`, SUM(NewCases) FROM CovidData WHERE `Date` BETWEEN '" + start + "' AND '" + end + "' GROUP BY `Date`;");
            if(!rs.isClosed())
            {
                while (rs.next())
                {
                    LocalDate date = LocalDate.parse(rs.getString("Date"));
                    Integer newCases = rs.getInt("SUM(NewCases)");
                    if (rs.wasNull())
                        newCases = null;
                    data.put(date, newCases);
                    }
                rs.close();
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return data;
    }

    public static Map<LocalDate, Integer> getDeathsPerDay(LocalDate start, LocalDate end)
    {
        if(conn == null)
            connect();
        Statement stmt;
        ResultSet rs;
        Map<LocalDate, Integer> data = new HashMap<>();
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT `Date`, SUM(NewDeaths) FROM CovidData WHERE `Date` BETWEEN '" + start + "' AND '" + end + "' GROUP BY `Date`;");
            if(!rs.isClosed())
            {
                while (rs.next())
                {
                    LocalDate date = LocalDate.parse(rs.getString("Date"));
                    Integer newDeaths = rs.getInt("SUM(NewDeaths)");
                    if (rs.wasNull())
                        newDeaths = null;
                    data.put(date, newDeaths);
                    }
                rs.close();
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return data;
    }

    public static List<CovidData> getBoroughData(LocalDate start, LocalDate end, String borough, String order)
    {
        if(conn == null)
            connect();
        Statement stmt;
        ResultSet rs;
        List<CovidData> data = new ArrayList<>();
        if(order.equals("Date"))
            order = "`Date`";
        try
        {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM CovidData WHERE Borough = '" + borough + "' AND `Date` BETWEEN '" + start + "' AND '" + end + "' ORDER BY " + order + ";");
            if(!rs.isClosed())
            {
                while (rs.next())
                {
                    LocalDate date = LocalDate.parse(rs.getString("Date"));
                    Integer retailRecreationalGMR = rs.getInt("RetailRecreationalGMR");
                    if (rs.wasNull())
                        retailRecreationalGMR = null;
                    Integer groceryPharmacyGMR = rs.getInt("GroceryPharmacyGMR");
                    if (rs.wasNull())
                        groceryPharmacyGMR = null;
                    Integer parksGMR = rs.getInt("ParksGMR");
                    if (rs.wasNull())
                        parksGMR = null;
                    Integer transitGMR = rs.getInt("TransitGMR");
                    if (rs.wasNull())
                        transitGMR = null;
                    Integer workplacesGMR = rs.getInt("WorkplacesGMR");
                    if (rs.wasNull())
                        workplacesGMR = null;
                    Integer residentialGMR = rs.getInt("ResidentialGMR");
                    if (rs.wasNull())
                        residentialGMR = null;
                    Integer newCases = rs.getInt("NewCases");
                    if (rs.wasNull())
                        newCases = null;
                    Integer totalCases = rs.getInt("TotalCases");
                    if (rs.wasNull())
                        totalCases = null;
                    Integer newDeaths = rs.getInt("NewDeaths");
                    if (rs.wasNull())
                        newDeaths = null;
                    Integer totalDeaths = rs.getInt("TotalDeaths");
                    if (rs.wasNull())
                        totalDeaths = null;
                    data.add(new CovidData(date, borough, retailRecreationalGMR, groceryPharmacyGMR, parksGMR, transitGMR, workplacesGMR, residentialGMR, newCases, totalCases, newDeaths, totalDeaths));
                }
                rs.close();
            }
            stmt.close();
        }
        catch (SQLException e)
        {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return data;
    }
}
