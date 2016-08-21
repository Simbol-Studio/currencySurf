package simbolstudio.projectcurrencysurf.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.base.ui.BaseAppCompatActivity;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;

public class SplashScreenActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        setupUI();
        setupData();
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void setupUI() {

    }

    @Override
    protected void setupData() {
        SharedPreferences sharedPreferences = getActivityContext().getSharedPreferences(ConstantHelper.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String countryISO = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_COUNTRY_ISO, null);

        if (countryISO == null) {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(getActivityContext().TELEPHONY_SERVICE);

            if (countryISO == null)
                countryISO = telephonyManager.getSimCountryIso();
            Log.v("vv", countryISO);
            if (countryISO == null)
                countryISO = telephonyManager.getNetworkCountryIso();
            Log.v("vv", countryISO);
            if (countryISO == null)
                countryISO = getActivityContext().getResources().getConfiguration().locale.getISO3Country();
            Log.v("vv", countryISO);

            if (countryISO == null)
                countryISO = "us";
            editor.putString(ConstantHelper.SHARED_PREFERENCES_COUNTRY_ISO, countryISO);
            editor.commit();
        }
    }
}