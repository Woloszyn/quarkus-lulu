package co.woloszyn.tournament.framework;

import co.woloszyn.tournament.dto.SetWinnerRequestDTO;
import co.woloszyn.tournament.dto.TournamentMatchDTO;
import co.woloszyn.tournament.usecase.CreateTournamentMatch;
import co.woloszyn.tournament.usecase.GetTournamentMatch;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("match")
public class TournamentMatchResource {

    @Inject
    CreateTournamentMatch createTournamentMatch;

    @Inject
    GetTournamentMatch getTournamentMatch;

    @PATCH
    @Path("set-winner")
    public Response setWinner(SetWinnerRequestDTO requestDTO) {
        createTournamentMatch.setWinner(requestDTO.getMatchId(), requestDTO.getWinnerId(), requestDTO.getScoreBoard());
        return Response.ok().build();
    }

    @GET
    @Path("/tournament/{tournamentId}")
    public List<TournamentMatchDTO> getMatchById(@PathParam("tournamentId") Long tournamentId) {
        return getTournamentMatch.getAllByTournamentId(tournamentId);
    }

}
