package amov.danieloliveira.batalhanaval.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import java.util.Observable;
import java.util.Observer;

import amov.danieloliveira.batalhanaval.Utils;
import amov.danieloliveira.batalhanaval.engine.GameObservable;

public class ShipDock extends ConstraintLayout implements Observer {
    private GameObservable obs;

    public ShipDock(Context context) {
        super(context);
        init(context);
    }

    public ShipDock(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShipDock(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        obs = Utils.getObservable(context);
        obs.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
