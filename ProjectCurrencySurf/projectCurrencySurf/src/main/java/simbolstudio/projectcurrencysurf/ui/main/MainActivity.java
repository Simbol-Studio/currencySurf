package simbolstudio.projectcurrencysurf.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.base.ui.BaseAppCompatActivity;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.model.ForexRate;

public class MainActivity extends BaseAppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SwipeRefreshLayout swipeContainer;
    RecyclerView mainRecycler;
    MainCurrencyAdapter mainCurrencyAdapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUI();
        setupData();
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void setupData() {
        Fresco.initialize(getActivityContext());
        sharedPreferences = getActivityContext().getSharedPreferences(ConstantHelper.SHARED_PREFERENCES, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCurrencyRateList();
            }
        });

        layoutManager = new LinearLayoutManager(getActivityContext(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }

            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (Exception ex) {
                    Log.v("Debug", "recyclerView onlayoutChildren error");
                    ex.printStackTrace();
                }
            }
        };
        mainRecycler.setLayoutManager(layoutManager);
        mainCurrencyAdapter = new MainCurrencyAdapter(getActivityContext());
        mainRecycler.setAdapter(mainCurrencyAdapter);

        String currencyRateListJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_SELECTED_CURRENCY_LIST, null);
        mainCurrencyAdapter.setForexList((ArrayList<ForexRate>) convertJSONStringToObject(ConstantHelper.KEY_FOREX_RATE,currencyRateListJSONString));
        mainCurrencyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setupUI() {
        setupToolbar(R.id.mainToolbar, getResources().getString(R.string.main_activity_title), false);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mainRecycler = (RecyclerView) findViewById(R.id.mainRecycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.setting:
                Log.v("","");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshCurrencyRateList() {

    }

    public void checkIsEmpty(){

    }

    @Override
    public void onBackPressed(){

    }
}