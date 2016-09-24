package simbolstudio.projectcurrencysurf.ui.search;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.common.ConstantHelper;
import simbolstudio.projectcurrencysurf.model.ForexRate;


/**
 * Created by Marcus on 11-Sep-2016.
 */
public class SearchCurrencyAdapter extends RecyclerView.Adapter<SearchCurrencyViewHolder> {
    private final ArrayList<LineItem> mItems;
    private final Context context;
    int allCurrencyCount = 0;
    int selectedCount = 0;

    public SearchCurrencyAdapter(Context context, ArrayList<ForexRate> allForexRate, ArrayList<ForexRate> selectedFrexRate) {
        this.context = context;
        this.mItems = new ArrayList<>();

        //Insert headers into list of items.
        String lastHeader = "";
        int sectionManager = -1;
        int headerCount = 0;
        int sectionFirstPosition = 0;
        String header = "";
        for (int i = 0; i < allForexRate.size(); i++) {
            if (i < 8)
                header = "Favourite";
            else
                header = allForexRate.get(i).getId().substring(0, 1);

            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                sectionManager = (sectionManager + 1) % 2;
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                mItems.add(new LineItem(allForexRate.get(i), header, true, sectionManager, sectionFirstPosition));
            }
            mItems.add(new LineItem(allForexRate.get(i), null, false, sectionManager, sectionFirstPosition));
        }

        allCurrencyCount = allForexRate.size();

        for (int i = 0; i < selectedFrexRate.size(); i++) {
            for (int j = 0; j < mItems.size(); j++) {
                if (selectedFrexRate.get(i).getId().equalsIgnoreCase(mItems.get(j).forexRate.getId()) && !mItems.get(j).isHeader) {
                    mItems.get(j).setSelected(true);
                    mItems.get(j).setOriIsSelected(true);
                    selectedCount++;
                    j = mItems.size();
                }
            }
        }
    }

    @Override
    public SearchCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ConstantHelper.VIEW_TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_search_currency_header, parent, false);
            return new SearchCurrencyViewHolder(view, true);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_search_currency, parent, false);
            return new SearchCurrencyViewHolder(view, false);
        }
    }

    @Override
    public void onBindViewHolder(final SearchCurrencyViewHolder holder, final int position) {
        final LineItem item = mItems.get(position);
        final ForexRate forexRate = item.forexRate;
        final View itemView = holder.itemView;
        //

        if (item.isHeader)
            holder.bindData(item.getHeader());
        else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(((SearchActivity) context).getCurrencyIcon(forexRate.getId())))
                    .build();
            holder.bindData(uri, forexRate.getId(), forexRate.getCurrencyNm(), item.isSelected());

            holder.getCurrencyLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.getCurrencyIsSelectedCheckBox().setChecked(!holder.getCurrencyIsSelectedCheckBox().isChecked());
                    mItems.get(position).setSelected(holder.getCurrencyIsSelectedCheckBox().isChecked());
                    setSingleSelected(mItems.get(position).isSelected());

                }
            });
            holder.getCurrencyIsSelectedCheckBox().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItems.get(position).setSelected(holder.getCurrencyIsSelectedCheckBox().isChecked());
                    setSingleSelected(mItems.get(position).isSelected());
                }
            });
        }

        //
        final GridSLM.LayoutParams lp = GridSLM.LayoutParams.from(itemView.getLayoutParams());
        if (item.isHeader) {
            lp.headerDisplay = GridSLM.LayoutParams.HEADER_STICKY | GridSLM.LayoutParams.HEADER_INLINE;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            lp.headerEndMarginIsAuto = true;
            lp.headerStartMarginIsAuto = true;
        }
        lp.setSlm(LinearSLM.ID);
        lp.setFirstPosition(item.sectionFirstPosition);
        itemView.setLayoutParams(lp);
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position).isHeader ? ConstantHelper.VIEW_TYPE_HEADER : ConstantHelper.VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public ArrayList<LineItem> getArray() {
        return mItems;
    }

    public LineItem getItem(int position) {
        return mItems.get(position);
    }

    public void setAllSelected(boolean allSelected) {
        if (mItems != null) {
            if (allSelected) {
                for (int i = 0; i < mItems.size(); i++) {
                    if (!mItems.get(i).isHeader) {
                        mItems.get(i).setSelected(true);
                    }
                }
                selectedCount = allCurrencyCount;
                ((SearchActivity) context).showSelectedCount();
            } else {
                for (int i = 0; i < mItems.size(); i++) {
                    if (!mItems.get(i).isHeader) {
                        mItems.get(i).setSelected(false);
                    }
                }
                selectedCount = 0;
                ((SearchActivity) context).showSelectedCount();
            }
            notifyDataSetChanged();
        }
    }

    public void setSingleSelected(boolean isSelected) {
        if (isSelected)
            selectedCount++;
        else
            selectedCount--;

        ((SearchActivity) context).showSelectedCount();
    }

    public int getSelectedCount() {
        return selectedCount;
    }

    public int getAllCurrencyCount() {
        return allCurrencyCount;
    }

    public static class LineItem {
        public int sectionManager;
        public int sectionFirstPosition;
        public boolean isHeader;
        public ForexRate forexRate;
        public String header;
        public boolean isSelected;
        public boolean oriIsSelected;

        public LineItem(ForexRate forexRate, String header, boolean isHeader, int sectionManager,
                        int sectionFirstPosition) {
            this.isHeader = isHeader;
            this.header = header;
            this.forexRate = forexRate;
            this.sectionManager = sectionManager;
            this.sectionFirstPosition = sectionFirstPosition;
            this.isSelected = false;
            this.oriIsSelected = false;
        }

        public String getHeader() {
            return header;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public boolean isOriIsSelected() {
            return oriIsSelected;
        }

        public void setOriIsSelected(boolean oriIsSelected) {
            this.oriIsSelected = oriIsSelected;
        }
    }
}
