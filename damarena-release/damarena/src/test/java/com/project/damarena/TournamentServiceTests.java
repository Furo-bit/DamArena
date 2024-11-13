package com.project.damarena;

import com.project.damarena.model.Notification;
import com.project.damarena.model.Tournament;
import com.project.damarena.model.User;
import com.project.damarena.repository.TournamentRepository;
import com.project.damarena.repository.UserRepository;
import com.project.damarena.service.TournamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TournamentServiceTests {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TournamentService tournamentService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckAndJoinTournament() {
        Tournament tournament = new Tournament();
        tournament.setPlayers(new HashMap<>());
        tournament.setMaxNumPlayers(2);

        User user = new User(
                "testName",
                "testSurname",
                "testUsername",
                "testEmail",
                "testPassword");
        user.setEmail("test@example");

        when(tournamentRepository.findById(anyString())).thenReturn(Optional.of(tournament));
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        boolean result = tournamentService.checkAndJoinTournament("tournamentId", "testEmail");

        assertTrue(result);
        assertEquals(1, tournament.getPlayers().size());
        verify(tournamentRepository, times(1)).save(tournament);
    }

    @Test
    public void testCheckStartedTournaments() {
        Tournament ongoingTournament = new Tournament();
        ongoingTournament.setStartingTime(LocalDateTime.now().minusMinutes(5));
        ongoingTournament.setEndingTime(LocalDateTime.now().plusMinutes(10));
        ongoingTournament.setPlayers(new HashMap<>());
        ongoingTournament.getPlayers().put("player1@example", new User("testName",
                                                                        "testSurname",
                                                                        "testUsername",
                                                                        "player1@example",
                                                                        "testPassword"));
        ongoingTournament.getPlayers().put("player2@example", new User("testName",
                                                                        "testSurname",
                                                                        "testUsername",
                                                                        "player2@example",
                                                                        "testPassword"));
        ongoingTournament.setOriginalPlayerCount(0);

        List<Tournament> startedTournaments = new ArrayList<>();
        startedTournaments.add(ongoingTournament);

        when(tournamentRepository.findByStartingTimeBefore(any(LocalDateTime.class))).thenReturn(startedTournaments);
        when(tournamentRepository.findByEndingTimeBefore(any(LocalDateTime.class))).thenReturn(new ArrayList<>());

        tournamentService.checkStartedTournaments();

        verify(tournamentRepository, times(1)).save(ongoingTournament);
        verify(tournamentRepository, never()).save(argThat(t -> t.getWinner() != null));
    }

    @Test
    public void testPairPlayers() {
        Tournament tournament = new Tournament();
        HashMap<String, User> players = new HashMap<>();
        players.put("player1", new User("testName",
                "testSurname",
                "player1",
                "player1@player.com",
                "testPassword"));
        players.put("player2", new User("testName",
                "testSurname",
                "player2",
                "player2@player.com",
                "testPassword"));
        players.put("player3", new User("testName",
                "testSurname",
                "player3",
                "player3@player.com",
                "testPassword"));
        tournament.setPlayers(players);

        tournamentService.pairPlayers(tournament);

        verify(userRepository, times(2)).save(any(User.class));
    }

    @Test
    public void testPairPlayersTogether() {
        User player1 = new User("testName",
                "testSurname",
                "player1",
                "player1@player.com",
                "testPassword");
        User player2 = new User("testName",
                "testSurname",
                "player2",
                "player2@player.com",
                "testPassword");
        Tournament tournament = new Tournament();
        tournament.setId("tournamentId");

        tournamentService.pairPlayersTogether(player1, player2, tournament);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(2)).save(userCaptor.capture());

        List<User> savedUsers = userCaptor.getAllValues();
        assertEquals(2, savedUsers.size());

        Notification notification1 = savedUsers.get(0).getNotifications().get(0);
        Notification notification2 = savedUsers.get(1).getNotifications().get(0);

        assertEquals("tournamentGame", notification1.getType());
        assertEquals("tournamentGame", notification2.getType());
    }
}
