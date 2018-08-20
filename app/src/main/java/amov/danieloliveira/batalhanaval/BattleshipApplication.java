package amov.danieloliveira.batalhanaval;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import amov.danieloliveira.batalhanaval.activities.GameStartActivity;
import amov.danieloliveira.batalhanaval.engine.GameModel;
import amov.danieloliveira.batalhanaval.engine.GameObservable;
import amov.danieloliveira.batalhanaval.engine.model.MatchHistory;
import amov.danieloliveira.batalhanaval.engine.model.User;

public class BattleshipApplication extends Application {
    private GameObservable obs;
    private GameCommunication gameCommunication;
    private User user;
    private List<MatchHistory> matchHistory = new ArrayList<>();

    public BattleshipApplication() {
        this.obs = new GameObservable(this);
        this.gameCommunication = null;
    }

    public GameObservable getObservable() {
        return obs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameObservable getObs() {
        return obs;
    }

    public void setObs(GameObservable obs) {
        this.obs = obs;
    }

    public GameCommunication getGameCommunication() {
        return gameCommunication;
    }

    public GameCommunication newGameCommunication(GameStartActivity activity, int mode) {
        if (gameCommunication != null) {
            gameCommunication.endCommunication();
        }

        gameCommunication = new GameCommunication(obs, activity, mode);

        return gameCommunication;
    }

    public List<MatchHistory> getMatchHistory() {
        return matchHistory;
    }

    public void setMatchHistoryList(List<MatchHistory> matchHistoryList) {
        this.matchHistory = matchHistoryList;
    }

    public void addNewMatchHistory(GameModel gameModel) {
        this.matchHistory.add(new MatchHistory(gameModel, user));
        Preferences.saveMatchHistoryListPreferences(this);
    }
}
