package no.ntnu.ui;

import javafx.application.Platform;
import no.ntnu.BoardGame;
import no.ntnu.Player;
import no.ntnu.observer.GameObserver;
import no.ntnu.tile.Tile;
import no.ntnu.tile.PropertyTile;
import no.ntnu.tile.RailroadTile;
import no.ntnu.tile.UtilityTile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Binds per-player buttons and updates board tokens
 */
public class GameController implements GameObserver {
    private final BoardGame game;
    private final GameView view;
    private int playerCount;
    private boolean hasRolled = false;

    public GameController(BoardGame game, GameView view) {
        this.game = game;
        this.view = view;
        this.playerCount = view.getPlayerCount();

        game.addObserver(this);

        for (int i = 0; i < playerCount; i++) {
            PlayerPane pane = view.getPlayerPane(i);
            final int index = i;
            pane.getRollButton().setOnAction(e -> handleRoll(index));
            pane.getBuyButton().setOnAction(e -> handleBuy(index));
            pane.getNextButton().setOnAction(e -> handleNext(index));
        }

        updateUIForCurrentPlayer();
    }

    private void handleRoll(int index) {
        if (index != game.getCurrentPlayerIndex()) return;
        
        view.getPlayerPane(index).getRollButton().setDisable(true);
        
        hasRolled = true;
        game.playTurn();
        
        updateButtonStatesAfterRoll();
    }

    private void handleBuy(int index) {
        if (index != game.getCurrentPlayerIndex()) return;
        Tile prop = view.getOfferedProperty();
        if (prop == null) return; 
        
        game.buyProperty(game.getCurrentPlayer(), prop);
        clearOffer();
        
        view.getPlayerPane(index).getBuyButton().setDisable(true);
        view.getPlayerPane(index).getNextButton().setDisable(false);
    }

    private void handleNext(int index) {
        if (index != game.getCurrentPlayerIndex()) return;
        if (!hasRolled) return; 
        
        hasRolled = false; 
        clearOffer();
        game.nextTurn();
        updateUIForCurrentPlayer();
    }

    /**
     * Clear any outstanding purchase offer
     */
    private void clearOffer() {
        view.clearPropertyOffer();
        int idx = game.getCurrentPlayerIndex();
        view.getPlayerPane(idx).getBuyButton().setDisable(true);
    }

    /**
     * Update button states after a roll has been made
     */
    private void updateButtonStatesAfterRoll() {
        int idx = game.getCurrentPlayerIndex();
        PlayerPane currentPane = view.getPlayerPane(idx);

        currentPane.getRollButton().setDisable(true);
        
        currentPane.getNextButton().setDisable(false);
        
    }

    private void updateUIForCurrentPlayer() {
        int idx = game.getCurrentPlayerIndex();
        view.setActivePlayerPane(idx);
        view.getBoardView().drawTokens(game.getPlayers());
        
        for (int i = 0; i < playerCount; i++) {
            Player p = game.getPlayers().get(i);
            List<String> assetNames = new ArrayList<>();
            assetNames.addAll(
                p.getOwnedProperties()
                .stream()
                .map(Tile::getName)
                .collect(Collectors.toList())
            );
            assetNames.addAll(
                p.getOwnedRailroads()
                .stream()
                .map(Tile::getName)
                .collect(Collectors.toList())
            );
            assetNames.addAll(
                p.getOwnedUtilities()
                .stream()
                .map(Tile::getName)
                .collect(Collectors.toList())
            );
            view.getPlayerPane(i).update(p, assetNames);
            
            if (i == idx) {
                view.getPlayerPane(i).getRollButton().setDisable(false);
                view.getPlayerPane(i).getBuyButton().setDisable(true);
                view.getPlayerPane(i).getNextButton().setDisable(true); // Must roll first
            } else {
                view.getPlayerPane(i).getRollButton().setDisable(true);
                view.getPlayerPane(i).getBuyButton().setDisable(true);
                view.getPlayerPane(i).getNextButton().setDisable(true);
            }
            view.getPlayerPane(i).setStatus("");
        }
    }

    @Override
    public void onDiceRoll(Player player, int die1, int die2) {
    }

    @Override
    public void onPlayerMoved(Player player, Tile from, Tile to, int steps) {
        view.getPlayerPane(game.getCurrentPlayerIndex())
            .setStatus("Moved to " + to.getName());
        view.drawTokens(game.getPlayers());
    }

    @Override
    public void onTileAction(Player player, String actionName, Tile tile) {
        view.getPlayerPane(game.getCurrentPlayerIndex())
            .setStatus(actionName + " on " + tile.getName());
        updateButtonStatesAfterRoll(); 
    }

    @Override
    public void onPropertyAvailable(Player player, Tile property) {
        int idx = game.getPlayers().indexOf(player);
        if (idx < 0) return;

        boolean canBuy = false;
        if (property instanceof PropertyTile pt && pt.getOwner() == null) {
            canBuy = true;
        } else if (property instanceof RailroadTile rt && rt.getOwner() == null) {
            canBuy = true;
        } else if (property instanceof UtilityTile ut && ut.getOwner() == null) {
            canBuy = true;
        }
        
        if (canBuy && player.getBalance() >= property.getPrice()) {
            view.getPlayerPane(idx).getBuyButton().setDisable(false);
            view.getPlayerPane(idx)
                .setStatus("Offer to buy " + property.getName() 
                 + " ($" + property.getPrice() + ")");
            view.setOfferedProperty(property);
        }
    }

    @Override
    public void onRentPaid(Player payer, Player owner, Tile property, int amount) {
        int idx = game.getCurrentPlayerIndex();
        view.getPlayerPane(idx).setStatus("Paid $" + amount + " rent to " + owner.getName());
        updateButtonStatesAfterRoll(); 
    }

    @Override
    public void onPlayerSkipped(Player player) {
        int idx = game.getCurrentPlayerIndex();
        view.getPlayerPane(idx).setStatus("Skipped");
        updateUIForCurrentPlayer();
    }

    @Override
    public void onGameEnd(Player winner) {
        Platform.runLater(() -> view.showWinnerDialog(winner.getName()));
    }
}