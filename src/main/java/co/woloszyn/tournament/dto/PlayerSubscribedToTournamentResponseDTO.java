package co.woloszyn.tournament.dto;

public class PlayerSubscribedToTournamentResponseDTO {

    private boolean subscribed;

    public PlayerSubscribedToTournamentResponseDTO(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isSubscribed() {
        return subscribed;
    }
}
