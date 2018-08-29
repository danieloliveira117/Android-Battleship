package amov.danieloliveira.batalhanaval.views;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.activities.GameStartActivity;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;
import amov.danieloliveira.batalhanaval.engine.model.Position;

public class BattleShipCellView extends AppCompatTextView implements Observer, View.OnDragListener, View.OnClickListener, View.OnLongClickListener {
    private static final String TAG = "BattleShipCellView";
    private GameObservable gameObs;
    private Position position;
    private Context context;
    private PlayerType type = null;

    private static Toast notCurrentPlayerToast;

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

        if (!isInEditMode()) {
            gameObs = Utils.getObservable(context);
            gameObs.addObserver(this);

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
            Log.e(TAG, "Invalid Position", e);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof GameMode) {
            TableLayout container = (TableLayout) getParent().getParent();

            if (container.getTag() == null) {
                return;
            } else if (container.getTag() instanceof Integer) {
                if ((Integer) container.getTag() == 0) {
                    type = PlayerType.PLAYER;
                } else {
                    type = PlayerType.ADVERSARY;
                }
            }
        }

        updateColor();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!Utils.parentHasID(this, R.id.tbl_adversary_ships)) {
            this.setOnDragListener(this);
            this.setOnClickListener(this);
            this.setOnLongClickListener(this);
        }
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
                    gameObs.placeShip(type, position, (Integer) view.getTag() - 1);
                    ((GameStartActivity) context).placedViews.add((Integer) view.getTag());
                } else if (view instanceof BattleShipCellView) {
                    gameObs.moveShip(type, position);
                }

                //Log.d(TAG, "ACTION_DRAG_ENTERED " + view.getTag());
                break;
            case DragEvent.ACTION_DRAG_ENDED:
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
        if (type == null)
            return;

        switch (gameObs.getPositionType(type, position)) {
            case ADJACENT:
                this.setBackgroundResource(R.color.adjacent);
                break;
            case VALID:
                this.setBackgroundResource(R.color.valid);
                break;
            case INVALID:
                this.setBackgroundResource(R.color.invalid);
                break;
            case UNKNOWN:
                this.setBackgroundResource(position.getColor());
                break;
            case SELECTED:
                this.setBackgroundResource(R.color.selected);
                break;
            case MISS:
                this.setBackgroundResource(R.color.miss);
                break;
            case HIT_ONE:
                this.setBackgroundResource(R.drawable.ic_one);
                break;
            case HIT_TWO:
                this.setBackgroundResource(R.drawable.ic_two);
                break;
            case HIT_THREE:
                this.setBackgroundResource(R.drawable.ic_three);
                break;
            case HIT_T_SHAPE:
                this.setBackgroundResource(R.drawable.ic_tshaped);
                break;
            case SHIP:
                this.setBackgroundResource(R.drawable.ic_cross);
                break;
        }

        this.invalidate();
    }

    @Override
    public void onClick(View v) {
        if (!gameObs.canDragAndDrop(type, position)) {
            if (gameObs.getCurrentPlayer() == type) {
                gameObs.clickNewPosition(type, position);

            } else {
                if (notCurrentPlayerToast != null) {
                    notCurrentPlayerToast.cancel();
                }

                notCurrentPlayerToast = Toast.makeText(context, R.string.not_your_turn, Toast.LENGTH_SHORT);
                notCurrentPlayerToast.show();
            }
        } else {
            gameObs.selectShip(type, position);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (gameObs.canDragAndDrop(type, position)) {
            final TableLayout container = (TableLayout) v.getParent().getParent();

            List<Position> positions = gameObs.getShipPositions(type, position);

            if (positions != null) {
                gameObs.setShipOnDragEvent(type, position);

                List<BattleShipCellView> viewList = Utils.findViewsWithPositions(container, positions);

                for (View view : viewList) {
                    view.setBackgroundResource(R.color.moved);
                }

                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(data, shadowBuilder, v, 0);
            }

            return true;
        }

        return false;
    }
}