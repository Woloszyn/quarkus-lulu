package co.woloszyn.tournament.usecase;

import co.woloszyn.tournament.dto.GameTableRowDTO;
import co.woloszyn.tournament.dto.TournamentMatchDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetTournamentTableTest {

    @InjectMock
    GetTournamentMatch getTournamentMatch;

    @Inject
    GetTournamentTable getTournamentTable;

    @Test
    void shouldGenerateTableWithCorrectScoresAndOrdering() {
        Long tournamentId = 100L;

        // Players: A(1), B(2), C(3)
        TournamentMatchDTO m1 = new TournamentMatchDTO();
        m1.setMatchId(1L);
        m1.setHomePlayerId(1L);
        m1.setHomePlayer("A");
        m1.setAwayPlayerId(2L);
        m1.setAwayPlayer("B");
        m1.setHomeScore(21);
        m1.setAwayScore(15);
        m1.setWinnerId(1L);
        m1.setWinner("A");

        TournamentMatchDTO m2 = new TournamentMatchDTO();
        m2.setMatchId(2L);
        m2.setHomePlayerId(1L);
        m2.setHomePlayer("A");
        m2.setAwayPlayerId(3L);
        m2.setAwayPlayer("C");
        m2.setHomeScore(21);
        m2.setAwayScore(10);
        m2.setWinnerId(1L);
        m2.setWinner("A");

        TournamentMatchDTO m3 = new TournamentMatchDTO();
        m3.setMatchId(3L);
        m3.setHomePlayerId(2L);
        m3.setHomePlayer("B");
        m3.setAwayPlayerId(3L);
        m3.setAwayPlayer("C");
        m3.setHomeScore(20);
        m3.setAwayScore(22);
        m3.setWinnerId(3L);
        m3.setWinner("C");

        when(getTournamentMatch.getAllByTournamentId(tournamentId)).thenReturn(Arrays.asList(m1, m2, m3));

        List<GameTableRowDTO> table = getTournamentTable.getTable(tournamentId);

        assertNotNull(table);
        assertEquals(3, table.size());

        // Expected order: A (2 wins), C (1 win), B (0 wins)
        GameTableRowDTO first = table.get(0);
        assertEquals("A", first.getPlayerName());
        assertEquals(2, first.getScore());
        assertEquals(2, first.getGames());
        assertEquals(17, first.getPointDiference()); // (21+21)-(15+10)=42-25=17

        GameTableRowDTO second = table.get(1);
        assertEquals("C", second.getPlayerName());
        assertEquals(1, second.getScore());
        assertEquals(2, second.getGames());
        assertEquals(-9, second.getPointDiference()); // (10+22)-(21+20)=32-41=-9

        GameTableRowDTO third = table.get(2);
        assertEquals("B", third.getPlayerName());
        assertEquals(0, third.getScore());
        assertEquals(2, third.getGames());
        assertEquals(-8, third.getPointDiference()); // (15+20)-(21+22)=35-43=-8
    }

    @Test
    void emptyMatchListShouldReturnEmptyTable() {
        Long tournamentId = 300L;
        when(getTournamentMatch.getAllByTournamentId(tournamentId)).thenReturn(Arrays.asList());

        List<GameTableRowDTO> table = getTournamentTable.getTable(tournamentId);
        assertNotNull(table);
        assertTrue(table.isEmpty());
    }
}

