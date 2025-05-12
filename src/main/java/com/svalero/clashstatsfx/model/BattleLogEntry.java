package com.svalero.clashstatsfx.model;

import java.util.List;

public class BattleLogEntry {

    private String type;
    private String battleTime;
    private List<Team> team;
    private List<Team> opponent;

    public String getType() {
        return type;
    }

    public String getBattleTime() {
        return battleTime;
    }

    public String getPlayerName() {
        return (team != null && !team.isEmpty()) ? team.get(0).getName() : "Desconocido";
    }

    public String getOpponentName() {
        return (opponent != null && !opponent.isEmpty()) ? opponent.get(0).getName() : "Desconocido";
    }

    public String getResult() {
        int playerCrowns = (team != null && !team.isEmpty()) ? team.get(0).getCrowns() : 0;
        int opponentCrowns = (opponent != null && !opponent.isEmpty()) ? opponent.get(0).getCrowns() : 0;

        if (playerCrowns > opponentCrowns) return "Victoria";
        if (playerCrowns < opponentCrowns) return "Derrota";
        return "Empate";
    }

    public static class Team {
        private String name;
        private int crowns;

        public String getName() {
            return name;
        }

        public int getCrowns() {
            return crowns;
        }
    }
}
