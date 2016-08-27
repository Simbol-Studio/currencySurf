package simbolstudio.projectcurrencysurf.model;

/**
 * Created by Marcus on 27-Aug-2016.
 */
public class Country {
    private String countryNm;
    private String countryISO2D;
    private String countryISO3D;
    private String currencyCode;

    public String getCountryNm() {
        return countryNm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCountryISO3D() {
        return countryISO3D;
    }

    public String getCountryISO2D() {
        return countryISO2D;
    }
}
