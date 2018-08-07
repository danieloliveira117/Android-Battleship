package amov.danieloliveira.batalhanaval.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.exceptions.InvalidPositionException;
import amov.danieloliveira.batalhanaval.engine.model.Position;

public class CellView extends View implements Observer {
    private GameObservable obs;
    private Position position;

    public CellView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        obs = Utils.getObservable(context);
        obs.addObserver(this);

        try {
            position = getPosition(this);

            if (position != null) {
                this.setBackgroundResource(position.getColor());
            } else {
                throw new InvalidPositionException("Posição inválida");
            }
        } catch (InvalidPositionException e) {
            e.printStackTrace();
        }

        /*paintSelected.setStyle(Paint.Style.FILL);
        paintSelected.setColor(ContextCompat.getColor(context, R.color.selectedPiece));

        paintMove.setStyle(Paint.Style.FILL);
        paintMove.setColor(ContextCompat.getColor(context, R.color.possibleMove));*/
    }

    @SuppressLint("ResourceType")
    public static Position getPosition(View view) {
        if (view.getId() == 0xffffffff) {
            return null;
        } else {
            String Id = view.getResources().getResourceName(view.getId());
            Id = Id.substring(Id.indexOf(":id/") + 4);

            return new Position(Id.charAt(0), Character.getNumericValue(Id.charAt(1)));
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
