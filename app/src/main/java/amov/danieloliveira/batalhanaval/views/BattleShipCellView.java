package amov.danieloliveira.batalhanaval.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;
import amov.danieloliveira.batalhanaval.engine.model.Position;

public class BattleShipCellView extends View implements Observer, View.OnClickListener {
    private GameObservable obs;
    private Position position;

    public BattleShipCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BattleShipCellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(!this.isInEditMode()) {
            obs = Utils.getObservable(context);
            obs.addObserver(this);

            this.setOnClickListener(this);
        }

        try {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.BattleShipCellView,
                    0, 0);

            position = new Position(a.getString(R.styleable.BattleShipCellView_position));

            this.setBackgroundResource(position.getColor());
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

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
