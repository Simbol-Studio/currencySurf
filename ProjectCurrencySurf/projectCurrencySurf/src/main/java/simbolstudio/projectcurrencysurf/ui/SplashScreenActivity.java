package simbolstudio.projectcurrencysurf.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.base.ui.BaseAppCompatActivity;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.controller.OkHttpClientSingleton;
import simbolstudio.projectcurrencysurf.model.Country;
import simbolstudio.projectcurrencysurf.model.CurrencyType;
import simbolstudio.projectcurrencysurf.model.YQLCurrencyQueryResponse;

public class SplashScreenActivity extends BaseAppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String baseCurrency;
    String currencyRateJSONString;
    Long lastUpdate;

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
        sharedPreferences = getActivityContext().getSharedPreferences(ConstantHelper.SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setLocalCountryISO();
        getCurrencyRate();

        ArrayList<Country> countryList = (ArrayList<Country>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES));
        ArrayList<CurrencyType> currencyList = (ArrayList<CurrencyType>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES));


    }

    private void setLocalCountryISO() {
        if (sharedPreferences != null & editor != null) {
            baseCurrency = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY, null);
            if (baseCurrency == null) {
                String countryISO = null;
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(getActivityContext().TELEPHONY_SERVICE);

                if (countryISO == null)
                    countryISO = telephonyManager.getSimCountryIso();
                Log.v("debug", "sim " + countryISO);
                if (countryISO == null)
                    countryISO = telephonyManager.getNetworkCountryIso();
                Log.v("debug", "network " + countryISO);
                if (countryISO == null)
                    countryISO = getActivityContext().getResources().getConfiguration().locale.getISO3Country();
                Log.v("debug", "locale " + countryISO);

                if (countryISO == null)
                    countryISO = "us";

                baseCurrency = getCurrencyByCountryISO(countryISO);

                editor.putString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY, baseCurrency);
                editor.commit();
            }
        }
    }

    private void getCurrencyRate() {
        currencyRateJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_CURRENCY_RATE_LIST, null);
        lastUpdate = sharedPreferences.getLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, 0);
        if (currencyRateJSONString == null || lastUpdate==0) {
            final OkHttpClientSingleton okHttpClientSingleton = new OkHttpClientSingleton().getInstance();
            try {
                Callback mCallback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ongetCurrencyRateFail();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful())
                            ongetCurrencyRateNotSuccess();

                        ongetCurrencyRate(okHttpClientSingleton.getmGson().fromJson(response.body().charStream(), YQLCurrencyQueryResponse.class));
                    }
                };

                okHttpClientSingleton.setParameter(getYQL(baseCurrency), mCallback);
                okHttpClientSingleton.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void ongetCurrencyRate(YQLCurrencyQueryResponse response) {
//        expected
        if (sharedPreferences != null & editor != null) {
            currencyRateJSONString = convertObjectToJSONString(ConstantHelper.KEY_CURRENCY_RATE, response.getQuery().getResults().getRate());

            editor.putLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, new Date().getTime());
            editor.putString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY, currencyRateJSONString);
            editor.commit();
        }
    }

    private void ongetCurrencyRateNotSuccess() {
//        backup

    }

    private void ongetCurrencyRateFail() {
//        connectivity

    }
}