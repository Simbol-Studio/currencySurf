package com.github.mikephil.charting.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Philipp Jahoda on 11/07/15.
 */
public abstract class LineScatterCandleRadarRenderer extends BarLineScatterCandleBubbleRenderer {
    private int outterCircleColor = Color.parseColor("#AA121212");
    private int outterCircleSize = 40;
    private int innerCircleColor = Color.parseColor("#22121212");
    private int innerCircleSize = 20;

    /**
     * path that is used for drawing highlight-lines (drawLines(...) cannot be used because of dashes)
     */
    private Path mHighlightLinePath = new Path();

    public LineScatterCandleRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws vertical & horizontal highlight-lines if enabled.
     *
     * @param c
     * @param x   x-position of the highlight line intersection
     * @param y   y-position of the highlight line intersection
     * @param set the currently drawn dataset
     */
    protected void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {

        // set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));//dot dot line
//        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());//straight line

        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(x, mViewPortHandler.contentTop());
            mHighlightLinePath.lineTo(x, mViewPortHandler.contentBottom());

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        Paint outterPaint = new Paint();
        outterPaint.setColor(outterCircleColor);
        Bitmap outterBitmap = Bitmap.createBitmap(outterCircleSize, outterCircleSize, Bitmap.Config.ALPHA_8);
        Canvas outterCanvas = new Canvas(outterBitmap);
        RectF outterRectF = new RectF();
        outterRectF.set(0, 0, outterCircleSize, outterCircleSize);
        outterCanvas.drawRoundRect(outterRectF, (outterCircleSize / 2), (outterCircleSize / 2), outterPaint);

        c.drawBitmap(outterBitmap, (x - (outterCircleSize / 2)), (y - (outterCircleSize / 2)), outterPaint);


        Paint innererPaint = new Paint();
        innererPaint.setColor(innerCircleColor);
        Bitmap innerBitmap = Bitmap.createBitmap(innerCircleSize, innerCircleSize, Bitmap.Config.ALPHA_8);
        Canvas innerCanvas = new Canvas(innerBitmap);
        RectF innerRectF = new RectF();
        innerRectF.set(0, 0, innerCircleSize, innerCircleSize);
        innerCanvas.drawRoundRect(innerRectF, (innerCircleSize / 2), (innerCircleSize / 2), outterPaint);

        c.drawBitmap(innerBitmap, (x - (innerCircleSize / 2)), (y - (innerCircleSize / 2)), outterPaint);

        // draw horizontal highlight lines
//        if (set.isHorizontalHighlightIndicatorEnabled()) {
//
//             create horizontal path
//            mHighlightLinePath.reset();
//            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
//            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);
//
//            c.drawPath(mHighlightLinePath, mHighlightPaint);
//
//            Drawable icon = .getResources().getDrawable(R.drawable.ico_plus);
//            // draw x mark
//            int itemHeight = itemView.getBottom() - itemView.getTop();
//            int intrinsicWidthorHeight = icon.getIntrinsicWidth();
//
//            int xMarkLeft = itemView.getRight() - iconMargin - intrinsicWidthorHeight;
//            int xMarkRight = itemView.getRight() - iconMargin;
//            int xMarkTop = itemView.getTop() + (itemHeight - intrinsicWidthorHeight) / 2;
//            int xMarkBottom = xMarkTop + intrinsicWidthorHeight;
//            icon.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);
//
//            icon.draw(c);
//        }
    }
}
