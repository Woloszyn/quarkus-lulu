package co.woloszyn.tournament.framework;

import co.woloszyn.tournament.dto.CreateTournamentRequestDTO;
import co.woloszyn.tournament.dto.TournamentDTO;
import co.woloszyn.tournament.usecase.CreateTournament;
import co.woloszyn.tournament.usecase.GetTournament;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestHeader;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

@Path("tournament")
public class TournamentResource {

    @Inject
    CreateTournament createTournament;

    @Inject
    GetTournament getTournament;

    @POST
    public TournamentDTO createTournament(CreateTournamentRequestDTO requestDTO, @RestHeader ("X-Customer-Id") Long customerId) {
        return createTournament.createTournament(requestDTO, customerId);
    }

    @PUT
    @Path("{tournamentId}/cancel")
    public Response cancelTournament(@RestPath("tournamentId") Long tournamentId, @RestHeader ("X-Customer-Id") Long customerId) {
        createTournament.cancelTournament(tournamentId, customerId);
        return Response.ok().build();
    }

    @PUT
    @Path("{tournamentId}/start")
    public Response startTournament(@RestPath("tournamentId") Long tournamentId, @RestHeader ("X-Customer-Id") Long customerId) {
        createTournament.startTournament(tournamentId, customerId);
        return Response.ok().build();
    }

    @GET
    @Path("{id}")
    public TournamentDTO getTournamentById(Long id, @RestHeader("X-Customer-Id") Long customerId) {
        return getTournament.getById(id, customerId);
    }

    @GET
    @Path("place/{placeId}")
    public List<TournamentDTO> getTournamentsByPlaceId(@RestPath("placeId") Long placeId, @RestHeader("X-Customer-Id") Long customerId) {
        return getTournament.getTournamentsByPlaceId(placeId, customerId);
    }

    @GET
    @Path("customer")
    public List<TournamentDTO> getTournamentsByCustomerId(@RestHeader("X-Customer-Id") Long customerId) {
        return getTournament.getTournamentsByCustomerId(customerId);
    }

    @GET
    public List<TournamentDTO> getAll(@RestHeader("X-Customer-Id") Long customerId) {
        return getTournament.getAllCreated(customerId);
    }

}

