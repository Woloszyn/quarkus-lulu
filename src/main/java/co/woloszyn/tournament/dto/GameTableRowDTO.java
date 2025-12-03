package co.woloszyn.tournament.dto;

public class GameTableRowDTO {
    private String playerName;
    private int score;
    private int games;
    private int pointDiference;

    public GameTableRowDTO(String playerName, int score, int games, int pointDiference) {
        this.playerName = playerName;
        this.score = score;
        this.games = games;
        this.pointDiference = pointDiference;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getPointDiference() {
        return pointDiference;
    }

    public void setPointDiference(int pointDiference) {
        this.pointDiference = pointDiference;
    }
}
