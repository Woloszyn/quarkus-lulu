package co.woloszyn.tournament.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class TournamentAdditionalInfoSubscription extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "additional_info_id", nullable = false)
    public TournamentAdditionalInfo additionalInfo;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Long getId() { return id; }

    public TournamentAdditionalInfo getAdditionalInfo() { return additionalInfo; }
    public void setAdditionalInfo(TournamentAdditionalInfo additionalInfo) { this.additionalInfo = additionalInfo; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

