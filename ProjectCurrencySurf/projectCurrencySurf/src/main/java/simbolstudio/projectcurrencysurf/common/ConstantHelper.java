package simbolstudio.projectcurrencysurf.common;

/**
 * Created by Marcus on 21-Aug-2016.
 */
public class ConstantHelper {
    public static final int TIMEOUT_SECONDS = 10;
    public static final Long FOREX_LAST_UPDATE = 1472638752l;

    public static final int REQUEST_CODE_SEARCH_CURRENCY=100;

    public static final int DEFAULT_CURRENCY_COUNT=8;

    public static final int MODE_NORMAL=0;
    public static final int MODE_SEARCH=1;

    public static final String EXTRA_BUNDLE_CURRENCY="EXTRA_BUNDLE_CURRENCY";
    public static final String EXTRA_ALL_CURRENCY_LIST="EXTRA_ALL_CURRENCY_LIST";
    public static final String EXTRA_SELECTED_CURRENCY_LIST="EXTRA_SELECTED_CURRENCY_LIST";

    public static final String DEFAULT_BASE_CURRENCY_ID = "USD";

    public static final String KEY_ASSETS_NAME_COUNTRIES = "countries.json";
    public static final String KEY_ASSETS_NAME_CURRENCIES = "currencies.json";

    public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";
    public static final String SHARED_PREFERENCES_BASE_CURRENCY_ID = "SHARED_PREFERENCES_BASE_CURRENCY_ID";
    public static final String SHARED_PREFERENCES_LAST_UPDATE = "SHARED_PREFERENCES_LAST_UPDATE";
    public static final String SHARED_PREFERENCES_FOREX_RATE_LIST = "SHARED_PREFERENCES_FOREX_RATE_LIST";
    public static final String SHARED_PREFERENCES_SELECTED_CURRENCY_LIST = "SHARED_PREFERENCES_SELECTED_CURRENCY_LIST";

    public static final String METHOD_GSON = "METHOD_GSON";
    public static final String METHOD_OBJECT_SERIALIZER = "METHOD_OBJECT_SERIALIZER";

    public static final int SNACKBAR_TEXT_SIZE = 16;
    public static final int SNACKBAR_TEXT_MAXLINE = 3;

    public static final int VIEW_TYPE_CONTENT = 0x00;
    public static final int VIEW_TYPE_HEADER = 0x01;
    public static final int VIEW_TYPE_EMPTY = 0x02;
}
