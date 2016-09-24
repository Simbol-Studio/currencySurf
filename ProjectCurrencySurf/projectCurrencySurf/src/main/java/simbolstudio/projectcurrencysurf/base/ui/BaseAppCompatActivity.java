package simbolstudio.projectcurrencysurf.base.ui;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import simbolstudio.projectcurrencysurf.model.ForexRate;

/**
 * Created by Marcus on 21-Aug-2016.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ImageButton customNavImg;
    TextView customTitleText;
    EditText customSearchEditText;

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

    public TextView getCustomTitleText() {
        return customTitleText;
    }

    public EditText getCustomSearchEditText() {
        return customSearchEditText;
    }

    protected void setupToolbar(int activityToolbarResourceId, String toolbarTitle, boolean isBack) {
        if (activityToolbarResourceId != 0) {
            if (toolbar == null) {
                toolbar = (Toolbar) findViewById(activityToolbarResourceId);
            }

            if (toolbarTitle != null && !toolbarTitle.equalsIgnoreCase("")) {
                customTitleText = (TextView) toolbar.findViewById(R.id.customTitleText);
                customTitleText.setText(toolbarTitle);
                if (Build.VERSION.SDK_INT < 23) {
                    customTitleText.setTextAppearance(getActivityContext(), R.style.DarkTitleText);
                } else {
                    customTitleText.setTextAppearance(R.style.DarkTitleText);
                }
                customTitleText.setTextColor(getResources().getColor(R.color.amber_50));
                customTitleText.setVisibility(View.VISIBLE);
            }

            if (isBack) {
                customNavImg = (ImageButton) toolbar.findViewById(R.id.customNavImg);
                customNavImg.setImageResource(R.drawable.ico_nav_back);
                customNavImg.setVisibility(View.VISIBLE);
                customNavImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onBackPressed();
                    }
                });
            }

            customSearchEditText = (EditText) toolbar.findViewById(R.id.customSearchEditText);

            setSupportActionBar(toolbar);
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
                    Type collectionType = new TypeToken<List<ForexRate>>() {
                    }.getType();
                    List<ForexRate> resultList = gson.fromJson(jsonString, collectionType);
                    return resultList;
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
            if (key.equalsIgnoreCase(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES) && forexRateArrayList != null) {
                return gson.toJson(forexRateArrayList);
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
            return ConstantHelper.DEFAULT_BASE_CURRENCY_ID;
        } else
            return ConstantHelper.DEFAULT_BASE_CURRENCY_ID;
    }

    public String getYQL(String baseCurrency) {
        ArrayList<ForexRate> currencyList = (ArrayList<ForexRate>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES));

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

    public ArrayList<ForexRate> mergeLatestForexRateWithCurrencyInfo(ArrayList<ForexRate> latestForexRateList) {
        ArrayList<ForexRate> currencyList = (ArrayList<ForexRate>) convertJSONStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, loadJSONToStringFromAsset(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES));

        for (int i = 0; i < currencyList.size() && i < latestForexRateList.size(); i++) {
            latestForexRateList.get(i).setId(currencyList.get(i).getId());
            latestForexRateList.get(i).setCurrencyNm(currencyList.get(i).getCurrencyNm());
            latestForexRateList.get(i).setCurrencySymbol(currencyList.get(i).getCurrencySymbol());
        }

        return latestForexRateList;
    }


    public ForexRate getForexById(String forexId, ArrayList<ForexRate> forexList) {
        if (forexList != null && forexList.size() > 0 && forexId != null && !forexId.equalsIgnoreCase("")) {
            for (int i = 0; i < forexList.size(); i++) {
                if (forexList.get(i).getId().equalsIgnoreCase(forexId))
                    return forexList.get(i);
            }
            return null;
        } else
            return null;
    }

    public int getCurrencyIcon(String currencyId) {
        if (currencyId.equalsIgnoreCase("try"))
            currencyId = "tryturkish";

        int resourceId = getResources().getIdentifier(currencyId.toLowerCase(), "drawable", getPackageName());
        if (resourceId > 0)
            return resourceId;
        else
            return R.drawable.usd;
    }
}