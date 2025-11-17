package co.woloszyn.tournament.dto;

import co.woloszyn.tournament.enumeration.TournamentStatus;
import co.woloszyn.tournament.enumeration.TournamentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TournamentDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime date;
    private TournamentType type;
    private BigDecimal price;
    private PlaceDTO place;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean online;
    private String imageUrl;
    private TournamentStatus status;
    private Integer maxQuantity;
    private Integer playersSubscribed;
    private boolean playerIsSubscribed;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public TournamentType getType() { return type; }
    public void setType(TournamentType type) { this.type = type; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public PlaceDTO getPlace() {
        return place;
    }

    public void setPlace(PlaceDTO place) {
        this.place = place;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public Integer getPlayersSubscribed() {
        return playersSubscribed;
    }

    public void setPlayersSubscribed(Integer playersSubscribed) {
        this.playersSubscribed = playersSubscribed;
    }

    public boolean isPlayerIsSubscribed() {
        return playerIsSubscribed;
    }

    public void setPlayerIsSubscribed(boolean playerIsSubscribed) {
        this.playerIsSubscribed = playerIsSubscribed;
    }
}

