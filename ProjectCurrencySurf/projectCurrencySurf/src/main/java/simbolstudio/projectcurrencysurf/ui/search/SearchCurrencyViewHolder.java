package simbolstudio.projectcurrencysurf.ui.search;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import simbolstudio.projectcurrencysurf.R;

/**
 * Created by Marcus on 11-Sep-2016.
 */
public class SearchCurrencyViewHolder extends RecyclerView.ViewHolder {
    boolean isHeader;
    //
    TextView currencyHeader;
    //
    RelativeLayout itemLayout;
    SimpleDraweeView currencyImg;
    TextView currencyCodeText;
    TextView currencyNmText;
    CheckBox currencyIsSelectedCheckBox;

    public SearchCurrencyViewHolder(View view, boolean isHeader) {
        super(view);
        this.isHeader = isHeader;
        if (isHeader) {
            currencyHeader = (TextView) view.findViewById(R.id.currencyHeader);
            itemLayout = (RelativeLayout) view.findViewById(R.id.headerLayout);
        } else {
            itemLayout = (RelativeLayout) view.findViewById(R.id.currencyLayout);
            currencyImg = (SimpleDraweeView) view.findViewById(R.id.currencyImg);
            currencyCodeText = (TextView) view.findViewById(R.id.currencyCode);
            currencyNmText = (TextView) view.findViewById(R.id.currencyNm);
            currencyIsSelectedCheckBox = (CheckBox) view.findViewById(R.id.currencyIsSelected);
        }
    }

    public void bindData(String header) {
        this.currencyHeader.setText(header);
    }

    public void bindData(Uri uri, String currencyCode, String currencyNm, boolean isSelected) {
        this.currencyImg.setImageURI(uri);
        this.currencyCodeText.setText(currencyCode);
        this.currencyNmText.setText(currencyNm);
        this.currencyIsSelectedCheckBox.setChecked(isSelected);
    }

    public RelativeLayout getItemLayout() {
        return this.itemLayout;
    }

    public CheckBox getCurrencyIsSelectedCheckBox() {
        return this.currencyIsSelectedCheckBox;
    }
}
