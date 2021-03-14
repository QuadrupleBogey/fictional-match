package com.mortonstudios.fictionalmatch.multiplayer.entities;

import java.util.Map;

public class Player {

    private String userName;
    private Map<String, SharedAnchor> army;

    public Player(String userName, Map<String, SharedAnchor> army) {
        this.setUserName(userName);
        this.army = army;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Map<String, SharedAnchor> getArmy() {
        return army;
    }

    public void setArmy(Map<String, SharedAnchor> army) {
        this.army = army;
    }


    

}
