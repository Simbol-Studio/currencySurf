package simbolstudio.projectcurrencysurf.base.ui;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.model.Country;
import simbolstudio.projectcurrencysurf.model.CurrencyType;
import simbolstudio.projectcurrencysurf.model.ForexRate;

/**
 * Created by Marcus on 21-Aug-2016.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    private Toolbar toolbar;

    protected abstract Context getActivityContext();

    protected abstract void setupUI();

    protected abstract void setupData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toolbar = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getToolbarInstance() {
        return toolbar;
    }

    protected void setupToolbar(int activityToolbarResourceId, String toolbarTitle, boolean isBack){
        if(activityToolbarResourceId!=0){
            if(toolbar==null){
                toolbar=(Toolbar)findViewById(activityToolbarResourceId);
            }

            TextView titleTV = null;
            for(int i =0;i<toolbar.getChildCount();i++){
                View child = toolbar.getChildAt(i);
                if(child instanceof TextView){
                    titleTV=(TextView)child;
                    titleTV.setText(toolbarTitle);
                    if(Build.VERSION.SDK_INT<23){
                        titleTV.setTextAppearance(getActivityContext(), R.style.TitleText);
                    }else{
                        titleTV.setTextAppearance(R.style.TitleText);
                    }
                    titleTV.setTextColor(getResources().getColor(R.color.amber_50));
                    break;
                }
            }
        }

    }

    public void showMessage(String title, String message, String positiveBtnText, AlertDialog.OnClickListener positiveOnclickListener, String negativeBtnText, AlertDialog.OnClickListener negativeOnClickListener) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(message);
        if (positiveBtnText != null && positiveOnclickListener != null)
            alertBuilder.setPositiveButton(positiveBtnText, positiveOnclickListener);
        if (negativeBtnText != null && negativeOnClickListener != null)
            alertBuilder.setNegativeButton(negativeBtnText, negativeOnClickListener);
        alertBuilder.show();
    }

    public void showSnackbar(final View view, final String message, boolean isDismissable) {
        final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        if (isDismissable)
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
        View snackbarView = snackbar.getView();
        TextView snackbarTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackbarTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ConstantHelper.SNACKBAR_TEXT_SIZE);
        snackbarTextView.setMaxLines(ConstantHelper.SNACKBAR_TEXT_MAXLINE);
        snackbar.show();
    }

    public String loadJSONToStringFromAsset(String fileNm) {
        String jsonString = null;
        try {
            InputStream is = getAssets().open(fileNm);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
            return jsonString;
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

    public Object convertJSONStringToObject(String key, String jsonString) {
        if (jsonString != null) {
            Gson gson = new Gson();
            try {
                if (key.equalsIgnoreCase(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES)) {
                    Type collectionType = new TypeToken<List<Country>>() {
                    }.getType();
                    List<Country> resultList = gson.fromJson(jsonString, collectionType);
                    return resultList;
                } else if (key.equalsIgnoreCase(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES)) {
                    Type collectionType = new TypeToken<List<CurrencyType>>() {
                    }.getType();
                    List<CurrencyType> resultList = gson.fromJson(jsonString, collectionType);
                    return resultList;
                } else if (key.equalsIgnoreCase(ConstantHelper.KEY_CURRENCY_RATE)) {
                    return null;
                } else {
                    return null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    public String convertObjectToJSONString(String key, ArrayList<ForexRate> forexRateArrayList) {
        Gson gson = new Gson();
        try {
            if (key.equalsIgnoreCase(ConstantHelper.KEY_CURRENCY_RATE) && forexRateArrayList != null) {
                return gson.toJson(forexRateArrayList);
            } else if (key.equalsIgnoreCase(ConstantHelper.KEY_CURRENCY_RATE)) {
                return null;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getCurrencyByCountryISO(String countryISO) {
        ArrayList<Country> countryList = (ArrayList<Country>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_COUNTRIES));

        if (countryList != null && countryList.size() > 0) {
            for (int i = 0; i < countryList.size(); i++) {
                if (countryList.get(i).getCountryISO2D().equalsIgnoreCase(countryISO)) {
                    return countryList.get(i).getCurrencyCode();
                }
            }
            return ConstantHelper.DEFAULT_BASE_CURRENCY;
        } else
            return ConstantHelper.DEFAULT_BASE_CURRENCY;
    }

    public String getYQL(String baseCurrency) {
        ArrayList<CurrencyType> currencyList = (ArrayList<CurrencyType>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES));

        String uri = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(";
        for (int i = 0; i < currencyList.size(); i++) {
            if (i != 0)
                uri += "%2C%22" + baseCurrency + currencyList.get(i).getId() + "%22";
            else
                uri += "%22" + baseCurrency + currencyList.get(i).getId() + "%22";
        }
        uri += ")&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";

        return uri;
    }
}
