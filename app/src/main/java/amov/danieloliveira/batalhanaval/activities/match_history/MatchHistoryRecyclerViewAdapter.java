package amov.danieloliveira.batalhanaval.activities.match_history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import amov.danieloliveira.batalhanaval.R;
import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.model.MatchHistory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link MatchHistory}
 */
public class MatchHistoryRecyclerViewAdapter extends RecyclerView.Adapter<MatchHistoryRecyclerViewAdapter.ViewHolder> {
    private final List<MatchHistory> mValues;
    private final Activity activity;

    public MatchHistoryRecyclerViewAdapter(List<MatchHistory> items, Activity activity) {
        mValues = new ArrayList<>(items);

        Collections.reverse(mValues);

        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_matchhistory, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MatchHistory matchHistory = mValues.get(position);

        if (matchHistory.getGameMode() == GameMode.vsAI)
            holder.tv_vs_game_mode.setText(activity.getResources().getString(R.string.vs_ai));
        else
            holder.tv_vs_game_mode.setText(activity.getResources().getString(R.string.vs_player));

        if (matchHistory.didPlayerWin()) {
            holder.tv_game_result.setText(activity.getResources().getString(R.string.victory));
            holder.tv_game_result.setTextColor(activity.getResources().getColor(R.color.valid));
        } else {
            holder.tv_game_result.setText(activity.getResources().getString(R.string.defeat));
            holder.tv_game_result.setTextColor(activity.getResources().getColor(R.color.colorRed));
        }

        holder.tv_player_name.setText(matchHistory.getPlayer());
        holder.tv_adversary_name.setText(matchHistory.getOpponent());

        holder.tv_num_hits_player.setText("" + matchHistory.getNumHitsPlayer());
        holder.tv_num_hits_adversary.setText("" + matchHistory.getNumHitsOpponent());

        holder.tv_ships_destroyed_player.setText("" + matchHistory.getShipsDestroyedPlayer());
        holder.tv_ships_destroyed_adversary.setText("" + matchHistory.getShipsDestroyedOpponent());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final AppCompatTextView tv_vs_game_mode;
        public final AppCompatTextView tv_game_result;
        public final AppCompatTextView tv_player_name;
        public final AppCompatTextView tv_adversary_name;
        public final AppCompatTextView tv_num_hits_player;
        public final AppCompatTextView tv_ships_destroyed_player;
        public final AppCompatTextView tv_num_hits_adversary;
        public final AppCompatTextView tv_ships_destroyed_adversary;

        public ViewHolder(View view) {
            super(view);

            this.tv_vs_game_mode = view.findViewById(R.id.tv_vs_game_mode);
            this.tv_game_result = view.findViewById(R.id.tv_game_result);
            this.tv_player_name = view.findViewById(R.id.tv_player_name);
            this.tv_adversary_name = view.findViewById(R.id.tv_adversary_name);
            this.tv_num_hits_player = view.findViewById(R.id.tv_num_hits_player);
            this.tv_ships_destroyed_player = view.findViewById(R.id.tv_ships_destroyed_player);
            this.tv_num_hits_adversary = view.findViewById(R.id.tv_num_hits_adversary);
            this.tv_ships_destroyed_adversary = view.findViewById(R.id.tv_ships_destroyed_adversary);
        }
    }
}
