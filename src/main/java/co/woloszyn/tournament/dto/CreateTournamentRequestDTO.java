package co.woloszyn.tournament.dto;

import co.woloszyn.tournament.enumeration.TournamentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateTournamentRequestDTO {
    private String name;
    private String description;
    private LocalDateTime date;
    private TournamentType type;
    private BigDecimal price;
    private Long placeId;
    private Boolean online;
    private Integer maxQuantity;

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

    public Long getPlaceId() { return placeId; }
    public void setPlaceId(Long placeId) { this.placeId = placeId; }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }
}

