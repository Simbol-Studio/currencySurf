package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class ForexRate implements Serializable {
    String id;
    String Name;
    Double Rate;
    Double Ask;
    Double Bid;
    String Date;
    String Time;

    public String getId() {
        return id;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public Double getAsk() {
        return Ask;
    }

    public Double getBid() {
        return Bid;
    }

    public Double getRate() {
        return Rate;
    }

    public String getName() {
        return Name;
    }
}
