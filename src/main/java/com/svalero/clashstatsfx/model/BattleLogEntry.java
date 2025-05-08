package com.svalero.clashstatsfx.model;

import java.util.List;

/*Contiene información sobre el tipo de batalla, el momento en que ocurrió, y detalles del jugador y su oponente.*/
public class BattleLogEntry {

    private String type;
    private String battleTime;
    private List<Team> team;
    private List<Team> opponent;

    /*Devuelve el tipo de batalla (ej: "ladder", "challenge").*/
    public String getType() {
        return type;
    }

    /*Devuelve la fecha y hora en la que ocurrió la batalla.*/
    public String getBattleTime() {
        return battleTime;
    }

    /*Devuelve el nombre del jugador, o "Desconocido" si no está disponible.*/
    public String getPlayerName() {
        return (team != null && !team.isEmpty()) ? team.get(0).getName() : "Desconocido";
    }

    /*Devuelve el nombre del oponente, o "Desconocido" si no está disponible.*/
    public String getOpponentName() {
        return (opponent != null && !opponent.isEmpty()) ? opponent.get(0).getName() : "Desconocido";
    }

    /*Calcula el resultado de la batalla: "Victoria", "Derrota" o "Empate".*/
    public String getResult() {
        int playerCrowns = (team != null && !team.isEmpty()) ? team.get(0).getCrowns() : 0;
        int opponentCrowns = (opponent != null && !opponent.isEmpty()) ? opponent.get(0).getCrowns() : 0;

        if (playerCrowns > opponentCrowns) return "Victoria";
        if (playerCrowns < opponentCrowns) return "Derrota";
        return "Empate";
    }

    /*Clase interna que representa a un miembro de un equipo (jugador u oponente).*/
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
