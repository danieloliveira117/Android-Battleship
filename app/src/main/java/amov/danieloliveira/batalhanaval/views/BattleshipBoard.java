package amov.danieloliveira.batalhanaval.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import amov.danieloliveira.batalhanaval.R;

import static amov.danieloliveira.batalhanaval.Consts.MAXROWS;
import static amov.danieloliveira.batalhanaval.Consts.MAXCOLUMNS;

public class BattleshipBoard extends TableLayout {

    public BattleshipBoard(Context context) {
        this(context, null);
    }

    public BattleshipBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @SuppressLint("SetTextI18n")
    private void init(Context context) {
        this.removeAllViews();

        this.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT));

        for (int i = 0; i <= MAXROWS; i++) {
            TableRow row = new TableRow(context);

            this.addView(row);

            row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f));
            row.refreshDrawableState();

            if (i == 0) {
                // Column Label
                row.addView(new AppCompatTextView(context));

                for (int j = 1; j <= MAXCOLUMNS; j++) {
                    AppCompatTextView textView = new AppCompatTextView(context);

                    textView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));

                    char letter = (char) ('A' + (j - 1));

                    textView.setText("" + letter);
                    textView.setTextColor(getResources().getColor(R.color.light));
                    textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);

                    row.addView(textView);
                }
            } else {
                row.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.table_label_padding_bottom));
                this.setColumnStretchable(i, true);

                for (int j = 1; j <= MAXCOLUMNS; j++) {
                    // Row Label
                    if (j == 1) {
                        AppCompatTextView textView = new AppCompatTextView(context);

                        textView.setLayoutParams(new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));

                        textView.setText("" + i);
                        textView.setTextColor(getResources().getColor(R.color.light));
                        textView.setPaddingRelative(0, 0, getResources().getDimensionPixelSize(R.dimen.table_label_padding_end), 0);
                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

                        row.addView(textView);
                    }

                    BattleshipCellView cell = new BattleshipCellView(context, i, j);

                    LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
                    params.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.table_label_padding_end));
                    cell.setLayoutParams(params);
                    cell.setAdjustViewBounds(true);

                    row.addView(cell);

                    cell.prepareListeners();
                }
            }
        }

        this.invalidate();
        this.requestLayout();
    }
}
