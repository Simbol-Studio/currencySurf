package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class ForexRateList implements Serializable {
    ArrayList<ForexRate> rate;

    public ArrayList<ForexRate> getRate() {
        return rate;
    }
}
