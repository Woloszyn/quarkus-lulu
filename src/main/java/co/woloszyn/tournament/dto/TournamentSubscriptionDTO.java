package co.woloszyn.tournament.dto;

public class TournamentSubscriptionDTO {
    private Long id;
    private TournamentDTO tournament;
    private Long customerId;

    public TournamentSubscriptionDTO(Long id, TournamentDTO tournament, Long customerId) {
        this.id = id;
        this.tournament = tournament;
        this.customerId = customerId;
    }

    public TournamentSubscriptionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentDTO getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDTO tournament) {
        this.tournament = tournament;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}