package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class ForexRate implements Serializable {
    private String id;
    private String Name;
    private Double Rate;
    private String currencyNm;
    private String currencySymbol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyNm() {
        return currencyNm;
    }

    public void setCurrencyNm(String currencyNm) {
        this.currencyNm = currencyNm;
    }

    public Double getRate() {
        return Rate;
    }

    public void setRate(Double rate) {
        Rate = rate;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
