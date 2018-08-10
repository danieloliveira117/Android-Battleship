package amov.danieloliveira.batalhanaval.engine;

import amov.danieloliveira.batalhanaval.engine.enums.GameMode;
import amov.danieloliveira.batalhanaval.engine.enums.PlayerType;
import amov.danieloliveira.batalhanaval.engine.model.Game;
import amov.danieloliveira.batalhanaval.engine.model.Player;
import amov.danieloliveira.batalhanaval.engine.model.Ship;
import amov.danieloliveira.batalhanaval.engine.model.User;

class GameModel {
    private Game game = new Game();

    public void setMode(GameMode mode) {
        game.setGameMode(mode);
    }

    public void updatePlayerData(PlayerType player, User user) {
        switch (player) {
            case PLAYER:
                game.getPlayer1().setUser(user);
                break;
            case ADVERSARY:
                game.getPlayer2().setUser(user);
                game.getPlayer2().setHuman(true);
                break;
        }
    }

    public Ship[] getPlayerShips(PlayerType player) {
        return getPlayer(player).getBoard().getShipList();
    }

    public boolean allShipsPlaced() {
        return getPlayer(PlayerType.PLAYER).allShipsPlaced() && getPlayer(PlayerType.ADVERSARY).allShipsPlaced();
    }

    public void confirmShipPlacement(PlayerType player) {
        getPlayer(player).setShipsPlaced(true);
    }

    private Player getPlayer(PlayerType player) {
        switch (player) {
            case PLAYER:
                return game.getPlayer1();
            default:
                return game.getPlayer2();
        }
    }
}
