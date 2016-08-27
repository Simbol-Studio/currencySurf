package simbolstudio.projectcurrencysurf.base.ui;

import android.content.Context;
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
import java.util.Collection;
import java.util.List;

import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.model.Country;
import simbolstudio.projectcurrencysurf.model.CurrencyType;

/**
 * Created by Marcus on 21-Aug-2016.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity {
    private Toolbar toolbar;

    protected abstract Context getActivityContext();

    protected abstract void setupUI();

    protected abstract void setupData();

    @Override
    public void setSupportActionBar(Toolbar toolbar) {
        super.setSupportActionBar(toolbar);
    }

    protected Toolbar getToolbarInstance() {
        return toolbar;
    }

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

    public Object loadJSONFromAsset(String fileNm) {
        String jsonString = null;
        try {
            InputStream is = getAssets().open(fileNm);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (jsonString != null) {
            Gson gson = new Gson();
            try {
                if (fileNm.equals(ConstantHelper.ASSETS_NAME_COUNTRIES)) {
                    Type collectionType = new TypeToken<List<Country>>() {}.getType();
                    List<Country> resultList = gson.fromJson(jsonString, collectionType);
                    return resultList;
                } else {
                    Type collectionType = new TypeToken<List<CurrencyType>>() {}.getType();
                    List<CurrencyType> resultList = gson.fromJson(jsonString, collectionType);
                    return resultList;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else
            return null;
    }
}
