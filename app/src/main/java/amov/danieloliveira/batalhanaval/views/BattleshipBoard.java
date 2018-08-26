package amov.danieloliveira.batalhanaval.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import static amov.danieloliveira.batalhanaval.Consts.MAXROWS;
import static amov.danieloliveira.batalhanaval.Consts.MAXCOLUMNS;

public class BattleshipBoard extends LinearLayout {
    public BattleshipBoard(Context context) {
        this(context, null, 0);
    }

    public BattleshipBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BattleshipBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.removeAllViews();

        TableLayout table = new TableLayout(context);
        table.removeAllViews();
        table.invalidate();
        table.refreshDrawableState();

        this.addView(table);

        table.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        table.setStretchAllColumns(true);
        table.setOrientation(LinearLayout.VERTICAL);

        for (int i = 1; i <= MAXROWS; i++) {
            TableRow row = new TableRow(context);

            row.removeAllViews();
            row.invalidate();
            row.refreshDrawableState();

            table.addView(row);

            row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            for (int j = 1; j <= MAXCOLUMNS; j++) {
                //final RelativeLayout placeLayout = new RelativeLayout(context);

                BattleshipCellView cell = new BattleshipCellView(context, i, j);

                //placeLayout.setLayoutParams(new TableRow.LayoutParams(
                //        TableRow.LayoutParams.MATCH_PARENT / 8,
                //        TableRow.LayoutParams.WRAP_CONTENT));

                //placeLayout.addView(cell, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                row.addView(cell);

                cell.prepareListeners();
            }
        }
    }
}
