package simbolstudio.projectcurrencysurf.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;

import java.util.ArrayList;
import java.util.Collections;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.library.itemtouchhelper.ItemTouchHelperAdapter;
import simbolstudio.projectcurrencysurf.library.itemtouchhelper.OnStartDragListener;
import simbolstudio.projectcurrencysurf.model.ForexRate;

/**
 * Created by Marcus on 28-Aug-2016.
 */
public class MainCurrencyAdapter extends RecyclerView.Adapter<MainCurrencyViewHolder> implements ItemTouchHelperAdapter {
    Context context;
    ArrayList<ForexRate> forexList;
    private final OnStartDragListener mDragStartListener;
    RecyclerView mainRecycler;

    public ArrayList<ForexRate> getForexList() {
        return forexList;
    }

    public MainCurrencyAdapter(Context context,OnStartDragListener dragStartListener,RecyclerView mainRecycler) {
        this.context = context;
        this.mDragStartListener = dragStartListener;
        forexList = new ArrayList<>();
        this.mainRecycler=mainRecycler;
    }

    public void setForexList(ArrayList<ForexRate> forexList) {
        this.forexList = forexList;
    }

    public void addForexRate(ForexRate forexRate) {
        if (forexList != null) {
            for (int i = 0; i < forexList.size(); i++) {
                if (forexList.get(i).getId().equalsIgnoreCase(forexRate.getId()))
                    return;
            }
            forexList.add(forexRate);
        }
    }

    public void updateForexRate(ForexRate forexRate) {
        if (forexList != null) {
            for (int i = 0; i < forexList.size(); i++) {
                if (forexList.get(i).getId().equalsIgnoreCase(forexRate.getId())) {
                    forexList.remove(i);
                    return;
                }
            }
            forexList.add(forexRate);
        }
    }

    public ForexRate getItem(int position) {
        if (forexList != null && position < forexList.size())
            return forexList.get(position);
        else
            return null;
    }

    @Override
    public void onItemSwipedLeft(int position) {
        forexList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        ((MainActivity)context).updateSelectedCurrency(forexList);
    }

    @Override
    public void onItemSwipedRight(int position) {
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(forexList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        ((MainActivity)context).updateSelectedCurrency(forexList);
        return true;
    }

    @Override
    public int getItemCount() {
        if (forexList != null)
            return forexList.size();
        else
            return 0;
    }

    @Override
    public MainCurrencyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main_currency, parent, false);
        return new MainCurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainCurrencyViewHolder holder, int position) {
        if (holder != null && forexList != null && position < forexList.size()) {
            ForexRate forexRate = forexList.get(position);
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                    .path(String.valueOf(((MainActivity) context).getCurrencyIcon(forexRate.getId())))
                    .build();
            Double rate = 0.0;
            if (forexRate.getRate() == null)
                rate = 0.0;
            else
                rate = forexRate.getRate();

            holder.bindData(uri, forexRate.getId(), forexRate.getCurrencyNm(), rate);
        }
    }
}
