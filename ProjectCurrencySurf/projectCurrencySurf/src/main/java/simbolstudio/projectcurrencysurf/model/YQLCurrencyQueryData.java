package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class YQLCurrencyQueryData implements Serializable {
    ForexRateList results;
    Date created;

    public ForexRateList getResults() {
        return results;
    }

    public Date getCreated() {
        return created;
    }
}
