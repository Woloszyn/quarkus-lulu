package co.woloszyn.tournament.dto;

public class SetWinnerRequestDTO {

    private String scoreBoard;
    private Long winnerId;
    private Long matchId;

    public String getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(String scoreBoard) {
        this.scoreBoard = scoreBoard;
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
