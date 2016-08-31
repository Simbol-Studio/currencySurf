package simbolstudio.projectcurrencysurf.ui.common;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import simbolstudio.projectcurrencysurf.R;

/**
 * Created by Marcus on 28-Aug-2016.
 */
public class MainCurrencyViewHolder extends RecyclerView.ViewHolder{
    SimpleDraweeView currencyImg;
    TextView currencyCode;
    TextView currencyNm;

    public MainCurrencyViewHolder(View view){
        super(view);
        currencyImg = (SimpleDraweeView)view.findViewById(R.id.currencyImg);
        currencyCode = (TextView)view.findViewById(R.id.currencyCode);
        currencyNm = (TextView)view.findViewById(R.id.currencyNm);
    }

    public void bindData(Uri uri, String currencyCode, String currencyNm){
        this.currencyImg.setImageURI(uri);
        this.currencyCode.setText(currencyCode);
        this.currencyNm.setText(currencyNm);
    }
}
