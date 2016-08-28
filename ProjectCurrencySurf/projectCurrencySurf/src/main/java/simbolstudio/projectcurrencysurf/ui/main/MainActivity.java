package simbolstudio.projectcurrencysurf.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.base.ui.BaseAppCompatActivity;

public class MainActivity extends BaseAppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

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
    }
}