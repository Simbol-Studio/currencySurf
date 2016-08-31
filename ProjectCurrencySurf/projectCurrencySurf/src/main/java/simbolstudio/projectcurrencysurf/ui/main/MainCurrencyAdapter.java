package simbolstudio.projectcurrencysurf.ui.main;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.common.util.UriUtil;

import java.util.ArrayList;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.model.ForexRate;
import simbolstudio.projectcurrencysurf.ui.common.MainCurrencyViewHolder;

/**
 * Created by Marcus on 28-Aug-2016.
 */
public class MainCurrencyAdapter extends RecyclerView.Adapter<MainCurrencyViewHolder> {
    Context context;
    ArrayList<ForexRate> forexList;

    public MainCurrencyAdapter(Context context) {
        this.context = context;
        forexList = new ArrayList<>();
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

    public ForexRate getItem(int position) {
        if (forexList != null && position < forexList.size())
            return forexList.get(position);
        else
            return null;
    }

    @Override
    public int getItemCount() {
        return forexList.size();
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
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(((MainActivity) context).getCurrencyIcon(forexRate.getId())))
                    .build();
            holder.getCurrencyImg().setImageURI(uri);

        }
    }
}
