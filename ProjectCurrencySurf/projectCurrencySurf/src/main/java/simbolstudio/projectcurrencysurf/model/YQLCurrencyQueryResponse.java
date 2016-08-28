package simbolstudio.projectcurrencysurf.model;

import java.io.Serializable;

/**
 * Created by Marcus on 22-Aug-2016.
 */
public class YQLCurrencyQueryResponse implements Serializable {
    YQLCurrencyQueryData query;

    public YQLCurrencyQueryData getQuery() {
        return query;
    }
}
