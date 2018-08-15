package amov.danieloliveira.batalhanaval.views;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
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

// TODO change PlayerType.PLAYER to current player in game data????
public class BattleShipCellView extends AppCompatTextView implements Observer, View.OnDragListener, View.OnTouchListener {
    private static final String TAG = "BattleShipCellView";
    private GameObservable gameObs;
    private Position position;
    private Context context;
    private GestureDetector gestureDetector;

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

            this.setOnDragListener(this);
            this.setOnTouchListener(this);

            gestureDetector = new GestureDetector(context, new SingleTapConfirm());
        }

        try {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.BattleShipCellView,
                    0, 0);

            position = new Position(a.getString(R.styleable.BattleShipCellView_position));

            this.setTag(position);

            this.setBackgroundResource(position.getColor());
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        updateColor();
    }

    /* Square */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int size = getMeasuredWidth();

        setMeasuredDimension(size, size);
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        View view = (View) event.getLocalState();

        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_LOCATION: // FIX: Crash
            case DragEvent.ACTION_DRAG_STARTED: // Start listening to drag events
                // do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:

                // Update ship position
                if (view instanceof ShipPartView) {
                    gameObs.placeShip(PlayerType.PLAYER, position, (Integer) view.getTag() - 1);
                    ((GameStartActivity) context).placedViews.add((Integer) view.getTag());
                } else if (view instanceof BattleShipCellView) {
                    gameObs.moveShip(PlayerType.PLAYER, (Position) view.getTag(), position);
                }

                //Log.d(TAG, "ACTION_DRAG_ENTERED " + view.getTag());
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                // TODO: 14/08/2018 Handle BattleShipCellView moves 
                if (view instanceof ShipPartView && !((GameStartActivity) context).placedViews.contains((Integer) view.getTag())) {
                    List<View> views = Utils.findViewsWithTag((TableLayout) view.getParent().getParent(), view.getTag());

                    for (View invView : views) {
                        invView.setVisibility(View.VISIBLE);
                    }
                } else if (view instanceof BattleShipCellView) {
                    updateColor();
                }

                //Log.d(TAG, "ACTION_DRAG_ENDED " + view.getTag());
            case DragEvent.ACTION_DRAG_EXITED:
                if (view instanceof ShipPartView) {
                    updateColor();
                }

                //Log.d(TAG, "ACTION_DRAG_EXITED " + view.getTag());
                break;
            case DragEvent.ACTION_DROP:
                //Log.d(TAG, "ACTION_DROP " + view.getTag());
                break;
            default:
                return false;
        }

        return true;
    }

    private void updateColor() {
        // TODO change player type
        switch (gameObs.getPositionType(PlayerType.PLAYER, position)) {
            case VALID:
                this.setBackgroundResource(R.color.VALID);
                break;
            case INVALID:
                this.setBackgroundResource(R.color.INVALID);
                break;
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

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            gameObs.addNewAttempt(PlayerType.PLAYER, position);
            return true;
        } else {
            final TableLayout container = (TableLayout) v.getParent().getParent();
            final int action = event.getAction();

            // Handles each of the expected events
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    List<Position> positions = gameObs.getShipPositions(PlayerType.PLAYER, position);

                    if (positions != null) {
                        gameObs.selectShip(PlayerType.PLAYER, position);

                        List<BattleShipCellView> viewList = Utils.findViewsWithPositions(container, positions);

                        for (View view : viewList) {
                            view.setBackgroundResource(R.color.MOVED);
                        }

                        ClipData data = ClipData.newPlainText("", "");
                        DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                        v.startDrag(data, shadowBuilder, v, 0);
                    }

                    return true;
                default:
                    return false;
            }
        }
    }

    // https://stackoverflow.com/questions/19538747/how-to-use-both-ontouch-and-onclick-for-an-imagebutton
    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }
}
