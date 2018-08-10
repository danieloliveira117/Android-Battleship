package amov.danieloliveira.batalhanaval.views;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;

class ShipPartView extends View implements Observer, View.OnTouchListener {
    private GameObservable obs;

    public ShipPartView(Context context) {
        super(context);
        init(context);
    }

    public ShipPartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShipPartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (!this.isInEditMode()) {
            obs = Utils.getObservable(context);
            obs.addObserver(this);
        }

        this.setOnTouchListener(this);
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(data, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    /* Square */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();

        setMeasuredDimension(width, width);
    }
}
