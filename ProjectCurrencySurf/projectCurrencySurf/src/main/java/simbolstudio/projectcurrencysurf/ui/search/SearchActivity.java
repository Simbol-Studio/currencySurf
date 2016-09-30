package simbolstudio.projectcurrencysurf.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
    MenuItem searchMenuItem;
    TextView titleText;
    RelativeLayout editTextLayout;
    EditText searchEditText;
    ImageButton clearTextImg;
    int currentMode;

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
        currentMode = ConstantHelper.MODE_NORMAL;
    }

    @Override
    protected void setupUI() {
        setupToolbar(R.id.mainToolbar, getResources().getString(R.string.search_activity_title), true, true);
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

        onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                searchCurrencyAdapter.setAllSelected(isChecked);
            }
        };

        selectAllCurrencyCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
        showSelectedCount();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (clearTextImg != null)
                    if (charSequence.toString().length() > 0) {
                        clearTextImg.setVisibility(View.VISIBLE);
                    } else {
                        clearTextImg.setVisibility(View.GONE);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        titleText = getCustomTitleText();
        editTextLayout = getEditTextLayout();
        searchEditText = getCustomSearchEditText();
        searchEditText.setHint(getResources().getString(R.string.search_currency));
        searchEditText.addTextChangedListener(textWatcher);
        getCustomNavImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        clearTextImg = getClearTextImg();
        clearTextImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        searchMenuItem = menu.findItem(R.id.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                currentMode = ConstantHelper.MODE_SEARCH;
                changeMode(currentMode);
                break;
            case R.id.done:
                ArrayList<ForexRate> forexRate = new ArrayList<>();
                for (int i = 0; i < searchCurrencyAdapter.getArray().size(); i++) {
                    if (searchCurrencyAdapter.getItem(i).isSelected() != searchCurrencyAdapter.getItem(i).isOriIsSelected() && !searchCurrencyAdapter.getItem(i).isHeader)
                        forexRate.add(searchCurrencyAdapter.getItem(i).forexRate);
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(ConstantHelper.EXTRA_SELECTED_CURRENCY_LIST, forexRate);
                intent.putExtra(ConstantHelper.EXTRA_BUNDLE_CURRENCY, bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSelectedCount() {
        String temp = searchCurrencyAdapter.getSelectedCount() + " selected";
        selectedCurrencyCountText.setText(temp);

        selectAllCurrencyCheckBox.setOnCheckedChangeListener(null);
        if (searchCurrencyAdapter.getSelectedCount() == searchCurrencyAdapter.getAllCurrencyCount())
            selectAllCurrencyCheckBox.setChecked(true);
        else
            selectAllCurrencyCheckBox.setChecked(false);
        selectAllCurrencyCheckBox.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    public void changeMode(int mode) {
        if (mode == ConstantHelper.MODE_SEARCH) {
            titleText.setVisibility(View.GONE);
            editTextLayout.setVisibility(View.VISIBLE);
            searchEditText.setVisibility(View.VISIBLE);
            searchMenuItem.setVisible(false);
        } else {
            titleText.setVisibility(View.VISIBLE);
            editTextLayout.setVisibility(View.GONE);
            searchEditText.setVisibility(View.GONE);
            searchMenuItem.setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentMode == ConstantHelper.MODE_NORMAL) {
            finish();
        } else {
            currentMode = ConstantHelper.MODE_NORMAL;
            changeMode(currentMode);
        }
    }
}