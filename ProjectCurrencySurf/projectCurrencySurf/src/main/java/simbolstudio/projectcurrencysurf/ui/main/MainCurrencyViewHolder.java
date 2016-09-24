package simbolstudio.projectcurrencysurf.ui.main;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import simbolstudio.projectcurrencysurf.R;

/**
 * Created by Marcus on 28-Aug-2016.
 */
public class MainCurrencyViewHolder extends RecyclerView.ViewHolder {
    SimpleDraweeView currencyImg;
    TextView currencyCodeText;
    TextView currencyNmText;
    TextView currencyAmtText;

    public MainCurrencyViewHolder(View view) {
        super(view);
        currencyImg = (SimpleDraweeView) view.findViewById(R.id.currencyImg);
        currencyCodeText = (TextView) view.findViewById(R.id.currencyCode);
        currencyNmText = (TextView) view.findViewById(R.id.currencyNm);
        currencyAmtText = (TextView) view.findViewById(R.id.currencyAmt);
    }

    public void bindData(Uri uri, String currencyCode, String currencyNm, double rate) {
        this.currencyImg.setImageURI(uri);
        this.currencyCodeText.setText(currencyCode);
        this.currencyNmText.setText(currencyNm);
        this.currencyAmtText.setHint(String.valueOf(rate));
    }
}
