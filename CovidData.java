import java.time.LocalDate;

/**
 * Represents one record in the COVID dataset.
 * This is essentially one row in the data table. Each column
 * has a corresponding field.
 * @author KCL Informatics, PPA, Muhammed Keeka
 * @version 2024-03-25
 */ 

public class CovidData {

    //The date the COVID information (cases & deaths) was collected.
    private LocalDate date;
    
    //The COVID information is organised by (London) borough.
    private String borough;
    
    //The COVID information that's collected daily for each London borough.
    private Integer newCases;
    private Integer totalCases;
    private Integer newDeaths;
    private Integer totalDeaths;
    
    /*
    Google analysed location data from Android smartphones to measure movement
    in London.  The data shows percent change from the baseline.  For example, 
    a negative value means there's less human traffic compared to the baseline.
    */
    private Integer retailRecreationalGMR;
    private Integer groceryPharmacyGMR;
    private Integer parksGMR;
    private Integer transitGMR;
    private Integer workplacesGMR;
    private Integer residentialGMR;

    public CovidData(LocalDate date, String borough, Integer retailRecreationalGMR, Integer groceryPharmacyGMR,
                        Integer parksGMR, Integer transitGMR, Integer workplacesGMR, Integer residentialGMR,
                        Integer newCases, Integer totalCases, Integer newDeaths, Integer totalDeaths) {
        this.date = date;
        this.borough = borough;
        this.retailRecreationalGMR = retailRecreationalGMR;
        this.groceryPharmacyGMR = groceryPharmacyGMR;
        this.parksGMR = parksGMR;
        this.transitGMR = transitGMR;
        this.workplacesGMR = workplacesGMR;
        this.residentialGMR = residentialGMR;
        this.newCases = newCases;
        this.totalCases = totalCases;
        this.newDeaths = newDeaths;
        this.totalDeaths = totalDeaths;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getBorough() {
        return borough;
    }

    public Integer getRetailRecreationalGMR() {
        return retailRecreationalGMR;
    }

    public Integer getGroceryPharmacyGMR() {
        return groceryPharmacyGMR;
    }

    public Integer getParksGMR() {
        return parksGMR;
    }

    public Integer getTransitGMR() {
        return transitGMR;
    }

    public Integer getWorkplacesGMR() {
        return workplacesGMR;
    }

    public Integer getResidentialGMR() {
        return residentialGMR;
    }

    public Integer getNewCases() {
        return newCases;
    }

        public Integer getTotalCases() {
        return totalCases;
    }

    public Integer getNewDeaths() {
        return newDeaths;
    }

    public Integer getTotalDeaths() {
        return totalDeaths;
    }

    @Override
    public String toString() {
        return "Covid Record {" + 
        " date='" + date +'\'' +
        ", borough='" + borough +'\'' +
        ", retailRecreationGMR=" + retailRecreationalGMR +
        ", groceryPharmacyGMR=" + groceryPharmacyGMR + 
        ", parksGMR=" + parksGMR + 
        ", transitGMR=" + transitGMR + 
        ", workplacesGMR=" + workplacesGMR + 
        ", residentialGMR=" + residentialGMR + 
        ", newCases=" + newCases + 
        ", totalCases=" + totalCases + 
        ", newDeaths=" + newDeaths + 
        ", totalDeaths=" + totalDeaths + 
        "}";
    }
}
