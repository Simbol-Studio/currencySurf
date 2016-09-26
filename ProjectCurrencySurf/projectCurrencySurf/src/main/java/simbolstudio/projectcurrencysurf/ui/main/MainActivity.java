package simbolstudio.projectcurrencysurf.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.base.ui.BaseAppCompatActivity;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.controller.OkHttpClientSingleton;
import simbolstudio.projectcurrencysurf.model.ForexRate;
import simbolstudio.projectcurrencysurf.model.YQLCurrencyQueryResponse;
import simbolstudio.projectcurrencysurf.ui.search.SearchActivity;

public class MainActivity extends BaseAppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    SwipeRefreshLayout swipeContainer;
    RecyclerView mainRecycler;
    MainCurrencyAdapter mainCurrencyAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<ForexRate> latestForexRate;
    String baseCurrencyId;
    YQLCurrencyQueryResponse yqlCurrencyQueryResponse;
    Long lastUpdatedAt;

    OkHttpClientSingleton okHttpClientSingleton;
    boolean doubleBackToExitPressedOnce = false;

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

        baseCurrencyId = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_BASE_CURRENCY_ID, null);
        if (baseCurrencyId == null)
            baseCurrencyId = ConstantHelper.DEFAULT_BASE_CURRENCY_ID;
        String selectedForexRateListJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_SELECTED_CURRENCY_LIST, null);
//        if ==null
        lastUpdatedAt = sharedPreferences.getLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, 0);
        String latestAllForexRateListJSONString = sharedPreferences.getString(ConstantHelper.SHARED_PREFERENCES_FOREX_RATE_LIST, null);
        latestForexRate = (ArrayList<ForexRate>) convertStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, ConstantHelper.METHOD_OBJECT_SERIALIZER, latestAllForexRateListJSONString);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshForexRateList();
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
                    Log.v("Debug", "recyclerView onLayoutChildren error");
                    ex.printStackTrace();
                }
            }
        };
        mainRecycler.setLayoutManager(layoutManager);
        mainCurrencyAdapter = new MainCurrencyAdapter(getActivityContext());
        mainRecycler.setAdapter(mainCurrencyAdapter);

        mainCurrencyAdapter.setForexList((ArrayList<ForexRate>) convertStringToObject(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, ConstantHelper.METHOD_OBJECT_SERIALIZER, selectedForexRateListJSONString));
        mainCurrencyAdapter.notifyDataSetChanged();
    }

    @Override
    protected void setupUI() {
        setupToolbar(R.id.mainToolbar, getResources().getString(R.string.main_activity_title), false);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mainRecycler = (RecyclerView) findViewById(R.id.mainRecycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem addCurrency = menu.findItem(R.id.addCurrency);
        addCurrency.getIcon().setAlpha(205);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Log.v("", "");
                break;
        }
        Intent intent = new Intent(getActivityContext(), SearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ConstantHelper.EXTRA_ALL_CURRENCY_LIST, latestForexRate);
        bundle.putSerializable(ConstantHelper.EXTRA_SELECTED_CURRENCY_LIST, mainCurrencyAdapter.getForexList());
        intent.putExtra(ConstantHelper.EXTRA_BUNDLE_CURRENCY, bundle);
        startActivityForResult(intent, ConstantHelper.REQUEST_CODE_SEARCH_CURRENCY);

        return super.onOptionsItemSelected(item);

//        swipeContainer.post(new Runnable() {
//            @Override public void run() {
//                swipeContainer.setRefreshing(true);
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantHelper.REQUEST_CODE_SEARCH_CURRENCY) {
            if (resultCode == RESULT_OK) {
                ArrayList<ForexRate> forexRate = (ArrayList<ForexRate>) data.getBundleExtra(ConstantHelper.EXTRA_BUNDLE_CURRENCY).getSerializable(ConstantHelper.EXTRA_SELECTED_CURRENCY_LIST);
                if (forexRate != null && forexRate.size() > 0) {
                    for (int i = 0; i < forexRate.size(); i++) {
                        mainCurrencyAdapter.updateForexRate(forexRate.get(i));
                    }
                    mainCurrencyAdapter.notifyDataSetChanged();
                    updateSelectedCurrency(mainCurrencyAdapter.getForexList());
                }
            }
        }
    }

    public void refreshForexRateList() {
        getForexRate();
    }

    public void checkIsEmpty() {

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private void getForexRate() {
        if (okHttpClientSingleton == null)
            okHttpClientSingleton = new OkHttpClientSingleton().getInstance();
        try {
            Callback mCallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ongetForexRateFail();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        ongetForexRateFail();
                    }

                    if (response != null && response.body() != null) {
                        yqlCurrencyQueryResponse = okHttpClientSingleton.getmGson().fromJson(response.body().charStream(), YQLCurrencyQueryResponse.class);
                        ongetForexRate();
                    }
                }
            };

            okHttpClientSingleton.setParameter(getYQL(baseCurrencyId), mCallback);
            okHttpClientSingleton.run();
        } catch (Exception ex) {
            ex.printStackTrace();
            ongetForexRateFail();
        }
    }

    private void ongetForexRate() {
//        expected
        if (sharedPreferences != null & editor != null && yqlCurrencyQueryResponse != null) {

            ArrayList<ForexRate> newForexRateList = yqlCurrencyQueryResponse.getQuery().getResults().getRate();
            latestForexRate = mergeLatestForexRateWithCurrencyInfo(newForexRateList);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainCurrencyAdapter.notifyDataSetChanged();
                }
            });
            String forexRateListJSONString = convertObjectToString(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, latestForexRate);
            lastUpdatedAt = yqlCurrencyQueryResponse.getQuery().getCreated().getTime();

            editor.putLong(ConstantHelper.SHARED_PREFERENCES_LAST_UPDATE, lastUpdatedAt);
            editor.putString(ConstantHelper.SHARED_PREFERENCES_FOREX_RATE_LIST, forexRateListJSONString);
            editor.commit();

            Log.v("Debug", "get rate success");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    swipeContainer.setRefreshing(false);
                }
            });
        }
    }

    private void ongetForexRateFail() {
        //backup
        //show msg
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
            }
        });
    }

    private void updateSelectedCurrency(ArrayList<ForexRate> selectedCurrency) {
        if (sharedPreferences != null & editor != null && selectedCurrency != null) {
            String selectedCurrencyListJSONString = convertObjectToString(ConstantHelper.KEY_ASSETS_NAME_CURRENCIES, selectedCurrency);
            editor.putString(ConstantHelper.SHARED_PREFERENCES_SELECTED_CURRENCY_LIST, selectedCurrencyListJSONString);
            editor.commit();
        }
    }
}