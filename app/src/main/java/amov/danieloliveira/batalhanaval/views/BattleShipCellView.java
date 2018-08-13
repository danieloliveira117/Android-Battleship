package amov.danieloliveira.batalhanaval.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TableLayout;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.activities.GameStartActivity;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;
import amov.danieloliveira.batalhanaval.engine.model.Position;

public class BattleShipCellView extends AppCompatTextView implements Observer, View.OnClickListener, View.OnDragListener {
    private static final String TAG = "BattleShipCellView";
    private GameObservable gameObs;
    private Position position;
    private Context context;

    public BattleShipCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BattleShipCellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        if (!this.isInEditMode()) {
            gameObs = Utils.getObservable(context);
            gameObs.addObserver(this);

            this.setOnClickListener(this);
            this.setOnDragListener(this);
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
        gameObs.addNewAttempt(PlayerType.PLAYER, position);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateColor();
    }

    /* Square */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //final int height = getMeasuredHeight();
        final int width = getMeasuredWidth();

        setMeasuredDimension(width, width);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        View view = (View) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                Log.d(TAG, "ACTION_DRAG_ENTERED");
                // TODO: 13/08/2018 Show entire ship + border
                this.setBackgroundResource(R.color.ship_t_shape);
                this.invalidate();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                if (!((GameStartActivity) context).placedViews.contains((Integer) view.getTag())) {
                    List<View> views = Utils.findViewsWithTag((TableLayout) view.getParent().getParent(), view.getTag());

                    for (View invView : views) {
                        invView.setVisibility(View.VISIBLE);
                    }
                }

                Log.d(TAG, "ACTION_DRAG_ENDED");
            case DragEvent.ACTION_DRAG_EXITED:
                Log.d(TAG, "ACTION_DRAG_EXITED");
                updateColor();
                break;
            case DragEvent.ACTION_DROP:
                ((GameStartActivity) context).placedViews.add((Integer) view.getTag());
                Log.d(TAG, "ACTION_DROP");
                break;
            default:
                return false;
        }

        return true;
    }

    private void updateColor() {
        // TODO change player type
        switch (gameObs.getPositionType(PlayerType.PLAYER, position)) {
            case UNKNOWN:
                this.setBackgroundResource(position.getColor());
                break;
            case SELECTED:
                this.setBackgroundResource(R.color.SELECTED);
                break;
            case MISS:
                this.setBackgroundResource(R.color.MISS);
                break;
            case HIT:
                // TODO change cross to ship part number (1, 2, 3 or T)
                this.setBackgroundResource(R.drawable.ic_cross);
                break;
            case SHIP:
                this.setBackgroundResource(R.color.SHIP);
                break;
        }

        this.invalidate();
    }

}
