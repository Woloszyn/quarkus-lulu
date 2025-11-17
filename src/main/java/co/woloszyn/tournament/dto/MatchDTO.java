package co.woloszyn.tournament.dto;

public class MatchDTO {
    private Long homePlayerId;
    private Long awayPlayerId;

    public MatchDTO(Long homePlayerId, Long awayPlayerId) {
        this.homePlayerId = homePlayerId;
        this.awayPlayerId = awayPlayerId;
    }

    public Long getHomePlayerId() {
        return homePlayerId;
    }

    public void setHomePlayerId(Long homePlayerId) {
        this.homePlayerId = homePlayerId;
    }

    public Long getAwayPlayerId() {
        return awayPlayerId;
    }

    public void setAwayPlayerId(Long awayPlayerId) {
        this.awayPlayerId = awayPlayerId;
    }
}
