package co.woloszyn.tournament.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tournament_match")
public class TournamentMatchEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_subscription_id", nullable = false)
    private TournamentSubscription homeTeam;

    @ManyToOne
    @JoinColumn(name = "versus_team_subscription_id", nullable = false)
    private TournamentSubscription versusTeam;

    @ManyToOne
    @JoinColumn(name = "winner_team_subscription_id")
    private TournamentSubscription winner;

    @Column(name = "scoreboard")
    private String scoreboard;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public TournamentMatchEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getScoreboard() {
        return scoreboard;
    }

    public void setScoreboard(String scoreboard) {
        this.scoreboard = scoreboard;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TournamentSubscription getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TournamentSubscription homeTeam) {
        this.homeTeam = homeTeam;
    }

    public TournamentSubscription getVersusTeam() {
        return versusTeam;
    }

    public void setVersusTeam(TournamentSubscription versusTeam) {
        this.versusTeam = versusTeam;
    }

    public TournamentSubscription getWinner() {
        return winner;
    }

    public void setWinner(TournamentSubscription winner) {
        this.winner = winner;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
