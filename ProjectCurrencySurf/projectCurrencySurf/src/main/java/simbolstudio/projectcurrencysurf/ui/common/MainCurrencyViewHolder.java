package simbolstudio.projectcurrencysurf.ui.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import simbolstudio.projectcurrencysurf.R;

/**
 * Created by Marcus on 28-Aug-2016.
 */
public class MainCurrencyViewHolder extends RecyclerView.ViewHolder{
    SimpleDraweeView currencyImg;

    public MainCurrencyViewHolder(View view){
        super(view);
        currencyImg = (SimpleDraweeView)view.findViewById(R.id.currencyImg);
    }

    public void bindDate(){

    }

    public SimpleDraweeView getCurrencyImg() {
        return currencyImg;
    }
}
