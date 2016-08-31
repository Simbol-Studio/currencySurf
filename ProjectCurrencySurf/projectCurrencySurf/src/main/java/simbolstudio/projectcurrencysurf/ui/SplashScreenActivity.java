package simbolstudio.projectcurrencysurf.ui;

import android.content.Context;
import android.content.Intent;
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
import simbolstudio.projectcurrencysurf.model.ForexRate;
import simbolstudio.projectcurrencysurf.model.YQLCurrencyQueryResponse;
import simbolstudio.projectcurrencysurf.ui.main.MainActivity;

public class SplashScreenActivity extends BaseAppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String baseCurrencyId;
    String forexRateListJSONString;
    String selectedCurrencyListJSONString;
    Long lastUpdate;
    YQLCurrencyQueryResponse yqlCurrencyQueryResponse;
    boolean flag;
    ArrayList<ForexRate> latestForexRate;

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
        getForexRate();

//        ArrayList<Country> countryList = (ArrayList<Country>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES));
//        ArrayList<CurrencyType> currencyList = (ArrayList<CurrencyType>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES));
    }

    private void setLocalCountryISO() {
        baseCurrencyId = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY_ID, null);
        if (baseCurrencyId == null) {
            if (sharedPreferences != null & editor != null) {
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

                if (countryISO != null) {
                    baseCurrencyId = getCurrencyByCountryISO(countryISO);
                    editor.putString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY_ID, baseCurrencyId);
                }else{
                    editor.putString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY_ID, ConstantHelper.DEFAULT_BASE_CURRENCY_ID);
                }

                editor.commit();
            }
        }
    }

    private void setSelectedCurrency() {
        selectedCurrencyListJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_SELECTED_CURRENCY_LIST, null);
        if (selectedCurrencyListJSONString == null) {
//            if (yqlCurrencyQueryResponse != null)
//                forexList = yqlCurrencyQueryResponse.getQuery().getResults().getRate();

//            if (latestForexRate == null) {
//                forexRateListJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_FOREX_RATE_LIST, null);
//                if (forexRateListJSONString != null)
//                    latestForexRate = (ArrayList<ForexRate>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, forexRateListJSONString);
//            }


            if (latestForexRate != null && sharedPreferences != null && editor != null) {
                ArrayList<ForexRate> selectedForexList = new ArrayList<>();
                selectedForexList.add(getForexById(baseCurrencyId, latestForexRate));
                boolean found = false;
                for (int i = 0; i < latestForexRate.size();i++){// && selectedForexList.size() < 5; i++) {
                    found = false;
                    for (int j = 0; j < selectedForexList.size(); j++) {
                        if (selectedForexList.get(j).getId().equalsIgnoreCase(latestForexRate.get(i).getId()))
                            found = true;
                    }
                    if (!found)
                        selectedForexList.add(latestForexRate.get(i));
                }
                selectedCurrencyListJSONString= convertObjectToJSONString(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES,selectedForexList);
                if (sharedPreferences != null & editor != null && selectedCurrencyListJSONString != null) {

                    editor.putString(ConstantHelper.SHARED_PREFERENCES_SELECTED_CURRENCY_LIST, selectedCurrencyListJSONString);
                    editor.commit();
                }
            }
        }
        finishLoad();
    }


    private void getForexRate() {
        forexRateListJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_FOREX_RATE_LIST, null);
        lastUpdate = sharedPreferences.getLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, 0);
        if (forexRateListJSONString == null || lastUpdate == 0) {
            final OkHttpClientSingleton okHttpClientSingleton = new OkHttpClientSingleton().getInstance();
            try {
                Callback mCallback = new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ongetForexRateFail();
                        setSelectedCurrency();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()){
                            ongetForexRateFail();
                            setSelectedCurrency();
                        }

                        yqlCurrencyQueryResponse = okHttpClientSingleton.getmGson().fromJson(response.body().charStream(), YQLCurrencyQueryResponse.class);
                        ongetForexRate();
                        setSelectedCurrency();
                    }
                };

                okHttpClientSingleton.setParameter(getYQL(baseCurrencyId), mCallback);
                okHttpClientSingleton.run();
            } catch (Exception ex) {
                ex.printStackTrace();
                ongetForexRateFail();
            }
        }else
            setSelectedCurrency();
    }

    private void ongetForexRate() {
//        expected
        if (sharedPreferences != null & editor != null && yqlCurrencyQueryResponse != null) {

            ArrayList<ForexRate> forexRateList = yqlCurrencyQueryResponse.getQuery().getResults().getRate();
            latestForexRate=mergeLatestForexRateWithCurrencyInfo(forexRateList);
            forexRateListJSONString = convertObjectToJSONString(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, latestForexRate);

            editor.putLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, new Date().getTime());
            editor.putString(ConstantHelper.SHARED_PREFERENCES_FOREX_RATE_LIST, forexRateListJSONString);
            editor.commit();
        }

        setSelectedCurrency();
    }

    private void ongetForexRateFail() {
        //backup
        forexRateListJSONString= loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES);
        latestForexRate=(ArrayList<ForexRate>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES,forexRateListJSONString);
        if (sharedPreferences != null & editor != null && yqlCurrencyQueryResponse != null) {

            editor.putString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY_ID, ConstantHelper.DEFAULT_BASE_CURRENCY_ID);
            editor.putLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, ConstantHelper.FOREX_LAST_UPDATE);
            editor.putString(ConstantHelper.SHARED_PREFERENCES_FOREX_RATE_LIST, forexRateListJSONString);
            editor.commit();
        }
        setSelectedCurrency();
    }

    private void finishLoad() {
        Intent intent = new Intent(getActivityContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}