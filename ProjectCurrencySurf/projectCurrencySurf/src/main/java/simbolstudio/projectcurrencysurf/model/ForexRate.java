package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class ForexRate implements Serializable {
    String id;
    String Name;
    Double Rate;

    public String getId() {
        return id;
    }

    public Double getRate() {
        return Rate;
    }

    public String getName() {
        return Name;
    }
}
