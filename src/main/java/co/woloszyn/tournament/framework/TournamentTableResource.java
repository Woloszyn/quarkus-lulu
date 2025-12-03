package co.woloszyn.tournament.framework;

import co.woloszyn.tournament.dto.GameTableRowDTO;
import co.woloszyn.tournament.usecase.GetTournamentTable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@Path("tournament-table")
public class TournamentTableResource {
    private final GetTournamentTable getTournamentTable;

    public TournamentTableResource(GetTournamentTable getTournamentTable) {
        this.getTournamentTable = getTournamentTable;
    }

    @Path("/tournament/{tournamentId}")
    @GET
    public List<GameTableRowDTO> getTournamentTableByTournamentId(@PathParam("tournamentId") Long tournamentId) {
        return getTournamentTable.getTable(tournamentId);
    }

}
