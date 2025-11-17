package co.woloszyn.tournament.framework;

import co.woloszyn.tournament.dto.PlayerSubscribedToTournamentResponseDTO;
import co.woloszyn.tournament.dto.TournamentSubscriptionDTO;
import co.woloszyn.tournament.usecase.CreateTournamentSubscrition;
import co.woloszyn.tournament.usecase.GetTournamentSubscription;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.jboss.resteasy.reactive.RestHeader;

import java.util.List;

@Path("subscription")
public class TournamentSubscriptionResource {

    @Inject
    CreateTournamentSubscrition createTournamentSubscription;

    @Inject
    GetTournamentSubscription getTournamentSubscription;

    @POST
    @Path("/tournament/{tournamentId}")
    public TournamentSubscriptionDTO subscribeToTournament(@PathParam("tournamentId") Long tournamentId, @RestHeader("X-Customer-Id") Long customerId) {
        return createTournamentSubscription.createSubscription(tournamentId, customerId);
    }

    @GET
    @Path("/tournament/{tournamentId}/is-subscribed")
    public PlayerSubscribedToTournamentResponseDTO isPlayerSubscribed(@PathParam("tournamentId") Long tournamentId, @RestHeader("X-Customer-Id") Long customerId) {
        return new PlayerSubscribedToTournamentResponseDTO(getTournamentSubscription.isCustomerSubscribedToTournament(tournamentId, customerId));
    }

    @GET
    @Path("/tournament/{tournamentId}")
    public List<TournamentSubscriptionDTO> getByTournament(@PathParam("tournamentId") Long tournamentId) {
        return getTournamentSubscription.getSubscriptionByTournamentId(tournamentId);
    }

    @GET
    @Path("/customer")
    public List<TournamentSubscriptionDTO> getSubscriptionsByCustomerId(@RestHeader("X-Customer-Id") Long customerId) {
        return getTournamentSubscription.getSubscriptionsByCustomerId(customerId);
    }

}
