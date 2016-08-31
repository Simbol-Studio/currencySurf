package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;

/**
 * Created by Marcus on 27-Aug-2016.
 */
public class Country implements Serializable {
    private String countryNm;
    private String countryISO2D;
    private String currencyCode;

    public String getCountryNm() {
        return countryNm;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getCountryISO2D() {
        return countryISO2D;
    }
}
