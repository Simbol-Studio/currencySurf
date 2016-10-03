package simbolstudio.projectcurrencysurf.ui.main;

import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import simbolstudio.projectcurrencysurf.R;
import simbolstudio.projectcurrencysurf.library.itemtouchhelper.ItemTouchHelperViewHolder;

/**
 * Created by Marcus on 28-Aug-2016.
 */
public class MainCurrencyViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
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

    @Override
    public void onItemSelected() {
//        itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
