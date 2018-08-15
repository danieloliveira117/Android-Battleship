package amov.danieloliveira.batalhanaval.views;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;

import android.view.MotionEvent;
import android.view.View;
import android.widget.TableLayout;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.ShipType;

class ShipPartView extends View implements Observer, View.OnTouchListener {
    private GameObservable obs;
    private ShipType type;
    private int number;

    public ShipPartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShipPartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!this.isInEditMode()) {
            obs = Utils.getObservable(context);
            obs.addObserver(this);
        }

        readShipAttrs(context, attrs);

        this.setOnTouchListener(this);
    }

    /*
        <enum name="ship_type_one" value="1" />
        <enum name="ship_type_two" value="2" />
        <enum name="ship_type_three" value="3" />
        <enum name="ship_type_t_shape" value="4" />
     */
    private void readShipAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShipPartView,
                0, 0);

        if (a.hasValue(R.styleable.ShipPartView_ship_number)) {
            this.number = a.getInt(R.styleable.ShipPartView_ship_number, 0);
            this.setTag(this.number);
        }

        if (a.hasValue(R.styleable.ShipPartView_ship_type)) {
            int oneDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());
            BorderDrawable drawable = null;

            boolean borderLeft = a.getBoolean(R.styleable.ShipPartView_border_left, false);
            boolean borderRight = a.getBoolean(R.styleable.ShipPartView_border_right, false);
            boolean borderTop = a.getBoolean(R.styleable.ShipPartView_border_top, false);
            boolean borderBottom = a.getBoolean(R.styleable.ShipPartView_border_bottom, false);

            switch (a.getInt(R.styleable.ShipPartView_ship_type, 0)) {
                case 1: {
                    drawable = new BorderDrawable(getResources().getDrawable(R.drawable.ship_one));
                    this.type = ShipType.ONE;
                }
                break;
                case 2:
                    drawable = new BorderDrawable(getResources().getDrawable(R.drawable.ship_two));
                    this.type = ShipType.TWO;
                    break;
                case 3:
                    drawable = new BorderDrawable(getResources().getDrawable(R.drawable.ship_three));
                    this.type = ShipType.THREE;
                    break;
                case 4:
                    drawable = new BorderDrawable(getResources().getDrawable(R.drawable.ship_t_shape));
                    this.type = ShipType.T_SHAPE;
                    break;
            }

            if (drawable != null) {
                if (borderLeft) {
                    drawable.setLeftBorder(2 * oneDP, getResources().getColor(R.color.MISS));
                }
                if (borderTop) {
                    drawable.setTopBorder(2 * oneDP, getResources().getColor(R.color.MISS));
                }
                if (borderRight) {
                    drawable.setRightBorder(2 * oneDP, getResources().getColor(R.color.MISS));
                }
                if (borderBottom) {
                    drawable.setBottomBorder(2 * oneDP, getResources().getColor(R.color.MISS));
                }

                this.setBackground(drawable);
            }

        }

        a.recycle();
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final TableLayout container = (TableLayout) v.getParent().getParent();
        final int action = event.getAction();

        // Handles each of the expected events
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                List<View> views = Utils.findViewsWithTag(container, number);

                for (View view : views) {
                    view.setVisibility(INVISIBLE);
                }

                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);

                return true;
            default:
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
