package simbolstudio.projectcurrencysurf.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayList;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.base.ui.BaseAppCompatActivity;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.model.ForexRate;

public class SearchActivity extends BaseAppCompatActivity {
    RecyclerView searchRecycler;
    SearchCurrencyAdapter searchCurrencyAdapter;
    TextView selectedCurrencyCountText;
    CheckBox selectAllCurrencyCheckBox;
    CompoundButton.OnCheckedChangeListener onCheckedChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupUI();
        setupData();
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void setupData() {
    }

    @Override
    protected void setupUI() {
        setupToolbar(R.id.mainToolbar, "", true);
        getCustomTitleText();
        getCustomSearchEditText();
        selectedCurrencyCountText = (TextView) findViewById(R.id.selectedCurrencyCountText);
        selectAllCurrencyCheckBox = (CheckBox) findViewById(R.id.selectAllCurrencyCheckBox);

        searchRecycler = (RecyclerView) findViewById(R.id.searchRecycler);
        searchRecycler.setLayoutManager(new LayoutManager(getActivityContext()));
        ArrayList<ForexRate> allForexRates = (ArrayList<ForexRate>) getIntent().getBundleExtra(ConstantHelper.EXTRA_BUNDLE_CURRENCY).getSerializable(ConstantHelper.EXTRA_ALL_CURRENCY_LIST);
        ArrayList<ForexRate> selectedFrexRate = (ArrayList<ForexRate>) getIntent().getBundleExtra(ConstantHelper.EXTRA_BUNDLE_CURRENCY).getSerializable(ConstantHelper.EXTRA_SELECTED_CURRENCY_LIST);
//        if  null
        searchCurrencyAdapter = new SearchCurrencyAdapter(getActivityContext(), allForexRates, selectedFrexRate);
        searchRecycler.setAdapter(searchCurrencyAdapter);

        showSelectedCount();
        if (searchCurrencyAdapter.getSelectedCount() == searchCurrencyAdapter.getAllCurrencyCount())
            selectAllCurrencyCheckBox.setChecked(true);

        onCheckedChangeListener= new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                searchCurrencyAdapter.setAllSelected(isChecked);
            }
        };

        selectAllCurrencyCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        showSelectedCount();
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
                ArrayList<ForexRate> forexRate = new ArrayList<>();
                for (int i = 0; i < searchCurrencyAdapter.getArray().size(); i++) {
                    if (searchCurrencyAdapter.getItem(i).isSelected() != searchCurrencyAdapter.getItem(i).isOriIsSelected() && !searchCurrencyAdapter.getItem(i).isHeader)
                        forexRate.add(searchCurrencyAdapter.getItem(i).forexRate);
                }
                if (forexRate.size() > 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstantHelper.EXTRA_SELECTED_CURRENCY_LIST, forexRate);
                    intent.putExtra(ConstantHelper.EXTRA_BUNDLE_CURRENCY, bundle);
                    setResult(RESULT_OK, intent);
                } else
                    setResult(RESULT_CANCELED);
                finish();

//

                Log.v("", "");
                getCustomSearchEditText().setText("");
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    public void showSelectedCount() {
        String temp = searchCurrencyAdapter.getSelectedCount() + " selected";
        selectedCurrencyCountText.setText(temp);

        selectAllCurrencyCheckBox.setOnCheckedChangeListener(null);
        if(searchCurrencyAdapter.getSelectedCount()==searchCurrencyAdapter.getAllCurrencyCount())
            selectAllCurrencyCheckBox.setChecked(true);
        else
            selectAllCurrencyCheckBox.setChecked(false);
        selectAllCurrencyCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}