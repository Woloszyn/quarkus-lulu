package co.woloszyn.tournament.dto;

public class TournamentMatchDTO {
    private String homePlayer;
    private Long homePlayerId;
    private String awayPlayer;
    private Long awayPlayerId;
    private Integer homeScore;
    private Integer awayScore;
    private String winner;
    private Long winnerId;
    private Long matchId;

    public String getHomePlayer() {
        return homePlayer;
    }

    public void setHomePlayer(String homePlayer) {
        this.homePlayer = homePlayer;
    }

    public Long getHomePlayerId() {
        return homePlayerId;
    }

    public void setHomePlayerId(Long homePlayerId) {
        this.homePlayerId = homePlayerId;
    }

    public String getAwayPlayer() {
        return awayPlayer;
    }

    public void setAwayPlayer(String awayPlayer) {
        this.awayPlayer = awayPlayer;
    }

    public Long getAwayPlayerId() {
        return awayPlayerId;
    }

    public void setAwayPlayerId(Long awayPlayerId) {
        this.awayPlayerId = awayPlayerId;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Long getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(Long winnerId) {
        this.winnerId = winnerId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }
}
