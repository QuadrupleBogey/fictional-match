package com.mortonstudios.fictionalmatch.multiplayer.entities;

import java.util.List;
import java.util.Map;

/**
 * Player Object, contains all of the information required for game management
 *
 * @author Cam
 * @since 1.0.0
 */
public class Player {

    private String userName;
    private List<Unit> army;
    private int victoryPoints = 0;
    private int commandPoints = 0;
    private int turn = 0;

    public Player(String userName, List<Unit> army, int victoryPoints, int commandPoints, int turn) {
        this.userName = userName;
        this.army = army;
        this.victoryPoints = victoryPoints;
        this.commandPoints = commandPoints;
        this.turn = turn;
    }

    public Player() {
        // does nothing
    }

    public Player(String userName, List<Unit> army) {
        this.setUserName(userName);
        this.army = army;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Unit> getArmy() {
        return this.army;
    }

    public void setArmy(List<Unit> army) {
        this.army = army;
    }

    public void incrementVictoryPoints() {
        this.victoryPoints++;
    }
    public void decrementVictoryPoints() {
        this.victoryPoints--;
    }

    public void incrementCommandPoints() {
        this.commandPoints++;
    }

    public void incrementTurn() {
        this.turn++;
    }

    public void decrementTurn() {
        this.turn--;
    }

    public int getTurn() {
        return turn;
    }

    private void setTurn(int turn) {
        this.turn = turn;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }

    public int getCommandPoints() {
        return commandPoints;
    }

    public void setCommandPoints(int commandPoints) {
        this.commandPoints = commandPoints;
    }

    @Override
    public String toString() {
        return "{ \"userName\": " + this.getUserName() + ", \"army\": " + this.getArmy().toString() +
                ", \"commandPoints\": " + this.getCommandPoints() + ", \"victoryPoints\": " + this.getVictoryPoints()
                + ", \"turn\": " + this.getTurn() + " }";
    }
}
